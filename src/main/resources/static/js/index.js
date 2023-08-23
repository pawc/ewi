$(document).ready(function(){

    var myNavBar = {
        flagAdd: true,
        elements: [],
        init: function (elements) {
            this.elements = elements;
        }
    };

    myNavBar.init(  [
        "header",
        "header-container",
        "brand"
    ]);

    function offSetManager(){
        var yOffset = 0;
        var currYOffSet = window.pageYOffset;
        if(yOffset < currYOffSet) {
        }

    }

    window.onscroll = function(e) {
        offSetManager();
    }

    offSetManager();

});