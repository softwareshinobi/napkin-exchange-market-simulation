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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class LimitOrderService {

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
  public LimitOrder saveLimitOrder(final LimitOrder limitOrder) {

        System.out.println("enter > saveLimitOrder");

        if (limitOrder.getTrader() == null) {

            System.err.println("trader name was null");//todo make this an exception on the request

            return null;

        } else {
            System.out.println("tradername set, all good");
        }

                System.out.println("saving...");

        LimitOrder savedLimitOrder = this.limitOrderRepository.save(limitOrder);

        System.out.println("savedLimitOrder / " + savedLimitOrder);

        this.notificationService.save(new Notification(
                        limitOrder.getTrader().getUsername(),
                        NotificationType.NEW_LIMIT_ORDER_CREATED,
                        limitOrder.toString()
                ));

        return savedLimitOrder;

    }

  @Transactional
  public void processLimitOrders() {

        if (this.limitOrderRepository.count() == 0) {
            return;
        }

        // //System.out.println("enter > processLimitOrders");
        this.limitOrderRepository.findAll().forEach(limitOrder -> {

            System.out.println("limitOrder / " + limitOrder);
            //   final String limitOrderType = openLimitOrder.getType();
            //   String limitOrderConstant = ;
            //   boolean equal = (limitOrderType.equals(limitOrderConstant));
            //  //System.out.println("limitOrderType /" + limitOrderType);
            //  //System.out.println("limitOrderConstant /" + limitOrderConstant);
            //   //System.out.println("equal /" + equal);
            switch (limitOrder.getType()) {

                case LimitOrderType.LONG_BUY_STOP:

                    System.out.println("is a LONG_BUY_STOP");

                    processBuyStopOrder(limitOrder);
                    break;
                case LimitOrderType.LONG_STOP_LOSS:

                    //System.out.println("is a LONG_STOP_LOSS");
                    this.evaluateLimitOrderLongStopLoss(limitOrder);

                    break;

                case LimitOrderType.LONG_TAKE_PROFIT:

                    System.out.println("is a LONG_TAKE_PROFIT");

                    this.qualifyTakeProfitOrder(limitOrder);

                    break;

                default:

                    //System.out.println("dont know how to handle this. what is it? /" + limitOrderType);
                    break;

            }

        });

        // //System.out.println("exit < processLimitOrders");
    }

    private void evaluateLimitOrderLongStopLoss(LimitOrder stopLossLimitOrder) {

        //System.out.println("enter > evaluateLimitOrderLongStopLoss");
        //System.out.println();
        //System.out.println("stop loss order / " + stopLossOrder);
        //System.out.println();
        //System.out.println("  current price / " + stopLossOrder.getSecurity().getPrice());
        //System.out.println("   strike price / " + stopLossOrder.getStrike());
        //System.out.println();
//todo, we shouldn't be compariing doubles like this
        if (stopLossLimitOrder.getSecurity().getPrice() < stopLossLimitOrder.getStrike()) {

            //System.out.println("IT'S TIME TO TRIGGER THIS STOP LOSS");
            try {

                notificationService.save(new Notification(
                                stopLossLimitOrder.getTrader().getUsername(),
                                NotificationType.LONG_STOP_LOSS_TRIGGERED,
                                stopLossLimitOrder
                        ));

                SecuritySellRequest sellStockRequest = new SecuritySellRequest(
                        stopLossLimitOrder.getTrader().getUsername(),
                        stopLossLimitOrder.getSecurity().getTicker(),
                        stopLossLimitOrder.getUnits()
                );

                securityPortfolioService.sellSecurityMarketPrice(sellStockRequest);

                this.purgeLimitOrder(stopLossLimitOrder);

                this.removeSmartRelated(stopLossLimitOrder);

            } catch (TraderNotFoundException exception) {

                exception.printStackTrace();

            }

        } else {

            ////System.out.println("this STOP LOSS did not trigger");
        }

    }

  @Transactional
  private void processBuyStopOrder(LimitOrder buyStopOrder) {

        System.out.println("enter > processBuyStopOrder");

        System.out.println("buyStopOrder /" + buyStopOrder);

        System.out.println("order.getStrike() /" + buyStopOrder.getStrike());

        System.out.println("order.getSecurity().getPrice() /" + buyStopOrder.getSecurity().getPrice());

        if (buyStopOrder.getSecurity().getPrice() > buyStopOrder.getStrike()) {

            System.out.println("TRIGGER A MARKET BUY B/C STRIKE CROSSED");

            notificationService.save(new Notification(
                            buyStopOrder.getTrader().getUsername(),
                            NotificationType.BUY_STOP_TRIGGER,
                            "LONG_BUY_STOP TRIGGERED / " + buyStopOrder.toString()
                    ));

            try {
                System.out.println("1");

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
                System.out.println("2");

                this.purgeLimitOrder(buyStopOrder);

                System.out.println("3");

                this.removeSmartRelated(buyStopOrder);

                System.out.println("4");

            } catch (TraderNotFoundException e) {

                e.printStackTrace();

            }

        } else {

            //System.out.println("this LONG_BUY_STOP didn't trigger");
        }
    }

  @Transactional
  private void qualifyTakeProfitOrder(LimitOrder takeProfitOrder) {

        System.out.println("enter > qualifyTakeProfitOrder");
        System.out.println();
        System.out.println("takeProfitOrder / " + takeProfitOrder);
        System.out.println();
        System.out.println("  current price / " + takeProfitOrder.getSecurity().getPrice());
        System.out.println("   strike price / " + takeProfitOrder.getStrike());
        System.out.println();

//todo, we shouldn't be compariing doubles like this
        if (takeProfitOrder.getSecurity().getPrice() >= takeProfitOrder.getStrike()) {

            System.out.println("IT'S TIME TO TRIGGER THIS TAKE PROFIT");

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

                securityPortfolioService.sellSecurityMarketPrice(marketSellStockRequest);

                this.purgeLimitOrder(takeProfitOrder);

                this.removeSmartRelated(takeProfitOrder);

                marketSellStockRequest = null;

            } catch (TraderNotFoundException exception) {

                exception.printStackTrace();

            }

        } else {

            System.out.println("this one didn't trigger");
        }

    }
//    @Transactional
//    public void truncateLimitOrders() {
//        limitOrderRepository.truncateTable();
//    }

   @Transactional
   private void removeSmartRelated(LimitOrder limitOrder) {
        /////////////////////////////////////////////////////////////////
        //System.out.println("removing the related");

        Optional<LimitOrder> limitOrderPartner = this.findLimitOrder(limitOrder.getPartnerID());

        if (limitOrderPartner.isPresent()) {

            LimitOrder relatedOrder = limitOrderPartner.get(); // Use the user object

            //System.out.println("related order / " + relatedOrder);
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

        limitOrder.setTrader(null);

        limitOrder.setSecurity(null);

        this.saveLimitOrder(limitOrder);

        this.deleteLimitOrder(limitOrder);

    }

  @Transactional
  private void deleteLimitOrder(LimitOrder limitOrder) {

        limitOrderRepository.delete(limitOrder);

//        notificationService.save(
//                new Notification(
//                        limitOrder.getTrader().getUsername(),
//                        NotificationType.NEW_LIMIT_ORDER_CREATED,
//                        "limit order cancelled / " + limitOrder.toString()
//                ));
    }

}
/*

 */
