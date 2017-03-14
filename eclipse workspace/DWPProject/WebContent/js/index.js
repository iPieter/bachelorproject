
var operator_id =null;
var data_history = $("#operator_statistics_history").val();
var data_donut=null;

$(document).ready(function() {
	
	operator_id = $("#current_operator_id").val();
	
	$.get( "rest/statistics_data/" + operator_id, function( data )
	{ 
		setDonutView(data);
	}).fail( function( error )
	{
		console.log( "Failed to fetch donut_data: " + error );
	});
	
});

function setDonutView(data)
{
	
	console.log(data);
	
	Highcharts.chart('donut_graph', {
	    chart: {
	        plotBackgroundColor: null,
	        plotBorderWidth: 0,
	        plotShadow: false
	    },
	    title: {
	        text: 'Problemen<br>Deze<br>Maand',
	        align: 'center',
	        verticalAlign: 'middle',
	        y: 40
	    },
	    tooltip: {
	        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	    },
	    plotOptions: {
	        pie: {
	            dataLabels: {
	                enabled: false,
	                distance: -50,
	                style: {
	                    fontWeight: 'bold',
	                    color: 'white'
	                }
	            },
	            startAngle: -95,
	            endAngle: 95,
	            center: ['50%', '75%']
	        }
	    },
	    series: [{
	        type: 'pie',
	        name: 'Percentage',
	        innerSize: '60%',
	        data: data.issue_counts
	    }]
	});
}