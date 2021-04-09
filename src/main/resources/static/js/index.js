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

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var headers = {};
    headers[header] = token;

    $('#logout').click(() => {
        $.ajax({
            url: '/logout',
            type: 'POST',
            headers: headers
        })
        .done(() => {
            window.location.href = '/'
        })
    })

});