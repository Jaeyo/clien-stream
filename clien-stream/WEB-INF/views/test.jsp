<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Insert title here</title>

<link href="<c:url value="/resource/css/bootstrap.css" />" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="<c:url value="/resource/js/bootstrap.min.js" /> "></script>

</head>
<body>

<div id="wrapper">
</div>

<div id="page-wrapper">
	<div class="row">
		<div class="col-xs-12">
			<textarea id="ta_chat" rows="12" class="form-control"></textarea>
			<input type="text" id="text_chat" class="form-control" />
		</div>
	</div>
</div>

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
		console.log("[WebSocket->onmessage] " + e.data);
		var jsonObj=JSON.parse(e.data);
		var sender=jsonObj.sender;
		var msg=jsonObj.msg;
		if(sender==model.nick)
			return;
		view.addMsg(sender, msg);
	} //onmessage
	
	this.socket.onclose=function(){
		console.log("[WebSocket->onclose]");
	} //onclose
	
	this.sendMsg=function(msg){
		jsonMsg=new Object();
		jsonMsg.kind="chatMsg";
		jsonMsg.msg=msg;
		this.socket.send(JSON.stringify(jsonMsg));
	} //send
	
	this.setNick=function(nick){
		jsonMsg=new Object();
		jsonMsg.kind="setNick";
		jsonMsg.nick=nick;
		this.waitForSocketConnection(function(){
			wsController.socket.send(JSON.stringify(jsonMsg));
		});
	} //setNick
	 
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
	this.addMsg=function(sender, msg){
		$("#ta_chat").val($("#ta_chat").val() + sender + " : " + msg + "\n");
	} //addMsg
} //View

function Model(){
	this.nick="${nick}";
} //Model

controller=new Controller();
wsController=new WsController();
view=new View();
model=new Model();
</script>

<script type="text/javascript">
wsController.setNick(model.nick);

$("#text_chat").keypress(function(e){
	if(e.which!=13)
		return;
	var textChat=$("#text_chat");
	var msg=textChat.val();
	view.addMsg(model.nick, msg);
	wsController.sendMsg(msg);
	textChat.val("");
});
</script>

</body>
</html>