var bbsUtil;
var homeUtil;

function BbsUtil(){
	this.registerClickEvent=function(item, view, afterClickColor){
		console.log("registerClickEvent"); //DEBUG
		console.log(item); //DEBUG
		
		
		item.click(function(){
			var bbsName=item.find("input.bbs_name").attr("value");
			var num=item.find("div.item_num").text();
			var title=item.find("div.item_title").text();
			
			item.click(function(){
				view.viewArticle(bbsName, num, title);
				item.css("background-color", afterClickColor);
				return false;
			});
			
			item.find("a.delete_item").click(function(){
				item.remove();
				var fixedContents=objectStorage.getDOM("fixedContents");
				if(fixedContents!=null){
					fixedContents.find("num_" + num).remove();
					objectStorage.setDOM("fixedContents", fixedContents);
				} //if
				return false;
			});
			
			item.find("a.fix_item").click(function(){
				var fixedContents=objectStorage.getDOM("fixedContents");
				if(fixedContents==null)
					fixedContents=$("<div />").attr("id", "fixed_contents");
				fixedContents.append(item);
				objectStorage.setDOM("fixedContents", fixedContents);
			
				view.showFixedItems();
			
				return false;
			});
		});
	} //registerClickEvent

	this.jsonToHtml=function(jsonStr){
		var jsonObj=JSON.parse(jsonStr);
		
		var numHtml=$("<small />").append(jsonObj.num);
		var titleHtml=$("<strong />").append(jsonObj.title);
		var dateHtml=$("<small />").append(jsonObj.date);
		var nickHtml;
		if(jsonObj.nick==null){
			nickHtml=$("<img />").attr("src", jsonObj.imgNickPath);
		} else{
			nickHtml=$("<div />").append(jsonObj.nick);
		} //if
		
		var deleteItemOptionHtml=$("<a>[-]</a>").attr("href", "#").addClass("delete_item");
		var fixItemOptionHtml=$("<a>[+]</a>").attr("href", "#").addClass("fix_item");
		
		var rowDiv=$("<div />").addClass("row");
		rowDiv.append($("<input />").attr("type", "hidden").addClass("bbs_name").attr("value", jsonObj.bbsName));
		rowDiv.append($("<div />").addClass("col-lg-1").addClass("item_num").append(numHtml));
		rowDiv.append($("<div />").addClass("col-lg-7").addClass("item_title").append(titleHtml));
		rowDiv.append($("<div />").addClass("col-lg-2").addClass("item_nick").append(nickHtml));
		rowDiv.append($("<div />").addClass("col-lg-1").addClass("item_date").append(dateHtml));
		rowDiv.append($("<div />").addClass("col-lg-1").addClass("item_option").append(deleteItemOptionHtml).append(fixItemOptionHtml));
		
		var retDiv=$("<div />").append(rowDiv);
		retDiv.css("display", "none").css("margin", "0").css("padding", "0");
		retDiv.addClass("bbsItem");
		retDiv.attr("id", "num_" + jsonObj.num);
		retDiv.append($("<hr>").css("margin", "0").css("padding", "0"));
		
		retDiv.hide();
		
		return retDiv;
	} //jsonToHtml
	
	this.htmlToJson=function(itemHtml){
		var jsonObj=new Object();
		
		jsonObj.num=itemHtml.find("div.item_num").find("small").text();
		jsonObj.title=itemHtml.find("div.item_title").find("strong").text();
		jsonObj.date=itemHtml.find("div.item_date").find("small").text();
		jsonObj.nick=null;
		jsonObj.imgNickPath=null;
		if(itemHtml.find("div.item_nick").find("img")==null){
			jsonObj.nick=itemHtml.find("div.item_nick").text();
		} else{
			jsonObj.imgNickPath=itemHtml.find("div.item_nick").find("img").attr("src");
		} //if
		
		return jsonObj;
	} //htmlToJson
} //function BbsUtil

function HomeUtil(){
	this.getObjectEmbedNode=function(url, width, height){
		var objectNode=$("<object />").attr("data", url).attr("width", width+150).attr("height", height+170);
		objectNode.css("position", "relative").css("left", -150).css("top", -170);
		var embedNode=$("<embed />").attr("data", url).attr("width", width).attr("height", height);
		objectNode.append(embedNode);
		return objectNode;
	} //getObjectEmbedNode
} //function CommonUtil

bbsUtil=new BbsUtil();
homeUtil=new HomeUtil();