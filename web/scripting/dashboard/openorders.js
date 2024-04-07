
$(document).ready(function () {

    visualizeTraderOpenOrders();

    setInterval(visualizeTraderOpenOrders, 1000 * 14);

});

function visualizeTraderOpenOrders() {

    console.debug(" -> :: visualizeTraderOpenOrders()");

    $.ajax({

        type: "GET",

        url: apiURL + "/broker/orders/" + traderName(),

        contentType: "text/plain",

        crossDomain: true,

        success: function (data, status, jqXHR) {

            console.debug("data / " + data);

            populateOrderDetailSections(data);

        },

        error: function (jqXHR, status) {

            console.log("Something Went wrong");

            console.log(jqXHR);

        }

    });

    console.debug(" <- :: visualizeTraderOpenOrders()");

}

function populateOrderDetailSections(responseData) {

    var html = '';

    for (var i = 0; i < responseData.length; i++) {

        html += '<tr>';

        html += '<td class="">' + responseData[i].id + '</td>';
        html += '<td class="">' + responseData[i].stock.ticker + '</td>';
        html += '<td class="">' + responseData[i].type + '</td>';
        html += '<td class="">' + responseData[i].sharesToBuy + '</td>';
        html += '<td class="">' + responseData[i].relatedOrderId + '</td>';
        html += '<td class="">' + responseData[i].strikePrice + '</td>';
        html += '<td class="">' + responseData[i].stock.price + '</td>';


        html += '</tr>';

    }

    $('#orders > tbody').html(html);

    console.log("html", html);

}

//function populateSidebarAccountDetails() {
//
//   visualizeTraderOpenOrders();
//
//   alert("asdf");
//
//  visualizeTraderDetails();
//
//}
//
//function visualizeTraderDetails() {
//    console.error("dddd");
//    console.debug(" -> :: visualizeTraderDetails()");
//    alert("worsssk");
//
//    $.ajax({
//
//        type: "GET",
//
//        url: apiURL + "/trader/" + traderName(),
//
//        contentType: "text/plain",
//
//        crossDomain: true,
//
//        success: function (data, status, jqXHR) {
//
//            insertTraderDetail(data);
//
//            insertTraderHoldings(data);
//            alert("worsssk");
//
//            //      insertTraderOrders(data);
//
//        },
//
//        error: function (exception, status) {
//
//            alert("huh?");
//
//            console.error("error fetting trader details / ", exception);
//
//        }
//
//    });
//
//}

//function insertTraderDetail(responseData) {
//
//    var html = '';
//
//    html += '<tr>';
//
//    html += '<td class="">' + responseData.username + '</td>';
//
//    html += '<td class="">' + responseData.accountBalance + '</td>';
//
//    html += '<td class="">' + responseData.totalProfits + '</td>';
//
//    html += '</tr>';
//
//    $('#profile1 > tbody').html(html);
//
//    console.log("html", html);
//
//}
//
//function insertTraderHoldings(responseData) {
//
//    alert("hello");
//
//    var html = '';
//
//    for (var i = 0; i < responseData.stocksOwned.length; i++) {
//
//        html += '<tr>';
//
//        html += '<td class="">' + responseData.stocksOwned[i].ticker + '</td>';
//
//        html += '<td class="">' + responseData.stocksOwned[i].amountOwned + '</td>';
//
//        html += '<td class="">' + responseData.stocksOwned[i].costBasis + '</td>';
//
//        html += '</tr>';
//
//    }
//
//    $('#holdings > tbody').html(html);
//
//    console.log("html", html);
//
//}

