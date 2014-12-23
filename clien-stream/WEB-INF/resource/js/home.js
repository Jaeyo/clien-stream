var bbsParser;
var homeUtil;

function BbsParser(){
	this.parseBbsItem=function(itemStr, view){
		var jsonItem=JSON.parse(itemStr);
		
		var retDiv=$("<div />")
		retDiv.css("display", "none").css("margin", "0").css("padding", "0");
		retDiv.addClass("bbsItem");
		retDiv.attr("id", "num_" + jsonItem.num);
		
		var rowDiv=$("<div />").addClass("row");
		rowDiv.append($("<div />").addClass("col-lg-1").addClass("item_num"));
		rowDiv.append($("<div />").addClass("col-lg-7").addClass("item_title"));
		rowDiv.append($("<div />").addClass("col-lg-2").addClass("item_nick"));
		rowDiv.append($("<div />").addClass("col-lg-1").addClass("item_date"));
		rowDiv.append($("<div />").addClass("col-lg-1").addClass("item_option"));
		rowDiv.find("div.item_option").append($("<a>[-]</a>").attr("href", "#").addClass("delete_item"));
		rowDiv.find("div.item_option").append($("<a>[+]</a>").attr("href", "#").addClass("fix_item"));
		
		rowDiv.find("div.item_num").append($("<small />").append(jsonItem.num));
		rowDiv.find("div.item_title").append($("<strong />").append(jsonItem.title));
		rowDiv.find("div.item_date").append($("<small />").append(jsonItem.date));
		if(jsonItem.nick==null){
			rowDiv.find("div.item_nick").append($("<img />").attr("src", jsonItem.imgNickPath));
		} else{
			rowDiv.find("div.item_nick").append(jsonItem.nick);
		} //if
		
		rowDiv.click(function(){
			view.viewArticle(jsonItem.bbsName, jsonItem.num, jsonItem.title);
			retDiv.css("background-color",  "rgb(230, 230, 230)");
		});
		
		rowDiv.find("a.delete_item").click(function(){
			retDiv.remove();
			
			var fixedContents=objectStorage.getDOM("fixedContents");
			if(fixedContents!=null){
				fixedContents.find("num_" + jsonItem.num).remove();
				objectStorage.setDOM("fixedContents", fixedContents);
			} //if
			
			return false;
		});
		
		rowDiv.find("a.fix_item").click(function(){
			var fixedContents=objectStorage.getDOM("fixedContents");
			if(fixedContents==null)
				fixedContents=$("<div />").attr("id", "fixed_contents");
			fixedContents.append(retDiv);
			objectStorage.setDOM("fixedContents", fixedContents);
			
			view.showFixedItems();
			
			return false;
		});
		
		retDiv.append(rowDiv);
		retDiv.append($("<hr>").css("margin", "0").css("padding", "0"));
		retDiv.hide();
		return retDiv;
	} //parseBbsItem
} //function Parser

function HomeUtil(){
	this.getObjectEmbedNode=function(url, width, height){
		var objectNode=$("<object />").attr("data", url).attr("width", width+150).attr("height", height+170);
		objectNode.css("position", "relative").css("left", -150).css("top", -170);
		var embedNode=$("<embed />").attr("data", url).attr("width", width).attr("height", height);
		objectNode.append(embedNode);
		return objectNode;
	} //getObjectEmbedNode
} //function CommonUtil

bbsParser=new BbsParser();
homeUtil=new HomeUtil();