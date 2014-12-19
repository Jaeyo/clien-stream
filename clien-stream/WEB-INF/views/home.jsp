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

</head>
<body>

	<div id="wrapper">
	
		<!-- sidebar -->
		<div id="sidebar-wrapper">
			<ul class="sidebar-nav">
				<li class="sidebar-brand">
					<a href="#">clien stream</a>
				</li>
				<li>
					<a href="#">모두의 공원</a>
				</li>
				<li>
					<a href="#">사진 게시판</a>
				</li>
				<li>
					<a href="#">아무거나 질문</a>
				</li>
				<!-- TODO -->
			</ul>
		</div>
		<!-- //sidebar-wrapper -->
		
		<!-- page content -->
		<div id="page-content-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<h1>모두의 공원</h1>
						<p>this is clien stream</p>
					</div>
				</div>
				
				<div id="preparedItemCount" onclick="javascript:view.showPreparedItems()"></div>
				
				<hr>
				
				<div id="contents"></div>
				
				<div id="prepared_item_template" style="display:none;">
					<div class="row">
						<div id="item_num" class="col-lg-1"><!-- num --></div>
						<div id="item_date" class="col-lg-3"><!-- date --></div>
						<div id="item_hit" class="col-lg-1"><!-- hit --></div>
						<div class="col-lg-7"></div>
					</div>
					<div class="row">
						<div id="item_title" class="col-lg-12"><h2><!-- title --></h2></div>
					</div>
					<div class="row">
						<div id="item_nick" class="col-lg-4"><h6><!-- nick --></h6></div>
						<div class="col-lg-8"></div>
					</div>
					<hr>
					<!-- reply area -->
				</div>
			</div>
		</div>
		<!-- //page content -->
		
	</div>

<script src="http://ironsummitmedia.github.io/startbootstrap-simple-sidebar/js/jquery.js"></script>
<script src="http://ironsummitmedia.github.io/startbootstrap-simple-sidebar/js/bootstrap.min.js"></script>
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
		var item=e.data;
		model.storeItem(item);
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
				this.waitForSocketConnection(callback);
			} //if
		}, 100);
	} //waitForSocketConnection	
} //WsController

function View(){
	this.addItem=function(item){
		item.insertAfter($("#contents"));
		item.show("300");
	} //addItem
	
	this.showPreparedItems=function(){
		if(model.preparedItems.length==0)
			return;
		
		var itemCount=model.preparedItems.length;
		var tmpItemArr=[];
		for(i=0; i<itemCount; i++){
			tmpItemArr.push(model.preparedItems.pop());
		} //for i
		
		itemCount=tmpItemArr.length;
		for(i=0; i<itemCount; i++){
			view.addItem(tmpItemArr.pop());
		} //for i
		
		view.refreshPreparedCount();
	} //showPreparedItems
	
	this.refreshPreparedCount=function(){
		$("#preparedItemCount").html(model.preparedItems.length);
	} //setPreparedCount
} //View

function Model(){
	this.preparedItems=[];  
	
	this.storeItem=function(item){
		var jsonItem=JSON.parse(item);
		var itemTemplate=$("#prepared_item_template").clone();
		itemTemplate.find("#item_num").html(jsonItem.num);
		itemTemplate.find("#item_date").html(jsonItem.date);
		itemTemplate.find("#item_hit").html(jsonItem.hit);
		itemTemplate.find("#item_title").html(jsonItem.title);
		if(jsonItem.nick==null){
			itemTemplate.find("#item_nick").html(jsonItem.nick);
		} else{
			itemTemplate.find("#item_nick").html("<img src='" + jsonItem.imgNickPath + "' />'");
		} //if
		
		itemTemplate.hide();
		this.preparedItems.push(itemTemplate);
		
		view.refreshPreparedCount();
	} //storeItem
} //Model

controller=new Controller();
wsController=new WsController();
view=new View();
model=new Model();
</script>

<script type="text/javascript">
wsController.requestMsging();
</script>

</body>
</html>