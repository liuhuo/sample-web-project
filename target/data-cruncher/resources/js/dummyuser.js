function getinfo() {
    $.ajax({
        url : "poll",
        success: function(data) {
            console.log(data);
        }
    });
}

$(document).ready(function() {
    alert("jquery ready!!");

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


    $.getJSON('http://www.highcharts.com/samples/data/jsonp.php?filename=aapl-c.json&callback=?', function(data) {
	// Create the chart
	$('#container2').highcharts('StockChart', {
	    rangeSelector : {
		selected : 1
	    },

	    title : {
		text : 'AAPL Stock Price'
	    },

	    series : [{
		name : 'AAPL',
		data : data,
		tooltip: {
		    valueDecimals: 2
		}
	    }]
	});
    });




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

});
