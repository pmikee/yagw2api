var guildWarsTypeOptions = {
	getTileUrl: function(coord, zoom) {
		var bound = Math.pow(2, zoom);
      
		return window.location.origin+'/map/tile/1/1/' + '/' + zoom + '/' + coord.x + '/' + coord.y + '.jpg';
	},
	tileSize: new google.maps.Size(256, 256),
	maxZoom: 9,
	minZoom: 0,
	radius: 1738000,
	name: 'GuildWars'
};

var guildWarsMapType = new google.maps.ImageMapType(guildWarsTypeOptions);

function initialize() {
	var myLatlng = new google.maps.LatLng(0, 0);
	var mapOptions = {
		center: myLatlng,
		zoom: 0,
		streetViewControl: false,
		mapTypeControlOptions: {
			mapTypeIds: ['guildWars']
		}
	};

	var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
	map.mapTypes.set('guildWars', guildWarsMapType);
	map.setMapTypeId('guildWars');
}

google.maps.event.addDomListener(window, 'load', initialize);