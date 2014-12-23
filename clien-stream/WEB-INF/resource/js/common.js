var objectStorage;

function ObjectStorage(){
	this.setJson=function(key, obj){
		if(obj!=null)
			localStorage[key]=JSON.stringify(obj);
	} //setObj
	
	this.getJson=function(key){
		if(key==null) return null;
		var jsonStr=localStorage[key];
		if(jsonStr==null) return null;
		return JSON.parse(localStorage[key]);
	} //getObj
	
	this.setDOM=function(key, DOM){
		if(DOM==null)
			return;
		var wrapper=$("<div />");
		wrapper.append(DOM);
		localStorage[key]=wrapper.html();
	} //setDOM
	
	this.getDOM=function(key){
		var strDOM=localStorage[key];
		if(strDOM==null)
			return null;
		var dom=$(strDOM);
		return dom;
	} //getDOM
} //function ObjectStorage

objectStorage=new ObjectStorage();