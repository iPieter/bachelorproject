var SENSOR_DATA = null;
var map = null;
var marker = null;
var latlngs = null;

$.get( "rest/processed_data", function( data )
{
	SENSOR_DATA = data;
	setView( "radio_yaw" );
	
	var radiobtn = document.getElementById("radio_yaw");
	console.log( radiobtn );
	radiobtn.checked = true;

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

	var polyline = L.polyline(latlngs, {color: 'black', smoothFactor:10}).addTo(map);
    map.fitBounds(polyline.getBounds());
	
}).fail( function( error )
{
	console.log( "Failed to fetch data: " + error );
});

function setView( input )
{
	var data = null;
	var title = "";
	if( input == "radio_roll" )
	{
		title = "roll";
		data = SENSOR_DATA.roll;
	}
	else
	{
		data = SENSOR_DATA.yaw;
		title = "yaw";
	}	
	Highcharts.chart('topleft', 
	{
        chart: {
            zoomType: 'x'
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
	                       
	                       var index = Math.round( (this.x / this.series.data.length) * latlngs.length );
	                       marker = L.circle( latlngs[ index ], 
	                       { color: 'red',
                	    	 fillColor: '#f03',
                	    	 fillOpacity: 0.5,
                	    	 radius: 5
                		   } ).addTo(map);
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
				data:
        data
        }]
    });
}

$('input:radio').on('change', function()
{
	setView( $(this).context.id );
});






