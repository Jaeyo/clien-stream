<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>clien stream</title>

<!-- 
<link href="<c:url value="/resource/css/bootstrap.css" />" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="<c:url value="/resource/js/bootstrap.min.js" /> "></script>
 -->

<link href="http://ironsummitmedia.github.io/startbootstrap-simple-sidebar/css/bootstrap.min.css" rel="stylesheet">
<link href="http://ironsummitmedia.github.io/startbootstrap-simple-sidebar/css/simple-sidebar.css" rel="stylesheet">

<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">

</head>
<body>

	<div id="wrapper">
	
		<!-- page content -->
		<div id="page-content-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<h1>${bbsName }</h1>
						<br />
					</div>
				</div>
				
				<div id="fixed_contents"></div>
				
				<input type="button" class="form-control" value="새로운 글이 0건 있습니다." id="newItemCount" onclick="javascript:view.showNewItems()" />
				
				<hr>
				
				<div id="contents"></div>
				
				<div id="articleView"></div>
			</div>
		</div>
		<!-- //page content -->
		
	</div>

<script src="http://ironsummitmedia.github.io/startbootstrap-simple-sidebar/js/jquery.js"></script>
<script src="http://ironsummitmedia.github.io/startbootstrap-simple-sidebar/js/bootstrap.min.js"></script>
<script src="http://code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
<script src="<c:url value="/resource/js/home.js?ver=25" /> "></script>
<script type="text/javascript">
var controller;
var wsController;
var view;
var model;

function Controller(){
} //Controller

function WsController(){
	this.socket=new WebSocket("ws://${pageContext.request.serverName}:${wsPort}/websocket/ws/server/${wsPort}")
	
	this.socket.onopen=function(){
		console.log("[WebSocket->onopen]");
	} //onopen
	
	this.socket.onmessage=function(e){
		console.log("[WebSocket->onmessage] ");
		var itemObj=JSON.parse(e.data);
		model.storeNewItem(itemObj);
	} //onmessage
	
	this.socket.onclose=function(){
		console.log("[WebSocket->onclose]");
	} //onclose
	
	this.requestMsging=function(){
		jsonMsg=new Object();
		jsonMsg.bbsName="${bbsName}";
		this.waitForSocketConnection(function(){
			wsController.socket.send(JSON.stringify(jsonMsg));
		});
	} //requestMsging
	
	this.stop=function(){
		this.socket.close();
	} //stop
	
	this.waitForSocketConnection=function(callback){
		setTimeout(function(){
			if(wsController.socket.readyState==WebSocket.OPEN){
				console.log("connection is made");
				if(callback!=null)
					callback();
			} else{
				console.log("wait for connection");
				wsController.waitForSocketConnection(callback);
			} //if
		}, 100);
	} //waitForSocketConnection	
} //WsController


function View(){
	this.showFixedItems=function(){
		var storedFixedContents=storedb("fixedContents").find();
		var fixedContentsHtml=$("<div />");
		
		console.log("storedFixedContents.length : " + storedFixedContents.length); 
		
		for(i=0; i<storedFixedContents.length; i++){
			var bbsItemObj=storedFixedContents[i];
			var bbsItemHtml=bbsUtil.objToHtml(bbsItemObj);
			fixedContentsHtml.append(bbsItemHtml);
		} //for i
		
		$("#fixed_contents").html(fixedContentsHtml.html());
		
		var bbsItemArr=$("#fixed_contents").find("div.bbsItem");
		for(i=0; i<bbsItemArr.length; i++)
			bbsUtil.registerClickEvent(bbsItemArr[i], view, model.afterClickColor);
	} //showFixedItems
	
	this.showNewItems=function(){
		var newItemCount=model.newItemArr.length;
		if(newItemCount==0)
			return;
		
		var reversedNewItemArr=[];
		for(i=0; i<newItemCount; i++){
			reversedNewItemArr.push(model.newItemArr.pop());
		} //for i
		
		newItemCount=reversedNewItemArr.length;
		for(i=0; i<newItemCount; i++){
			var newItemObj=reversedNewItemArr[i];
			var newItemDOM=bbsUtil.objToHtml(newItemObj);
			bbsUtil.registerClickEvent(newItemDOM, view, model.afterClickColor);
			
			newItemDOM.hide();
			$("#contents").append(newItemDOM);
			newItemDOM.show("300");
		} //for i
		
		view.refreshNewItemCount();
		view.removeOldItem();
	} //showNewItems
	
	this.removeOldItem=function(){
		var itemArr=$("#contents").find("div.bbsItem");
		if(itemArr.length<model.maxItemCount) 
			return;
		
		var removeCount=itemArr.length-model.maxItemCount;
		for(i=0; i<removeCount; i++){
			$(itemArr[itemArr.length-1]).remove();
			itemArr.splice(itemArr.length-1, 1);
		} //for i
	} //removeOldItem
	
	this.refreshNewItemCount=function(){
		var count=model.newItemArr.length;
		var newItemCountDOM=$("#newItemCount");
		newItemCountDOM.attr("value", "새로운 글이 " + count + "건 있습니다.");
		if(count!=0){
			newItemCountDOM.css("background-color", model.clienBlue).css("color", "white");
		} else{
			newItemCountDOM.css("background-color", "white").css("color", model.clienBlue);
		} //if
	} //refreshNewItemCount
	
	this.viewArticle=function(bbsName, num, title){
		var url= "http://www.clien.net/cs2/bbs/board.php?bo_table=" + bbsName + "&wr_id=" + num;
		var articleView=$("#articleView").html("");
		articleView.show(100).append(homeUtil.getObjectEmbedNode(url, 780, 580)).dialog({
			close: function(event, ui){
				articleView.hide().html("");
			},
			title: title,
			resizable: false,
			height: 600, 
			width: 800,
			open: function(event, ui){
				articleView.css("overflow", "hidden");
			} //open
		});
	} //viewArticle
} //View



function Model(){
	this.newItemArr=[];
	this.afterClickColor="rgb(230, 230, 230)";
	this.clienBlue="rgb(55, 66, 155)";
	this.maxItemCount=17;
	
	this.storeNewItem=function(itemObj){
		model.newItemArr.push(itemObj);
		view.refreshNewItemCount();
	} //storeNewItem
} //Model

controller=new Controller();
wsController=new WsController();
view=new View();
model=new Model();
</script>

<script type="text/javascript">
$("#preparedItemCount").css("color", model.clienBlue);

wsController.requestMsging();
view.showFixedItems();
</script>

</body>
</html>