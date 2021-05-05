var type = ''
var dialog, dialogUsun, t
var currentNumer

$(document).ready(() => {
    $('#dokumentyLink').css("font-weight", "bold");
    $('#dokumentyLink').css("text-decoration", "underline");

    var queryString = window.location.search;
    var urlParams = new URLSearchParams(queryString);
    var monthParam = urlParams.get('month');
    if(monthParam){
        $('#miesiac').val(monthParam)
    }
    else{
        var month = new Date().getMonth()+1
        if(month < 10) month = '0' + month

        var year = new Date().getFullYear()
        $('#miesiac').val(year + '-' + month)
    }

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
        },
    });

    updateTable();

    $('#miesiac').change(() => {
        updateTable();
    })

    $('#maszyna').change(() => {

        var maszynaId = $('#maszyna option:selected').val();

        var maxRelDoc = 0;
        var rows = $('#dokumentyTable').DataTable().rows().data()
        $.each(rows, (i, row) => {
            if(row[0].includes(maszynaId)){
                maxRelDoc = Math.max(parseInt(row[0].split('/')[0]), maxRelDoc)
            }
        })

        var docMonth = $('#data').val().substr(5,2);
        var docYear = $('#data').val().substr(0,4);
        $('#numer').val((maxRelDoc+1) + '/' + docMonth + '/' + docYear + '/' + maszynaId)

        $("#zuzycieTable > tr").remove();

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
                    <td><input type="number" step="0.01" id="'+norma.id+'" normaId="'+norma.id+'" class="form-control" style="width: 110px;" value="0"></input></td>\
                    <td><b><span id="input-'+norma.id+'">0</span></b><span> * <b>'+norma.wartosc+'</b> ['+norma.jednostka+'] = </span><b><span id="wynik-'+norma.id+'">0</span></b></td>\
                    <td><input type="number" step="0.01" id="tankowanie-'+norma.id+'" normaId="'+norma.id+'" class="form-control" style="width: 110px;" value="0"></input></td>\
                </tr>\
                ')
                $('#'+norma.id).keyup(() => {
                    var wpisana = $('#'+norma.id).val()
                    var wynik = Math.round(((wpisana * norma.wartosc) + Number.EPSILON) * 10)/10
                    $('#input-'+norma.id).text(wpisana);
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

function updateTable(){

    $.ajax({
        url: contextRoot + 'dokumentyGet',
        data: {
            rok: $('#miesiac').val().split('-')[0],
            miesiac: $('#miesiac').val().split('-')[1]
        }
    })
    .done(dokumenty => {
        t.clear().draw();
        $.each(dokumenty, (i, dokument) => {
            t.row.add([
                dokument.numer,
                dokument.data,
                dokument.maszyna.nazwa + ' (' + dokument.maszyna.id + ')',
                '<button class="btn btn-info" onclick="edytujBtn(\''+dokument.numer+'\')">edytuj <i class="fas fa-edit"></i></button>'
                 +'<button class="btn btn-warning" onclick="usunBtn(\''+dokument.numer+'\')">usuń <i class="fas fa-trash-alt"></i></button>'
            ]).draw(false);
        })
    })

    t.order([1, 'desc']).draw()
}

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
        $('#kilometry').val(dokument.kilometry)
        $('#data').val(dokument.data)
        $("#maszyna option[value='"+dokument.maszyna.id.toString()+"']").attr("selected", "selected");
        $("#zuzycieTable > tbody > tr").remove();
        $('#zuzycieTable').append('<tr><th>zużycie</th><th>zużycie * norma = wynik</th><th>tankowanie</th></tr>')
        $.each(dokument.zuzycie, (i, zuzycie) => {
            $('#zuzycieTable').append('\
            <tr class="zuzycie">\
                <td><input type="number" step="0.01" id="'+zuzycie.id+'" normaId="'+zuzycie.norma.id+'" class="form-control" style="width: 110px;"></input></td>\
                <td><b><span id="input-'+zuzycie.norma.id+'"></span></b><span> * <b>'+zuzycie.norma.wartosc+'</b> ['+zuzycie.norma.jednostka+'] = </span><b><span id="wynik-'+zuzycie.norma.id+'"></span></b></td>\
                <td><input type="number" step="0.01" id="tankowanie-'+zuzycie.norma.id+'" normaId="'+zuzycie.norma.id+'" class="form-control" style="width: 110px;" value="0"></input></td>\
            </tr>\
            ')
            $('#'+zuzycie.id).val(zuzycie.wartosc);
            $('#input-'+zuzycie.norma.id).text(zuzycie.wartosc);
            $('#tankowanie-'+zuzycie.norma.id).val(zuzycie.zatankowano);
            var wynik = Math.round(((zuzycie.wartosc * zuzycie.norma.wartosc) + Number.EPSILON) * 10)/10
            $('#wynik-'+zuzycie.norma.id).text(wynik);
            $('#'+zuzycie.id).keyup(() => {
                var wpisana = $('#'+zuzycie.id).val()
                var wynik = Math.round(((wpisana * zuzycie.norma.wartosc) + Number.EPSILON) * 10)/10
                $('#input-'+zuzycie.norma.id).text(wpisana);
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

    var today = new Date().getDate();
    var todayS = (today > 9) ? today : '0' + today
    $('#data').val($('#miesiac').val()+'-'+todayS)
    $('#kilometry').val(0)

    $("#zuzycieTable > tbody > tr").remove();
    $('#zuzycieTable').append('<tr><th>zużycie</th><th>zużycie * norma = wynik</th><th>tankowanie</th></tr>')
    $('#maszyna').val(-1);
    type = 'POST';
    dialog.dialog("open");
}

function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    return [year, month, day].join('-');
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
        width: 670,
        modal: true,
        buttons: {
            "Zapisz": function(){

                 var zuzycie = []
                 $('.zuzycie').each(function(i, tr){
                    var wartosc = $(this).find('td:eq(0) > input').val()
                    var zatankowano =  $(this).find('td:eq(2) > input').val()
                    var zuzycieId = $(this).find('td:eq(0) > input').attr('ID')
                    var normaId = $(this).find('td:eq(0) > input').attr('normaId')
                    var z = {
                        wartosc: wartosc,
                        id: zuzycieId,
                        norma: {
                            id: normaId
                        },
                        zatankowano: zatankowano
                    }
                    zuzycie.push(z)
                })

                var dokument = {
                    numer: $('#numer').val(),
                    data: $('#data').val(),
                    kilometry: $('#kilometry').val(),
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
                            window.location.href = contextRoot + "dokumenty?success="+dokument.numer+'&month='+$('#miesiac').val();
                        }
                        else{
                            window.location.href = contextRoot + "dokumenty?month="+$('#miesiac').val()
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