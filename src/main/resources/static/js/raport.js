var t;
$(document).ready(function() {

    $('#raportLink').css("font-weight", "bold");
    $('#raportLink').css("text-decoration", "underline");

    var month = new Date().getMonth()+1
    if(month < 10) month = '0' + month

    var year = new Date().getFullYear()
    $('#miesiac').val(year + '-' + month)

    t = $('#raportTable').DataTable({
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

    $('#raportTable_wrapper > div.dt-buttons > button > span').text('Drukuj raport')
    $('#raportTable_wrapper > div.dt-buttons > button').addClass('btn btn-info')
    $('#raportTable_wrapper > div.dt-buttons').css('text-align', 'right')
    $('#raportTable_wrapper > div.dt-buttons').css('margin-right', '50px')
    $('#raportTable_filter').css('margin-right', '50px')
    $('#raportTable_length').css('margin-left', '5%')

    updateTable();

    $('#miesiac').change(() => {
        updateTable();
    })
});

function updateTable(){
   t.clear().draw();
    var rok = $('#miesiac').val().split('-')[0]
    var miesiac = $('#miesiac').val().split('-')[1]
    $.ajax({
        url: contextRoot + 'raport',
        data: {
            rok: rok,
            miesiac: miesiac
        }
    })
    .done(pozycje => {
        var nextMonthVal = nextMonth()
        $.each(pozycje, (i, pozycja) => {
            var endStateKilometry = Math.round(((pozycja.stankilometry + pozycja.kilometry) + 0.00001) * 100)/100
            var endState = Math.round(((pozycja.stanPoprz - pozycja.suma + pozycja.zatankowano - pozycja.ogrzewanie) + 0.00001) * 10)/10
            t.row.add( [
                pozycja.maszyna,
                pozycja.stankilometry,
                pozycja.kilometry,
                endStateKilometry,
                pozycja.kilometryprzyczepa,
                pozycja.jednostka,
                pozycja.stanPoprz,
                pozycja.suma,
                pozycja.zatankowano,
                pozycja.ogrzewanie,
                endState,
                pozycja.sumagodzin,
                pozycja.normaId,
                nextMonthVal[0],
                nextMonthVal[1],
                pozycja.maszynaid
            ]).draw(false);
        })
    })
    .fail(() => {
        alert('Problem z pobraniem raportu za dany miesiąc')
    })
}

function nextMonth(){
    var year = parseInt($('#miesiac').val().split('-')[0])
    var month = parseInt($('#miesiac').val().split('-')[1]) + 1

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

    var stany = []
    var rows = $('#raportTable').DataTable().rows().data()
    $.each(rows, (i, row) => {
        var stan = {
            rok : row[13],
            miesiac : row[14],
            norma : {
                id : row[12]
            },
            wartosc : row[10]
        }
        stany.push(stan)
    })

    var propertiesStany = {
        url: contextRoot + 'stany',
        data: JSON.stringify(stany),
        type: 'POST',
        headers: headers
    }

    var kilometry = []
    var rows = $('#raportTable').DataTable().rows().data()

    $.each(rows, (i, row) => {
        var km = {
            rok : row[13],
            miesiac : row[14],
            maszyna : {
                id : row[15]
            },
            wartosc : row[3]
        }
        kilometry.push(km)
    })

    var propertiesKm = {
        url: contextRoot + 'kilometryList',
        data: JSON.stringify(kilometry),
        type: 'POST',
        headers: headers
    }

    $.when(
        $.ajax(propertiesKm),
        $.ajax(propertiesStany)
    )
    .done(() => {
        window.location.href = contextRoot + "?zapisSuccess=true"
    })
    .fail(() => {
        window.location.href = contextRoot + "?zapisSuccess=false"
    })

}