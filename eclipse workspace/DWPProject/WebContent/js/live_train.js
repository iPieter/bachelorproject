
Highcharts.setOptions({
    global: {
        useUTC: false
    }
});

var map = L.map( "map" ).setView( [51.0499582, 3.7270672], 10 );
L.tileLayer( 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}',
{
    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox.streets',
    accessToken: 'pk.eyJ1IjoiYW50b25kIiwiYSI6ImNpbXRkM2wwNDAwNmd2d20xNDJnN3RwYjMifQ.PtxXr8pyGM4qccCXDecL2A'
} ).addTo( map );

var train_path = [];
var pathPolyline = null;

var lastDate = null;
var initialLoad = true;

// Create the chart
Highcharts.stockChart('topleft', {
    chart: {
        events: {
            load: function () 
            {
            	console.log( "loading..." );
            	var series = this.series[0];
                var corePath = "http://localhost:8080/DWPProject-0.0.1-SNAPSHOT/rest/live_data/get/280/";
            	
            	var path;
        		path = corePath + "2013-01-01_00-00-00";
        		lastDate = "2013-01-01_00-00-00";
   
                $.get( path, function(data)
                {
            		var initialData = [];
            		for( var i = 0; i < data.data.length; i++ )
            		{
            			initialData.push( data.data[i].yaw );
            			train_path.push( [ data.data[i].lat * 180.0 / Math.PI, data.data[i].lng * 180.0 / Math.PI ] );
            			lastDate = data.data[i].time;
            		}
            			
            		series.setData( initialData );
                	if( pathPolyline != null )
                        map.removeLayer( pathPolyline );
                    
                	if( train_path.length > 0 )
            		{
                        pathPolyline = L.polyline(train_path, {color: 'red'}).addTo(map);
                        map.fitBounds(pathPolyline.getBounds());
            		}
        		
                	setInterval(function () 
                    {
                		path = corePath + lastDate;
                		
                        $.get( path, function(data)
                        {
                        	console.log( data.data.length );
                    		for( var i = 0; i < data.data.length; i++ )
                            {
                            	var dataPoint = data.data[i];
                                series.addPoint( dataPoint.yaw );
    							
                                train_path.push( [ dataPoint.lat * 180.0 / Math.PI, dataPoint.lng * 180.0 / Math.PI ] );
                                lastDate = dataPoint.time;
                            }
                                
                        	if( pathPolyline != null )
                                map.removeLayer( pathPolyline );
                            
                        	if( train_path.length > 0 )
                    		{
                                pathPolyline = L.polyline(train_path, {color: 'red'}).addTo(map);
                                map.fitBounds(pathPolyline.getBounds());

                    		}
                        } );

                    }, 1000);
                });
            	
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

    title: {
        text: 'Yaw'
    },

    exporting: {
        enabled: false
    },

    series: [{
        name: 'Yaw',
        data: [ 0,0,0,0,0,0,0,0,0,0,0]
    }]
});

//var marker = L.marker([51.00, 3.73]).addTo(map);
//marker.bindPopup("<b>Trein 1: Sensor 1</b><br>Afwijkende waarde x-as").openPopup();

//var polyline = L.polyline(latlngs, {color: 'black', smoothFactor:10}).addTo(map);
//map.fitBounds(polyline.getBounds());
