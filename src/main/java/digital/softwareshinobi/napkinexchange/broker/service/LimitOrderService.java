package digital.softwareshinobi.napkinexchange.broker.service;

import digital.softwareshinobi.napkinexchange.broker.request.SellStockRequest;
import digital.softwareshinobi.napkinexchange.broker.types.LimitOrderTypes;
import digital.softwareshinobi.napkinexchange.notification.model.Notification;
import digital.softwareshinobi.napkinexchange.notification.service.NotificationService;
import digital.softwareshinobi.napkinexchange.notification.model.NotificationType;
import digital.softwareshinobi.napkinexchange.trader.exception.AccountNotFoundException;
import digital.softwareshinobi.napkinexchange.trader.model.Account;
import digital.softwareshinobi.napkinexchange.trader.model.LimitOrder;
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
    private final StockOwnedService stockOwnedService;

    @Autowired
    private final NotificationService notificationService;

    public List<LimitOrder> findLimitOrders() {

        return limitOrderRepository.findAll();

    }

    public List<LimitOrder> findLimitOrders(Account account) {

        return limitOrderRepository.findAll().stream()
                .filter(order -> order.getAccount().getUsername().equals(account.getUsername()))
                .collect(Collectors.toList());

    }

    public Optional< LimitOrder> findLimitOrder(Integer id) {

        return limitOrderRepository.findById(id);

    }

    public void saveLimitOrder(LimitOrder limitOrder) {

        System.out.println("enter > saveLimitOrder");

        limitOrderRepository.save(limitOrder);

        if (limitOrder.getAccount() != null) {

            notificationService.save(
                    new Notification(
                            limitOrder.getAccount().getUsername(),
                            NotificationType.LIMITORDER,
                            "Created future or limit order created /  + limitOrder.toString()"
                    ));

        }

    }

    public void processLimitOrders() {

        // System.out.println("enter > processLimitOrders");
        limitOrderRepository.findAll().forEach(limitOrder -> {

            System.out.println("limitOrder / " + limitOrder);

            String limitOrderType = limitOrder.getType();
            //   String limitOrderConstant = ;
            //   boolean equal = (limitOrderType.equals(limitOrderConstant));
            //  System.out.println("limitOrderType /" + limitOrderType);
            //  System.out.println("limitOrderConstant /" + limitOrderConstant);
            //   System.out.println("equal /" + equal);

            switch (limitOrderType) {

                case LimitOrderTypes.LONG_BUY_STOP:

                    System.out.println("is a LONG_BUY_STOP");

//                    processLongBuyStop(limitOrder);
                    break;

                case LimitOrderTypes.LONG_STOP_LOSS:

                    System.out.println("is a LONG_STOP_LOSS");

                    processLongStopLoss(limitOrder);

                    break;

                case LimitOrderTypes.LONG_TAKE_PROFIT:

                    System.out.println("is a LONG_TAKE_PROFIT");

                    processLongTakeProfit(limitOrder);

                    break;

                default:

                    System.out.println("dont know how to handle this. what is it? /" + limitOrderType);

                    break;

            }

        });

        // System.out.println("exit < processLimitOrders");
    }

    private void processLongStopLoss(LimitOrder stopLossOrder) {

        System.out.println("enter > processLongStopLossOrder");

//        System.out.println("stop loss order / " + stopLossOrder);
//        System.out.println("  current price / " + stopLossOrder.getStock().getPrice());
//        System.out.println("   strike price / " + stopLossOrder.getStrikePrice());
//todo, we shouldn't be compariing doubles like this
        if (stopLossOrder.getStock().getPrice() < stopLossOrder.getStrikePrice()) {

            System.out.println("IT'S TIME TO TRIGGER THIS STOP LOSS");

            try {

                notificationService.save(
                        new Notification(
                                stopLossOrder.getAccount().getUsername(),
                                NotificationType.LONG_STOP_LOSS,
                                "STOP LOSS triggerd /  + stopLossOrder.toString()"
                        ));

                stockOwnedService.sellStockMarketPrice(
                        new SellStockRequest(
                                stopLossOrder.getAccount().getUsername(),
                                stopLossOrder.getStock().getTicker(),
                                stopLossOrder.getSharesToBuy()));

                clearAndDeleteLimitOrder(stopLossOrder);

                /////////////////////////////////////////////////////////////////
                System.out.println("removing the related");

                Optional<LimitOrder> relatedOrderOptional = this.findLimitOrder(stopLossOrder.getRelatedOrderId());

                if (relatedOrderOptional.isPresent()) {

                    LimitOrder relatedOrder = relatedOrderOptional.get(); // Use the user object

                    System.out.println("related order / " + relatedOrder);

                    notificationService.save(
                            new Notification(
                                    relatedOrder.getAccount().getUsername(),
                                    NotificationType.LONG_STOP_LOSS,
                                    "RELATED TRANSACTION CANCELLED /  + relatedOrder.toString()"
                            ));

                    clearAndDeleteLimitOrder(relatedOrder);

                } else {
                    // Handle the case where no relatedOrder is found
                }

/////////////////////////////////////////////////////////////////
            } catch (AccountNotFoundException exception) {

                exception.printStackTrace();

            }

        } else {

            //System.out.println("this STOP LOSS did not trigger");
        }

    }

    private void processLongTakeProfit(LimitOrder takeProfitOrder) {

        System.out.println("enter > processLongTakeProfit");

        System.out.println("take profit order / " + takeProfitOrder);
        System.out.println("  current price / " + takeProfitOrder.getStock().getPrice());
        System.out.println("   strike price / " + takeProfitOrder.getStrikePrice());

        //todo, we shouldn't be compariing doubles like this
        if (takeProfitOrder.getStock().getPrice() > takeProfitOrder.getStrikePrice()) {

            System.out.println("IT'S TIME TO TRIGGER THIS TAKE PROFIT");

            try {

                stockOwnedService.sellStockMarketPrice(
                        new SellStockRequest(
                                takeProfitOrder.getAccount().getUsername(),
                                takeProfitOrder.getStock().getTicker(),
                                takeProfitOrder.getSharesToBuy()));

                notificationService.save(
                        new Notification(
                                takeProfitOrder.getAccount().getUsername(),
                                NotificationType.TAKEPROFIT,
                                "STOP LOSS triggerd / " + takeProfitOrder.toString()
                        ));

                clearAndDeleteLimitOrder(takeProfitOrder);

            } catch (AccountNotFoundException exception) {

                exception.printStackTrace();

            }

        } else {

            //   System.out.println("this one didn't trigger");
        }

    }
