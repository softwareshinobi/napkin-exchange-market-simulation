
$(document).ready(function () {

    fetchNotifications();

    setInterval(fetchNotifications, 1000 * 10);

});

function fetchNotifications() {

    console.debug("enter > fetchNotifications()");

    $.ajax({

        type: "GET",

        url: apiURL + "/notification/" + traderName(),

        contentType: "text/plain",

        crossDomain: true,

        success: function (data, status, jqXHR) {

            displayNotifications(data);

        },

        error: function (exception, status) {

            console.error("error getting notifications / ", exception);

        }

    });

    console.debug("exit > fetchNotifications()");
}

function displayNotifications(notifications) {

    var content = '';

    for (var index = notifications.length - 1; index >= 0; index--) {

        content += '<tr>';

        content += '<td>' + notifications[index].id + '</td>';

        content += '<td>' + notifications[index].time + '</td>';

        content += '<td>' + notifications[index].type + '</td>';

        content += '<td>' + notifications[index].description + '</td>';

        content += '</tr>';

    }

    $('#notifications > tbody').html(content);

}
