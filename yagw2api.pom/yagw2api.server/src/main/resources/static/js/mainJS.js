var guildWarsTypeOptions = {
	getTileUrl: function(coord, zoom) {
		return window.location.origin+'/map/tile/1/1/' + '/' + zoom + '/' + coord.x + '/' + coord.y + '.jpg';
	},
	tileSize: new google.maps.Size(256, 256),
	maxZoom: 9,
	minZoom: 0,
	radius: 1738000,
	name: 'GuildWars'
};

var guildWarsMapType = new google.maps.ImageMapType(guildWarsTypeOptions);

var initialize = function() {
	var myLatlng = new google.maps.LatLng(0, 0);
	var mapOptions = {
		center: myLatlng,
		zoom: 0,
		streetViewControl: false,
	    panControl: true,
	    zoomControl: true,
	    scaleControl: true,
		mapTypeControlOptions: {
			mapTypeIds: ['guildWars']
		}
	};

	var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
	map.mapTypes.set('guildWars', guildWarsMapType);
	map.setMapTypeId('guildWars');
	addWaypoints(map);
	addLabels(map);
}

var addWaypoints = function(map) {
    var points = [new google.maps.LatLng(10.281819, 10.287132),
                  new google.maps.LatLng(12.281839, 12.287152)];

    var imageBounds = new google.maps.LatLngBounds(points[0], points[1]);

    var waypoint = new google.maps.GroundOverlay('https://render.guildwars2.com/file/32633AF8ADEA696A1EF56D3AE32D617B10D3AC57/157353.png', imageBounds);
    waypoint.setMap(map);
}

var addLabels = function(map){
	var url = window.location.origin+'/map/1/maps';

	URLRequest(
			'get',
			url,
			'',
			function(text){
				var response = text || "no response text";
				var mapInfoObj = JSON.parse(response);
	
				for (m in mapInfoObj) {
					var myTitle = document.createElement('span');
					myTitle.style.color = 'white';
					myTitle.innerHTML = 'Hello World';
					
				    var points = [new google.maps.LatLng(10.281819, 10.287132),
				                  new google.maps.LatLng(12.281839, 12.287152)];
					var textBounds = new google.maps.LatLngBounds(points[0], points[1]);
					var x = new google.maps.GroundOverlay(myTitle, textBounds);
				    x.setMap(map);
				}

			});
}




google.maps.event.addDomListener(window, 'load', initialize);