//    @Transactional
//    public void truncateLimitOrders() {
//        limitOrderRepository.truncateTable();
//    }

    private void clearAndDeleteLimitOrder(LimitOrder limitOrder) {

        if (true) {
            //     return;
        }

        limitOrder.setAccount(null);

        limitOrder.setStock(null);

        saveLimitOrder(limitOrder);

        deleteLimitOrder(limitOrder);

    }

    private void deleteLimitOrder(LimitOrder limitOrder) {

        limitOrderRepository.delete(limitOrder);

//        notificationService.save(
//                new Notification(
//                        limitOrder.getAccount().getUsername(),
//                        NotificationType.LIMITORDER,
//                        "limit order cancelled / " + limitOrder.toString()
//                ));
    }

}
/*

    private void processLongBuyStop(LimitOrder buyStopOrder) {

        System.out.println("enter > processLongBuyStopLimitOrder");

        System.out.println("buyStopOrder /" + buyStopOrder);

        System.out.println("order.getStrikePrice() /" + buyStopOrder.getStrikePrice());

        System.out.println("order.getStock().getPrice() /" + buyStopOrder.getStock().getPrice());

        if (buyStopOrder.getStock().getPrice() > buyStopOrder.getStrikePrice()) {

            System.out.println("IT'S TIME TO OPEN THIS LONG_BUY_STOP");

            try {

                stockOwnedService.fillBuyStockRequest(
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

            System.out.println("this LONG_BUY_STOP didn't trigger");

        }
    }

 */
