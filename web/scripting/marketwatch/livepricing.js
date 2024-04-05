
$(document).ready(function () {

	setInterval(getLivePricing, 1000);

});

function getLivePricing(){

//    alert("getLisssvePricing");

console.debug(" -> :: getLivePricing()");	

$.ajax({

type: "GET",

url: apiURL + "/candlestick",

contentType: "text/plain",

crossDomain: true,				

success: function (data, status, jqXHR) {

console.log("huh?");

yo(data);

},

error: function (jqXHR, status) {
alert("leaw3333derbeeeeeeeoard.js");
console.log("Something Went wrong");

console.log(jqXHR);

}

});

}
function yo(responseData) {

console.log("price");

console.log(responseData);

var trHTML = '';

for (var i =0;i< responseData.length; i++) {

trHTML += '<tr>';

trHTML += '<td>' + responseData[i].ticker + '</td>';

trHTML += '<td>' + responseData[i].companyName + '</td>';

trHTML += '<td>' + responseData[i].price + '</td>';

trHTML += '</tr>';	

}

console.log(trHTML);

$('#livepricing > tbody').html(trHTML); 

console.log("ecirp");
}

