
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

    console.log("Something Went wrong");

    console.log(jqXHR);

}

});

}

function profitleaderboard(leaderBoardList) {

    var html = '';

    for (var i =0;i< leaderBoardList.length; i++) {

        html += '<tr>';

        html += '<td class="METADATA DEBUG">' + leaderBoardList[i].ranking + '</td>';  // Integer (ranking)
        html += '<td class="METADATA DEBUG">' + leaderBoardList[i].username + '</td>';  // String (username)
        html += '<td class="METADATA DEBUG">' + "$" + leaderBoardList[i].accountBalance.toLocaleString() + '</td>';  // Number (account balance) with 2 decimal places
        html += '<td class="METADATA DEBUG">' + "$" + leaderBoardList[i].totalProfits.toLocaleString() + '</td>';  // Number (total profits) with 2 decimal places

        html += '</tr>';

    }

    $('#leaderboard > tbody').html(html); 

}
