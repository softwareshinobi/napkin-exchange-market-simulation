$(document).ready(function () {
  
  console.log("list / cookies / all");

    console.log(Cookies.get());

  //  console.log("list / cookies / all");

  //  console.log(Cookies.get());

  //  currentTraderUsername=Cookies.get('trader-username');

// alert("currentTraderUsername / "+currentTraderUsername);
 //   displayAllActivityRecords();

});

var KEY_USERNAME="traderName";

function traderName() {

	console.debug("enter > traderName");	

    var traderName = Cookies.get(KEY_USERNAME);

    console.log("traderName / " + traderName);

    return traderName;

}
apiURL="http://localhost:8888";

//currentTraderUsername="";



/*
function s444aveUsername() {

	console.debug(" -> :: saveUsername()");	

    console.log("list / cookies / before");

    console.log(Cookies.get());

    Cookies.set('trader-username', $("#current-trader-username").val(), { path: '/' , expires: 365 })

    console.log("list / cookies / after");

    console.log(Cookies.get());

    console.log("list / cookies / target");

    console.log(Cookies.get('trader-username'));

    console.debug(" <- :: saveUsername()");	

}
*/
