package digital.softwareshinobi.napkinexchange.broker.service;

import digital.softwareshinobi.napkinexchange.broker.request.SellStockRequest;
import digital.softwareshinobi.napkinexchange.broker.types.LimitOrderTypes;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.model.NotificationType;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountNotFoundException;
import digital.softwareshinobi.napkinexchange.trader.model.Trader;
import digital.softwareshinobi.napkinexchange.broker.order.LimitOrder;
import digital.softwareshinobi.napkinexchange.trader.repository.LimitOrderRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
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

    public void saveLimitOrder(LimitOrder limitOrder) {

        //System.out.println("enter > saveLimitOrder");

        this.limitOrderRepository.save(limitOrder);

        if (limitOrder.getTrader() != null) {

            this.notificationService.save(
                    new Notification(
                            limitOrder.getTrader().getUsername(),
                            NotificationType.NEW_LIMIT_ORDER_CREATED,
                            limitOrder.toString()
                    ));

        }

    }

    public void processLimitOrders() {

        // //System.out.println("enter > processLimitOrders");
        this.limitOrderRepository.findAll().forEach(limitOrder -> {

            //System.out.println("limitOrder / " + limitOrder);

         //   final String limitOrderType = openLimitOrder.getType();
            //   String limitOrderConstant = ;
            //   boolean equal = (limitOrderType.equals(limitOrderConstant));
            //  //System.out.println("limitOrderType /" + limitOrderType);
            //  //System.out.println("limitOrderConstant /" + limitOrderConstant);
            //   //System.out.println("equal /" + equal);

            switch (limitOrder.getType()) {

//                case LimitOrderTypes.LONG_BUY_STOP:
//
//                    //System.out.println("is a LONG_BUY_STOP");
//
////                    processLongBuyStop(limitOrder);
//                    break;
                case LimitOrderTypes.LONG_STOP_LOSS:

                    //System.out.println("is a LONG_STOP_LOSS");

                    this.evaluateLimitOrderLongStopLoss(limitOrder);

                    break;

                case LimitOrderTypes.LONG_TAKE_PROFIT:

                    //System.out.println("is a LONG_TAKE_PROFIT");

                    this.evaluateLimitOrderLongTakeProfit(limitOrder);

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
        //System.out.println("  current price / " + stopLossOrder.getStock().getPrice());
        //System.out.println("   strike price / " + stopLossOrder.getStrike());
        //System.out.println();

//todo, we shouldn't be compariing doubles like this
        if (stopLossLimitOrder.getStock().getPrice() < stopLossLimitOrder.getStrike()) {

            //System.out.println("IT'S TIME TO TRIGGER THIS STOP LOSS");

            try {

                notificationService.save(
                        new Notification(
                                stopLossLimitOrder.getAccount().getUsername(),
                                NotificationType.LONG_STOP_LOSS_TRIGGERED,
                                stopLossLimitOrder
                        ));

                SellStockRequest sellStockRequest = new SellStockRequest(
                        stopLossLimitOrder.getAccount().getUsername(),
                        stopLossLimitOrder.getStock().getTicker(),
                        stopLossLimitOrder.getSharesToBuy()
                );

                securityPortfolioService.sellSecurityMarketPrice(sellStockRequest);

                this.clearAndDeleteLimitOrder(stopLossLimitOrder);

                this.removeSmartRelated(stopLossLimitOrder);

            } catch (AccountNotFoundException exception) {

                exception.printStackTrace();

            }

        } else {

            ////System.out.println("this STOP LOSS did not trigger");
        }

    }

    private void evaluateLimitOrderLongTakeProfit(LimitOrder takeProfitLimitOrder) {

        //System.out.println("enter > evaluateLimitOrderLongTakeProfit");

        //System.out.println();
        //System.out.println("take profit order / " + takeProfitOrder);
        //System.out.println();
        //System.out.println("  current price / " + takeProfitOrder.getStock().getPrice());
        //System.out.println("   strike price / " + takeProfitOrder.getStrike());
        //System.out.println();

        //todo, we shouldn't be compariing doubles like this
        if (takeProfitLimitOrder.getStock().getPrice() > takeProfitLimitOrder.getStrike()) {

            //System.out.println("IT'S TIME TO TRIGGER THIS TAKE PROFIT");

            try {

                notificationService.save(
                        new Notification(
                                takeProfitLimitOrder.getAccount().getUsername(),
                                NotificationType.LONG_TAKE_PROFIT_TRIGGERED,
                                takeProfitLimitOrder
                        ));

                SellStockRequest marketSellStockRequest = new SellStockRequest(
                        takeProfitLimitOrder.getAccount().getUsername(),
                        takeProfitLimitOrder.getStock().getTicker(),
                        takeProfitLimitOrder.getSharesToBuy());

                securityPortfolioService.sellSecurityMarketPrice(marketSellStockRequest);

                this.clearAndDeleteLimitOrder(takeProfitLimitOrder);

                this.removeSmartRelated(takeProfitLimitOrder);

                marketSellStockRequest =null;
                
            } catch (AccountNotFoundException exception) {

                exception.printStackTrace();

            }

        } else {

            //   //System.out.println("this one didn't trigger");
        }

    }
//    @Transactional
//    public void truncateLimitOrders() {
//        limitOrderRepository.truncateTable();
//    }

    private void removeSmartRelated(LimitOrder limitOrder) {
        /////////////////////////////////////////////////////////////////
        //System.out.println("removing the related");

        Optional<LimitOrder> relatedOrderOptional = this.findLimitOrder(limitOrder.getRelatedOrderId());

        if (relatedOrderOptional.isPresent()) {

            LimitOrder relatedOrder = relatedOrderOptional.get(); // Use the user object

            //System.out.println("related order / " + relatedOrder);

            notificationService.save(
                    new Notification(
                            relatedOrder.getAccount().getUsername(),
                            NotificationType.LONG_SMART_BUY_CANCELLATION,
                            relatedOrder
                    ));

            clearAndDeleteLimitOrder(relatedOrder);

        } else {
            // Handle the case where no relatedOrder is found
        }

/////////////////////////////////////////////////////////////////
    }

    private void clearAndDeleteLimitOrder(LimitOrder limitOrder) {

//        if (true) {
//            //     return;
//        }

        limitOrder.setTrader(null);

        limitOrder.setStock(null);

        saveLimitOrder(limitOrder);

        deleteLimitOrder(limitOrder);

    }

    private void deleteLimitOrder(LimitOrder limitOrder) {

        limitOrderRepository.delete(limitOrder);

//        notificationService.save(
//                new Notification(
//                        limitOrder.getAccount().getUsername(),
//                        NotificationType.NEW_LIMIT_ORDER_CREATED,
//                        "limit order cancelled / " + limitOrder.toString()
//                ));
    }

}
/*

    private void processLongBuyStop(LimitOrder buyStopOrder) {

        //System.out.println("enter > processLongBuyStopLimitOrder");

        //System.out.println("buyStopOrder /" + buyStopOrder);

        //System.out.println("order.getStrike() /" + buyStopOrder.getStrike());

        //System.out.println("order.getStock().getPrice() /" + buyStopOrder.getStock().getPrice());

        if (buyStopOrder.getStock().getPrice() > buyStopOrder.getStrike()) {

            //System.out.println("IT'S TIME TO OPEN THIS LONG_BUY_STOP");

            try {

                stockOwnedService.fillStandardBuyStockRequest(
                        new BuyStockRequest(
                                buyStopOrder.getAccount().getUsername(),
                                buyStopOrder.getStock().getTicker(),
                                buyStopOrder.getSharesToBuy()));

                notificationService.save(
                        new Notification(
                                buyStopOrder.getAccount().getUsername(),
                                NotificationType.BUYSTOPP,
                                "LONG_BUY_STOP TRIGGERED / " + buyStopOrder.toString()
                        ));

                clearAndDeleteLimitOrder(buyStopOrder);

            } catch (AccountNotFoundException e) {

                e.printStackTrace();

            }

        } else {

            //System.out.println("this LONG_BUY_STOP didn't trigger");

        }
    }

 */
