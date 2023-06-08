let jednostkaId = null

$(document).ready(() => {

    $('#jednostkiLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#definicjeDropdownMenuLink').css("font-weight", "bold").css("text-decoration", "underline");

    $('#jednostkiTable').DataTable({
        "language": {
            "search": "Szukaj",
            "emptyTable": "Brak jednostek",
            "lengthMenu": "Pokaż _MENU_",
            "zeroRecords": "Nie znaleziono jednostek",
            "paginate": {
                "first": "Pierwsza",
                "last": "Ostatnia",
                "next": "Następna",
                "previous": "Poprzednia"
            },
            "info": "_START_ do _END_ z _TOTAL_ jednostek",
            "infoEmpty":      "",
            "infoFiltered":   ""
        }
    });

})

var dialog, form

function edytujBtn(id, nazwa, waga){
    $("span.ui-dialog-title").text('Edytuj jednostkę');
    jednostkaId = id
    $('#nazwa').val(nazwa)
    $('#waga').val(waga)
    dialog.dialog("open");
}

function dodajBtn(){
    jednostkaId = null
    $('#nazwa').val('')
    $('#waga').val('')
    $("span.ui-dialog-title").text('Dodaj jednostkę');
    dialog.dialog("open");
}

$(function() {

    dialog = $( "#dialog-form" ).dialog({
        autoOpen: false,
        height: 260,
        width: 210,
        modal: true,
        buttons: {
            "Zapisz": function(){

               var jednostka = {
                    id: jednostkaId,
                    nazwa: $('#nazwa').val(),
                    waga: $('#waga').val()
                }

                var headers = {};
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                headers["Content-Type"] = "application/json; charset=utf-8";
                headers[header] = token;

                $.ajax({
                    url: contextRoot + 'jednostka',
                    type: 'POST',
                    data: JSON.stringify(jednostka),
                    headers: headers
                })
                .done(() => {
                    window.location.href = contextRoot + "jednostki?success="+jednostka.nazwa
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