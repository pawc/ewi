var type = ''
var dialog, dialogUsun, t
var currentNumer, formatter

$(document).ready(() => {
    $('#dokumentyLink').css("font-weight", "bold");
    $('#dokumentyLink').css("text-decoration", "underline");

    formatter = new Intl.NumberFormat('pl-PL', {
       minimumFractionDigits: 2,
       maximumFractionDigits: 2,
    });

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

        $("#zuzycieTable > tbody > tr").remove();

        $.ajax({
            url: contextRoot + 'maszyna',
            data: {
                id: maszynaId,
                miesiac: $('#miesiac').val()
            }
        })
        .done(maszyna => {

            var czyOgrzewanie = false;
            $.each(maszyna.normy, (i, norma) => {
                czyOgrzewanie = czyOgrzewanie || norma.czyOgrzewanie
            })

            if(czyOgrzewanie){
                $('#zuzycieTable').append('<tr><th width="60px;">suma przed</th><th width="60px;">zużycie</th><th width="200px;">zużycie * norma</th><th width="60px;">ogrzewanie [L]</th><th width="60px;">tankowanie [L]</th><th width="60px;">suma po</th></tr>')
            }
            else{
                $('#zuzycieTable').append('<tr><th width="60px;">suma przed</th><th width="60px;">zużycie</th><th width="200px;">zużycie * norma</th><th width="60px;">tankowanie [L]</th><th width="60px;">suma po</th></tr>')
            }

            $('#kmPrzed').html(maszyna.sumaKilometry)

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
                    class: 'form-control zuzycie-val',
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
                    inputOgrzewanieVal = (inputOgrzewanie == null) ? 0 : parseFloat(inputOgrzewanie.val())
                    td5.html(normaSuma - parseFloat(wynik.html()) + inputTankowanieVal - inputOgrzewanieVal)
                })

                if(czyOgrzewanie){
                    var tdOgrzewanie = $('<td>')

                    if(norma.czyOgrzewanie){
                        var inputOgrzewanie = $('<input>').attr({
                            type: 'number',
                            step: '0.01',
                            normaId: norma.id,
                            class: 'form-control ogrzewanie-val',
                            value : '0'
                        }).appendTo(tdOgrzewanie)
                        .keyup(() => {
                            wynik.html(Math.round(((inputZuzycie.val() * norma.wartosc) + Number.EPSILON) * 10)/10)
                            td5.html(normaSuma - parseFloat(wynik.html()) + parseFloat(inputTankowanie.val()) - parseFloat(inputOgrzewanie.val()))
                        })
                    }
                    tdOgrzewanie.appendTo(tr)
                }

                var td4 = $('<td>')
                td4.appendTo(tr)

                var inputTankowanie = $('<input>').attr({
                    type: 'number',
                    step: '0.01',
                    normaId: norma.id,
                    class: 'form-control tankowanie-val',
                    value : '0'
                }).appendTo(td4)

                var td5 = $('<td>')
                td5.appendTo(tr)

                inputTankowanie.keyup(() => {
                    wynik.html(Math.round(((inputZuzycie.val() * norma.wartosc) + Number.EPSILON) * 10)/10)
                    inputOgrzewanieVal = (inputOgrzewanie == null) ? 0 : parseFloat(inputOgrzewanie.val())
                    td5.html(normaSuma - parseFloat(wynik.html()) + parseFloat(inputTankowanie.val()) - inputOgrzewanieVal)
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
                `<button class="btn btn-info" onclick="edytujBtn('${dokument.numer}')">edytuj <i class="fas fa-edit"></i></button>`,
                `<button class="btn btn-warning" onclick="usunBtn('${dokument.numer}')">usuń <i class="fas fa-trash-alt"></i></button>`
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

       var czyOgrzewanie = false;
       $.each(dokument.zuzycie, (i, z) => {
           czyOgrzewanie = czyOgrzewanie || z.norma.czyOgrzewanie
       })

       if(czyOgrzewanie){
           $('#zuzycieTable').append('<tr><th width="60px;">suma przed</th><th width="60px;">zużycie</th><th width="200px;">zużycie * norma</th><th width="60px;">ogrzewanie [L]</th><th width="60px;">tankowanie [L]</th><th width="60px;">suma po</th></tr>')
       }
       else{
           $('#zuzycieTable').append('<tr><th width="60px;">suma przed</th><th width="60px;">zużycie</th><th width="200px;">zużycie * norma</th><th width="60px;">tankowanie [L]</th><th width="60px;">suma po</th></tr>')
       }

        $.each(dokument.zuzycie, (i, zuzycie) => {
            var tr = $('<tr>').attr({
               class: 'zuzycie'
            }).appendTo($('#zuzycieTable'))

            var td1 = $('<td>').html('X')
            td1.appendTo(tr)

            var td2 = $('<td>')
            td2.appendTo(tr)

            var inputZuzycie = $('<input>').attr({
               type: 'number',
               step: '0.01',
               normaId: zuzycie.norma.id,
               zuzycieId: zuzycie.id,
               class: 'form-control zuzycie-val',
               value : '0'
            }).appendTo(td2)
            .val(zuzycie.wartosc)

            var td3 = $('<td>')
            td3.appendTo(tr)

            var inputZuzycieEcho = $('<span>').html(0).appendTo(td3)
            td3.append(` * ${zuzycie.norma.wartosc} [${zuzycie.norma.jednostka}] = `)

            var wynik = $('<span>').html(0).appendTo(td3)

            if(czyOgrzewanie){
               var tdOgrzewanie = $('<td>')

               if(zuzycie.norma.czyOgrzewanie){
                   var inputOgrzewanie = $('<input>').attr({
                       type: 'number',
                       step: '0.01',
                       normaId: zuzycie.norma.id,
                       class: 'form-control ogrzewanie-val',
                       value : '0'
                   }).appendTo(tdOgrzewanie)
                   .val(zuzycie.ogrzewanie)
               }
               tdOgrzewanie.appendTo(tr)
            }

            var td4 = $('<td>')
            td4.appendTo(tr)

            var inputTankowanie = $('<input>').attr({
               type: 'number',
               step: '0.01',
               normaId: zuzycie.norma.id,
               class: 'form-control tankowanie-val',
               value : '0'
            }).appendTo(td4)
            .val(zuzycie.zatankowano)

            var td5 = $('<td>').html('X')
            td5.appendTo(tr)
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
    .keyup(() => {
        var przed = parseFloat($('#kmPrzed').html())
        var km = parseFloat($('#kilometry').val())
        var po = przed + km
        $('#kmPo').html(formatter.format(po))
    })

    $("#zuzycieTable > tbody > tr").remove();
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
                    var wartosc = $(this).find('td > input.zuzycie-val').val()
                    var zatankowano =  $(this).find('td > input.tankowanie-val').val()
                    var ogrzewanie = $(this).find('td > input.ogrzewanie-val').val()
                    if(!ogrzewanie) ogrzewanie = '0'
                    var zuzycieId = $(this).find('td > input.zuzycie-val').attr('zuzycieId')
                    var normaId = $(this).find('td > input.zuzycie-val').attr('normaId')
                    var z = {
                        wartosc: wartosc,
                        id: zuzycieId,
                        norma: {
                            id: normaId
                        },
                        zatankowano: zatankowano,
                        ogrzewanie: ogrzewanie

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