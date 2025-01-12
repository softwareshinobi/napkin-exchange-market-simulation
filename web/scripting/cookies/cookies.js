
//
// keys and things
//

const KEY_USERNAME="traderName";

var apiSymbol="CALLISTO"

//
// backend string info. hack.
//

apiURL="https://apis.napkinexchange.softwareshinobi.com";

//apiURL="http://localhost:8888";

$(document).ready(function () {
  
    //console.log("list / cookies / all");

    //console.log(Cookies.get());

});

function traderName() {

	console.debug("enter > traderName");	

    var traderName = Cookies.get(KEY_USERNAME);

    console.log("traderName / " + traderName);

    return traderName;

}



