
//
// keys and things
//

var KEY_USERNAME="traderName";

//
// backend string info. hack.
//

apiURL="https://apis.napkinexchange.softwareshinobi.digital";

// apiURL="http://localhost:8888";

$(document).ready(function () {
  
    console.log("list / cookies / all");

    console.log(Cookies.get());

});

function traderName() {

	console.debug("enter > traderName");	

    var traderName = Cookies.get(KEY_USERNAME);

    console.log("traderName / " + traderName);

    return traderName;

}
