var type = ''
var dialog, dialogDelete, t
var currentNumber, formatter

$(document).ready(() => {
    $('#documentsLink').css("font-weight", "bold");
    $('#documentsLink').css("text-decoration", "underline");

    formatter = new Intl.NumberFormat('pl-PL', {
       minimumFractionDigits: 2,
       maximumFractionDigits: 2,
    });

    var queryString = window.location.search;
    var urlParams = new URLSearchParams(queryString);
    var monthParam = urlParams.get('month');
    if(monthParam){
        $('#month').val(monthParam)
    }
    else{
        var month = new Date().getMonth()+1
        if(month < 10) month = '0' + month

        var year = new Date().getFullYear()
        $('#month').val(year + '-' + month)
    }

    t = $('#documentsTable').DataTable({
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

    $('#month').change(() => {
        updateTable();
    })

    const machineSelect = (type) => {

        var machineId;
        if(type){
            machineId = $('#machine option:selected').val();
            $('#tags').val('')
        }
        else machineId = $('#tags').val().match(/\((.*?)\)/)[1];

        var maxRelDoc = 0;
        var rows = $('#documentsTable').DataTable().rows().data()
        $.each(rows, (i, row) => {
            if(row[0].split('/')[3] === machineId){
                maxRelDoc = Math.max(parseInt(row[0].split('/')[0]), maxRelDoc)
            }
        })

        var docMonth = $('#date').val().substr(5,2);
        var docYear = $('#date').val().substr(0,4);
        $('#number').val((maxRelDoc+1) + '/' + docMonth + '/' + docYear + '/' + machineId)

        $("#consumptionTable > tbody > tr").remove();

        $.ajax({
            url: contextRoot + 'machine',
            data: {
                id: machineId,
                month: $('#month').val()
            }
        })
        .done(machine => {

            if(machine.description){
                $('#machineDescription').text(machine.opis)
            }
            else{
                $('#machineDescription').text('')
            }
            var usedForHeating = false;
            $.each(machine.fuelConsumptionStandards, (i, fuelConsumptionStandard) => {
                usedForHeating = usedForHeating || fuelConsumptionStandard.usedForHeating
            })

            if(usedForHeating){
                $('#consumptionTable').append('<tr><th width="60px;">suma przed</th><th width="60px;">zużycie</th><th width="200px;">zużycie * norma</th><th width="60px;">ogrzewanie<br>/przepał [L]</th><th width="60px;">tankowanie [L]</th><th width="60px;">suma po</th></tr>')
            }
            else{
                $('#consumptionTable').append('<tr><th width="60px;">suma przed</th><th width="60px;">zużycie</th><th width="200px;">zużycie * norma</th><th width="60px;">tankowanie [L]</th><th width="60px;">suma po</th></tr>')
            }

            $('#kmBefore').html(machine.sumOfKilometers)

            $.each(machine.fuelConsumptionStandards, (i, fuelConsumptionStandard) => {
                var fuelConsumptionStandardSum = (fuelConsumptionStandard.sum == null) ? 0 : fuelConsumptionStandard.sum

                var tr = $('<tr>').attr({
                    class: 'fuelConsumption'
                }).appendTo($('#consumptionTable'))

                var td1 = $('<td>').html(fuelConsumptionStandardSum)
                td1.appendTo(tr)

                var td2 = $('<td>')
                td2.appendTo(tr)

                var inputConsumption = $('<input>').attr({
                    type: 'number',
                    step: '0.01',
                    fuelConsumptionStandardId: fuelConsumptionStandard.id,
                    class: 'form-control fuelConsumption-val',
                    value : '0'
                }).appendTo(td2)

                var td3 = $('<td>')
                td3.appendTo(tr)

                var inputFuelConsumptionEcho = $('<span>').html(0).appendTo(td3)
                td3.append(` * ${fuelConsumptionStandard.value} [${fuelConsumptionStandard.unitObj == null ? fuelConsumptionStandard.unit : fuelConsumptionStandard.unitObj.name}] = `)

                var result = $('<span>').html(0).appendTo(td3)

                inputConsumption.keyup(() => {
                    inputHeatingVal = (inputHeating == null) ? 0 : inputHeating.val()
                    calc(td5, result, inputFuelConsumptionEcho, fuelConsumptionStandardSum, fuelConsumptionStandard.value, inputConsumption.val(), inputHeatingVal, inputRefueled.val(), fuelConsumptionStandard.rounded)
                })

                var inputHeating = null

                if(usedForHeating){
                    var tdHeating = $('<td>')

                    if(fuelConsumptionStandard.usedForHeating){
                        inputHeating = $('<input>').attr({
                            type: 'number',
                            step: '0.01',
                            fuelConsumptionStandardId: fuelConsumptionStandard.id,
                            class: 'form-control heating-val',
                            value : '0'
                        }).appendTo(tdHeating)
                    }
                    tdHeating.appendTo(tr)
                }

                var td4 = $('<td>')
                td4.appendTo(tr)

                var inputRefueled = $('<input>').attr({
                    type: 'number',
                    step: '0.01',
                    fuelConsumptionStandardId: fuelConsumptionStandard.id,
                    class: 'form-control refueled-val',
                    value : '0'
                }).appendTo(td4)

                var td5 = $('<td>')
                td5.appendTo(tr)

                if(fuelConsumptionStandard.usedForHeating){
                    inputHeating.keyup(() => {
                        calc(td5, result, inputFuelConsumptionEcho, fuelConsumptionStandardSum ,fuelConsumptionStandard.value, inputConsumption.val(), inputHeating.val(), inputRefueled.val(), fuelConsumptionStandard.rounded)
                    })
                }

                inputRefueled.keyup(() => {
                    inputHeatingVal = (inputHeating == null) ? 0 : inputHeating.val()
                    calc(td5, result, inputFuelConsumptionEcho, fuelConsumptionStandardSum, fuelConsumptionStandard.value, inputConsumption.val(), inputHeatingVal, inputRefueled.val(), fuelConsumptionStandard.rounded)
                })

            })
        })
    }

    $('#error').hide();
    $('#number').keypress(() => {
        $('#error').hide();
    })

    $('#machine').change(() => machineSelect(true))

    $( function() {
        $("#tags").autocomplete({
            source: machinesX.map(m => m.name + '(' + m.id + ')'),
            select: function(event, ui) {
                $(this).val(ui.item.value);
                machineSelect(false)
            }
        });
    });

})

function calc(el1, el2, el3, before, fuelConsumptionStandard, fuelConsumptionStandardVal, heating, refueled, isRounded){

     if(isNaN(heating) || heating === '') heating = 0;
     if(isNaN(heating) || heating === '') heating = 0;
     if(isNaN(fuelConsumptionStandardVal) || fuelConsumptionStandardVal === '') fuelConsumptionStandardVal = 0;

     el3.html(fuelConsumptionStandardVal)

     $.ajax({
        url: contextRoot + 'calc',
        data: {
            before: parseFloat(before),
            fuelConsumptionStandard: parseFloat(fuelConsumptionStandard),
            fuelConsumptionStandardVal: parseFloat(fuelConsumptionStandardVal),
            heating: parseFloat(heating),
            refueled: parseFloat(refueled),
            isRounded: isRounded
        }
    })
    .done(result => {
        el1.html(result[0])
        el2.html(result[1])
    })
}

function updateTable(){

    $.ajax({
        url: contextRoot + 'documents',
        data: {
            year: $('#month').val().split('-')[0],
            month: $('#month').val().split('-')[1]
        }
    })
    .done(documents => {
        t.clear().draw();
        $.each(documents, (i, document) => {
            t.row.add([
                document.number,
                document.date,
                document.machine.name + ' (' + document.machine.id + ')',
                `<button class="btn btn-info" onclick="editBtn('${document.number}')">edytuj <i class="fas fa-edit"></i></button>`,
                `<button class="btn btn-warning" onclick="deleteBtn('${document.number}')">usuń <i class="fas fa-trash-alt"></i></button>`
            ]).draw(false);
        })
    })

    t.order([1, 'desc']).draw()
}

function editBtn(number){
    $("span.ui-dialog-title").text('Edytuj dokument');
    type = 'PUT'

    $('#tags').val('').prop("disabled", true);

    var headers = {};
    headers["Content-Type"] = "application/json; charset=utf-8";

    $.ajax({
        url: contextRoot + 'document',
        data: {
            'number': number,
            month: $('#month').val()
        },
        headers: headers
    })
    .done(document => {
        $("#consumptionTable > tr").remove();
        $("#number").prop("disabled", true);
        $('#machine').prop("disabled", true);
        $('#number').val(document.number)
        $('#kilometersTrailer').val(document.kilometersTrailer)
        $('#kilometers').val(document.kilometers)
        .keyup(() => {
            $('#kmAfter').html(parseFloat(document.kilometersBefore) + parseFloat($('#kilometers').val()))
        })
        $('#kmBefore').html(document.kilometersBefore)
        $('#kmAfter').html(document.kilometersBefore + document.kilometers)
        $('#date').val(document.date)
        $("#machine option[value='"+document.machine.id.toString()+"']").attr("selected", "selected");
        $("#consumptionTable > tbody > tr").remove();

       var usedForHeating = false;
       $.each(document.fuelConsumption, (i, z) => {
           usedForHeating = usedForHeating || z.fuelConsumptionStandard.usedForHeating
       })

       if(usedForHeating){
           $('#consumptionTable').append('<tr><th width="60px;">suma przed</th><th width="60px;">zużycie</th><th width="200px;">zużycie * norma</th><th width="60px;">ogrzewanie<br>/przepał [L]</th><th width="60px;">tankowanie [L]</th><th width="60px;">suma po</th></tr>')
       }
       else{
           $('#consumptionTable').append('<tr><th width="60px;">suma przed</th><th width="60px;">zużycie</th><th width="200px;">zużycie * norma</th><th width="60px;">tankowanie [L]</th><th width="60px;">suma po</th></tr>')
       }

        $.each(document.fuelConsumption, (i, z) => {
            var tr = $('<tr>').attr({
               class: 'fuelConsumption'
            }).appendTo($('#consumptionTable'))

            var sumBefore = z.fuelConsumptionStandard.sumBefore == null ? 0 : z.fuelConsumptionStandard.sumBefore
            sumBefore = (sumBefore == 0 ? z.fuelConsumptionStandard.initialState : sumBefore)
            var td1 = $('<td>').html(sumBefore)
            td1.appendTo(tr)

            var td2 = $('<td>')
            td2.appendTo(tr)

            var inputConsumption = $('<input>').attr({
               type: 'number',
               step: '0.01',
               fuelConsumptionStandardId: z.fuelConsumptionStandard.id,
               fuelConsumptionId: z.id,
               class: 'form-control fuelConsumption-val',
               value : '0'
            }).appendTo(td2)
            .val(z.value)

            var td3 = $('<td>')
            td3.appendTo(tr)

            var inputFuelConsumptionEcho = $('<span>').html(z.value).appendTo(td3)
            td3.append(` * ${z.fuelConsumptionStandard.value} [${z.fuelConsumptionStandard.unitObj == null
                ? z.fuelConsumptionStandard.unit : z.fuelConsumptionStandard.unitObj.name}] = `)

            var resultVal = z.value * z.fuelConsumptionStandard.value
            var result = $('<span>').html(Math.round((resultVal + 0.00001) * 10)/10).appendTo(td3)

            var inputHeating = null
            if(usedForHeating){
               var tdHeating = $('<td>')

               if(z.fuelConsumptionStandard.usedForHeating){
                   var inputHeating = $('<input>').attr({
                       type: 'number',
                       step: '0.01',
                       fuelConsumptionStandardId: z.fuelConsumptionStandard.id,
                       class: 'form-control heating-val',
                       value : '0'
                   }).appendTo(tdHeating)
                   .val(z.heating)
               }
               tdHeating.appendTo(tr)
            }

            var td4 = $('<td>')
            td4.appendTo(tr)

            var inputRefueled = $('<input>').attr({
               type: 'number',
               step: '0.01',
               fuelConsumptionStandardId: z.fuelConsumptionStandard.id,
               class: 'form-control refueled-val',
               value : '0'
            }).appendTo(td4)
            .val(z.refueled)

            var td5 = $('<td>').appendTo(tr)

            if(z.fuelConsumptionStandard.usedForHeating){
                inputHeating.keyup(() => {
                   calc(td5, result, inputFuelConsumptionEcho, sumBefore, z.fuelConsumptionStandard.value, inputConsumption.val(),
                        inputHeating.val(), inputRefueled.val(), z.fuelConsumptionStandard.rounded)
                })
            }

            calc(td5, result, inputFuelConsumptionEcho, sumBefore, z.fuelConsumptionStandard.value, z.value, z.heating, z.refueled, z.fuelConsumptionStandard.rounded)

            inputConsumption.keyup(() => {
                inputHeatingVal = (inputHeating == null) ? 0 : inputHeating.val()
                calc(td5, result, inputFuelConsumptionEcho, sumBefore, z.fuelConsumptionStandard.value, inputConsumption.val(), inputHeatingVal, inputRefueled.val(), z.fuelConsumptionStandard.rounded)
            })

            inputRefueled.keyup(() => {
                inputHeatingVal = (inputHeating == null) ? 0 : inputHeating.val()
                calc(td5, result, inputFuelConsumptionEcho, sumBefore, z.fuelConsumptionStandard.value, inputConsumption.val(), inputHeatingVal, inputRefueled.val(), z.fuelConsumptionStandard.rounded)
           })
        })
        dialog.dialog( "open" );
    })
    .fail(() => {
        alert('Bład podczas pobrania danych dokumentu ' + number)
    })
}

function deleteBtn(number){
    currentNumber = number
    $("span.ui-dialog-title").text('Usuń dokument');
    $('#dialog-confirm-p').text('Czy na pewno usunąć dokument o numerze ' + number + ' ?');
    dialogDelete.dialog("open");
}

function addBtn(){
    $("#consumptionTable > tr").remove();
    $("span.ui-dialog-title").text('Dodaj dokument');
    $('#machine').prop("disabled", false);
    $('#number').prop("disabled", false);
    $('#tags').val('').prop("disabled", false);

    var today = new Date().getDate();
    var todayS = (today > 9) ? today : '0' + today
    $('#date').val($('#month').val()+'-'+todayS)
    $('#kilometers').val(0)
    .keyup(() => {
        var before = parseFloat($('#kmBefore').html())
        var km = parseFloat($('#kilometers').val())
        var after = before + km
        $('#kmAfter').html(formatter.format(after))
    })

    $("#consumptionTable > tbody > tr").remove();
    $('#machine').val(-1);
    $('#kilometersTrailer').val(0);
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
                        number: currentNumber
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
        height: 590,
        width: 1100,
        modal: true,
        buttons: {
            "Zapisz": function(){

                 var fuelConsumption = []
                 $('.fuelConsumption').each(function(i, tr){
                    var value = $(this).find('td > input.fuelConsumption-val').val()
                    var refueled =  $(this).find('td > input.refueled-val').val()
                    var heating = $(this).find('td > input.heating-val').val()
                    if(!heating) heating = '0'
                    var fuelConsumptionId = $(this).find('td > input.fuelConsumption-val').attr('fuelConsumptionId')
                    var fuelConsumptionStandardId = $(this).find('td > input.fuelConsumption-val').attr('fuelConsumptionStandardId')
                    var z = {
                        value: value,
                        id: fuelConsumptionId,
                        fuelConsumptionStandard: {
                            id: fuelConsumptionStandardId
                        },
                        refueled: refueled === '' ? 0 : refueled,
                        heating: heating === '' ? 0 : heating

                    }
                    fuelConsumption.push(z)
                })

                var machineId
                if($('#tags').val()) machineId = $('#tags').val().match(/\((.*?)\)/)[1]
                else machineId = $('#machine option:selected').val()

                var document = {
                    number: $('#number').val(),
                    date: $('#date').val(),
                    kilometers: $('#kilometers').val(),
                    kilometersTrailer: $('#kilometersTrailer').val(),
                    machine: {
                        id: machineId
                    },
                    fuelConsumption: fuelConsumption
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
                            url: contextRoot + 'document',
                            data: {
                                number: document.number
                            },
                            headers: headers
                        })
                        .done((response) => {
                            if(response.number != null){
                                $('#error').show();
                                throw new Error('Dokument o podanym numerze już istnieje')
                            }
                        })
                    ]
                }
                $.when.apply($, preCalls).then(() => {
                    $.ajax({
                        url: contextRoot + 'document',
                        type: type,
                        data: JSON.stringify(document),
                        headers: headers
                    })
                    .done(() => {
                        if(type == 'POST'){
                            window.location.href = contextRoot + "documentsView?success="+document.number+'&month='+$('#month').val();
                        }
                        else{
                            window.location.href = contextRoot + "documentsView?month="+$('#month').val()
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