$(document).ready(function () {

    console.log("list / cookies / all");

    console.log(Cookies.get());

});

var KEY_USERNAME="traderName";

function loginUser(userName) {

	console.debug("enter > loginUser");	

    console.log("list / cookies / before");

    console.log(Cookies.get());

    Cookies.set(KEY_USERNAME, $("#userName").val(), { path: '/' , expires: 365 })

    console.log("list / cookies / after");

    console.log(Cookies.get());

    console.log("list / cookies / ", KEY_USERNAME);

    console.log(Cookies.get(KEY_USERNAME));

	console.debug("exit < loginUser");	

    alert("you are now logged in as: " + Cookies.get(KEY_USERNAME));

    window.location.replace("../dashboard.html");

}
