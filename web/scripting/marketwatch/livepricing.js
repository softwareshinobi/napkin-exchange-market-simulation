
$(document).ready(function () {

	setInterval(fetchLivePricing, 1000);

});

function fetchLivePricing(){

    console.debug("enter > fetchLivePricing()");	

    $.ajax({

        type: "GET",

        url: apiURL + "/candlestick",

        contentType: "text/plain",

        crossDomain: true,				

        success: function (data, status, jqXHR) {

            displayLivePricing(data);

        }, error: function (exception, status) {

            console.log("error / " + exception);

        }

    });

}

function displayLivePricing(priceData) {

    console.log("priceData / " + priceData);

    var html = '';

    for (var index = 0; index < priceData.length; index++) {

        html += '<tr>';

        html += '<td>' + priceData[index].ticker + '</td>';

        html += '<td>' + priceData[index].companyName + '</td>';

        html += '<td>' + priceData[index].price + '</td>';

        html += '</tr>';	

    }

    console.log(html);

    $('#livepricing > tbody').html(html); 

}
