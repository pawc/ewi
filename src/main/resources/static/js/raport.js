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
                pozycja.jednostka,
                pozycja.stanPoprz,
                pozycja.suma,
                pozycja.zatankowano,
                endState,
                `<label class="label-stan" year="${nextMonthVal[0]}" month="${nextMonthVal[1]}" \
                    normaId="${pozycja.normaId}" endState="${endState}"</label>`
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
    $.each($('.label-stan'), (i, btn) => {
        var stan = {
            rok : $(btn).attr('year'),
            miesiac : $(btn).attr('month'),
            norma : {
                id : $(btn).attr('normaid')
            },
            wartosc : $(btn).attr('endstate')
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