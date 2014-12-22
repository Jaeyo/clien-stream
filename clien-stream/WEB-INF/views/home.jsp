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
	
		<!-- sidebar -->
		<div id="sidebar-wrapper">
			<ul class="sidebar-nav">
				<li class="sidebar-brand">
					<a href="${pageContext.request.contextPath}/home/">clien stream</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/home/park">모두의 공원</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/home/image/">사진 게시판</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/home/kin/">아무거나 질문</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/home/news/">새로운 소식</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/home/lecture/">팁과 강좌</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/home/use/">사용기 게시판</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/home/chehum/">체험단사용기</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/home/useful/">유용한사이트</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/home/jirum/">알뜰구매</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/home/coupon/">쿠폰/이벤트</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/home/hongbo/">직접 홍보</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/home/pds/">자료실</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/home/sold/">회원중고장터</a>
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
						<h1>${bbsName }</h1>
						<br />
					</div>
				</div>
				
				<div id="fixed_contents"></div>
				
				<input type="button" class="form-control" value="새로운 글이 0건 있습니다." id="preparedItemCount" onclick="javascript:view.showPreparedItems()" />
				
				<hr>
				
				<div id="contents"></div>
				
				<!-- prepared item template -->
				<div id="prepared_item_template" style="display:none; margin:0; padding:0;">
					<div class="row">
						<div id="item_num" class="col-lg-1"><!-- num --></div>
						<div id="item_title" class="col-lg-7"><h2><!-- title --></h2></div>
						<div id="item_nick" class="col-lg-2"><h6><!-- nick --></h6></div>
						<div id="item_date" class="col-lg-1"><!-- date --></div>
						<div id="item_option" class="col-lg-1">
							<a href="#" class="delete_item">[-]</a>
							<a href="#" class="fix_item">[+]</a>
						</div>
					</div>
					<hr style="margin:0; padding:0">
				</div>
				<!-- prepared item template -->
				
				<!-- article view -->
				<div id="articleView" style="display:none;">
				</div>
				<!-- article view -->
				
			</div>
		</div>
		<!-- //page content -->
		
	</div>

<script src="http://ironsummitmedia.github.io/startbootstrap-simple-sidebar/js/jquery.js"></script>
<script src="http://ironsummitmedia.github.io/startbootstrap-simple-sidebar/js/bootstrap.min.js"></script>
<script src="http://code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
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
		console.log("request msging");
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
		item.insertAfter($("#contents")).show("300");
		item.show("300");
	} //addItem
	
	this.fixItem=function(item){
		item.hide().insertAfter($("#fixed_contents")).show(300);
	} //fixItem
	
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
		view.removeOldItem(); 
	} //showPreparedItems
	
	this.removeOldItem=function(){
		var itemArr=$("div.bbsItem");
		var maxCount=17;
		if(itemArr.length<maxCount) 
			return;
		
		var removeCount=itemArr.length-maxCount;
		for(i=0; i<removeCount; i++){
			$(itemArr[itemArr.length-1]).remove();
			itemArr.splice(itemArr.length-1, 1);
		} //for i
	} //removeOldItem
	
	this.refreshPreparedCount=function(){
		var count=model.preparedItems.length;
		$("#preparedItemCount").attr("value", "새로운 글이 " + count+ "건 있습니다.");
		if(count!=0){
			$("#preparedItemCount").css("background-color", "rgb(55,66,155)").css("color", "white");
		} else{
			$("#preparedItemCount").css("background-color", "white").css("color", "");
		} //if
	} //setPreparedCount
	
	this.viewArticle=function(bbsName, num, title){
		var url= "http://www.clien.net/cs2/bbs/board.php?bo_table=" + bbsName + "&wr_id=" + num;
		$("#articleView").show();
		var objectNode=$("<object />").attr("data", url).attr("width", 780+150).attr("height", 580+170).attr("id", "articleNode").attr("style", "position:relative; left:-150px; top:-170px");
		var embedNode=$("<embed />").attr("data", url).attr("width", 780).attr("height", 580);
		objectNode.append(embedNode);
		$("#articleView").append(objectNode).dialog({
			close: function(event, ui){
				view.closeArticle();
			},
			title: title,
			resizable: false,
			height: 600, 
			width: 800,
			open: function(event, ui){
				$("#articleView").css("overflow", "hidden");
			}
			});
	} //viewArticle
	
	this.closeArticle=function(){
		$("#articleView").hide();
		$("#articleView").html("");
	} //closeArticle
} //View



function Model(){
	this.preparedItems=[];  
	
	this.storeItem=function(item){
		var jsonItem=JSON.parse(item);
		var itemTemplate=$("#prepared_item_template").clone();
		itemTemplate.attr("class", "bbsItem");
		itemTemplate.attr("id", "num_" + jsonItem.num);
		itemTemplate.find("#item_num").html("<small>"+jsonItem.num+"</small>");
		itemTemplate.find("#item_date").html("<small>" + jsonItem.date+"</small>");
		itemTemplate.find("#item_title").html("<strong>"+jsonItem.title+"</strong>");
		if(jsonItem.nick==null){
			itemTemplate.find("#item_nick").html("<img src='" + jsonItem.imgNickPath + "' />'");
		} else{
			itemTemplate.find("#item_nick").html(jsonItem.nick);
		} //if
		
		itemTemplate.click(function(){ 
			view.viewArticle(jsonItem.bbsName, jsonItem.num, jsonItem.title); 
			itemTemplate.css("background-color", "rgb(230, 230, 230)"); 
		});
		
		itemTemplate.find("#item_option").find("a.delete_item").click(function(){
			$("#num_" + jsonItem.num).remove();
			return false;
		});
		
		itemTemplate.find("#item_option").find("a.fix_item").click(function(){
			view.fixItem($("#num_" + jsonItem.num));
			return false;
		});
		
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