var t;
$(document).ready(function() {

    $('#kilometersLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#initialStatesDropdownMenuLink').css("font-weight", "bold").css("text-decoration", "underline");

    var queryString = window.location.search;
    var urlParams = new URLSearchParams(queryString);
    var monthParam = urlParams.get('month');
    if(monthParam){
        $('#month').val(monthParam)
        $('#reportLink').attr('href', $('#raportLink').attr('href') + '?month=' + monthParam)
        $('#documentsLink').attr('href', $('#dokumentyLink').attr('href') + '?month=' + monthParam)
        $('#machinesLink').attr('href', $('#dokumentyLink').attr('href') + '?month=' + monthParam)
        $('#initialStatesDropdownMenuLink').attr('href', $('#stanyLink').attr('href') + '?month=' + monthParam)
        $('#kilometersLink').attr('href', $('#kilometryLink').attr('href') + '?month=' + monthParam)
    }
    else{
        var month = new Date().getMonth()+1
        if(month < 10) month = '0' + month

        var year = new Date().getFullYear()
        $('#month').val(year + '-' + month)
    }

    var year = new Date().getFullYear()
    $('#month').val(year + '-' + month)

    $('#dataSpan').text($('#month').val())

    t = $('#kilometersTable').DataTable({
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
        if(!monthParam){
            $('#reportLink').attr('href', $('#raportLink').attr('href') + '?month=' + $('#miesiac').val())
            $('#documentsLink').attr('href', $('#dokumentyLink').attr('href') + '?month=' + $('#miesiac').val())
            $('#machinesLink').attr('href', $('#maszynyLink').attr('href') + '?month=' + $('#miesiac').val())
            $('#initialStatesDropdownMenuLink').attr('href', $('#stanyLink').attr('href') + '?month=' + $('#miesiac').val())
            $('#kilometersLink').attr('href', $('#kilometryLink').attr('href') + '?month=' + $('#miesiac').val())
        }
        $('#dataSpan').text($('#month').val())
        updateTable();
    })

});

function updateTable(){
   t.clear().draw();
    var year = $('#month').val().split('-')[0]
    var month = $('#month').val().split('-')[1]
    $.ajax({
        url: contextRoot + 'kilometers',
        data: {
            year: year,
            month: month
        }
    })
    .done(kilometry => {
        $.each(kilometry, (i, k) => {
            t.row.add([
                `${k.machineName} (${k.machineId})`,
                `<input class="form-control text-center maszyna" type="number" step="0.01" value="${k.initialState}" style="width: 110px;"></input>`,
                `<button class="btn btn-info" year="${year}" month="${month}" machineid="${k.machineId}" onclick="saveKilometers(this)">zapisz</button>\
                <b><span id="success-${k.machineId}" class="text-success" style="font-size: 12px;"></span></b>\
                <b><span id="danger-${k.machineId}" class="text-danger" style="font-size: 12px;"></span></b>`
            ]).draw(false);
        })
    })
    .fail(() => {
        alert('Problem z pobraniem kilometrów za dany miesiąc')
    })
}

function saveKilometers(btn){
    var machineId = $(btn).attr('machineid');
    var year = $(btn).attr('year');
    var month = $(btn).attr('month');
    var value = $(btn).parent().parent().find('input').val()

    var kilometers = {
        value: value,
        machine: {
            id: machineId
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
        url: contextRoot + 'kilometers',
        data: JSON.stringify(kilometers),
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