package digital.softwareshinobi.napkinexchange.broker.service;

import digital.softwareshinobi.napkinexchange.broker.request.SecuritySellRequest;
import digital.softwareshinobi.napkinexchange.broker.types.LimitOrderType;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.trader.exception.TraderNotFoundException;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.broker.order.LimitOrder;
import digital.softwareshinobi.napkinexchange.broker.request.SecurityBuyRequest;
import digital.softwareshinobi.napkinexchange.notification.type.NotificationType;
import digital.softwareshinobi.napkinexchange.trader.repository.LimitOrderRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class LimitOrderService {

    private final Logger logger = LoggerFactory.getLogger(LimitOrderService.class);

    @Autowired
    private final LimitOrderRepository limitOrderRepository;

    @Autowired
    private final SecurityPortfolioService securityPortfolioService;

    @Autowired
    private final NotificationService notificationService;

    public List<LimitOrder> findLimitOrders() {

        return this.limitOrderRepository.findAll();

    }

    public List<LimitOrder> findLimitOrders(Trader trader) {

        return this.limitOrderRepository.findAll().stream()
                .filter(limitOrder -> limitOrder.getTrader().getUsername().equals(trader.getUsername()))
                .collect(Collectors.toList());

    }

    public Optional<LimitOrder> findLimitOrder(Integer id) {

        return this.limitOrderRepository.findById(id);

    }

    @Transactional
    public synchronized LimitOrder saveLimitOrder(final LimitOrder limitOrder) {

        logger.debug("enter > saveLimitOrder");
        logger.debug("limitOrder / " + limitOrder);

        logger.debug("saving...");

        LimitOrder savedLimitOrder = this.limitOrderRepository.save(limitOrder);

        logger.debug("savedLimitOrder / " + savedLimitOrder);

        if (limitOrder.getTrader() != null) {

            System.err.println("trader name was null");//todo make this an exception on the request
            this.notificationService.save(new Notification(
                    savedLimitOrder.getTrader().getUsername(),
                    NotificationType.NEW_LIMIT_ORDER_CREATED,
                    savedLimitOrder.toString()
            ));
        }

        return savedLimitOrder;

    }

    @Transactional
    public synchronized void processLimitOrders() {

        //    logger.debug("enter > processLimitOrders");
        if (this.limitOrderRepository.count() == 0) {
            //       logger.debug("no limit orders to process. returning;");

            return;

        }

        logger.debug("looping through all limit orders for processing");

        this.limitOrderRepository.findAll().forEach(limitOrder -> {

            logger.debug("limit Order being processed / " + limitOrder);

            //   final String limitOrderType = openLimitOrder.getType();
            //   String limitOrderConstant = ;
            //   boolean equal = (limitOrderType.equals(limitOrderConstant));
            //  //logger.debug("limitOrderType /" + limitOrderType);
            //  //logger.debug("limitOrderConstant /" + limitOrderConstant);
            //   //logger.debug("equal /" + equal);
            switch (limitOrder.getType()) {

                case LimitOrderType.LONG_BUY_STOP:

                    logger.debug("is a LONG_BUY_STOP");

                    this.qualifyLongBuyStop(limitOrder);

                    break;

                case LimitOrderType.LONG_STOP_LOSS:

                    logger.debug("is a LONG_STOP_LOSS");

                    this.qualifyLongStopLoss(limitOrder);

                    break;

                case LimitOrderType.LONG_TAKE_PROFIT:

                    logger.debug("is a LONG_TAKE_PROFIT");

                    this.qualifyTakeProfitOrder(limitOrder);

                    break;

                default:

                    //logger.debug("dont know how to handle this. what is it? /" + limitOrderType);
                    break;

            }

        });

        logger.debug("exit < processLimitOrders");

    }

    private synchronized void qualifyLongStopLoss(LimitOrder stopLossOrder) {

        logger.debug("enter > qualifyLongStopLoss");

        if (!stopLossOrder.getActive()) {
            logger.debug("not active. skipping.");
            return;
        }
        //logger.debug();
        logger.debug("stop loss order / " + stopLossOrder);
        //logger.debug();
        logger.debug("   strike price / " + stopLossOrder.getStrike());
        logger.debug("  current price / " + stopLossOrder.getSecurity().getPrice());

        //logger.debug();
//todo, we shouldn't be compariing doubles like this
        if (stopLossOrder.getSecurity().getPrice() <= stopLossOrder.getStrike()) {

            logger.debug("IT'S TIME TO TRIGGER THIS STOP LOSS");

            try {

                notificationService.save(new Notification(
                        stopLossOrder.getTrader().getUsername(),
                        NotificationType.LONG_STOP_LOSS_TRIGGERED,
                        stopLossOrder
                ));

                SecuritySellRequest sellStockRequest = new SecuritySellRequest(
                        stopLossOrder.getTrader().getUsername(),
                        stopLossOrder.getSecurity().getTicker(),
                        stopLossOrder.getUnits()
                );

                this.securityPortfolioService.sellSecurityMarketPrice(sellStockRequest);

                this.purgeLimitOrder(stopLossOrder);

                this.removeSmartRelated(stopLossOrder);

                logger.debug("removing orders");

            } catch (TraderNotFoundException exception) {

                exception.printStackTrace();

            }

        } else {

            ////logger.debug("this STOP LOSS did not trigger");
        }

    }

    @Transactional
    private synchronized void qualifyLongBuyStop(LimitOrder buyStopOrder) {

        logger.debug("enter > processBuyStopOrder");

        logger.debug("buyStopOrder /" + buyStopOrder);

        logger.debug("order.getStrike() /" + buyStopOrder.getStrike());

        logger.debug("order.getSecurity().getPrice() /" + buyStopOrder.getSecurity().getPrice());

        if (buyStopOrder.getSecurity().getPrice() > buyStopOrder.getStrike()) {

            logger.debug("TRIGGER A MARKET BUY B/C STRIKE CROSSED");

            notificationService.save(new Notification(
                    buyStopOrder.getTrader().getUsername(),
                    NotificationType.BUY_STOP_TRIGGER,
                    "LONG_BUY_STOP TRIGGERED / " + buyStopOrder.toString()
            ));

            try {
                logger.debug("1");

                this.securityPortfolioService.buyMarketPrice(
                        new SecurityBuyRequest(
                                buyStopOrder.getTrader().getUsername(),
                                buyStopOrder.getSecurity().getTicker(),
                                buyStopOrder.getUnits()));

//                notificationService.save(
//                        new Notification(
//                                buyStopOrder.getTrader().getUsername(),
//                                NotificationType.BUY_STOP_TRIGGER,
//                                "LONG_BUY_STOP TRIGGERED / " + buyStopOrder.toString()
//                        ));
                logger.debug("2");

                this.purgeLimitOrder(buyStopOrder);

                logger.debug("3");

                this.removeSmartRelated(buyStopOrder);

                logger.debug("4");

            } catch (TraderNotFoundException e) {

                e.printStackTrace();

            }

        } else {

            //logger.debug("this LONG_BUY_STOP didn't trigger");
        }
    }

    @Transactional
    private synchronized void qualifyTakeProfitOrder(LimitOrder takeProfitOrder) {

        logger.debug("enter > qualifyTakeProfitOrder");

        if (!takeProfitOrder.getActive()) {
            logger.debug("not active. skipping.");
            return;
        }
        logger.debug("");
        logger.debug("evaluating TP order / " + takeProfitOrder);
        logger.debug("");
        logger.debug("  current price / " + takeProfitOrder.getSecurity().getPrice());
        logger.debug("   strike price / " + takeProfitOrder.getStrike());
        logger.debug("");

//todo, we shouldn't be compariing doubles like this
        if (takeProfitOrder.getSecurity().getPrice() >= takeProfitOrder.getStrike()) {

            logger.debug("IT'S TIME TO TRIGGER THIS TAKE PROFIT");

            try {

                notificationService.save(new Notification(
                        takeProfitOrder.getTrader().getUsername(),
                        NotificationType.LONG_TAKE_PROFIT_TRIGGERED,
                        takeProfitOrder
                ));

                SecuritySellRequest marketSellStockRequest = new SecuritySellRequest(
                        takeProfitOrder.getTrader().getUsername(),
                        takeProfitOrder.getSecurity().getTicker(),
                        takeProfitOrder.getUnits());

                this.securityPortfolioService.sellSecurityMarketPrice(marketSellStockRequest);

                this.purgeLimitOrder(takeProfitOrder);

                this.removeSmartRelated(takeProfitOrder);

                marketSellStockRequest = null;

            } catch (TraderNotFoundException exception) {

                exception.printStackTrace();

            }

        } else {

            logger.debug("this one didn't trigger");
        }

    }
//    @Transactional
//    public void truncateLimitOrders() {
//        limitOrderRepository.truncateTable();
//    }

    @Transactional
    private synchronized void removeSmartRelated(LimitOrder limitOrder) {
        /////////////////////////////////////////////////////////////////
        //logger.debug("removing the related");

        Optional<LimitOrder> limitOrderPartner = this.findLimitOrder(limitOrder.getPartnerID());

        if (limitOrderPartner.isPresent()) {

            LimitOrder relatedOrder = limitOrderPartner.get(); // Use the user object

            //logger.debug("related order / " + relatedOrder);
            this.notificationService.save(new Notification(
                    relatedOrder.getTrader().getUsername(),
                    NotificationType.LONG_SMART_BUY_CANCELLATION,
                    relatedOrder
            ));

            purgeLimitOrder(relatedOrder);

        } else {
            // Handle the case where no relatedOrder is found
        }

/////////////////////////////////////////////////////////////////
    }

    @Transactional
    private void purgeLimitOrder(LimitOrder limitOrder) {

        limitOrder.setActive(false);

        limitOrder.setTrader(null);

        limitOrder.setSecurity(null);

        this.saveLimitOrder(limitOrder);

        this.deleteLimitOrder(limitOrder);

    }

    @Transactional
    private synchronized Boolean deleteLimitOrder(LimitOrder limitOrder) {

        logger.debug("enter > delete");

        logger.debug("limitOrder > " + limitOrder);
//        notificationService.save(
//                new Notification(
//                        limitOrder.getTrader().getUsername(),
//                        NotificationType.NEW_LIMIT_ORDER_CREATED,
//                        "limit order cancelled / " + limitOrder.toString()
//                ));
        boolean doDelete = false;

        boolean contains = this.limitOrderRepository.findAll().contains(limitOrder);
        logger.debug("contains > " + contains);

        logger.debug("rep before > ");
        logger.debug("{}", this.limitOrderRepository.findAll());

        this.limitOrderRepository.delete(limitOrder);

        logger.debug("rep after > ");
        logger.debug("{}", this.limitOrderRepository.findAll());
        boolean noContain = !this.limitOrderRepository.findAll().contains(limitOrder);

        logger.debug("contains > " + contains);

        logger.debug("returning > " + noContain);
        logger.debug("exit > delete");
        return noContain;
    }

}
/*

 */
