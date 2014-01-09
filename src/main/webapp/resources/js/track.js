function setConnected(connected) {
    $("#connect").prop('disabled',connected);
    $("#disconnect").prop('disabled',!connected);
    $("#echo").prop('disabled',!connected);
}

function connect() {
    var url;
    
    if (window.location.protocol == 'http:') {
        url = "ws://" + window.location.host +"/data-cruncher/track";
    }
    else {
        url = "wss://" + window.location.host +"/data-cruncher/track";
    }

    ws = new WebSocket(url);

    ws.onopen = function() {
        setConnected(true);
        log('Info: connection opened.');
    }
    ws.onmessage = function (event) {
        var num = $("#table1 tr").length
        if (num > 5) {
            $("#table1 tbody tr:first").remove()
        }
            
        $("#table1 > tbody:last").append(event.data)
        log('Received: ' + event.data);
    };
    ws.onclose = function (event) {
        setConnected(false);
        log('Info: connection closed.');
        log(event);
    };
}

function disconnect() {
    if (ws != null) {
        ws.close();
        ws = null;
    }
    setConnected(false);
}

function log(message) {
    var console = document.getElementById('console');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(message));
    console.appendChild(p);
    while (console.childNodes.length > 25) {
        console.removeChild(console.firstChild);
    }
    console.scrollTop = console.scrollHeight;
}



$(document).ready(function() {
    var ws;
    $("#connect").click(function() {
        connect();
    });

    $("#disconnect").click(function() {
        disconnect();
    });

    $("#echo").click(function() {
        log("Sent a message");
        ws.send("message");
    })
    
    $("#button1").click(function() {
        if( $("#table2").length != 0) {
            $('#table2').dataTable().fnDestroy();
            $("#table2").remove();
        }

        if (ws == null || ws == undefined) {
            alert("init websocket")
            var url;
            if (window.location.protocol == 'http:') {
                url = "ws://" + window.location.host +"/data-cruncher/track";
            }
            else {
                url = "wss://" + window.location.host +"/data-cruncher/track";
            }
            ws = new WebSocket(url);
            ws.onopen = function() {
                console.log("open");
                ws.send("a message");
                var table = '<table border="1" id="table1"><thead></thead><tbody></tbody></table>'
                $("#all-table-container").append(table)
                $("#table1 thead:last").append("<tr></tr>")
                $("#table1 thead tr:last").append("<th>source ip</th>")
                $("#table1 thead tr:last").append("<th>service name</th>")
                $("#table1 thead tr:last").append("<th>service category</th>")
                $("#table1 thead tr:last").append("<th>event type</th>")
                $("#table1 thead tr:last").append("<th>event time</th>")
                $("#table1 thead tr:last").append("<th>event count</th>")
                $("#table1 thead tr:last").append("<th>record time</th>")
                $("#table1 thead tr:last").append("<th>is test data</th>")
                $("#table1 thead tr:last").append('<th class="error">error msg</th>')


            }
            ws.onmessage = function (event) {
                obj = $.parseJSON(event.data);

                $.each(obj, function(idx,val) {
                    $("#table1 tbody").append("<tr></tr>")
                    $("#table1 tbody tr:last").append("<td>"+val.ip+"</td>")
                    $("#table1 tbody tr:last").append("<td>"+val.serviceName+"</td>")
                    $("#table1 tbody tr:last").append("<td>"+val.serviceCategory+"</td>")

                    $("#table1 tbody tr:last").append("<td>"+val.eventType+"</td>")
                    $("#table1 tbody tr:last").append("<td>"+val.eventTime+"</td>")
                    $("#table1 tbody tr:last").append("<td>"+val.eventCount+"</td>")
                    $("#table1 tbody tr:last").append("<td>"+val.recordTime+"</td>")
                    $("#table1 tbody tr:last").append("<td>"+val.testFlag+"</td>")
                    $("#table1 tbody tr:last").append('<td class="error">'+val.errorMsg+"</td>")
                });                



                while ($("#table1 tr").length > 10) {
                    $("#table1 tbody tr:first").remove()
                }
                // log('Received: ' + event.data);
            };
            ws.onclose = function (event) {
                // setConnected(false);
                // log('Info: connection closed.');
                // log(event);
            }; 
        }
        
    });
    
    $("#button2").click(function() {
        if (ws != null) {
            ws.close();
            ws = null;
        }
        $("#table1").remove();
        $.ajax({
            url: "track/all",
            success: function(data) {
                var table = '<table border="1" id="table2"><thead></thead><tbody></tbody></table>'
                $("#all-table-container").append(table)
                $("#table2 thead:last").append("<tr></tr>")
                $("#table2 thead tr:last").append("<th>source ip</th>")
                $("#table2 thead tr:last").append("<th>service name</th>")
                $("#table2 thead tr:last").append("<th>service category</th>")
                $("#table2 thead tr:last").append("<th>event type</th>")
                $("#table2 thead tr:last").append("<th>event time</th>")
                $("#table2 thead tr:last").append("<th>event count</th>")
                $("#table2 thead tr:last").append("<th>record time</th>")
                $("#table2 thead tr:last").append("<th>is test data</th>")
                $("#table2 thead tr:last").append('<th class="error">error msg</th>')
                $.each(data, function(idx,val) {
                        $("#table2 tbody").append("<tr></tr>")
                        $("#table2 tbody tr:last").append("<td>"+val.ip+"</td>")
                        $("#table2 tbody tr:last").append("<td>"+val.serviceName+"</td>")
                        $("#table2 tbody tr:last").append("<td>"+val.serviceCategory+"</td>")

                        $("#table2 tbody tr:last").append("<td>"+val.eventType+"</td>")
                        $("#table2 tbody tr:last").append("<td>"+val.eventTime+"</td>")
                        $("#table2 tbody tr:last").append("<td>"+val.eventCount+"</td>")
                        $("#table2 tbody tr:last").append("<td>"+val.recordTime+"</td>")
                        $("#table2 tbody tr:last").append("<td>"+val.testFlag+"</td>")
                        $("#table2 tbody tr:last").append('<td class="error">'+val.errorMsg+"</td>")
                });
                $('#table2').dataTable({
                    "bPaginate": true,
                    "bSort": false
                } );
            }
        })
    })
})
