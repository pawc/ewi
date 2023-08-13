$(document).ready(() => {

    $('#categoriesLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#definitionsDropdownMenuLink').css("font-weight", "bold").css("text-decoration", "underline");

    $('#categoriesTable').DataTable({
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

function toggleCarriedOver(name){
    var headers = {};
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    headers["Content-Type"] = "application/json; charset=utf-8";
    headers[header] = token;

    var category = {
        name: name
    }

   $.ajax({
        url: contextRoot + 'toggleCarriedOver',
        type: 'PUT',
        data: JSON.stringify(category),
        headers: headers
    })
}

function addBtn(){
    $('#nazwa').val('')
    $("span.ui-dialog-title").text('Dodaj kategorię');
    dialog.dialog("open");
}

function deleteBtn(nazwa){
    var headers = {};
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    headers["Content-Type"] = "application/json; charset=utf-8";
    headers[header] = token;

    var category = {
        name: name
    }

    $.ajax({
        url: contextRoot + 'category',
        type: 'DELETE',
        data: JSON.stringify(category),
        headers: headers
    })
    .done(() => {
        window.location.href = contextRoot + "categories?delete=true"
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

                var category = {
                    name: $('#name').val()
                }

                $.ajax({
                    url: contextRoot + 'category',
                    type: 'POST',
                    data: JSON.stringify(category),
                    headers: headers
                })
                .done(() => {
                    window.location.href = contextRoot + "categories?success=true"
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