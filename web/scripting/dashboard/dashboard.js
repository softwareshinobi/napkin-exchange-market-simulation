$(document).ready(function () {

//alert("dashboard.hs");

    visualizeTraderDetails();

   // visualizeTraderOpenOrders();

});


function visualizeTraderDetails() {

	console.debug(" -> :: visualizeTraderDetails()");	

	$.ajax({

		type: "GET",
		
		url: apiURL + "/trader/" + traderName(),

		contentType: "text/plain",
		
		crossDomain: true,				

		success: function (data, status, jqXHR) {

            insertTradeDetailContent(data);           

		},

		error: function (exception, status) {

			console.log("error getting trader data / ", exception);

		}

	});

	console.debug(" <- :: fetchTraderDetails()");	

}


function insertTradeDetailContent(trader) {

//alert(trader);

console.log("trader / " + trader);

$('#sidebarTraderName').html(trader.username); 



$('#dropdownTraderName').html(trader.username); 


var formattedNumber = trader.accountBalance.toLocaleString('en-US', {
  style: 'currency',
  currency: 'USD',
});

$('#sidebarTraderBalance').html(formattedNumber); 

$('#dashboardAccountBalance').html(formattedNumber); 


var trHTML = '';

trHTML += '<tr>';

trHTML += '<td class="METADATA DEBUG">' +  + '</td>';

trHTML += '<td class="METADATA DEBUG">' + trader.accountBalance + '</td>';

trHTML += '<td class="METADATA DEBUG">' + trader.totalProfits + '</td>';

trHTML += '</tr>';	

for (var i =0;i< trader.stocksOwned.length; i++) {

trHTML += '<tr>';

trHTML += '<td class="METADATA DEBUG">' + '</td>';

trHTML += '</tr>';

trHTML += '<tr>';

trHTML += '<td class="METADATA DEBUG">' + '</td>';

trHTML += '</tr>';

trHTML += '<tr>';

trHTML += '<td class="METADATA DEBUG">' + trader.stocksOwned[i].ticker + '</td>';

trHTML += '<td class="METADATA DEBUG">' + trader.stocksOwned[i].amountOwned + '</td>';

trHTML += '<td class="METADATA DEBUG">' + trader.stocksOwned[i].costBasis + '</td>';

trHTML += '</tr>';	

}

$('#activity-display-table  > tbody').html(trHTML); 

console.log("trHTML", trHTML);

}


function visualizeTraderOpenOrders() {

	console.debug(" -> :: visualizeTraderOpenOrders()");	

	$.ajax({

		type: "GET",
		
		url: apiURL + "/inventory/orders/" + traderName(),

		contentType: "text/plain",
		
		crossDomain: true,				

		success: function (data, status, jqXHR) {

          	console.debug("data / "+ data);

            populateOrderDetailSections(data);           

		},

		error: function (jqXHR, status) {

			console.log("Something Went wrong");
		
			console.log(jqXHR);

		}

	});

	console.debug(" <- :: visualizeTraderOpenOrders()");	

}


function populateOrderDetailSections(trader) {

var trHTML = '';

trHTML += '<tr>';

trHTML += '<td class="METADATA DEBUG">' + trader.username + '</td>';

trHTML += '<td class="METADATA DEBUG">' + trader.accountBalance + '</td>';

trHTML += '<td class="METADATA DEBUG">' + trader.totalProfits + '</td>';

trHTML += '</tr>';	

for (var i =0;i< trader.length; i++) {

trHTML += '<tr>';

trHTML += '<td class="METADATA DEBUG">' + '</td>';

trHTML += '</tr>';

trHTML += '<tr>';

trHTML += '<td class="METADATA DEBUG">' + '</td>';

trHTML += '</tr>';

trHTML += '<tr>';

trHTML += '<td class="METADATA DEBUG">' + trader[i].stock.ticker + '</td>';
trHTML += '<td class="METADATA DEBUG">' + trader[i].stock.price + '</td>';
trHTML += '<td class="METADATA DEBUG">' + trader[i].sharesToBuy + '</td>';
trHTML += '<td class="METADATA DEBUG">' + trader[i].limitPrice + '</td>';
trHTML += '<td class="METADATA DEBUG">' + trader[i].limitOrderType + '</td>';


//trHTML += '<td class="METADATA DEBUG">' + trader.stocksOwned[i].amountOwned + '</td>';

//trHTML += '<td class="METADATA DEBUG">' + trader.stocksOwned[i].costBasis + '</td>';

trHTML += '</tr>';	

}

$('#activity-display-table  > tbody').html(trHTML); 

console.log("trHTML", trHTML);

}
