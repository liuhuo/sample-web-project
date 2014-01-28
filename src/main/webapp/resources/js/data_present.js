function show_chart(a,type,days,container) {
    ydata = [];
    isp = ""
    elem = ""
    for (var i = 0; i < a.length; i++) {
        // var dateParts = a[i].date.split("-")
        // var utcDate = Date.UTC(dateParts[0],dateParts[1],dateParts[2])
        utcDate = a[i].date
        if (a[i].isp != isp) {
            if (i != 0) ydata.push(elem)
            isp = a[i].isp
            elem= {'name':isp, data:[ [utcDate, a[i].clicks] ]}
        }
        else elem.data.push([utcDate, a[i].clicks])
    }
    ydata.push(elem);
    //    console.log(ydata);

    //container = "#"
    $(container).highcharts({
        title: {
            text: days+type,
            x: -20 //center
        },
        subtitle: {
            text: '来源 数据库',
            x: -20
        },
        xAxis: {
            type: 'datetime',
            dateTimeLabelFormats: { // don't display the dummy year
                month: '%e. %b',
                year: '%b'
            }
            //categories: xdata
        },
        yAxis: {
            title: {
                text: 'clicks'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: ydata
    });
}

function remove() {
    $("#table1").remove();
    $("#overall-b1").remove();
    $("#overall-b2").remove();
    $("#isp-b1").remove();
    $("#isp-b2").remove();

    $("#select1").remove();
    $("#select_isp").remove();
    $("#overall-graph-container").empty();
    $("#isp-service-area").empty();
    $("#isp-graph-container").empty();
}

$(document).ready(function() {
    $("#button2").click(function() {
        remove();
        $.ajax({
            url: "/data-cruncher/remote/isp/names"
        }).then(function(data,other1,other2) {
            var select_isp = '<select id="select_isp"></select>';
            $("#isp-data").append(select_isp);
            $.each(data, function(idx, val) {
                $("#select_isp").append($("<option></option>").val(val).html(val));
            });
            $("#select_isp").prop("selectedIndex", -1);
        })
    })

    $("#isp-data").on("change","select",function() {
        var isp = this.value;
        $.ajax({
            url: "/data-cruncher/remote/isp/summary",
            data: {"isp": isp}
        }).then(function(data,other1, other2) {
            $("#table1").remove()
            var table = '<table border="1" id="table1"><thead></thead><tbody></tbody></table>'
            $("#isp-data").append(table);
            $("#table1 thead:last").append("<tr></tr>")
            $("#table1 thead tr:last").append("<th>"+isp+"数据</th>")
            $("#table1 thead tr:last").append("<th>浏览次数(PV)</th>")
            $("#table1 thead tr:last").append("<th>独立访客(UV)</th>")
            $.each(data,function(idx,val) {
                $("#table1 tbody").append("<tr></tr>")
                $("#table1 tbody tr:last").append("<td>"+val.name+"</td>")
                $("#table1 tbody tr:last").append("<td>"+val.data.pv+"</td>")
                $("#table1 tbody tr:last").append("<td>"+val.data.uv+"</td>")
            });
            return $.ajax({
                url: "/data-cruncher/remote/isp/services",
                data: {"isp": isp}
            })
        }).then(function(data, other1, other2) {
            $("#isp-b1").remove()
            $("#isp-b2").remove()
            $("#select1").remove()
            var checkboxdiv = $("#isp-service-area")
            checkboxdiv.empty();
            $.each(data, function(idx, val) {
                checkboxdiv.append($('<input type="checkbox" name="services[]" value=' + val+ '>'+val +'</br>') )
            });

                var b1_text = '<button id="isp-b1" type="button">last 7 days graph</button>'
                var b2_text = '<button id="isp-b2" type="button">last 30 days graph</button>'
                $("#isp-button-area").append(b1_text)
                $("#isp-button-area").append(b2_text)
                $("#isp-button-area").append('<select id="select1"><option value="pv">浏览次数(PV)</option><option value="uv">独立访客(UV)</option></select>')
        })
    })

    $("#isp-button-area").on("click","button",function() {
        var days = this.id == "isp-b1" ? 7 : 30
        var selected_option = $('#select1 option:selected');
        var checked = $("input:checked")
        var services = ""
        $.each(checked,function(idx, val) {
            services += $(val).val()
            services += "#"
        });
        services = services.substring(0,services.length-1);
        var url = "/data-cruncher/remote/isp/service/detail"
        var isp = $("#select_isp option:selected").val()

        $.ajax({
            url: url,
            data: {"services":services, "days":days, "type":selected_option.val(),"isp":isp}
        }).then(function(data, other1, other2) {
            //console.log(data)
            var type = selected_option.val() == "pv" ? "浏览次数(PV)": "独立访客(UV)"
            var range = "最近"+days+"日";
            show_chart(data,type,range,"#isp-graph-container");
            return $.ajax({
                url: "/data-cruncher/remote/isp/service/month",
                data: {"isp":isp}
            })
        }).then(function(data, other1, other2) {
            console.log(data);
            $("#isp-table1").remove()
            var dateSet = [];
            var serviceSet = [];
            var prev = -1;
            var map = {}
            for (var i = 0; i < data.length; i++) {
                if (data[i].date != prev) {
                    dateSet.push(data[i].date)
                    prev = data[i].date
                }
            }
            for (var i = 0; i < data.length; i++) {
                if (map[data[i].isp] == null || map[data[i].isp] == undefined) {
                    map[data[i].isp] = {}
                    serviceSet.push(data[i].isp)
                }
                var key = data[i].date
                var value = data[i].clicks
                map[data[i].isp][key] = value
            }
            console.log(serviceSet)
            var table = '<table border="1" id="isp-table1"><thead></thead><tbody></tbody></table>'
            $("#isp-data-table-area").append(table);
            $("#isp-table1 thead:last").append("<tr></tr>")
            $("#isp-table1 thead tr:last").append("<th></th>")
            $.each(dateSet,function(idx,val) {
                var dateString = new Date(val).toString().substring(4,16)
                $("#isp-table1 thead tr:last").append("<th>"+dateString+"</th>")
            });
            $.each(serviceSet, function(idx,val) {
                $("#isp-table1 tbody").append("<tr></tr>")
                $("#isp-table1 tbody tr:last").append("<td>"+val+"</td>")
                $.each(dateSet, function(idx1, val1) {
                    if (map[val] != undefined && map[val][val1] != undefined) {
                        $("#isp-table1 tbody tr:last").append("<td>"+map[val][val1]+"</td>")
                    }
                    else {
                        $("#isp-table1 tbody tr:last").append("<td>"+0+"</td>")
                    }
                })
            })
        });

        // var url = "/data-cruncher/remote/isp/" + selected_option.val() + "/" + days+ "days";
        // var type = selected_option.val() == "pv" ? "浏览次数(PV)": "独立访客(UV)"
        // var range = "最近"+days+"日"
        // $.ajax({
        //     url: url
        // }).then(function(data,other1,other2) {
        //     console.log(data)
        //     show_chart(data,type,range);
        // });
    })


    $("#button1").click(function() {
        $.ajax({
            url: "/data-cruncher/remote/count/overall",
        }).then(function(data,other1,other2) {
            remove();

            var table = '<table border="1" id="table1"><thead></thead><tbody></tbody></table>'
            $("#overall-data").append(table);
            $("#table1 thead:last").append("<tr></tr>")
            $("#table1 thead tr:last").append("<th>所有区域综合数据</th>")
            $("#table1 thead tr:last").append("<th>浏览次数(PV)</th>")
            $("#table1 thead tr:last").append("<th>独立访客(UV)</th>")
            $.each(data,function(idx,val) {
                $("#table1 tbody").append("<tr></tr>")
                $("#table1 tbody tr:last").append("<td>"+val.name+"</td>")
                $("#table1 tbody tr:last").append("<td>"+val.data.pv+"</td>")
                $("#table1 tbody tr:last").append("<td>"+val.data.uv+"</td>")
            });
            var b1_text = '<button id="overall-b1" type="button">last 7 days graph</button>'
            var b2_text = '<button id="overall-b2" type="button">last 30 days graph</button>'
            $("#overall-button-area").append(b1_text)
            $("#overall-button-area").append(b2_text)
            $("#overall-button-area").append('<select id="select1"><option value="pv">浏览次数(PV)</option><option value="uv">独立访客(UV)</option></select>')
        });
    });


    $("#overall-button-area").on("click","button",function() {
        var days = this.id == "overall-b1" ? 7 : 30
        var selected_option = $('#select1 option:selected');
        var url = "/data-cruncher/remote/isp/" + selected_option.val() + "/" + days+ "days";
        var type = selected_option.val() == "pv" ? "浏览次数(PV)": "独立访客(UV)"
        var range = "最近"+days+"日"
        $.ajax({
            url: url
        }).then(function(data,other1,other2) {
            console.log(data)
            show_chart(data,type,range,"#overall-graph-container");
        });
    })

    $("#overall-button-area").on("change","select",function() {
        var title = $(".highcharts-title").text()
        var selected_option = $('#select1 option:selected');
        if (title != null && title != undefined && title != "") {
            var days = /\d+/.exec(title)[0]
            var url = "/data-cruncher/remote/isp/" + selected_option.val() + "/" + days+ "days";
            var type = selected_option.val() == "pv" ? "浏览次数(PV)": "独立访客(UV)"
            var range = "最近"+days+"日"
            $.ajax({
                url: url
            }).then(function(data,other1,other2) {
                console.log(data)
                show_chart(data,type,range,"#overall-graph-container");
            });
        }
    })
});
