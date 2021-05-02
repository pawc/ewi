var t;
$(document).ready(function() {

    $('#stanyLink').css("font-weight", "bold");
    $('#stanyLink').css("text-decoration", "underline");

    var month = new Date().getMonth()+1
    if(month < 10) month = '0' + month

    var year = new Date().getFullYear()
    $('#miesiac').val(year + '-' + month)

    $('#dataSpan').text($('#miesiac').val())

    t = $('#stanyTable').DataTable({
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
        $('#dataSpan').text($('#miesiac').val())
        updateTable();
    })

});

function updateTable(){
   t.clear().draw();
    var rok = $('#miesiac').val().split('-')[0]
    var miesiac = $('#miesiac').val().split('-')[1]
    $.ajax({
        url: contextRoot + 'stanyGet',
        data: {
            rok: rok,
            miesiac: miesiac
        }
    })
    .done(stany => {
        $.each(stany, (i, stan) => {
            t.row.add([
                `${stan.maszynanazwa} (${stan.maszynaid})`,
                stan.jednostka,
                `<input class="form-control text-center stan" id="${stan.normaid}" type="number" step="0.01" value="${stan.stanpoczatkowy}" style="width: 110px;"></input>`,
                `<button class="btn btn-info" normaid="${stan.normaid}" rok="${rok}" miesiac="${miesiac}" onclick="saveStan(this)">zapisz</button>\
                <b><span id="success-${stan.normaid}" class="text-success" style="font-size: 12px;"></span></b>\
                <b><span id="danger-${stan.normaid}" class="text-danger" style="font-size: 12px;"></span></b>`
            ]).draw(false);
        })
    })
    .fail(() => {
        alert('Problem z pobraniem stanów za dany miesiąc')
    })
}

function saveStan(btn){
    var normaId = $(btn).attr('normaid');
    var rok = $(btn).attr('rok');
    var miesiac = $(btn).attr('miesiac');
    var wartosc = $(`#${normaId}`).val();

    var stan = {
        wartosc: wartosc,
        norma: {
            id: normaId
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
        url: contextRoot + 'stan',
        data: JSON.stringify(stan),
        type: 'PUT',
        headers: headers
    }

    $.ajax(properties)
    .done(() => {
        $(`#success-${normaId}`).text('zapisano')
    })
    .fail(() => {
        $(`#danger-${normaId}`).text('problem z zapisem')
    })

}