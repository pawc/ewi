var t;
$(document).ready(function() {

    $('#raportKwartalnyLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#raportyDropdownMenuLink').css("font-weight", "bold").css("text-decoration", "underline");

    var quarter = '0' + Math.floor((new Date().getMonth() + 3) / 3);

    var year = new Date().getFullYear()
    $('#kwartal').val(year + '-' + quarter)

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

    $('#kwartal').change(() => {
        updateTable();
    })
});

function updateTable(){
    $('#loadingDiv').show()
    $('#tableDiv').hide()
   t.clear().draw();
    var rok = $('#kwartal').val().split('-')[0]
    var kwartal = $('#kwartal').val().split('-')[1]
    $.ajax({
        url: contextRoot + 'getRaportKwartalny',
        data: {
            rok: rok,
            kwartal: kwartal
        }
    })
    .done(pozycje => {
        $.each(pozycje, (i, pozycja) => {
            t.row.add( [
                pozycja.maszyna,
                pozycja.stankilometry,
                pozycja.kilometry,
                pozycja.endStateKilometry,
                pozycja.kilometryprzyczepa,
                pozycja.jednostka,
                pozycja.stanPoprz,
                pozycja.suma,
                pozycja.zatankowano,
                pozycja.ogrzewanie,
                pozycja.endState,
                pozycja.sumagodzin,
                pozycja.normaId,
                '0',
                '0',
                pozycja.maszynaid
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