let unitId = null

$(document).ready(() => {

    $('#unitsLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#definitionsDropdownMenuLink').css("font-weight", "bold").css("text-decoration", "underline");

    $('#unitsTable').DataTable({
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

function editBtn(id, name, weightRatio){
    $("span.ui-dialog-title").text('Edytuj jednostkę');
    unitId = id
    $('#name').val(name)
    $('#weightRatio').val(weightRatio)
    dialog.dialog("open");
}

function addBtn(){
    unitId = null
    $('#name').val('')
    $('#weightRatio').val('')
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

               var unit = {
                    id: unitId,
                    name: $('#name').val(),
                    weightRatio: $('#weightRatio').val()
                }

                var headers = {};
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                headers["Content-Type"] = "application/json; charset=utf-8";
                headers[header] = token;

                $.ajax({
                    url: contextRoot + 'unit',
                    type: 'POST',
                    data: JSON.stringify(unit),
                    headers: headers
                })
                .done(() => {
                    window.location.href = contextRoot + "unitsView?success="+unit.name
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