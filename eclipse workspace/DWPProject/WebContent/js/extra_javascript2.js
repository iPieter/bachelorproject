
/* AT DOCUMENT STARTUP*/
$(document).ready(function() {
	//Map setup
	var id = $("#current_workplace").val();
	console.log( "CURRENT USER ID: " + id );
	$.get( "rest/workplace/" + id + "/map/", function( data )
			{ 
				console.log(data);
				setWorkplaceMapView( data );
			}).fail( function( error )
			{
				console.log( "Failed to fetch workplacemap_data: " + error );
			});	
});

function setWorkplaceMapView( data )
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
	
	//Marker Layout
	var icon = L.icon({
	    iconUrl: 'img/marker-icon.png',
	    shadowUrl: 'img/marker-shadow.png',
	    popupAnchor:  [16, 0] // point from which the popup should open relative to the iconAnchor
	});
	
	//Adding Markers to Map
	var marker=null;
	var polygon_data = [];
	for(var i=0; i< data.gpsLat.length;i++){
		marker=L.marker([ data.gpsLat[i], data.gpsLon[i]],{icon: icon}).addTo(map);
		marker.bindPopup("<b>"+data.traincoach[i]+ "</b></br>"+ data.descr[i]);
		polygon_data.push([ Number(data.gpsLat[i]) , Number(data.gpsLon[i]) ]);
	}
	
	//Fit to bounds of screen
	var polygon = L.polygon( polygon_data );
	map.fitBounds( polygon.getBounds() );
};
