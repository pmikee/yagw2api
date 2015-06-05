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