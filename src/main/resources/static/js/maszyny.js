$(document).ready(() => {
    $('#maszynyLink').css("font-weight", "bold");
    $('#maszynyLink').css("text-decoration", "underline");

    $('#maszynyTable').DataTable({
        "language": {
            "search": "Szukaj",
            "emptyTable": "Brak maszyn",
            "lengthMenu": "Pokaż _MENU_",
            "zeroRecords": "Nie znaleziono maszyn",
            "paginate": {
                "first": "Pierwsza",
                "last": "Ostatnia",
                "next": "Następna",
                "previous": "Poprzednia"
            },
            "info": "_START_ do _END_ z _TOTAL_ maszyn",
            "infoEmpty":      "",
            "infoFiltered":   ""
        }
    });

    $('#dodajNormaBtn').click(() => {
        $('#normyTable').append('\
            <tr class="norma">\
                <td><input type="number" step="0.01" class="form-control" style="width: 90px;"></input></td>\
                <td><input type="text" class="form-control" placeholder="jednostka"></input></td>\
                <td><button class="btn usunNormaBtn" onclick="$(this).parents(\'tr\').first().remove()">usuń</button></td>\
            </tr>\
        ')
    })

    $('.usunNormaBtn').on('click', function (){
        $(this).parents('tr').first().remove();
    });

})

var dialog, form, currentId, type

function edytujBtn(id){
    type = 'PUT'
    $("#normyTable > tr").remove();
    currentId = id

    $("span.ui-dialog-title").text('Edytuj maszynę');

    $.ajax({
        url: '/maszyna/'+id
    })
    .done(maszyna => {
        $("#numer").prop("disabled", true);
        $('#numer').val(maszyna.id)
        $('#nazwa').val(maszyna.nazwa)
        $('#opis').val(maszyna.opis)
        $.each(maszyna.normy, (i, norma) => {
            $('#normyTable').append('\
                <tr class="norma">\
                    <td><input type="numeric" step="0.01" id="'+norma.id+'-wartosc" class="form-control" style="width: 60px;" disabled></input></td>\
                    <td><input type="text" id="'+norma.id+'-jednostka" class="form-control" placeholder="jednostka" disabled></input></td>\
                </tr>\
            ')
            $('#'+norma.id+'-wartosc').val(norma.wartosc)
            $('#'+norma.id+'-jednostka').val(norma.jednostka)
        })
        dialog.dialog( "open" );
    })
    .fail(() => {
        alert('Bład podczas pobrania danych maszyny ' + id)
    })
}

function dodajBtn(){
    type = 'POST'
    $("#normyTable > tr").remove();
    currentId = 0
    $("#numer").prop("disabled", false);
    $("#numer").val('')
    $('#nazwa').val('')
    $('#opis').val('')
    $("span.ui-dialog-title").text('Dodaj maszynę');
    dialog.dialog("open");
}

$(function() {

    dialog = $( "#dialog-form" ).dialog({
        autoOpen: false,
        height: 570,
        width: 430,
        modal: true,
        buttons: {
            "Zapisz": function(){

                var normy = [];
                $('.norma').each(function(i, tr){
                    var wartosc = $(this).find('td:eq(0) > input').val()
                    var jednostka = $(this).find('td:eq(1) > input').val()
                    var norma = {
                        wartosc: wartosc,
                        jednostka: jednostka
                    }

                    if(jednostka && wartosc){

                        // unikalność jednostki
                        var test = true
                        $.each(normy, (i, j) => {
                            if(jednostka == j.jednostka) test = false
                        })
                        if(test) normy.push(norma);

                    }
                })

                var maszyna = {
                    id: $("#numer").val(),
                    nazwa: $('#nazwa').val(),
                    opis: $('#opis').val(),
                    normy: normy
                }

                var headers = {};
                headers["Content-Type"] = "application/json; charset=utf-8";

                var preCalls = []
                if(type == 'POST'){
                    preCalls = [
                        $.ajax({
                            url: '/maszyna/' + maszyna.id,
                            headers: headers
                        })
                        .done((response) => {
                            if(response.id != null){
                                alert('Maszyna o podanym numerze już istnieje w bazie');
                                throw new Error('Maszyna o podanym numerze już istnieje w bazie')
                            }
                        })
                    ]
                }

                $.when.apply($, preCalls).then(() => {
                    $.ajax({
                        url: '/maszyna',
                        type: type,
                        data: JSON.stringify(maszyna),
                        headers: headers
                    })
                    .done(() => {
                        window.location.reload()
                    })
                    .fail(() => {
                        alert('Problem z zapisem do bazy')
                    })
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