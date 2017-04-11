
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
    accessToken: 'pk.eyJ1IjoiYW50b25kIiwiYSI6ImNpbXRkM2wwNDAwNmd2d20xNDJnN3RwYjMifQ.PtxXr8pyGM4qccCXDecL2A',
    ZIndex: 10
} ).addTo( map );

var train_path = [];
var pathPolyline = null;

var lastDate = null;
var initialLoad = true;

var yawSeries = null;
var rollSeries = null;

// Create the chart
Highcharts.chart('realtime_chart_yaw', {
    chart: {
        events: {
            load: function () 
            {
            	yawSeries = this.series[0];
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

Highcharts.chart('realtime_chart_roll', {
    chart: {
        events: {
            load: function () 
            {
            	rollSeries = this.series[0];
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
        text: 'Roll'
    },

    exporting: {
        enabled: false
    },

    series: [{
        name: 'Roll',
        data: [ 0,0,0,0,0,0,0,0,0,0,0]
    }]
});

loadData();

function loadData()
{
	console.log( "loading..." );
	var id = $( "#sensor_id" ).val();
    var corePath = "rest/live_data/get/" + id + "/";
	
	var path;
	path = corePath + "2013-01-01_00-00-00";
	lastDate = "2013-01-01_00-00-00";


	map.zoomControl.setPosition('bottomleft');
	
	$( "#loading_modal" ).modal({backdrop: 'static', keyboard: false, show:true});
	
    $.get( path, function(data)
    {
    	
		var initialDataYaw = [];
		var initialDataRoll = [];
		
		for( var i = 0; i < data.data.length; i++ )
		{
			var dateTime = data.data[i].time;
			
			var split = dateTime.split("_");
        	var ymd = split[0].split("-");
        	var hms = split[1].split("-");
        	var date = new Date( ymd[0], ymd[1] - 1, ymd[2], hms[0], hms[1], hms[2] );
			
        	initialDataYaw.push( [date, data.data[i].yaw] );
        	initialDataRoll.push( [date, data.data[i].roll] );
        	
			train_path.push( [ data.data[i].lat * 180.0 / Math.PI, data.data[i].lng * 180.0 / Math.PI ] );
			lastDate = data.data[i].time;
			
			$( "#current_speed" ).html( data.data[i].speed.toFixed(4) + " m/s" );
            $( "#current_accel" ).html( data.data[i].accel.toFixed(4) + " m/s²" );
		}
			
		yawSeries.setData( initialDataYaw );
		rollSeries.setData( initialDataRoll );
		
    	if( pathPolyline != null )
            map.removeLayer( pathPolyline );
        
    	if( train_path.length > 0 )
		{
            pathPolyline = L.polyline(train_path, {color: '#b0cb1b'}).addTo(map);
            map.fitBounds(pathPolyline.getBounds(), 
            		{
        		paddingTopLeft: [ $("#left-panel").width(), $("#left-panel").height() ],
        		paddingBottomRight: [ $("#right-panel").width(), $("#right-panel").width() ]

    		});
		}
    	
    	$( "#loading_modal" ).modal("hide");
	
    	setInterval(function () 
        {
    		path = corePath + lastDate;
    		
            $.get( path, function(data)
            {
            	console.log( data.data.length );
        		for( var i = 0; i < data.data.length; i++ )
                {
                	var dataPoint = data.data[i];
                	lastDate = dataPoint.time;
                	
                	var split = lastDate.split("_");
                	var ymd = split[0].split("-");
                	var hms = split[1].split("-");
                	var date = new Date( ymd[0], ymd[1] - 1, ymd[2], hms[0], hms[1], hms[2] );

                	yawSeries.addPoint( [ date, dataPoint.yaw ] );
                	rollSeries.addPoint( [ date, dataPoint.roll ] );
					
                    train_path.push( [ dataPoint.lat * 180.0 / Math.PI, dataPoint.lng * 180.0 / Math.PI ] );
                    
                    $( "#current_speed" ).html( dataPoint.speed.toFixed(4) + " m/s" );
                    $( "#current_accel" ).html( dataPoint.accel.toFixed(4) + " m/s²" );
                }
                    
            	if( pathPolyline != null )
                    map.removeLayer( pathPolyline );
                
            	if( train_path.length > 0 )
        		{
                    pathPolyline = L.polyline(train_path, {color: '#b0cb1b'}).addTo(map);
                    map.fitBounds(pathPolyline.getBounds(), 
                    	{
                    		paddingTopLeft: [ $("#left-panel").width(), $("#left-panel").height() ],
                    		paddingBottomRight: [ $("#right-panel").width(), $("#right-panel").width() ]

                		}); 


        		}
            } );

        }, 1000);
    });
}