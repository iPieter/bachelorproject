$(function () {
	
	$.get( "rest/processed_data", function( data )
	{
		console.log( data );
		
		Highcharts.chart('topleft', 
		{
            chart: {
                zoomType: 'x'
            },
            title: {
                text: 'Gyroscoop: yaw'
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
            series: [{
                name: 'Afwijking(°)',
					data:
            data.yaw
            }]
        });

	    var map = L.map( "map" ).setView( [51.0499582, 3.7270672], 10 );
		L.tileLayer( 'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}',
		{
			attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
			maxZoom: 18,
			id: 'mapbox.streets',
			accessToken: 'pk.eyJ1IjoiYW50b25kIiwiYSI6ImNpbXRkM2wwNDAwNmd2d20xNDJnN3RwYjMifQ.PtxXr8pyGM4qccCXDecL2A'
		} ).addTo( map );
	
		map.locate(
		{
			setView: true,
			maxZoom: 8,
			maximumAge: 10000,
			timeout: 20000,
			enableHighAccuracy: false
		} );
	
	    //var marker = L.marker([51.00, 3.73]).addTo(map);
	    //marker.bindPopup("<b>Trein 1: Sensor 1</b><br>Afwijkende waarde x-as").openPopup();
		var latlngs = [];
		for( var i = 0; i < data.lat.length; i++ )
			latlngs.push( [data.lat[i] + data.lat_off,data.lng[i]  + data.lng_off ] );

		var polyline = L.polyline(latlngs, {color: 'black', smoothFactor:10}).addTo(map);
	    map.fitBounds(polyline.getBounds());
		
		}).fail( function( error )
		{
			console.log( "Failed to fetch data: " + error );
		});
});
