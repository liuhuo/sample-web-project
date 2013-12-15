function getinfo() {
    $.ajax({
        url : "poll",
        success: function(data) {
            console.log(data);
        }
    });
}

$(document).ready(function() {
    //alert("jquery ready!!");

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

    $('#container').highcharts({
            title: {
                text: 'Monthly Average Temperature',
                x: -20 //center
            },
            subtitle: {
                text: 'Source: WorldClimate.com',
                x: -20
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            yAxis: {
                title: {
                    text: 'Temperature (°C)'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                valueSuffix: '°C'
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: [{
                name: 'Tokyo',
                data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            }, {
                name: 'New York',
                data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
            }, {
                name: 'Berlin',
                data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
            }, {
                name: 'London',
                data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
            }]
    });


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

    _$MADS({
        appsid: 'debug',                    // 必选，应用id// 必选，应用id
        appsec: 'debug',                    // 必选，计费名// 必选，计费名
        domain: 'localhost',            // 显示广告的域名
        referer: 'http://localhost:8080/data-cruncher/all', // 显示广告的页面地址
        keywords: ['关键词1', '关键词2'],   // 关键词列表
        ads: [{
            container: 'ad1', // 广告容器的id
            apid: 'apid1',                  // 广告位id，用于标记
            style: 'textlink',              // 广告类型，暂时只有textlink
            count: 1,                       // 最大展示广告数量
            /**
             * 自定义广告生成接口
             * @param {Object} ad 广告对象，包含广告的所有信息
             * @param {number} i 当前是第几个广告
             * @param {number} n 中共有几个广告
             * @return {string} 返回生成的自定义html
             */
            builder: function(ad, i, n) {
                var fz = document.getElementById('ad1').offsetWidth / 17.78;
                return ''
                    + '<span style="white-space:nowrap;font-family:微软雅黑;font-size:' + fz + 'px;">'
                    +   '<span style="font-weight:bold;color:#003399;line-height:1.2em;">'
                    +     ad.tit.replace(/[\{\}]/g, '')
                    +   '</span>'
                    +   '<br>'
                    +   '<span style="color:#999;font-size:63%;line-height:1.2em;">'
                    +     ad.desc.replace(/[\{\}]/g, '')
                    +   '</span>'
                    + '</span>'
            },
            at: ['text']                    // 请求的广告类型，文字(text)，或图片(image)
        }],
        /**
         * 广告展示成功监听器
         * @param {Object} evt 事件对象
         */
        onAdShow: function(evt) {
            console.log('onAdShow global');
        },
        /**
         * 广告被点击监听器
         * @param {Object} evt 事件对象
         */
        onAdClick: function(evt) {
            console.log('onAdClick global');
        },
        /**
         * 广告展示失败监听器
         * @param {Object} evt 事件对象
         */
        onAdFailed: function(evt){
            console.log('onAdFailed global');
        }
    });

    _$BDS({
        cid:'ad2',   // [必选]，容器ID
        appsid:'debug',      // [必选]，应用id
        appsec:'debug',      // [必选]，计费名
        ap: false,           // 广告不轮播
        w: 480,              // 容器宽度，设置<meta name="viewport" />后，大部分浏览器具有320px宽度的窗口
        h: 64,               // 容器高度，参考值：h = w * 0.15
        PS: {
            KEY: ['测试']    // 关键词列表
        },
        listener: {}         // 事件监听
    });
});
