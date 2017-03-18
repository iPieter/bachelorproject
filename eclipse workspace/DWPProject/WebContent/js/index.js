var map=null;
var operator_id =null;
var data_donut=null;

/* AT DOCUMENT STARTUP*/
$(document).ready(function() {
	
	//DonutGraphic setup
	$.get( "rest/statistics_data", function( data )
	{ 
		setDonutView(data);
	}).fail( function( error )
	{
		console.log( "Failed to fetch donut_data: " + error );
	});
	
	//Heatmap setup
	$.get( "rest/heatmap_data", function( data )
			{ 
				console.log(data);
				setHeatMapView( data );
			}).fail( function( error )
			{
				console.log( "Failed to fetch heatmap_data: " + error );
			});	
});

/* DONUTVIEW HIGHCHARTS*/
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

/* HEATMAP*/
function setHeatMapView( data ){
	map = L.map( "map" ).setView( [51.0499582, 3.7270672], 10 );
	L.tileLayer( 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}',
	{
		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
		maxZoom: 18,
		id: 'mapbox.streets',
		accessToken: 'pk.eyJ1IjoiYW50b25kIiwiYSI6ImNpbXRkM2wwNDAwNmd2d20xNDJnN3RwYjMifQ.PtxXr8pyGM4qccCXDecL2A'
	} ).addTo( map );
	
	if(data!=null)
	{
	map_entries = [];
	var intensity= 0.2;
	for( var i = 0; i < data.lat.length; i++ )
		map_entries.push( [ data.gpsLat[i] , data.gpsLon[i], intensity] );
	
	// lat, lng, intensity
	var heat = L.heatLayer(
		map_entries
		, {radius: 25}).addTo(map);
	}
}