package digital.softwareshinobi.napkinexchange.broker.service;

import digital.softwareshinobi.napkinexchange.broker.request.SecuritySellRequest;
import digital.softwareshinobi.napkinexchange.broker.types.LimitOrderType;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.model.NotificationType;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.trader.exception.TraderNotFoundException;
import digital.softwareshinobi.napkinexchange.broker.order.LimitOrder;
import digital.softwareshinobi.napkinexchange.broker.request.SecurityBuyRequest;
import digital.softwareshinobi.napkinexchange.trader.repository.LimitOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LimitOrderEvaluationService {

    @Autowired
    private final LimitOrderRepository limitOrderRepository;

    @Autowired
    private final SecurityPortfolioService securityPortfolioService;

    @Autowired
    private final NotificationService notificationService;

    protected void processLimitOrders() {

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

    protected void evaluateLimitOrderLongStopLoss(LimitOrder stopLossLimitOrder) {

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

                notificationService.save(
                        new Notification(
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

    protected void processBuyStopOrder(LimitOrder buyStopOrder) {

        System.out.println("enter > processBuyStopOrder");

        System.out.println("buyStopOrder /" + buyStopOrder);

        System.out.println("order.getStrike() /" + buyStopOrder.getStrike());

        System.out.println("order.getSecurity().getPrice() /" + buyStopOrder.getSecurity().getPrice());

        if (buyStopOrder.getSecurity().getPrice() > buyStopOrder.getStrike()) {

            System.out.println("TRIGGER A MARKET BUY B/C STRIKE CROSSED");

            notificationService.save(
                    new Notification(
                            buyStopOrder.getTrader().getUsername(),
                            NotificationType.BUY_STOP_TRIGGER,
                            "LONG_BUY_STOP TRIGGERED / " + buyStopOrder.toString()
                    ));

            try {
                System.out.println("1");

                this.securityPortfolioService.fillBuyMarketStockRequest(
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

    protected void evaluateLimitOrderLongTakeProfit(LimitOrder takeProfitLimitOrder) {

        //System.out.println("enter > evaluateLimitOrderLongTakeProfit");
        //System.out.println();
        //System.out.println("take profit order / " + takeProfitOrder);
        //System.out.println();
        //System.out.println("  current price / " + takeProfitOrder.getSecurity().getPrice());
        //System.out.println("   strike price / " + takeProfitOrder.getStrike());
        //System.out.println();
        //todo, we shouldn't be compariing doubles like this
        if (takeProfitLimitOrder.getSecurity().getPrice() > takeProfitLimitOrder.getStrike()) {

            //System.out.println("IT'S TIME TO TRIGGER THIS TAKE PROFIT");
            try {

                notificationService.save(
                        new Notification(
                                takeProfitLimitOrder.getTrader().getUsername(),
                                NotificationType.LONG_TAKE_PROFIT_TRIGGERED,
                                takeProfitLimitOrder
                        ));

                SecuritySellRequest marketSellStockRequest = new SecuritySellRequest(
                        takeProfitLimitOrder.getTrader().getUsername(),
                        takeProfitLimitOrder.getSecurity().getTicker(),
                        takeProfitLimitOrder.getUnits());

                securityPortfolioService.sellSecurityMarketPrice(marketSellStockRequest);

                this.purgeLimitOrder(takeProfitLimitOrder);

                this.removeSmartRelated(takeProfitLimitOrder);

                marketSellStockRequest = null;

            } catch (TraderNotFoundException exception) {

                exception.printStackTrace();

            }

        } else {

            //   //System.out.println("this one didn't trigger");
        }

    }

}
/*

 */
