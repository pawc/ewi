var t;
$(document).ready(function() {

    $('#raportLink').css("font-weight", "bold");
    $('#raportLink').css("text-decoration", "underline");

    var month = new Date().getMonth()+1
    if(month < 10) month = '0' + month

    var year = new Date().getFullYear()
    $('miesiac').val(year + '-' + month)

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
        },
        columnDefs: [{
            targets: -1,
            className: 'dt-body-center'
        }]
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
        url: '/raport',
        data: {
            rok: rok,
            miesiac: miesiac
        }
    })
    .done(pozycje => {
        $.each(pozycje, (i, pozycja) => {
            t.row.add( [
                pozycja.maszyna,
                pozycja.jednostka,
                pozycja.suma
            ]).draw(false);
        })
    })
    .fail(() => {
        alert('Problem z pobraniem raportu za dany miesiąc')
    })
}