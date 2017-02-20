$(function () {
    Highcharts.chart('topleft', {
        title: {
            text: 'Sensor 1',
            x: -20 //center
        },
        subtitle: {
            text: 'Trilling z-axis',
            x: -20
        },
        xAxis: {
            categories: ['0:00', '0:01', '0:02', '0:03', '0:04', '0:05', '0:06', '0:07', '0:08']
        },
        yAxis: {
            title: {
                text: 'Afwijking'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: 'mm'
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: [{
            name: 'Gent-Kortrijk',
            data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
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
		maxZoom: 12,
		maximumAge: 10000,
		timeout: 20000,
		enableHighAccuracy: false
	} );
});
