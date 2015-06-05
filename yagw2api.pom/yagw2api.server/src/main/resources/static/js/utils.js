URLRequest = function(type, url, params, oncomplete) {

	if (window.XMLHttpRequest) {
		cR = new XMLHttpRequest();  
	}else if (window.ActiveXObject) { 
		try {  
			cR = new ActiveXObject("Msxml2.XMLHTTP");  
		}catch (e) {  
			try {  
			  cR = new ActiveXObject("Microsoft.XMLHTTP");  
			}catch (e) { 
				return; 
			}  
		}  
	}
	
	cR.onreadystatechange = function() {
    	if (self.cR.readyState === 4 && self.cR.status === 200){
    		oncomplete(cR.responseText);
    	}
	};
                
   self.cR.open(type, url);  
   self.cR.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');  
   self.cR.send(params); 

}


//Define the overlay, derived from google.maps.OverlayView
function AnyOverlay(options) {
console.log(options);
 // Initialization
 this.setValues(options);

	if(options.type == 'label'){
		var span = this.span_ = document.createElement('span');
		span.style.cssText = 'position: relative; left: -50%; top: -8px; ' +
			      'white-space: nowrap; border: 1px solid blue; ' +
			      'padding: 2px; background-color: white';

		var div = this.div_ = document.createElement('div');
		div.appendChild(span);
		div.style.cssText = 'position: absolute; display: none';
	}
};
AnyOverlay.prototype = new google.maps.OverlayView;

// Implement onAdd
AnyOverlay.prototype.onAdd = function() {
 var pane = this.getPanes().overlayLayer;
 pane.appendChild(this.div_);

 // Ensures the label is redrawn if the text or position is changed.
 var me = this;
 this.listeners_ = [
   google.maps.event.addListener(this, 'position_changed',
       function() { me.draw(); }),
   google.maps.event.addListener(this, 'text_changed',
       function() { me.draw(); })
 ];
};

// Implement onRemove
AnyOverlay.prototype.onRemove = function() {
 this.div_.parentNode.removeChild(this.div_);

 // AnyOverlay is removed from the map, stop updating its position/text.
 for (var i = 0, I = this.listeners_.length; i < I; ++i) {
   google.maps.event.removeListener(this.listeners_[i]);
 }
};

// Implement draw
AnyOverlay.prototype.draw = function() {
 var projection = this.getProjection();
 var position = projection.fromLatLngToDivPixel(this.get('position'));

 var div = this.div_;
 div.style.left = position.x + 'px';
 div.style.top = position.y + 'px';
 div.style.display = 'block';

 this.span_.innerHTML = this.get('text').toString();
};