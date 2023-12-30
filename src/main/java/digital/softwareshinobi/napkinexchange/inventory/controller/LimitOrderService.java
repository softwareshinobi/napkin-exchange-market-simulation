package digital.softwareshinobi.napkinexchange.inventory.controller;

import lombok.AllArgsConstructor;
import digital.softwareshinobi.napkinexchange.account.exception.AccountNotFoundException;
import digital.softwareshinobi.napkinexchange.account.model.entity.Account;
import digital.softwareshinobi.napkinexchange.account.model.entity.LimitOrder;
import digital.softwareshinobi.napkinexchange.account.model.payload.BuyStockRequest;
import digital.softwareshinobi.napkinexchange.account.model.payload.SellStockRequest;
import digital.softwareshinobi.napkinexchange.account.repository.LimitOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LimitOrderService {

    @Autowired
    private final LimitOrderRepository limitOrderRepository;

    @Autowired
    private final StockOwnedService stockOwnedService;

    public void saveLimitOrder(LimitOrder limitOrder) {
        limitOrderRepository.save(limitOrder);
    }

    public void deleteLimitOrder(LimitOrder limitOrder) {
        limitOrderRepository.delete(limitOrder);
    }

    public List<LimitOrder> findLimitOrders() {
        System.out.println("findLimitOrdersfindLimitOrdersfindLimitOrders");
        return limitOrderRepository.findAll();
    }

    public List<LimitOrder> findLimitOrders(Account account) {
        return limitOrderRepository.findAll().stream()
                .filter(order -> order.getAccount().getUsername().equals(account.getUsername()))
                .collect(Collectors.toList());
    }

    public void processAllLimitOrders() {

        // System.out.println("enter > processAllLimitOrders");
        limitOrderRepository.findAll().forEach(limitOrder -> {

            System.out.println("limitOrder / " + limitOrder);

            String limitOrderType = limitOrder.getLimitOrderType();
            //   String limitOrderConstant = ;
            //   boolean equal = (limitOrderType.equals(limitOrderConstant));
            //  System.out.println("limitOrderType /" + limitOrderType);
            //  System.out.println("limitOrderConstant /" + limitOrderConstant);
            //   System.out.println("equal /" + equal);

            switch (limitOrderType) {

                case LimitOrderTypes.BUY_STOP:

                    System.out.println("is a buy stop");

                    processBuyStopLimitOrders(limitOrder);

                    break;

                case LimitOrderTypes.SELL_STOP_LOSS:

                    System.out.println("is a SELL_STOP_LOSS");

                    processSellStopLimitOrders(limitOrder);

                    break;

                case LimitOrderTypes.SELL_TAKE_PROFIT:

                    System.out.println("is a SELL_TAKE_PROFIT");

                    processTakeProfitLimitOrders(limitOrder);

                    break;

                default:

                    System.out.println("dont know how to handle this. what is it? /" + limitOrderType);

                    break;

            }

        });

        // System.out.println("exit < processAllLimitOrders");
    }

    private void processBuyStopLimitOrders(LimitOrder limitOrder) {

        System.out.println("enter > processBuyStopLimitOrders");

        System.out.println("limitOrder /" + limitOrder);

        System.out.println("order.getLimitPrice() /" + limitOrder.getLimitPrice());

        System.out.println("order.getStock().getPrice() /" + limitOrder.getStock().getPrice());

        if (limitOrder.getLimitPrice() < limitOrder.getStock().getPrice()) {

            System.out.println("IT'S TIME TO OPEN THIS TRADE");

            try {

                stockOwnedService.fillBuyStockRequest(
                        new BuyStockRequest(
                                limitOrder.getAccount().getUsername(),
                                limitOrder.getStock().getTicker(),
                                limitOrder.getSharesToBuy()));

                clearAndDeleteLimitOrder(limitOrder);

            } catch (AccountNotFoundException e) {

                e.printStackTrace();

            }

        } else {

            System.out.println("this one didn't trigger");

        }
    }

    private void processSellStopLimitOrders(LimitOrder limitOrder) {

        System.out.println("enter > processSellStopLimitOrders");

        System.out.println("limitOrder /" + limitOrder);

        System.out.println("order.getLimitPrice() /" + limitOrder.getLimitPrice());

        System.out.println("order.getStock().getPrice() /" + limitOrder.getStock().getPrice());

        //todo, we shouldn't be compariing doubles like this
        if (limitOrder.getStock().getPrice() < limitOrder.getLimitPrice()) {

            System.out.println("IT'S TIME TO TRIGGER THIS STOP LOSS");

            try {

                stockOwnedService.sellStockMarketPrice(
                        new SellStockRequest(
                                limitOrder.getAccount().getUsername(),
                                limitOrder.getStock().getTicker(),
                                limitOrder.getSharesToBuy()));

                clearAndDeleteLimitOrder(limitOrder);

            } catch (AccountNotFoundException e) {

                e.printStackTrace();

            }

        } else {

            System.out.println("this one didn't trigger");

        }

    }

    private void processTakeProfitLimitOrders(LimitOrder limitOrder) {

        System.out.println("enter > processTakeProfitLimitOrders");

        System.out.println("limitOrder /" + limitOrder);

        System.out.println("order.getLimitPrice() /" + limitOrder.getLimitPrice());

        System.out.println("order.getStock().getPrice() /" + limitOrder.getStock().getPrice());

        //todo, we shouldn't be compariing doubles like this
        if (limitOrder.getStock().getPrice() > limitOrder.getLimitPrice()) {

            System.out.println("IT'S TIME TO TRIGGER THIS STOP LOSS");

            try {

                stockOwnedService.sellStockMarketPrice(
                        new SellStockRequest(
                                limitOrder.getAccount().getUsername(),
                                limitOrder.getStock().getTicker(),
                                limitOrder.getSharesToBuy()));

                clearAndDeleteLimitOrder(limitOrder);

            } catch (AccountNotFoundException e) {

                e.printStackTrace();

            }

        } else {

            System.out.println("this one didn't trigger");

        }

    }
//    @Transactional
//    public void truncateLimitOrders() {
//        limitOrderRepository.truncateTable();
//    }

    private void clearAndDeleteLimitOrder(LimitOrder limitOrder) {
        limitOrder.setAccount(null);
        limitOrder.setStock(null);

        saveLimitOrder(limitOrder);
        deleteLimitOrder(limitOrder);
    }
}
