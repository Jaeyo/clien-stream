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
				
				<div class="row">
					<button id="testBtn">test</button>
				</div>
				
				<div id="contents"></div>
				
				<div id="prepared_item_template" style="visibility: hidden;">
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
					<div class="row">
						<div id="item_content" class="col-lg-12"><!-- content --></div>
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
	
} //WsController

function View(){
	this.cloneItem=function(num, date, hit, title, nick, content){
		var item=$("#prepared_item_template").clone();
		item.find("#item_num").html(num);
		item.find("#item_date").html(date);
		item.find("#item_hit").html(hit);
		item.find("#item_title").html(title);
		item.find("#item_nick").html(nick);
		item.find("#item_content").html(content);
		item.css("visibility", "visible");
		item.insertAfter("#contents");
	} //cloneItem
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
var counter=0;
$("#testBtn").click(function(){
	counter=counter+1;
	view.cloneItem(counter, "2014-11-11 12:23:33", "8123", "THIS IS TITLE", "nick", "this is contentntew");
});
</script>

</body>
</html>