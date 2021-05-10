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

        $("#zuzycieTable > tbody > tr > td").remove();

        $.ajax({
            url: contextRoot + 'maszyna',
            data: {
                id: maszynaId,
                miesiac: $('#miesiac').val()
            }
        })
        .done(maszyna => {
            $.each(maszyna.normy, (i, norma) => {
                var normaSuma = (norma.suma == null) ? 0 : norma.suma

                var tr = $('<tr>').attr({
                    class: 'zuzycie'
                }).appendTo($('#zuzycieTable'))

                var td1 = $('<td>').html(normaSuma)
                td1.appendTo(tr)

                var td2 = $('<td>')
                td2.appendTo(tr)

                var inputZuzycie = $('<input>').attr({
                    type: 'number',
                    step: '0.01',
                    normaId: norma.id,
                    class: 'form-control',
                    value : '0'
                }).appendTo(td2)

                var td3 = $('<td>')
                td3.appendTo(tr)

                var inputZuzycieEcho = $('<span>').html(0).appendTo(td3)
                td3.append(` * ${norma.wartosc} [${norma.jednostka}] = `)

                var wynik = $('<span>').html(0).appendTo(td3)

                inputZuzycie.keyup(() => {
                    inputZuzycieEcho.html(inputZuzycie.val())
                    wynik.html(Math.round(((inputZuzycie.val() * norma.wartosc) + Number.EPSILON) * 10)/10)
                    var inputTankowanieVal = (inputTankowanie.val() == '') ? 0 : parseFloat(inputTankowanie.val())
                    td5.html(normaSuma + parseFloat(wynik.html()) + inputTankowanieVal)
                })

                var td4 = $('<td>')
                td4.appendTo(tr)

                var inputTankowanie = $('<input>').attr({
                    type: 'number',
                    step: '0.01',
                    normaId: norma.id,
                    class: 'form-control',
                    value : '0'
                }).appendTo(td4)

                var td5 = $('<td>')
                td5.appendTo(tr)

                inputTankowanie.keyup(() => {
                    wynik.html(Math.round(((inputZuzycie.val() * norma.wartosc) + Number.EPSILON) * 10)/10)
                    td5.html(normaSuma + parseFloat(wynik.html()) + parseFloat(inputTankowanie.val()))
                })

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
            'numer': numer,
            miesiac: $('#miesiac').val()
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
        $('#zuzycieTable').append('<tr><th>suma przed</th><th>zużycie</th><th>zużycie * norma</th><th>tankowanie</th><th>suma po</th></tr>')
        $.each(dokument.zuzycie, (i, zuzycie) => {
            var normaSuma = (zuzycie.norma.suma == null) ? 0 : zuzycie.norma.suma
            $('#zuzycieTable').append(`\
            <tr class="zuzycie">\
                <td><label></label>${normaSuma}</td>\
                <td style="width: 100px;"><input type="number" step="0.01" id="${zuzycie.id}" normaId="${zuzycie.norma.id}" class="form-control"></input></td>\
                <td><b><span id="input-${zuzycie.norma.id}"></span></b><span> * <b>${zuzycie.norma.wartosc}</b> [${zuzycie.norma.jednostka}] = </span><b><span id="wynik-${zuzycie.norma.id}"></span></b></td>\
                <td style="width: 100px;"><input type="number" step="0.01" id="tankowanie-${zuzycie.norma.id}" normaId="${zuzycie.norma.id}" class="form-control" value="0"></input></td>\
            </tr>\
            `)
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
    $('#zuzycieTable').append('<tr><th width="60px;">suma przed</th><th width="60px;">zużycie</th><th width="200px;">zużycie * norma</th><th width="60px;">tankowanie</th><th width="60px;">suma po</th></tr>')
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
        width: 1100,
        modal: true,
        buttons: {
            "Zapisz": function(){

                 var zuzycie = []
                 $('.zuzycie').each(function(i, tr){
                    var wartosc = $(this).find('td:eq(1) > input').val()
                    var zatankowano =  $(this).find('td:eq(3) > input').val()
                    var zuzycieId = $(this).find('td:eq(1) > input').attr('ID')
                    var normaId = $(this).find('td:eq(1) > input').attr('normaId')
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