
$(document).ready(function () {

	//alert("balances!");

    visualizeAccountBalances();

    setInterval(visualizeAccountBalances, 1000 * 10);

});



function visualizeAccountBalances() {

    console.debug(" -> :: visualizeAccountBalances()");

    $.ajax({

        type: "GET",

        url: apiURL + "/trader/" + traderName(),

        contentType: "text/plain",

        crossDomain: true,

        success: function (data, status, jqXHR) {
//
            console.warn(data);
//
//alert("worsssk");

            //   insertTraderDetail(data);

            injectAccountBalanceData(data);

            //      insertTraderOrders(data);

        },

        error: function (exception, status) {

            alert("eorrrrrrrrrrrrrr?");

            console.error("error fetting trader details / ", exception);

        }

    });

}

function injectAccountBalanceData(trader) {

    console.debug(" -> :: injectAccountBalanceData()");

//    console.warn("html / ", html);

   var formattedNumberTraderAccountValue = trader.accountValue.toLocaleString('en-US', {
        style: 'currency',
        currency: 'USD',
    });

    $('#traderAccountValue').html(formattedNumberTraderAccountValue);

////////

   var formattedPortfolioValue = trader.portfolioValue.toLocaleString('en-US', {
        style: 'currency',
        currency: 'USD',
    });

    $('#traderPortfolioValue').html(formattedPortfolioValue);

////////
    
   var formattedAccountAvailable = trader.accountBalance.toLocaleString('en-US', {
        style: 'currency',
        currency: 'USD',
    });

    $('#traderAccountAvailable').html(formattedAccountAvailable);

//    $("#traderAccountAvailable").html(trader.accountBalance);

///////

var pct = (1.0 - trader.utilizationPercentage) * 100;


const formatter = new Intl.NumberFormat('en-US', {
   minimumFractionDigits: 1,      
   maximumFractionDigits: 1,
});

console.log(); // "2.01"
    $("#traderAccountUsage").html(formatter.format(100*trader.utilizationPercentage)+"%");

}
