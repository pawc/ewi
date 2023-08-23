var t;
$(document).ready(function() {

    $('#quarterlyReportLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#reportsDropdownMenuLink').css("font-weight", "bold").css("text-decoration", "underline");

    var quarter = '0' + Math.floor((new Date().getMonth() + 3) / 3);

    var year = new Date().getFullYear()
    $('#quarter').val(year + '-' + quarter)

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
    $('#raportTable_filter').css('margin-right', '50px')
    $('#reportTable_length').css('margin-left', '5%')

    updateTable();

    $('#quarter').change(() => {
        updateTable();
    })
});

function updateTable(){
    $('#loadingDiv').show()
    $('#tableDiv').hide()
   t.clear().draw();
    var year = $('#quarter').val().split('-')[0]
    var quarter = $('#quarter').val().split('-')[1]
    $.ajax({
        url: contextRoot + 'getReportQuarterly',
        data: {
            year: year,
            quarter: quarter
        }
    })
    .done(records => {
        $.each(records, (i, p) => {
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
                '0',
                '0',
                p.machineId
            ]).draw(false);
        })
        $('#loadingDiv').hide()
        $('#tableDiv').show()
    })
    .fail(() => {
        alert('Problem z pobraniem raportu za dany kwartał')
        $('#loadingDiv').hide()
    })
}