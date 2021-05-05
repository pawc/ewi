var t;
$(document).ready(function() {

    $('#raportLink').css("font-weight", "bold");
    $('#raportLink').css("text-decoration", "underline");

    var month = new Date().getMonth()+1
    if(month < 10) month = '0' + month

    var year = new Date().getFullYear()
    $('#miesiac').val(year + '-' + month)

    t = $('#raportTable').DataTable({
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
            var endState = Math.round(((pozycja.stanPoprz - pozycja.suma + pozycja.zatankowano) + Number.EPSILON) * 10)/10
            t.row.add( [
                pozycja.maszyna,
                pozycja.kilometry,
                pozycja.jednostka,
                pozycja.stanPoprz,
                pozycja.suma,
                pozycja.zatankowano,
                endState,
                pozycja.normaId,
                nextMonthVal[0],
                nextMonthVal[1]
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

    var stany = []
    var rows = $('#raportTable').DataTable().rows().data()
    $.each(rows, (i, row) => {
        var stan = {
            rok : row[8],
            miesiac : row[9],
            norma : {
                id : row[7]
            },
            wartosc : row[6]
        }
        stany.push(stan)
    })

    var headers = {};
    headers["Content-Type"] = "application/json; charset=utf-8";
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    headers[header] = token
    var properties = {
        url: contextRoot + 'stany',
        data: JSON.stringify(stany),
        type: 'POST',
        headers: headers
    }

    $.ajax(properties)
    .done(() => {
        window.location.href = contextRoot + "?stanySuccess=true"
    })
    .fail(() => {
        window.location.href = contextRoot + "?stanySuccess=false"
    })

}