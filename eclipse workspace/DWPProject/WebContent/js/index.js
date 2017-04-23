var map=null;
var operator_id =null;
var data_donut=null;

/* AT DOCUMENT STARTUP*/
$(document).ready(function() {
	
	//DonutGraphic setup
	$.ajax({
        url: "rest/statistics_data/" ,
        type: "GET",
        beforeSend: function(xhr){xhr.setRequestHeader('Authorization', 'Bearer ' + $("#navbar-form\\:token").val());},
        success: function( data )
		{ 
    		setDonutView(data);
		},
        fail: function( error )
		{
    		console.log( "Failed to fetch donut_data: " + error );
		},
     });
	
	
	//Heatmap setup
	$.ajax({
        url: "rest/heatmap_data",
        type: "GET",
        beforeSend: function(xhr){xhr.setRequestHeader('Authorization', 'Bearer ' + $("#navbar-form\\:token").val());},
        success: function( data )
		{ 
        	console.log(data);
			setHeatMapView( data );
		},
        fail: function( error )
		{
			console.log( "Failed to fetch heatmap_data: " + error );
		},
     });
	
});

/* DONUTVIEW HIGHCHARTS*/
function setDonutView(data)
{
	
	if(data["Gesloten"]==0 && data["In behandeling"]==0 && data["Toegewezen"]==0){
		var display_msg="<b>Welcome back!</b> Laatste 30 dagen nog geen activiteit geregistreerd, let's get started.";
		$("#display_when_empty_donut").html(display_msg);
	}
	else{
		//Hide error msg
		$("#display_when_empty_donut").hide();
		
		Highcharts.setOptions({
		    colors: ['#c0392b','#e67e22', '#27ae60'] // Toegewezen=Rood, In Behandeling=oranje, Closed=Groen
		});
		
		Highcharts.chart('donut_graph', {
		    chart: {
		        backgroundColor: "#f5f5f5",
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
		        data: 
		        	[ /*hack*/
		        		["Toegewezen problemen", data["Toegewezen"]],
		        		["Problemen in  behandeling", data["In behandeling"]],
		        		["Gesloten problemen", data["Gesloten"]],	    
		        	],
		        
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
	var intensity= 1;
	var polygon_data = [];
	for( var i = 0; i < data.gpsLat.length; i++ ){
		map_entries.push( [ data.gpsLat[i] , data.gpsLon[i], intensity] );
		polygon_data.push([ Number(data.gpsLat[i]) , Number(data.gpsLon[i]) ]);
	}
	
	// lat, lng, intensity
	var heat = L.heatLayer(
		map_entries
		, {radius: 25}).addTo(map);
	}
	
	//Fit Issues to screen
	var polygon = L.polygon( polygon_data );
	map.fitBounds( polygon.getBounds() );
}
