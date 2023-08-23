var t;
$(document).ready(function() {

    $('#reportLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#reportsDropdownMenuLink').css("font-weight", "bold").css("text-decoration", "underline");

    var month = new Date().getMonth()+1
    if(month < 10) month = '0' + month

    var year = new Date().getFullYear()
    $('#month').val(year + '-' + month)

    t = $('#reportTable').DataTable({
        dom: 'Blfrtip',
        buttons: [
            'print',
        ],
        "pageLength": 10,
        lengthMenu: [
            [ 5, 10, 25, 50, -1 ],
            [ '5', '10', '25', '50', 'Wszystko' ]
        ],
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

    $('#reportTable_wrapper > div.dt-buttons > button > span').text('Drukuj raport')
    $('#reportTable_wrapper > div.dt-buttons > button').addClass('btn btn-info')
    $('#reportTable_wrapper > div.dt-buttons').css('text-align', 'right')
    $('#reportTable_wrapper > div.dt-buttons').css('margin-right', '50px')
    $('#reportTable_wrapper').css('margin-right', '50px')
    $('#raportTable_length').css('margin-left', '5%')

    updateTable();

    $('#month').change(() => {
        updateTable();
    })
});

function updateTable(){
    $('#loadingDiv').show()
    $('#tableDiv').hide()
   t.clear().draw();
    var year = $('#month').val().split('-')[0]
    var month = $('#month').val().split('-')[1]
    $.ajax({
        url: contextRoot + 'report',
        data: {
            year: year,
            month: month
        }
    })
    .done(elements => {
        var nextMonthVal = nextMonth()
        $.each(elements, (i, p) => {
            t.row.add( [
                p.machine,
                p.kilometersInitialState,
                p.kilometers,
                p.endStateKilometers,
                p.kilometersTrailer,
                p.unit,
                p.initialState,
                p.sum,
                p.refueled,
                p.heating,
                p.endState,
                p.sumHours,
                p.fuelConsumptionStandardId,
                nextMonthVal[0],
                nextMonthVal[1],
                p.machineId
            ]).draw(false);
        })
        $('#loadingDiv').hide()
        $('#tableDiv').show()
    })
    .fail(() => {
        alert('Problem z pobraniem raportu za dany miesiąc')
        $('#loadingDiv').hide()
    })
}

function nextMonth(){
    var year = parseInt($('#month').val().split('-')[0])
    var month = parseInt($('#month').val().split('-')[1]) + 1

    if(month > 12){
        month = 1
        year += 1
    }

    var monthString = (month > 9) ? month : '0' + month
    return [year, month, `${year}-${monthString}`]
}

function prepareAjax(properties) {
  var defer = $.Deferred();

  var promise = defer.promise();

  return $.extend(promise, {
    execute: function () {
      return $.ajax(properties).then(defer.resolve.bind(defer), defer.reject.bind(defer));
    }
  });
}

function saveAll(){

    var headers = {};
    headers["Content-Type"] = "application/json; charset=utf-8";
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    headers[header] = token

    var initialStates = []
    var rows = $('#reportTable').DataTable().rows().data()
    $.each(rows, (i, row) => {
        var initialState = {
            year : row[13],
            month : row[14],
            fuelConsumptionStandard : {
                id : row[12]
            },
            value : row[10]
        }
        initialStates.push(state)
    })

    var propertiesInitialStates = {
        url: contextRoot + 'fuelInitialStates',
        data: JSON.stringify(initialStates),
        type: 'POST',
        headers: headers
    }

    var kilometers = []
    var rows = $('#reportTable').DataTable().rows().data()

    $.each(rows, (i, row) => {
        var km = {
            year : row[13],
            month : row[14],
            machine : {
                id : row[15]
            },
            value : row[3]
        }
        kilometers.push(km)
    })

    var propertiesKm = {
        url: contextRoot + 'kilometersList',
        data: JSON.stringify(kilometry),
        type: 'POST',
        headers: headers
    }

    $.when(
        $.ajax(propertiesKm),
        $.ajax(propertiesInitialStates)
    )
    .done(() => {
        window.location.href = contextRoot + "?saveSuccess=true"
    })
    .fail(() => {
        window.location.href = contextRoot + "?saveSuccess=false"
    })

}