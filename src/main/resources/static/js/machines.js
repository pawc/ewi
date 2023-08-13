$(document).ready(() => {

    $('#machinesLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#definitionsDropdownMenuLink').css("font-weight", "bold").css("text-decoration", "underline");

    $('#machinesTable').DataTable({
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

    $('#addFuelConsumptionStandardBtn').click(() => {

        var row = $('<tr>').attr({
            class: 'fuelConsumptionStandard'
        })

        var fuelConsumptionStandardValue = $('<input>').attr({
            type: 'number',
            step: '0.01',
            class: 'form-control'
        })
        .css('width', '90px')

        var col1 = $('<td>')
        fuelConsumptionStandardValue.appendTo(col1)
        col1.appendTo(row)

        let fuelConsumptionStandardUnit = $('<select>').attr({
            class: 'form-control',
            placeholder: 'jednostka'
        })

        $.each(units, function(key, unit) {
          fuelConsumptionStandardUnit.append($("<option></option>")
             .attr("value", unit.id).text(unit.name));
        });

        var col2 = $('<td>')
        fuelConsumptionStandardUnit.appendTo(col2)
        col2.appendTo(row)

        var col3 = $('<td>')
        .css('text-align', 'center')

        var isUsedForHeatingCheck = $('<input>')
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

        var col5 = $('<td>')
        .css('text-align', 'center')
        col5.appendTo(row)
        var isRoundedCheck = $('<input>')
        .attr({
            type: 'checkbox',
            class: 'form-check-input'
        })
        .css('display', 'inline-block')
        .appendTo(col5)

        $('<label>').attr({
            class: 'form-check-label'
        })
        .text(' zaokr. 0.01 ')
        .appendTo(col5)

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

        row.appendTo($('#fuelConsumptionStandardsTable'))

    })

    $('.deleteFuelConsumptionStandardBtn').on('click', function (){
        $(this).parents('tr').first().remove();
    });

    $('#error').hide();
    $('#number').keypress(() => {
        $('#error').hide();
    })

})

var dialog, form, currentId, type

function editBtn(id){
    type = 'PUT'
    $("#fuelConsumptionStandardsTable > tbody > tr").remove();
    currentId = id

    $("span.ui-dialog-title").text('Edytuj maszynę');

    $.ajax({
        url: contextRoot + 'machine',
        data: {
            id: id
        }
    })
    .done(machine => {
        $("#number").prop("disabled", true);
        $('#number').val(machine.id)
        $('#name').val(machine.name)
        $('#description').val(machine.description)
        $('#isActive').prop('checked', machine.active)

        $.each($('input[name=categories]'), (i, k) => {
            $(k).prop('checked', false)
        })

        $.each(machine.categories, (i, category) => {
             $.each($('input[name=categories]'), (j, k) => {
                 if(category.name == $(k).val()) $(k).prop('checked', true)
             })
        })

        $.each(machine.fuelConsumptionStandards, (i, fuelConsumptionStandard) => {

            var row = $('<tr>').attr({
                class: 'fuelConsumptionStandard',
                fuelConsumptionStandardId: fuelConsumptionStandard.id
            })

            var fuelConsumptionStandardValue = $('<input>').attr({
                type: 'number',
                step: '0.01',
                class: 'form-control'
            })
            .css('width', '90px')
            .val(fuelConsumptionStandard.value)

            var col1 = $('<td>')
            fuelConsumptionStandardValue.appendTo(col1)
            col1.appendTo(row)

            var fuelConsumptionStandardUnit = $('<select>').attr({
                class: 'form-control',
                placeholder: 'jednostka'
            })
            .prop('disabled', true)

            $.each(units, function(key, unit) {
              fuelConsumptionStandardUnit.append($("<option></option>")
                 .attr("value", unit.id).text(unit.name));
            });

            if(fuelConsumptionStandard.unitObj != null) fuelConsumptionStandardUnit.val(fuelConsumptionStandard.unitObj.id)
            else {
                fuelConsumptionStandardUnit.append($("<option></option>").attr("value", fuelConsumptionStandard.unit).text(fuelConsumptionStandard.unit));
                fuelConsumptionStandardUnit.val(fuelConsumptionStandard.unit)
            }

            var col2 = $('<td>')
            fuelConsumptionStandardUnit.appendTo(col2)
            col2.appendTo(row)

            var col3 = $('<td>')
            .css('text-align', 'center')

            var isUsedForHeating = $('<input>')
            .attr({
                type: 'checkbox',
                class: 'form-check-input'
            })
            .prop('checked', fuelConsumptionStandard.usedForHeating)
            .prop('disabled', true)
            .css('display', 'inline-block')
            .appendTo(col3)

            $('<label>').attr({
                class: 'form-check-label'
            })
            .text('ogrzewanie')
            .appendTo(col3)

            col3.appendTo(row)

            var col5 = $('<td>')
            .css('text-align', 'center')
            col5.appendTo(row)
            var isRoundedCheck = $('<input>')
            .attr({
                type: 'checkbox',
                class: 'form-check-input'
            })
            .prop('checked', fuelConsumptionStandard.rounded)
            .css('display', 'inline-block')
            .appendTo(col5)

            $('<label>').attr({
                class: 'form-check-label'
            })
            .text(' zaokr. 0.01 ')
            .appendTo(col5)

            row.appendTo($('#fuelConsumptionStandardsTable'))

        })
        dialog.dialog( "open" );
    })
    .fail(() => {
        alert('Bład podczas pobrania danych maszyny ' + id)
    })
}

function addBtn(){
    type = 'POST'
    $("#fuelConsumptionStandardsTable > tbody > tr").remove();
    currentId = 0
    $("#number").prop("disabled", false);
    $("#number").val('')
    $('#name').val('')
    $('#description').val('')
    $('#isActive').prop('checked', true)
    $("span.ui-dialog-title").text('Dodaj maszynę');
    dialog.dialog("open");
}

$(function() {

    dialog = $( "#dialog-form" ).dialog({
        autoOpen: false,
        height: 570,
        width: 700,
        modal: true,
        buttons: {
            "Zapisz": function(){

                var fuelConsumptionStandards = [];
                $('.fuelConsumptionStandard').each(function(i, tr){
                    var fuelConsumptionStandardId = $(this).attr('fuelConsumptionStandardId')
                    var value = $(this).find('td:eq(0) > input').val()
                    var unitId = $(this).find('td:eq(1) > select').val()
                    var usedForHeating = $(this).find('td:eq(2) > input').prop('checked')
                    var rounded = $(this).find('td:eq(3) > input').prop('checked')

                    var fuelConsumptionStandard = {
                        id: fuelConsumptionStandardId,
                        value: value,
                        unitObj: {
                            id: unitId
                        },
                        usedForHeating: usedForHeating,
                        rounded: rounded
                    }

                    if(unitId && value){

                        // unikalność jednostki
                        var test = true
                        $.each(fuelConsumptionStandards, (i, n) => {
                            if(unitId == n.unitObj.id) test = false
                        })
                        if(test) fuelConsumptionStandards.push(fuelConsumptionStandard);

                    }
                })

                var categories = []
                $('input[name=categories]').each((i, category) => {
                    if($(category).is(':checked')){
                        categories.push({
                            name: $(category).val()
                        })
                    }
                })

                var machine = {
                    id: $("#number").val(),
                    name: $('#name').val(),
                    description: $('#description').val(),
                    fuelConsumptionStandards: fuelConsumptionStandards,
                    categories: categories,
                    active: $('#isActive').prop('checked')
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
                            url: contextRoot + 'machine',
                            data: {
                                id: machine.id
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
                        url: contextRoot + 'machine',
                        type: type,
                        data: JSON.stringify(machine),
                        headers: headers
                    })
                    .done(() => {
                        if(type == 'POST'){
                            window.location.href = contextRoot + "machines?success="+machine.id
                        }
                        else{
                            window.location.href = contextRoot + "machines"
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