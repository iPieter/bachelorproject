$(function ()
{
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

    var marker = L.marker([51.00, 3.73]).addTo(map);
    marker.bindPopup("<b>Trein 1: Sensor 1</b><br>Afwijkende waarde x-as").openPopup();

    var marker2 = L.marker([51.00, 3.75]).addTo(map);
    marker2.bindPopup("<b>Trein 2: Sensor 10</b><br>Afwijkende waarde x-as").openPopup();

    var marker3 = L.marker([51.00, 3.78]).addTo(map);
    marker3.bindPopup("<b>Trein 3: Sensoren 1 en 3</b><br>Afwijkende waarde x-as").openPopup();

    var marker4 = L.marker([51.10, 3.78]).addTo(map);
    marker4.bindPopup("<b>Trein 4: Sensor 5</b><br>Afwijkende waarde x-as").openPopup();
});
