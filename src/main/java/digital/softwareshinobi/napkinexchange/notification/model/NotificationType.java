package digital.softwareshinobi.napkinexchange.notification.model;

public enum NotificationType {

    BUY_STOP_TRIGGER,
    ////////
    SYSTEM_NOTIFICATION,
    ////////
    NEW_TRADER_CREATED,
    ////////
    LONG_MARKET_BUY_CREATED,
    ////////
    LONG_SMART_BUY_CREATED,
    LONG_SMART_BUY_FULFILLED,
    LONG_SMART_BUY_CANCELLATION,
    ////////
    MARKET_BUY_ORDER_CREATED,
    MARKET_BUY_INSUFFICIENT_FUNDS,
    MARKET_BUY_ORDER_FULFILLED,
    ////////
    NEW_LIMIT_ORDER_CREATED,
    ////////
    LONG_STOP_LOSS_CREATED,
    LONG_STOP_LOSS_TRIGGERED,
    ////////
    LONG_TAKE_PROFIT_CREATED,
    LONG_TAKE_PROFIT_TRIGGERED,
    ////
    //  LONG_SELL_ORDER_CREATED,
    //  LONG_SELL_ORDER_FULFILLED,
    ////////
    MARKET_SELL_ORDER_CREATED,
    MARKET_SELL_ORDER_FULFILLED,

}
