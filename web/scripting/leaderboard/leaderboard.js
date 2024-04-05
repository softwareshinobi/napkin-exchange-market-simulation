
$(document).ready(function () {

//    alert("overlord actual");

fetchLeaderboardDetails();

setInterval(fetchLeaderboardDetails, 1000*10);

});

function fetchLeaderboardDetails() {

console.debug(" -> :: fetchLeaderboardDetails()");	

$.ajax({

type: "GET",

url: apiURL + "/leaderboard/",

contentType: "text/plain",

crossDomain: true,				

success: function (data, status, jqXHR) {

console.log("huh?");

profitleaderboard(data);

},

error: function (jqXHR, status) {
alert("leaw3333derbeeeeeeeoard.js");
console.log("Something Went wrong");

console.log(jqXHR);

}

});

}

function profitleaderboard(responseData) {

console.log("yo profitleaderboard");

console.log(responseData);

var trHTML = '';

for (var i =0;i< responseData.length; i++) {

trHTML += '<tr>';

trHTML += '<td class="METADATA DEBUG">' + responseData[i].ranking + '</td>';

trHTML += '<td class="METADATA DEBUG">' + responseData[i].username + '</td>';

trHTML += '<td class="METADATA DEBUG">' + responseData[i].accountBalance + '</td>';

trHTML += '<td class="METADATA DEBUG">' + responseData[i].totalProfits + '</td>';

trHTML += '</tr>';	

}

console.log(trHTML);

$('#leaderboard > tbody').html(trHTML); 

console.log("oy");
}
