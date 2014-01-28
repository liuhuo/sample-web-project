function getinfo() {
    $.ajax({
        url : "poll",
        success: function(data) {
            console.log(data);
        }
    });
}
var sendData = function(event, ui) {
    var selected = $(".ui-selected")
    var data = ""
    $.each(selected,function(idx, val) {
        data += $(val).text()
        data += "#"
    });
    data = data.substring(0,data.length-1);
    if (data == "") return
    var defer2 = $.ajax({
        url: "selected_service",
        data: {'services':data}
    });
    var defer1 = $.ajax("dates");
    $.when(defer1,defer2).done(show_chart)
}


function show_chart(a1, a2) {
    console.log(a1[0]);
    console.log(a2[0]);
    xdata = a1[0];
    ydata = [];
    isp = ""
    elem = ""
    for (var i = 0; i < a2[0].length; i++) {
        // var dateParts = a2[0][i].date.split("-")
        // var utcDate = Date.UTC(dateParts[0],dateParts[1],dateParts[2])
        var utcDate = a2[0][i].date
        if (a2[0][i].isp != isp) {
            if (i != 0) ydata.push(elem)
            isp = a2[0][i].isp
            // elem = {'name':isp,data:[a2[0][i].clicks]}

            elem= {'name':isp, data:[ [utcDate, a2[0][i].clicks]]}
        }
        // else elem.data.push(a2[0][i].clicks)
        else elem.data.push([utcDate, a2[0][i].clicks])
    }
    ydata.push(elem);
    console.log(ydata);

    $('#container').highcharts({
        title: {
            text: 'monthly clicks stats',
            x: -20 //center
        },
        subtitle: {
            text: 'Source: from excel',
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

$(document).ready(function() {
    // var query = btoa(encodeURIComponent('event_count=3&event_source_ip=1.2.3.4&datetime=2013-12-31 19:23:59&service_name=重定向麒&event_type=CC'))
    // alert(query)

    // $.ajax("saveremotedata",{
    //     data:query,
    //     type:"GET"})

    var Service = Backbone.Model.extend({
        urlRoot: 'service',
        defaults: {
            title: "My service",
            price: 100,
            checked: false
        },
        toggle: function(){
            this.set('checked', !this.get('checked'));
        }
    });

    var ServiceList = Backbone.Collection.extend({
        model: Service,
        initialize: function() {
            this.on('reset',function(c,o) {
                console.log('inside reset')
                console.log(c);
            })
        },
        getChecked: function() {
            return this.where({checked: true});
        }
    })

    var service = new Service({id: "serv1"});
    // console.log("before fetch");
    // console.log(service)
    // service.fetch()
    // console.log("after fetch");
    // console.log(service)

    // var services = new ServiceList();
    // services.url="services";
    // services.fetch({reset: true});

    // var services = new ServiceList();
    // $.ajax({
    //     url: "services",
    //     success: function(data) {
    //         console.log(data)
    //         services.add(data);
    //     }
    // })
    // console.log(services)
    // console.log(services)
    // services.url="services";
    // services.fetch({reset: true});
    // console.log(services);

    var services = new ServiceList([
        new Service({ title: 'web development', price: 200}),
        new Service({ title: 'web design', price: 250}),
        new Service({ title: 'photography', price: 100}),
        new Service({ title: 'coffee drinking', price: 10})
    ]);
    console.log(services)

    var ServiceView = Backbone.View.extend({
        tagName: 'li',
        events: {
            'click': 'toggleService'
        },
        initialize: function () {
            this.listenTo(this.model, 'change', this.render);
        },
        render: function() {
            this.$el.html('<input type="checkbox" value="1" name="' + this.model.get('title') + '" /> ' + this.model.get('title') + '<span>$' + this.model.get('price') + '</span>');
            this.$('input').prop('checked', this.model.get('checked'));
            return this;
        },
        toggleService: function() {
            this.model.toggle();
        }
    });

    var App = Backbone.View.extend({
        el: $("#form2"),
        initialize: function() {

            this.total = $('#total span');
            this.list = $('#services');
            console.log(services)
            this.listenTo(services, 'change', this.render);
            services.each(function(service){
                var view = new ServiceView({ model: service });
                this.list.append(view.render().el);
            }, this);
        },
        render: function() {
            var total = 0;
            _.each(services.getChecked(),function(elem) {
                total += elem.get('price');
            });
            this.total.text("$"+total);
            return this;
        }
    });

    new App();

    // $.when($.ajax({
    //     url: "isps",
    //     success: function(data) {
    //         console.log(data);
    //         var select = $("#allisp");
    //         $.each(data, function(idx, val) {
    //             select.append($("<option></option>").val(val).html(val));
    //         });
    //     }
    // })).then(function() {
    //     var isp = $("#allisp").val()
    // });

    var checkboxes = function(data) {
        var checkboxdiv = $("#massive")
        checkboxdiv.empty();
        $.each(data, function(idx, val) {
            checkboxdiv.append($('<input type="checkbox" name="services[]" value=' + val+ '>'+val +'</br>') )
        });
    }

    $.ajax("isps").then(function(data,other1,other2) {
        var select = $("#listisp");
        $.each(data, function(idx, val) {
            select.append($("<option></option>").val(val).html(val));
        });

        select.change(function() {
            $.ajax({
                url: "ispservices",
                data: {'isp': select.val()},
                success: checkboxes
            })
        });


        return $.ajax({
            url: "ispservices",
            data:  {'isp': select.val()}});
    }).then(function(data, other1,other2) {
        checkboxes(data);
        $("#massive").on("click","input",function() {
            var checked = $("input:checked")
            var data = ""
            $.each(checked,function(idx, val) {
                data += $(val).val()
                data += "#"
            });
            data = data.substring(0,data.length-1);

            if (data == "") return
            var defer2 = $.ajax({
                url: "selected_service",
                data: {'services':data}
            });
            var defer1 = $.ajax("dates");
            $.when(defer1,defer2).done(show_chart)
        })
    });

    // $("#allisp").change(function() {
    //     $.ajax({
    //         url: "ispservices",
    //         data: {'isp':$(this).val()},
    //         contentType: "application/x-www-form-urlencoded; charset=UTF-8",
    //         success: function(data) {
    //             console.log(data)
    //             $("#servicenames").empty()
    //             $.each(data, function(idx, val) {
    //                 $("#servicenames").append($('<li class="ui-widget-content"></li>').html(val));
    //             });
    //             $("#servicenames").selectable({
    //                 selected: sendData,
    //                 unselected: sendData
    //             });
    //         }
    //     })
    // })

    $.ajax({
        url: "names",
        success: function(data) {
            $("#ac").typeahead({
                name: 'amount',
                local: data
            })
        }
    })
    // $("#ac").typeahead({
    //     name: 'accounts',
    //     local:
    // });

    // $("#ac").focus(function() {
    //     $.ajax({
    //         url: "names",
    //         success: function(data) {
    //             console.log(data)
    //             $("#ac").typeahead({
    //                 name: "website names",
    //                 local: data
    //             })
    //         }
    //     })
    // })

    $("#date").datepicker()
    $( "#from" ).datepicker({
        defaultDate: "+1d",
        changeMonth: true,
        numberOfMonths: 1,
        onClose: function( selectedDate ) {
            $( "#to" ).datepicker( "option", "minDate", selectedDate );
        }
    });
    $( "#to" ).datepicker({
        defaultDate: "+1d",
        changeMonth: true,
        numberOfMonths: 1,
        onClose: function( selectedDate ) {
            $( "#from" ).datepicker( "option", "maxDate", selectedDate );
        }
    });


    $("#button4").click(function() {
        $.ajax({
            url: "remote",
            type: "POST",
            data: {"dummy": "dummy data","dummy2":"dummy value2","dummy3":"dummy value 3"},
            success: function(data) {
                console.log(data);
            }
        })
    });

    $("#botton3").click(function() {
        var isp = $("#isp").val();
        var website = $("#website").val();
        var date = $("#date").val();
        var clicks = $("#clicks").val();
        $.ajax({
            url: "save",
            type: "POST",
            data: {'isp':isp, 'website':website,'date':date,'clicks':clicks},
            success: function(data) {
                console.log(data)
            }
        })
    })

    $("#botton2").click(function () {
        var start = $("#from").val();
        var end = $("#to").val();
        var defer1 = $.ajax({
            url: 'rangedate',
            data: {'from': start, 'to':end}
        })
        var defer2 = $.ajax({
            url: 'rangeclick',
            data: {'from': start, 'to':end}
        })
        $.when(defer1,defer2).done(show_chart);
    });

    $.when($.ajax("dates"),$.ajax("clicks")).done(show_chart);

    $("#button1").click(function() {
        $.ajax({
            type:"GET",
            url:"alluser",
            success: function(data) {
                var formatted = [ ["id","name","role"]];
                for (var i = 0 ; i < data.length; i++) {
                    formatted.push([data[i].id,data[i].name,data[i].role])
                }
                $("#dataTable").handsontable({
                    data: formatted,
                    startRows: 6,
                    startCols: 8,
                    minSpareRows: 1,
                    colHeaders: true,
                    contextMenu: true
                });
            }
        });
    });

    // setInterval(function () { getinfo(); },5000);

    // $('#container').highcharts({
    //         title: {
    //             text: 'Monthly Average Temperature',
    //             x: -20 //center
    //         },
    //         subtitle: {
    //             text: 'Source: WorldClimate.com',
    //             x: -20
    //         },
    //         xAxis: {
    //             categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
    //                 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
    //         },
    //         yAxis: {
    //             title: {
    //                 text: 'Temperature (°C)'
    //             },
    //             plotLines: [{
    //                 value: 0,
    //                 width: 1,
    //                 color: '#808080'
    //             }]
    //         },
    //         tooltip: {
    //             valueSuffix: '°C'
    //         },
    //         legend: {
    //             layout: 'vertical',
    //             align: 'right',
    //             verticalAlign: 'middle',
    //             borderWidth: 0
    //         },
    //         series: [{
    //             name: 'Tokyo',
    //             data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
    //         }, {
    //             name: 'New York',
    //             data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
    //         }, {
    //             name: 'Berlin',
    //             data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
    //         }, {
    //             name: 'London',
    //             data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
    //         }]
    // });


    // $.getJSON('http://www.highcharts.com/samples/data/jsonp.php?filename=aapl-c.json&callback=?', function(data) {
    //     // Create the chart
    //     $('#container2').highcharts('StockChart', {
    //         rangeSelector : {
    //     	selected : 1
    //         },

    //         title : {
    //     	text : 'AAPL Stock Price'
    //         },

    //         series : [{
    //     	name : 'AAPL',
    //     	data : data,
    //     	tooltip: {
    //     	    valueDecimals: 2
    //     	}
    //         }]
    //     });
    // });




    Highcharts.setOptions({
	global : {
	    useUTC : false
	}
    });

    // Create the chart
    $('#container1').highcharts('StockChart', {
	chart : {
	    events : {
		load : function() {

		    // set up the updating of the chart each second
		    var series = this.series[0];
		    setInterval(function() {
			var x = (new Date()).getTime(), // current time
			y = Math.round(Math.random() * 100);
			series.addPoint([x, y], true, true);
		    }, 1000);
		}
	    }
	},

	rangeSelector: {
	    buttons: [{
		count: 1,
		type: 'minute',
		text: '1M'
	    }, {
		count: 5,
		type: 'minute',
		text: '5M'
	    }, {
		type: 'all',
		text: 'All'
	    }],
	    inputEnabled: false,
	    selected: 0
	},

	title : {
	    text : 'Live random data'
	},

	exporting: {
	    enabled: false
	},

	series : [{
	    name : 'Random data',
	    data : (function() {
		// generate an array of random data
		var data = [], time = (new Date()).getTime(), i;

		for( i = -999; i <= 0; i++) {
		    data.push([
			time + i * 1000,
			Math.round(Math.random() * 100)
		    ]);
		}
		return data;
	    })()
	}]
    });

    // _$MADS({
    //     appsid: 'debug',                    // 必选，应用id// 必选，应用id
    //     appsec: 'debug',                    // 必选，计费名// 必选，计费名
    //     domain: 'localhost',            // 显示广告的域名
    //     referer: 'http://localhost:8080/data-cruncher/all', // 显示广告的页面地址
    //     keywords: ['关键词1', '关键词2'],   // 关键词列表
    //     ads: [{
    //         container: 'ad1', // 广告容器的id
    //         apid: 'apid1',                  // 广告位id，用于标记
    //         style: 'textlink',              // 广告类型，暂时只有textlink
    //         count: 1,                       // 最大展示广告数量
    //         /**
    //          * 自定义广告生成接口
    //          * @param {Object} ad 广告对象，包含广告的所有信息
    //          * @param {number} i 当前是第几个广告
    //          * @param {number} n 中共有几个广告
    //          * @return {string} 返回生成的自定义html
    //          */
    //         builder: function(ad, i, n) {
    //             var fz = document.getElementById('ad1').offsetWidth / 17.78;
    //             return ''
    //                 + '<span style="white-space:nowrap;font-family:微软雅黑;font-size:' + fz + 'px;">'
    //                 +   '<span style="font-weight:bold;color:#003399;line-height:1.2em;">'
    //                 +     ad.tit.replace(/[\{\}]/g, '')
    //                 +   '</span>'
    //                 +   '<br>'
    //                 +   '<span style="color:#999;font-size:63%;line-height:1.2em;">'
    //                 +     ad.desc.replace(/[\{\}]/g, '')
    //                 +   '</span>'
    //                 + '</span>'
    //         },
    //         at: ['text']                    // 请求的广告类型，文字(text)，或图片(image)
    //     }],
    //     /**
    //      * 广告展示成功监听器
    //      * @param {Object} evt 事件对象
    //      */
    //     onAdShow: function(evt) {
    //         console.log('onAdShow global');
    //     },
    //     /**
    //      * 广告被点击监听器
    //      * @param {Object} evt 事件对象
    //      */
    //     onAdClick: function(evt) {
    //         console.log('onAdClick global');
    //     },
    //     /**
    //      * 广告展示失败监听器
    //      * @param {Object} evt 事件对象
    //      */
    //     onAdFailed: function(evt){
    //         console.log('onAdFailed global');
    //     }
    // });
    // 189 8188 0902
    // _$BDS({
    //     cid:'ad2',   // [必选]，容器ID
    //     appsid:'debug',      // [必选]，应用id
    //     appsec:'debug',      // [必选]，计费名
    //     ap: false,           // 广告不轮播
    //     w: 480,              // 容器宽度，设置<meta name="viewport" />后，大部分浏览器具有320px宽度的窗口
    //     h: 64,               // 容器高度，参考值：h = w * 0.15
    //     PS: {
    //         KEY: ['测试']    // 关键词列表
    //     },
    //     listener: {}         // 事件监听
    // });
});
