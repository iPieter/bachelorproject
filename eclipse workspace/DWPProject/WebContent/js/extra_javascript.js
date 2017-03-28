var SENSOR_DATA = null;
var map = null;
var marker = null;
var latlngs = null;
var index = null;
var xTimeVal = null;
var selectedIndex = null;
var datapointSelected= false;
var id = $("#current_traincoach").val();

/* Map Setup */
$.get( "rest/processed_data/" + id, function( data )
{
	SENSOR_DATA = data;
	setView( "radio_yaw" );
	
	var radiobtn = document.getElementById("radio_yaw");
	console.log( radiobtn );
	radiobtn.checked = true;
	
	$( "#max" ).html( "<b>Max yaw: </b>" + data.max_yaw.toFixed(3) );
	$( "#min" ).html( "<b>Min yaw: </b>" + data.min_yaw.toFixed(3) );

    map = L.map( "map" ).setView( [51.0499582, 3.7270672], 10 );
	L.tileLayer( 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}',
	{
		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
		maxZoom: 18,
		id: 'mapbox.streets',
		accessToken: 'pk.eyJ1IjoiYW50b25kIiwiYSI6ImNpbXRkM2wwNDAwNmd2d20xNDJnN3RwYjMifQ.PtxXr8pyGM4qccCXDecL2A'
	} ).addTo( map );

	latlngs = [];
	for( var i = 0; i < data.lat.length; i++ )
		latlngs.push( [ (data.lat[i] + data.lat_off * 0 ) * 180.0 / Math.PI , (data.lng[i]  + data.lng_off * 0)  * 180.0 / Math.PI ] );

	var polyline = L.polyline(latlngs, {color: '#b0cb1b', smoothFactor:0.10}).addTo(map);
    map.fitBounds(polyline.getBounds());
	
}).fail( function( error )
{
	console.log( "Failed to fetch data: " + error );
	$( "#topleft" ).html( "<b>ERROR: Failed to load data</b>");
});

/* HighCharts Setup */
function setView( input )
{
	var data = null;
	var title = "";
	if( input == "radio_roll" )
	{
		title = "roll";
		data = SENSOR_DATA.roll;
		$( "#max" ).html( "<b>Max roll: </b>" + SENSOR_DATA.max_roll.toFixed(3) );
		$( "#min" ).html( "<b>Min roll: </b>" + SENSOR_DATA.min_roll.toFixed(3) );
	}
	else
	{
		data = SENSOR_DATA.yaw;
		title = "yaw";
		$( "#max" ).html( "<b>Max yaw: </b>" + SENSOR_DATA.max_yaw.toFixed(3) );
		$( "#min" ).html( "<b>Min yaw: </b>" + SENSOR_DATA.min_yaw.toFixed(3) );
	}	
	Highcharts.chart('topleft', 
	{
        chart: {
            zoomType: 'x',
        },
        title: {
            text: 'Gyroscoop: ' + title
        },
        subtitle: {
            text: document.ontouchstart === undefined ?
                    'Click and drag in the plot area to zoom in' : 'Pinch the chart to zoom in'
        },
        xAxis: {
            title:
			{
				text: 'Tijd(ms)'
			}
        },
        yAxis: {
            title: {
                text: 'Afwijking(°)'
            }
        },
        legend: {
            enabled: false
        },
        plotOptions: {
            series: {
                cursor: 'pointer',
                point: {
                    events: {
                        mouseOver: function (e) {
	                       if( marker != null )
	                    	   map.removeLayer( marker );
	                       
	                       index = Math.round( (this.x / this.series.data.length) * latlngs.length );
	                       marker = L.circle( latlngs[ index ], 
	                       { color: 'red',
                	    	 fillColor: '#f03',
                	    	 fillOpacity: 0.5,
                	    	 radius: 5
                		   } );
	                       marker.addTo( map );
	                       $( "#current_speed" ).html( "<b>Snelheid: </b>" + SENSOR_DATA.speed[ index ].toFixed(3) );
	                       $( "#current_accel" ).html( "<b>Versnelling: </b>" + SENSOR_DATA.accel[ index ].toFixed(3) );
                        },
			    		click: function(e){
							xTimeVal=e.point.x;
							selectedIndex=index;
							setDatapointSelected();
							console.log("The graph value onClick:"+ xTimeVal + ", latlon: "+latlngs[selectedIndex]);
						}
                    }
                },
                marker: {
                    lineWidth: 1
                }
            }
        },
        series: [{
            name: 'Afwijking(°)',
			data: data
        }]
    });
}

/* Which data to display on Highcharts */
$('input:radio').on('change', function()
{
	setView( $(this).context.id );
});

/*Set true if GPS time value of Issue is selected*/
function setDatapointSelected(){
	datapointSelected=true;
	$('#isDatapointSelected').val("true");
	console.log("datapointSelected="+datapointSelected);
}

/*Modal depends on Issue value selected*/
function setModal(){
	console.log("Entered setModal");
	
	if(datapointSelected == true){
		/*Value to display in modal */
		$('#modal-form\\:selected_time_value').val(xTimeVal + " ms");
		$('#modal-form\\:selected_lat_value').val(latlngs[selectedIndex][0].toFixed(4));
		$('#modal-form\\:selected_lon_value').val(latlngs[selectedIndex][1].toFixed(4));
		
		/*Selecting Modal*/
		$('#create_issue_button').attr("data-target","#assignModal");
		console.log("Entered setModalHTML: if(true)");
	}
	else{
		/*Selecting Modal*/
		$('#create_issue_button').attr("data-target","#pickAPointModal");
		console.log("Entered setModalHTML: if(false)");
	}
}

