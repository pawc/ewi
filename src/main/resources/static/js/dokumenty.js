var type = ''
var dialog, dialogUsun, t
var currentNumer

$(document).ready(() => {
    $('#dokumentyLink').css("font-weight", "bold");
    $('#dokumentyLink').css("text-decoration", "underline");

   t = $('#dokumentyTable').DataTable({
        "language": {
            "search": "Szukaj",
            "emptyTable": "Brak dokumentów",
            "lengthMenu": "Pokaż _MENU_",
            "zeroRecords": "Nie znaleziono dokumentów",
            "paginate": {
                "first": "Pierwsza",
                "last": "Ostatnia",
                "next": "Następna",
                "previous": "Poprzednia"
            },
            "info": "_START_ do _END_ z _TOTAL_ dokumentów",
            "infoEmpty":      "",
            "infoFiltered":   "",
        }
    });

    t.order([1, 'desc']).draw()

    $('#maszyna').change(() => {
        $("#zuzycieTable > tr").remove();
        var maszynaId = $('#maszyna option:selected').val();
        $.ajax({
            url: contextRoot + 'maszyna',
            data: {
                id: maszynaId
            }
        })
        .done(maszyna => {
            $.each(maszyna.normy, (i, norma) => {
                $('#zuzycieTable').append('\
                <tr class="zuzycie">\
                    <td><input type="number" step="0.01" id="'+norma.id+'" normaId="'+norma.id+'" class="form-control" style="width: 90px;"></input></td>\
                    <td><span>[h] * <b>'+norma.wartosc+'</b> ['+norma.jednostka+'] = </span><b><span id="wynik-'+norma.id+'"></span></b></td>\
                </tr>\
                ')
                $('#'+norma.id).keyup(() => {
                    var wpisana = $('#'+norma.id).val()
                    var wynik = Math.round(((wpisana * norma.wartosc) + Number.EPSILON) * 100)/100
                    $('#wynik-'+norma.id).text(wynik);
                });
            })
        })
    })

    $('#error').hide();
    $('#numer').keypress(() => {
        $('#error').hide();
    })

})

function edytujBtn(numer){
    $("span.ui-dialog-title").text('Edytuj dokument');
    type = 'PUT'

    var headers = {};
    headers["Content-Type"] = "application/json; charset=utf-8";

    $.ajax({
        url: contextRoot + 'dokument',
        data: {
            'numer': numer
        },
        headers: headers
    })
    .done(dokument => {
        $("#zuzycieTable > tr").remove();
        $("#numer").prop("disabled", true);
        $('#maszyna').prop("disabled", true);
        $('#numer').val(dokument.numer)
        $('#data').val(dokument.data)
        $("#maszyna option[value='"+dokument.maszyna.id.toString()+"']").attr("selected", "selected");
        $.each(dokument.zuzycie, (i, zuzycie) => {
            $('#zuzycieTable').append('\
            <tr class="zuzycie">\
                <td><input type="number" step="0.01" id="'+zuzycie.id+'" normaId="'+zuzycie.norma.id+'" class="form-control" style="width: 90px;"></input></td>\
                <td><span>[h] * <b>'+zuzycie.norma.wartosc+'</b> ['+zuzycie.norma.jednostka+'] = </span><b><span id="wynik-'+zuzycie.norma.id+'"></span></b></td>\
            </tr>\
            ')
            $('#'+zuzycie.id).val(zuzycie.wartosc);
            var wynik = Math.round(((zuzycie.wartosc * zuzycie.norma.wartosc) + Number.EPSILON) * 100)/100
            $('#wynik-'+zuzycie.norma.id).text(wynik);
            $('#'+zuzycie.id).keyup(() => {
                var wpisana = $('#'+zuzycie.id).val()
                var wynik = Math.round(((wpisana * zuzycie.norma.wartosc) + Number.EPSILON) * 100)/100
                $('#wynik-'+zuzycie.norma.id).text(wynik);
            });
        })
        dialog.dialog( "open" );
    })
    .fail(() => {
        alert('Bład podczas pobrania danych dokumentu ' + numer)
    })
}

function usunBtn(numer){
    currentNumer = numer
    $("span.ui-dialog-title").text('Usuń dokument');
    $('#dialog-confirm-p').text('Czy na pewno usunąć dokument o numerze ' + numer + ' ?');
    dialogUsun.dialog("open");
}

function dodajBtn(){
    $("#zuzycieTable > tr").remove();
    $("span.ui-dialog-title").text('Dodaj dokument');
    $('#maszyna').prop("disabled", false);
    $('#numer').prop("disabled", false);
    $('#numer').val('');
    $('#data').val('');
    $('#maszyna').val(-1);
    type = 'POST'
    dialog.dialog("open");
}

$(function() {

    dialogUsun = $("#dialog-confirm").dialog({
        autoOpen: false,
        resizable: false,
        height: "auto",
        width: 400,
        modal: true,
        buttons: {
            "Tak": function() {
                var headers = {};
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");
                headers[header] = token;
                $.ajax({
                    url: contextRoot + 'dokument',
                    data: {
                        numer: currentNumer
                    },
                    type: 'DELETE',
                    headers: headers
                })
                .done(() => {
                    window.location.reload()
                })
                .fail(() => {
                    alert('Problem z usunięciem dokumentu o numerze ' + currentNumer)
                })
            },
            Anuluj: function() {
                $(this).dialog( "close" );
            }
        }
    });

    dialog = $( "#dialog-form" ).dialog({
        autoOpen: false,
        height: 490,
        width: 610,
        modal: true,
        buttons: {
            "Zapisz": function(){

                 var zuzycie = []
                 $('.zuzycie').each(function(i, tr){
                    var wartosc = $(this).find('td:eq(0) > input').val()
                    var zuzycieId = $(this).find('td:eq(0) > input').attr('ID')
                    var normaId = $(this).find('td:eq(0) > input').attr('normaId')
                    var z = {
                        wartosc: wartosc,
                        id: zuzycieId,
                        norma: {
                            id: normaId
                        }
                    }
                    zuzycie.push(z)
                })

                var dokument = {
                    numer: $('#numer').val(),
                    data: $('#data').val(),
                    maszyna: {
                        id: $('#maszyna option:selected').val()
                    },
                    zuzycie: zuzycie
                }

                var headers = {};
                headers["Content-Type"] = "application/json; charset=utf-8";
                var header = $("meta[name='_csrf_header']").attr("content");
                var token = $("meta[name='_csrf']").attr("content");
                headers[header] = token;

                var preCalls = []
                if(type == 'POST'){
                    preCalls = [
                        $.ajax({
                            url: contextRoot + 'dokument',
                            data: {
                                numer: dokument.numer
                            },
                            headers: headers
                        })
                        .done((response) => {
                            if(response.numer != null){
                                $('#error').show();
                                throw new Error('Dokument o podanym numerze już istnieje')
                            }
                        })
                    ]
                }
                $.when.apply($, preCalls).then(() => {
                    $.ajax({
                        url: contextRoot + 'dokument',
                        type: type,
                        data: JSON.stringify(dokument),
                        headers: headers
                    })
                    .done(() => {
                        if(type == 'POST'){
                            window.location.href = contextRoot + "dokumenty?success="+dokument.numer;
                        }
                        else{
                            window.location.href = contextRoot + "dokumenty"
                        }
                    })
                    .fail(() => {
                        alert('Problem z zapisem do bazy')
                    })
                })
            },
            Anuluj: function() {
                dialog.dialog( "close" );
            }
        }
    });

    form = dialog.find( "form" ).on( "submit", function( event ) {
        event.preventDefault();
    });

});