var t;
$(document).ready(function() {

    $('#raportRocznytLink').css("font-weight", "bold");
    $('#raportRocznytLink').css("text-decoration", "underline");

    $("#rok").datepicker({
        changeMonth: false,
        changeYear: true,
        dateFormat: 'yy'
    });

    $('#rok').on('click', function() {
        $('.ui-datepicker-calendar').hide();
    });

    var year = new Date().getFullYear()
    $('#rok').val(year)

    t = $('#raportRocznyTable').DataTable({
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

    $('#raportRocznyTable_wrapper > div.dt-buttons > button > span').text('Drukuj raport')
    $('#raportRocznyTable_wrapper > div.dt-buttons > button').addClass('btn btn-info')
    $('#raportRocznyTable_wrapper > div.dt-buttons').css('text-align', 'right')
    $('#raportRocznyTable_wrapper > div.dt-buttons').css('margin-right', '50px')
    $('#raportRocznyTable_filter').css('margin-right', '50px')
    $('#raportRocznyTable_length').css('margin-left', '5%')

    updateTable();

    $('#rok').change(() => {
        updateTable();
    })
});

function updateTable(){
    t.clear().draw();
    var rok = $('#rok').val()
    $.ajax({
        url: contextRoot + 'raportRoczny',
        data: {
            rok: rok
        }
    })
    .done(pozycje => {
        $.each(pozycje, (i, pozycja) => {
            t.row.add( [
                pozycja.kategoria,
                pozycja.jednostka,
                pozycja.suma
            ]).draw(false);
        })
    })
    .fail(() => {
        alert('Problem z pobraniem raportu za dany rok')
    })
}