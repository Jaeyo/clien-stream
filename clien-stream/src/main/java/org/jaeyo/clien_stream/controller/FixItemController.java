package org.jaeyo.clien_stream.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jaeyo.clien_stream.consts.BbsNames;
import org.jaeyo.clien_stream.entity.BbsItem;
import org.jaeyo.clien_stream.repo.mongodb.MongoDbAdapter;
import org.jaeyo.clien_stream.service.FixItemService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@Controller
public class FixItemController {
	private static final Logger logger = LoggerFactory.getLogger(FixItemController.class);

	@Inject
	FixItemService fixItemService;

	@RequestMapping(value = "/putFixBbsItem/{bbsName}/{num}", method = RequestMethod.GET)
	public @ResponseBody String putFixBbsItem(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "bbsName") String bbsName,
			@PathVariable(value = "num") String num) {
		String collectionName = String.format("bbsItem_%s", bbsName.toUpperCase());
		DBCollection coll = MongoDbAdapter.getInstance().getCollection(collectionName);

		DBCursor findResult = coll.find(new BasicDBObject().append("num", num));
		if (!findResult.hasNext()) {
			logger.warn("find job has no result for num : {}, bbsName : {}", num, bbsName);
			return new JSONObject().append("success", "0").toString();
		} // if

		BasicDBObject bbsItemObj = (BasicDBObject) findResult.next();
		BbsItem bbsItem = new BbsItem(bbsItemObj);

		boolean result = fixItemService.putFixedItems(BbsNames.valueOf(bbsName), bbsItem, response, request);
		if (!result) {
			logger.error("failed to put FixedItem, bbsName : {}, num : {}", bbsName, num);
			return new JSONObject().append("success", "0").toString();
		} // if

		return new JSONObject().append("success", "1").toString();
	} // putFixBbsItem

	@RequestMapping(value = "/getFixBbsItem/{bbsName}", method = RequestMethod.GET)
	public @ResponseBody String getFixBbsItems(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "bbsName") String bbsName) {
		List<BbsItem> bbsItems = fixItemService.getFixedItems(BbsNames.valueOf(bbsName.toUpperCase()), request);
		if (bbsItems == null) {
			logger.warn("getFixedItems() has no result for bbsName : {}", bbsName);
			return new JSONObject().append("success", "0").toString();
		} // if

		JSONArray dataArr = new JSONArray();
		for (BbsItem bbsItem : bbsItems)
			dataArr.put(bbsItem.toJSON());

		return new JSONObject().append("success", "1").append("data", dataArr).toString();
	} // getFixBbsItems

	@RequestMapping(value = "/removeFixBbsItem/{bbsName}/{num}", method = RequestMethod.GET)
	public @ResponseBody String getFixBbsItems(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "bbsName") String bbsName,
			@PathVariable(value = "num") String num) {
		long numLong;

		try {
			numLong = Long.parseLong(num);
		} catch (NumberFormatException e) {
			logger.error("invalid num : {}", num);
			return new JSONObject().append("success", "0").toString();
		} // if

		boolean result = fixItemService.removeFixedItem(BbsNames.valueOf(bbsName.toUpperCase()), numLong, request);
		if (!result) {
			logger.warn("failed to remove FixedItem for bbsName : {}, num  : {}", bbsName, numLong);
			return new JSONObject().append("success", "0").toString();
		} // if

		return new JSONObject().append("success", "1").toString();
	} // getFixBbsItems
} // class