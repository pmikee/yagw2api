(function() {
		"use strict";

		var map, southWest, northEast;

		function unproject(coord) {
		    return map.unproject(coord, map.getMaxZoom());
		}

		map = L.map("map", {
		  minZoom: 0,
		  maxZoom: 7,
		  crs: L.CRS.Simple
		}).setView([0, 0], 0);

		southWest = unproject([0, 32768]);
		northEast = unproject([32768, 0]);

		map.setMaxBounds(new L.LatLngBounds(southWest, northEast));
		  L.tileLayer(window.location.origin+"/map/tile/1/1/{z}/{x}/{y}.jpg", {
		  minZoom: 0,
		  maxZoom: 7,
		  continuousWorld: true,
		  subdomains: [1, 2, 3, 4 ]
		}).addTo(map);

		}());