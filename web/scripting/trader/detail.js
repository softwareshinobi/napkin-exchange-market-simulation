$(document).ready(function () {

doWork();

setInterval(doWork,1000*8);

});

function doWork(){
    visualizeTraderOpenOrders();
    visualizeTraderDetails();



}

function visualizeTraderDetails() {

	console.debug(" -> :: visualizeTraderDetails()");	

	$.ajax({

		type: "GET",
		
		url: apiURL + "/trader/" + traderName(),

		contentType: "text/plain",
		
		crossDomain: true,				

		success: function (data, status, jqXHR) {
            
            insertTraderDetail(data);           

            insertTraderHoldings(data);

      //      insertTraderOrders(data);

		},

		error: function (jqXHR, status) {

			console.log("Something Went wrong");
		
			console.log(jqXHR);

		}

	});

}

function insertTraderDetail(responseData) {

var trHTML = '';

trHTML += '<tr>';

trHTML += '<td class="">' + responseData.username + '</td>';

trHTML += '<td class="">' + responseData.accountBalance + '</td>';

trHTML += '<td class="">' + responseData.totalProfits + '</td>';

trHTML += '</tr>';	

$('#profile1 > tbody').html(trHTML); 

console.log("trHTML", trHTML);

}

function insertTraderHoldings(responseData) {

var trHTML = '';

for (var i =0;i< responseData.stocksOwned.length; i++) {

trHTML += '<tr>';

trHTML += '<td class="">' + responseData.stocksOwned[i].ticker + '</td>';

trHTML += '<td class="">' + responseData.stocksOwned[i].amountOwned + '</td>';

trHTML += '<td class="">' + responseData.stocksOwned[i].costBasis + '</td>';

trHTML += '</tr>';	

}

$('#holdings > tbody').html(trHTML); 

console.log("trHTML", trHTML);

}


function visualizeTraderOpenOrders() {

	console.debug(" -> :: visualizeTraderOpenOrders()");	

	$.ajax({

		type: "GET",
		
		url: apiURL + "/broker/orders/" + traderName(),

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


function populateOrderDetailSections(responseData) {

var trHTML = '';

for (var i =0;i< responseData.length; i++) {

trHTML += '<tr>';

trHTML += '<td class="">' + responseData[i].id + '</td>';
trHTML += '<td class="">' + responseData[i].stock.ticker + '</td>';
trHTML += '<td class="">' + responseData[i].type + '</td>';
trHTML += '<td class="">' + responseData[i].sharesToBuy + '</td>';
trHTML += '<td class="">' + responseData[i].relatedOrderId + '</td>';
trHTML += '<td class="">' + responseData[i].strikePrice + '</td>';
trHTML += '<td class="">' + responseData[i].stock.price + '</td>';


trHTML += '</tr>';	

}

$('#orders > tbody').html(trHTML); 

console.log("trHTML", trHTML);

}
