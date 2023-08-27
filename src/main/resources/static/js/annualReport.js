var t;
$(document).ready(function() {

    $('#annualReportLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#reportsDropdownMenuLink').css("font-weight", "bold").css("text-decoration", "underline");

    $("#year").datepicker({
        changeMonth: false,
        changeYear: true,
        dateFormat: 'yy'
    });

    $('#year').on('click', function() {
        $('.ui-datepicker-calendar').hide();
    });

    var year = new Date().getFullYear()
    $('#year').val(year)

    t = $('#annualReportTable').DataTable({
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

    $('#annualReportTable_wrapper > div.dt-buttons > button > span').text('Drukuj raport')
    $('#annualReportTable_wrapper > div.dt-buttons > button').addClass('btn btn-info')
    $('#annualReportTable_wrapper > div.dt-buttons').css('text-align', 'right')
    $('#annualReportTable_wrapper > div.dt-buttons').css('margin-right', '50px')
    $('#annualReportTable_filter').css('margin-right', '50px')
    $('#annualReportTable_length').css('margin-left', '5%')

    updateTable();

    $('#year').change(() => {
        updateTable();
    })
});

function updateTable(){
    $('#tableDiv').hide()
    $('#loadingDiv').show()
    t.clear().draw();
    var year = $('#year').val()
    $.ajax({
        url: contextRoot + 'reportAnnual',
        data: {
            year: year
        }
    })
    .done(records => {
        $.each(records, (i, p) => {
            t.row.add( [
                p.category,
                p.unit,
                p.weight,
                p.sum,
                p.sumMultipliedByWeight
            ]).draw(false);
        })
        $('#loadingDiv').hide()
        $('#tableDiv').show()
    })
    .fail(() => {
        alert('Problem z pobraniem raportu za dany rok')
        $('#loadingDiv').hide()
    })
}