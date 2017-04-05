var map_modal = L.map("map_modal").setView([ 51.0499582, 3.7270672 ], 10);
L.tileLayer(
				'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}',
				{
					attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
					maxZoom : 18,
					id : 'mapbox.streets',
					accessToken : 'pk.eyJ1IjoiYW50b25kIiwiYSI6ImNpbXRkM2wwNDAwNmd2d20xNDJnN3RwYjMifQ.PtxXr8pyGM4qccCXDecL2A'
				}).addTo(map_modal);

var map_big = L.map("map_big").setView([ 51.0499582, 3.7270672 ], 10);
L
		.tileLayer(
				'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}',
				{
					attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
					maxZoom : 18,
					id : 'mapbox.streets',
					accessToken : 'pk.eyJ1IjoiYW50b25kIiwiYSI6ImNpbXRkM2wwNDAwNmd2d20xNDJnN3RwYjMifQ.PtxXr8pyGM4qccCXDecL2A'
				}).addTo(map_big);

var constraintPolygons =
{};

$("input").each(function()
{
	if (this.id.indexOf("constraint") >= 0)
	{
		var id = this.id.substr(17, this.id.length);
		var string = this.value.substr(1, this.value.length - 2);
		var split = string.split(",");
		var polygon = [];
		for (var i = 0; i < split.length - 1; i += 2)
		{
			var point = [ Number(split[i]), Number(split[i + 1]) ];
			polygon.push(point);
		}
		console.log(polygon);
		constraintPolygons[id] = polygon;
		var polygonLayer = L.polygon(polygon);
		polygonLayer.addTo(map_big);
	}
});

map_big.on("click", function(ev)
{
	for ( var key in constraintPolygons)
	{
		var polygon = constraintPolygons[key];
		if (isInPolygon(polygon, ev.latlng.lat, ev.latlng.lng))
		{
			setCurrentConstraint(key);
			break;
		}
	}
});

$(".constraint").on("click", function()
{
	var id = this.id.substr(11, this.id.length);
	setCurrentConstraint(id);
});

var polygonData = [ [ 51.05, 3.72 ], [ 51.06, 3.73 ], [ 51.06, 3.72 ] ];
var polygon = L.polygon(polygonData);

map_modal.zoomControl.setPosition('bottomleft');

polygon.addTo(map_modal);

map_modal.fitBounds(polygon.getBounds());

map_modal.on("click", function(ev)
{
	var type = document.getElementById("checkbox");
	if (type.checked)
	{
		if (polygonData !== [])
			map_modal.removeLayer(polygon);

		polygonData.push([ ev.latlng.lat, ev.latlng.lng ]);
		polygon = L.polygon(polygonData);
		polygon.addTo(map_modal);
		$("#location-data input").val(polygonData);

	} else
	{
		var status = document.getElementById("click_status");

		if (isInPolygon(polygonData, ev.latlng.lat, ev.latlng.lng))
			status.innerHTML = "IN";
		else
			status.innerHTML = "OUT";
	}
});

/**
 * Hack to make ajax redraw working ...
 * 
 */
function redrawModalMap(data)
{
	var status = data.status; // Can be "begin", "complete" or "success".
	var source = data.source; // The parent HTML DOM element.

	switch (status)
	{
	case "begin": // Before the ajax request is sent.
		// ...
		break;

	case "complete": // After the ajax response is arrived.
		// ...
		break;

	case "success": // After update of HTML DOM based on ajax response.

		if ($("#map_modal").length > 0)
		{
			map_modal = L.map("map_modal").setView([ 51.0499582, 3.7270672 ],
					10);
			L.tileLayer(
							'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}',
							{
								attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
								maxZoom : 18,
								id : 'mapbox.streets',
								accessToken : 'pk.eyJ1IjoiYW50b25kIiwiYSI6ImNpbXRkM2wwNDAwNmd2d20xNDJnN3RwYjMifQ.PtxXr8pyGM4qccCXDecL2A'
							}).addTo(map_modal);

			map_modal.zoomControl.setPosition('bottomleft');

			polygon.addTo(map_modal);

			map_modal.fitBounds(polygon.getBounds());

			map_modal.on("click", function(ev)
			{
				var type = document.getElementById("checkbox");
				if (type.checked)
				{
					if (polygonData !== [])
						map_modal.removeLayer(polygon);

					polygonData.push([ ev.latlng.lat, ev.latlng.lng ]);
					polygon = L.polygon(polygonData);
					polygon.addTo(map_modal);
					$("#location-data input").val(polygonData);
				} else
				{
					var status = document.getElementById("click_status");

					if (isInPolygon(polygonData, ev.latlng.lat, ev.latlng.lng))
						status.innerHTML = "IN";
					else
						status.innerHTML = "OUT";
				}
			});
		}
		break;

	}
}

function clearPolygon()
{
	map_modal.removeLayer(polygon);
	polygonData = [];
}

function clearLast()
{
	if (polygonData.length > 0)
	{
		polygonData.splice(polygonData.length - 1, 1);
		map_modal.removeLayer(polygon);

		polygon = L.polygon(polygonData);
		polygon.addTo(map_modal);
	}
}

function openModal()
{
	$("#new_constraint_modal").modal();

	setTimeout(function()
	{
		map_modal.invalidateSize();
		map_modal.fitBounds(polygon.getBounds());
	}, 500);
}

function setCurrentConstraint(id)
{
	$("#current_constraint").html($("#constraint_" + id).clone());

	console.log($("#constraint_" + id));
}

function isInPolygon(polygon, lat, lng)
{
	polygon.push(polygon[0]);
	var wn = 0;
	for (var i = 0; i < polygon.length - 1; i++)
	{
		if (polygon[i][1] <= lng)
		{
			if (polygon[i + 1][1] > lng)
			{
				if (isLeft(polygon[i], polygon[i + 1], lat, lng) > 0)
					wn++;
			}
		} else
		{
			if (polygon[i + 1][1] <= lng)
			{
				if (isLeft(polygon[i], polygon[i + 1], lat, lng) < 0)
					wn--;
			}
		}
	}
	polygon.pop();

	return wn !== 0;
}

function isLeft(p0, p1, p2x, p2y)
{
	return (p1[0] - p0[0]) * (p2y - p0[1]) - (p2x - p0[0]) * (p1[1] - p0[1]);
}