var t;
$(document).ready(function() {

    $('#initialStatesLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#initialStatesDropdownMenuLink').css("font-weight", "bold").css("text-decoration", "underline");

    var month = new Date().getMonth()+1
    if(month < 10) month = '0' + month

    var year = new Date().getFullYear()
    $('#month').val(year + '-' + month)

    $('#dataSpan').text($('#month').val())

    t = $('#initialStatesTable').DataTable({
        "language": {
            "search": "Szukaj",
            "emptyTable": "Brak pozycji",
            "lengthMenu": "Pokaż _MENU_",
            "zeroRecords": "Nie znaleziono pozycji",
            "paginate": {
                "first": "Pierwsza",
                "last": "Ostatnia",
                "next": "Następna",
                "previous": "Poprzednia"
            },
            "info": "_START_ do _END_ z _TOTAL_ pozycji",
            "infoEmpty":      "",
            "infoFiltered":   "",
        }
    });

    updateTable();

    $('#month').change(() => {
        $('#dataSpan').text($('#month').val())
        updateTable();
    })

});

function updateTable(){
   t.clear().draw();
    var year = $('#month').val().split('-')[0]
    var month = $('#month').val().split('-')[1]
    $.ajax({
        url: contextRoot + 'fuelInitialStateReport',
        data: {
            year: year,
            month: month
        }
    })
    .done(fuelInitialStateReport => {
        $.each(fuelInitialStateReport, (i, record) => {
            t.row.add([
                `${record.machineName} (${record.machineId})`,
                record.unit,
                `<input class="form-control text-center stan" id="${record.fuelConsumptionStandardId}" type="number" step="0.01" value="${record.fuelInitialState}" style="width: 110px;"></input>`,
                `<button class="btn btn-info" fuelConsumptionStandardId="${record.fuelConsumptionStandardId}" year="${year}" month="${month}" onclick="saveInitialState(this)">zapisz</button>\
                <b><span id="success-${record.fuelConsumptionStandardId}" class="text-success" style="font-size: 12px;"></span></b>\
                <b><span id="danger-${record.fuelConsumptionStandardId}" class="text-danger" style="font-size: 12px;"></span></b>`
            ]).draw(false);
        })
    })
    .fail(() => {
        alert('Problem z pobraniem stanów za dany miesiąc')
    })
}

function saveInitialState(btn){
    var fuelConsumptionStandardId = $(btn).attr('fuelConsumptionStandardId');
    var year = $(btn).attr('year');
    var month = $(btn).attr('month');
    var value = $(`#${fuelConsumptionStandardId}`).val();

    var fuelInitialState = {
        value: value,
        fuelConsumptionStandard: {
            id: fuelConsumptionStandardId
        },
        year : year,
        month : month
    }

    var headers = {};
    headers["Content-Type"] = "application/json; charset=utf-8";
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    headers[header] = token
    var properties = {
        url: contextRoot + 'fuelInitialState',
        data: JSON.stringify(fuelInitialState),
        type: 'PUT',
        headers: headers
    }

    $.ajax(properties)
    .done(() => {
        $(`#success-${fuelConsumptionStandardId}`).text('zapisano')
    })
    .fail(() => {
        $(`#danger-${fuelConsumptionStandardId}`).text('problem z zapisem')
    })

}