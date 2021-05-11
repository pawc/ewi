var t;
$(document).ready(function() {

    $('#kilometryLink').css("font-weight", "bold");
    $('#kilometryLink').css("text-decoration", "underline");

    var queryString = window.location.search;
    var urlParams = new URLSearchParams(queryString);
    var monthParam = urlParams.get('month');
    if(monthParam){
        $('#miesiac').val(monthParam)
        $('#raportLink').attr('href', $('#raportLink').attr('href') + '?month=' + monthParam)
        $('#dokumentyLink').attr('href', $('#dokumentyLink').attr('href') + '?month=' + monthParam)
        $('#maszynyLink').attr('href', $('#dokumentyLink').attr('href') + '?month=' + monthParam)
        $('#stanyLink').attr('href', $('#stanyLink').attr('href') + '?month=' + monthParam)
        $('#kilometryLink').attr('href', $('#kilometryLink').attr('href') + '?month=' + monthParam)
    }
    else{
        var month = new Date().getMonth()+1
        if(month < 10) month = '0' + month

        var year = new Date().getFullYear()
        $('#miesiac').val(year + '-' + month)
    }

    var year = new Date().getFullYear()
    $('#miesiac').val(year + '-' + month)

    $('#dataSpan').text($('#miesiac').val())

    t = $('#kilometryTable').DataTable({
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
        if(!monthParam){
            $('#raportLink').attr('href', $('#raportLink').attr('href') + '?month=' + $('#miesiac').val())
            $('#dokumentyLink').attr('href', $('#dokumentyLink').attr('href') + '?month=' + $('#miesiac').val())
            $('#maszynyLink').attr('href', $('#maszynyLink').attr('href') + '?month=' + $('#miesiac').val())
            $('#stanyLink').attr('href', $('#stanyLink').attr('href') + '?month=' + $('#miesiac').val())
            $('#kilometryLink').attr('href', $('#kilometryLink').attr('href') + '?month=' + $('#miesiac').val())
        }
        $('#dataSpan').text($('#miesiac').val())
        updateTable();
    })

});

function updateTable(){
   t.clear().draw();
    var rok = $('#miesiac').val().split('-')[0]
    var miesiac = $('#miesiac').val().split('-')[1]
    $.ajax({
        url: contextRoot + 'kilometryGet',
        data: {
            rok: rok,
            miesiac: miesiac
        }
    })
    .done(kilometry => {
        $.each(kilometry, (i, k) => {
            t.row.add([
                `${k.maszynanazwa} (${k.maszynaid})`,
                `<input class="form-control text-center maszyna" type="number" step="0.01" value="${k.stanpoczatkowy}" style="width: 110px;"></input>`,
                `<button class="btn btn-info" rok="${rok}" miesiac="${miesiac}" maszynaid="${k.maszynaid}" onclick="saveKilometry(this)">zapisz</button>\
                <b><span id="success-${k.maszynaid}" class="text-success" style="font-size: 12px;"></span></b>\
                <b><span id="danger-${k.maszynaid}" class="text-danger" style="font-size: 12px;"></span></b>`
            ]).draw(false);
        })
    })
    .fail(() => {
        alert('Problem z pobraniem kilometrów za dany miesiąc')
    })
}

function saveKilometry(btn){
    console.log('ok')
    var maszynaid = $(btn).attr('maszynaid');
    var rok = $(btn).attr('rok');
    var miesiac = $(btn).attr('miesiac');
    console.log($(`#${maszynaid}`))
    var wartosc = $(btn).parent().parent().find('input').val()

    var kilometry = {
        wartosc: wartosc,
        maszyna: {
            id: maszynaid
        },
        rok : rok,
        miesiac : miesiac
    }

    var headers = {};
    headers["Content-Type"] = "application/json; charset=utf-8";
    var header = $("meta[name='_csrf_header']").attr("content");
    var token = $("meta[name='_csrf']").attr("content");
    headers[header] = token
    var properties = {
        url: contextRoot + 'kilometry',
        data: JSON.stringify(kilometry),
        type: 'POST',
        headers: headers
    }

    $.ajax(properties)
    .done(() => {
        $(btn).parent().find('span.text-success').html('zapisano')
    })
    .fail(() => {
        $(btn).parent().find('span.text-danger').html('problem z zapisem')
    })

}