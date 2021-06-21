$(document).ready(() => {

    $('#kategorieLink').css("font-weight", "bold");
    $('#kategorieLink').css("text-decoration", "underline");

    $('#kategorieTable').DataTable({
        "language": {
            "search": "Szukaj",
            "emptyTable": "Brak kategorii",
            "lengthMenu": "Pokaż _MENU_",
            "zeroRecords": "Nie znaleziono kategorii",
            "paginate": {
                "first": "Pierwsza",
                "last": "Ostatnia",
                "next": "Następna",
                "previous": "Poprzednia"
            },
            "info": "_START_ do _END_ z _TOTAL_ kategorii",
            "infoEmpty":      "",
            "infoFiltered":   ""
        }
    });

})

var dialog, form

function dodajBtn(){
    $('#nazwa').val('')
    $("span.ui-dialog-title").text('Dodaj kategorię');
    dialog.dialog("open");
}

function usunBtn(nazwa){
    var headers = {};
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    headers["Content-Type"] = "application/json; charset=utf-8";
    headers[header] = token;

    var kategoria = {
        nazwa: nazwa
    }

    $.ajax({
        url: contextRoot + 'kategoria',
        type: 'DELETE',
        data: JSON.stringify(kategoria),
        headers: headers
    })
    .done(() => {
        window.location.href = contextRoot + "kategorie?delete=true"
    })
    .fail(() => {
        alert('Problem z usunięciem kategorii')
    })
}

$(function() {

    dialog = $( "#dialog-form" ).dialog({
        autoOpen: false,
        height: 200,
        width: 300,
        modal: true,
        buttons: {
            "Zapisz": function(){

                var headers = {};
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                headers["Content-Type"] = "application/json; charset=utf-8";
                headers[header] = token;

                var kategoria = {
                    nazwa: $('#nazwa').val()
                }

                $.ajax({
                    url: contextRoot + 'kategoria',
                    type: 'POST',
                    data: JSON.stringify(kategoria),
                    headers: headers
                })
                .done(() => {
                    window.location.href = contextRoot + "kategorie?success=true"
                })
                .fail(() => {
                    alert('Problem z zapisem do bazy')
                })
            },
            Anuluj: function() {
                dialog.dialog("close");
            }
        }
    });

    form = dialog.find( "form" ).on( "submit", function( event ) {
        event.preventDefault();
    });

});