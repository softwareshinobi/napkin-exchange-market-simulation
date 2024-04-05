
$(document).ready(function () {

  //alert("notification.js");

	// the first run starts after waiting first
	
displayAllActivityRecords();

	setInterval(displayAllActivityRecords,1000*30);
	
});


function displayAllActivityRecords() {

	console.debug(" -> :: displayAllActivityRecords()");	

	$.ajax({

		type: "GET",
		
		url: apiURL + "/notification/" + traderName(),

		contentType: "text/plain",
		
		crossDomain: true,				

		success: function (data, status, jqXHR) {
            
            setResultsArea(data);           

		},

		error: function (jqXHR, status) {

			console.log("Something Went wrong");
		
			console.log(jqXHR);

		}

	});

}

function setResultsArea(responseData) {

    var trHTML = '';

    for (var i =0;i< responseData.length; i++) {

    trHTML += '<tr>';

    trHTML += '<td class="METADATA DEBUG">' + responseData[i].id + '</td>';

    trHTML += '<td class="METADATA DEBUG">' + responseData[i].type + '</td>';

    trHTML += '<td class="METADATA DEBUG">' + responseData[i].description + '</td>';

    trHTML += '</tr>';	
		  
}

    $('#notifications > tbody').html(trHTML);     

    console.log("trHTML", trHTML);

}
