var t;
$(document).ready(function() {

    $('#machineKilometersReportLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#reportsDropdownMenuLink').css("font-weight", "bold").css("text-decoration", "underline");
    $('#loadingDiv').hide()

    t = $('#reportMachineKilometersTable').DataTable({
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

    $('#machine').change(() => {
        updateTable();
    })

     $('#dateStart').change(() => {
        updateTable();
    })

     $('#dateEnd').change(() => {
        updateTable();
    })

});

function updateTable(){

    if(!$('#machine').val() || !$('#dateStart').val() || !$('#dateEnd').val()) return

    $('#tableDiv').hide()
    $('#loadingDiv').show()
    t.clear().draw();
    $.ajax({
        url: contextRoot + 'getReportMachineKilometers',
        data: {
            machineId: $('#machine').val(),
            start: $('#dateStart').val(),
            end: $('#dateEnd').val()
        }
    })
    .done(machineKilometers => {
        $('#kilometers').html(machineKilometers.sumKilometers)
        $('#kilometersTrailer').html(machineKilometers.sumKilometersTrailer)
        $.each(machineKilometers.documents, (i, document) => {
            t.row.add( [
                document.number,
                document.date,
                document.kilometers,
                document.kilometersTrailer
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