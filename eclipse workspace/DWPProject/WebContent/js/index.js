var operator_id =null;
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
	console.log(data.issue_counts[0][1]);
	if(data.issue_counts[0][1]==0 && data.issue_counts[1][1]==0 && data.issue_counts[2][1]==0){
		var display_msg="<b>Welcome back!</b> Laatste 30 dagen nog geen activiteit geregistreerd, let's get started.";
		$("#display_when_empty_donut").html(display_msg);
	}
	else{
		Highcharts.chart('donut_graph', {
		    chart: {
		        plotBackgroundColor: null,
		        plotBorderWidth: 0,
		        plotShadow: false
		    },
		    title: {
		        text: 'Activiteit<br>Afgelopen<br>30 Dagen',
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
}