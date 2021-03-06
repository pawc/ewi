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

        var row = $('<tr>').attr({
            class: 'norma'
        })

        var normaWartosc = $('<input>').attr({
            type: 'number',
            step: '0.01',
            class: 'form-control'
        })
        .css('width', '90px')

        var col1 = $('<td>')
        normaWartosc.appendTo(col1)
        col1.appendTo(row)

        var normaJednostka = $('<input>').attr({
            type: 'text',
            class: 'form-control',
            placeholder: 'jednostka'
        })

        var col2 = $('<td>')
        normaJednostka.appendTo(col2)
        col2.appendTo(row)

        var col3 = $('<td>')
        .css('text-align', 'center')

        var czyOgrzewanieCheck = $('<input>')
        .attr({
            type: 'checkbox',
            class: 'form-check-input'
        })
        .css('display', 'inline-block')
        .appendTo(col3)

        $('<label>').attr({
            class: 'form-check-label'
        })
        .text(' ogrzewanie')
        .appendTo(col3)

        col3.appendTo(row)

        var col4 = $('<td>')
        $('<button>').attr({
            class: 'btn usunNormaBtn',
        })
        .click(() => {
            row.remove()
        })
        .html('usuń')
        .appendTo(col4)

        col4.appendTo(row)

        row.appendTo($('#normyTable'))

    })

    $('.usunNormaBtn').on('click', function (){
        $(this).parents('tr').first().remove();
    });

    $('#error').hide();
    $('#numer').keypress(() => {
        $('#error').hide();
    })

})

var dialog, form, currentId, type

function edytujBtn(id){
    type = 'PUT'
    $("#normyTable > tbody > tr").remove();
    currentId = id

    $("span.ui-dialog-title").text('Edytuj maszynę');

    $.ajax({
        url: contextRoot + 'maszyna',
        data: {
            id: id
        }
    })
    .done(maszyna => {
        $("#numer").prop("disabled", true);
        $('#numer').val(maszyna.id)
        $('#nazwa').val(maszyna.nazwa)
        $('#opis').val(maszyna.opis)

        $.each($('input[name=kategorie]'), (i, k) => {
            $(k).prop('checked', false)
        })

        $.each(maszyna.kategorie, (i, kategoria) => {
             $.each($('input[name=kategorie]'), (j, k) => {
                 if(kategoria.nazwa == $(k).val()) $(k).prop('checked', true)
             })
        })

        $.each(maszyna.normy, (i, norma) => {

            var row = $('<tr>').attr({
                class: 'norma'
            })

            var normaWartosc = $('<input>').attr({
                type: 'number',
                step: '0.01',
                class: 'form-control'
            })
            .css('width', '90px')
            .prop('disabled', true)
            .val(norma.wartosc)

            var col1 = $('<td>')
            normaWartosc.appendTo(col1)
            col1.appendTo(row)

            var normaJednostka = $('<input>').attr({
                type: 'text',
                class: 'form-control',
                placeholder: 'jednostka'
            })
            .prop('disabled', true)
            .val(norma.jednostka)

            var col2 = $('<td>')
            normaJednostka.appendTo(col2)
            col2.appendTo(row)

            var col3 = $('<td>')
            .css('text-align', 'center')

            var czyOgrzewanieCheck = $('<input>')
            .attr({
                type: 'checkbox',
                class: 'form-check-input'
            })
            .prop('checked', norma.czyOgrzewanie)
            .prop('disabled', true)
            .css('display', 'inline-block')
            .appendTo(col3)

            $('<label>').attr({
                class: 'form-check-label'
            })
            .text('ogrzewanie')
            .appendTo(col3)

            col3.appendTo(row)

            row.appendTo($('#normyTable'))

        })
        dialog.dialog( "open" );
    })
    .fail(() => {
        alert('Bład podczas pobrania danych maszyny ' + id)
    })
}

function dodajBtn(){
    type = 'POST'
    $("#normyTable > tbody > tr").remove();
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
        width: 540,
        modal: true,
        buttons: {
            "Zapisz": function(){

                var normy = [];
                $('.norma').each(function(i, tr){
                    var wartosc = $(this).find('td:eq(0) > input').val()
                    var jednostka = $(this).find('td:eq(1) > input').val()
                    var czyOgrzewanie = $(this).find('td:eq(2) > input').prop('checked')
                    var norma = {
                        wartosc: wartosc,
                        jednostka: jednostka,
                        czyOgrzewanie: czyOgrzewanie
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

                var kategorie = []
                $('input[name=kategorie]').each((i, kategoria) => {
                    if($(kategoria).is(':checked')){
                        kategorie.push({
                            nazwa: $(kategoria).val()
                        })
                    }
                })

                var maszyna = {
                    id: $("#numer").val(),
                    nazwa: $('#nazwa').val(),
                    opis: $('#opis').val(),
                    normy: normy,
                    kategorie: kategorie
                }

                var headers = {};
                var token = $("meta[name='_csrf']").attr("content");
                var header = $("meta[name='_csrf_header']").attr("content");
                headers["Content-Type"] = "application/json; charset=utf-8";
                headers[header] = token;

                var preCalls = []
                if(type == 'POST'){
                    preCalls = [
                        $.ajax({
                            url: contextRoot + 'maszyna',
                            data: {
                                id: maszyna.id
                            },
                            headers: headers
                        })
                        .done((response) => {
                            if(response.id != null){
                                $('#error').show();
                                throw new Error('Maszyna o podanym numerze już istnieje w bazie')
                            }
                        })
                    ]
                }

                $.when.apply($, preCalls).then(() => {
                    $.ajax({
                        url: contextRoot + 'maszyna',
                        type: type,
                        data: JSON.stringify(maszyna),
                        headers: headers
                    })
                    .done(() => {
                        if(type == 'POST'){
                            window.location.href = contextRoot + "maszyny?success="+maszyna.id
                        }
                        else{
                            window.location.href = contextRoot + "maszyny"
                        }
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