$(document).ready(function() {
    alert("jquery ready!!");

    $("#button1").click(function() {
        $.ajax({
            type:"GET",
            url:"alluser",
            success: function(data) {
                var userdata = [
                    [ "id", "name", "role", ],
                    [data.id,data.name,data.role]
                ];
                $("#dataTable").handsontable({
                    data: data,
                    startRows: 6,
                    startCols: 8
                });
            }
        });
    });
});
