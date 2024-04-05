
$(document).ready(function () {

    fetchNotifications();

    setInterval(fetchNotifications, 1000 * 10);

});

function fetchNotifications() {

	console.debug(" enter > fetchNotifications()");

	$.ajax({

		type: "GET",
		
		url: apiURL + "/notification/" + traderName(),

		contentType: "text/plain",
		
		crossDomain: true,				

		success: function (data, status, jqXHR) {
            
            displayNotifications(data);           

		},

		error: function (jqXHR, status) {

			console.log("Something Went wrong");
		
			console.log(jqXHR);

		}

	});

}

function displayNotifications(notifications) {

    var content = '';

    for (var index = 0; index < notifications.length; index++) {

        content += '<tr>';

        content += '<td>' + notifications[index].id + '</td>';

        content += '<td>' + notifications[index].type + '</td>';

        content += '<td>' + notifications[index].description + '</td>';

        content += '</tr>';	

    }

        $('#notifications > tbody').html(content);     

        console.log("content", content);

}
