var t;
$(document).ready(function() {

    $('#raportMaszynaKilometryLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#raportyDropdownMenuLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#loadingDiv').hide()

    t = $('#raportMaszynaKilometry').DataTable({
        "pageLength": 10,
        lengthMenu: [
            [ 5, 10, 25, 50, -1 ],
            [ '5', '10', '25', '50', 'Wszystko' ]
        ],
        "language": {
            "search": "Szukaj",
            "emptyTable": "Brak dokumentów",
            "lengthMenu": "Pokaż _MENU_",
            "zeroRecords": "Nie znaleziono dokumentów",
            "paginate": {
                "first": "Pierwsza",
                "last": "Ostatnia",
                "next": "Następna",
                "previous": "Poprzednia"
            },
            "info": "_START_ do _END_ z _TOTAL_ dokumentów",
            "infoEmpty":      "",
            "infoFiltered":   "",
        }
    });

    $('#maszyna').change(() => {
        updateTable();
    })

     $('#dataStart').change(() => {
        updateTable();
    })

     $('#dataEnd').change(() => {
        updateTable();
    })

});

function updateTable(){

    if(!$('#maszyna').val() || !$('#dataStart').val() || !$('#dataEnd').val()) return

    $('#tableDiv').hide()
    $('#loadingDiv').show()
    t.clear().draw();
    $.ajax({
        url: contextRoot + 'getRaportMaszynaKilometry',
        data: {
            maszynaId: $('#maszyna').val(),
            start: $('#dataStart').val(),
            end: $('#dataEnd').val()
        }
    })
    .done(maszynaKilometry => {
        $('#kilometry').html(maszynaKilometry.sumaKilometry)
        $('#kilometryPrzyczepa').html(maszynaKilometry.sumaKilometryPrzyczepa)
        $.each(maszynaKilometry.dokumenty, (i, dokument) => {
            t.row.add( [
                dokument.numer,
                dokument.data,
                dokument.kilometry,
                dokument.kilometryPrzyczepa
            ]).draw(false);
        })
        $('#loadingDiv').hide()
        $('#tableDiv').show()
    })
    .fail(() => {
        alert('Problem z pobraniem danych dla zadanych parametrów')
        $('#loadingDiv').hide()
    })

}