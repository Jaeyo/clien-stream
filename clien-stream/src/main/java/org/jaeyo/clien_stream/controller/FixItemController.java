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
		long numLong;

		try {
			numLong = Long.parseLong(num);
		} catch (NumberFormatException e) {
			String msg=String.format("invalid num : %s", num);
			logger.error(msg);
			return new JSONObject().put("success", 0).put("desc", msg).toString();
		} // if
		
		String collectionName = String.format("bbsItem_%s", bbsName.toUpperCase());
		DBCollection coll = MongoDbAdapter.getInstance().getCollection(collectionName);
		
		DBCursor findResult = coll.find(new BasicDBObject("num", new BasicDBObject("$eq", numLong)));
		if (!findResult.hasNext()) {
			String msg=String.format("find job has no result for num : %s, bbsName : %s", num, bbsName);
			logger.warn(msg);
			return new JSONObject().put("success", 0).put("desc", msg).toString();
		} // if

		BasicDBObject bbsItemObj = (BasicDBObject) findResult.next();
		BbsItem bbsItem = new BbsItem(bbsItemObj);

		boolean result = fixItemService.putFixedItems(BbsNames.valueOf(bbsName.toUpperCase()), bbsItem, response, request);
		if (!result) {
			String msg=String.format("failed to put FixedItem, bbsName : %s, num : %s", bbsName, num);
			logger.error(msg);
			return new JSONObject().put("success", 0).put("desc", msg).toString();
		} // if

		return new JSONObject().put("success", 1).toString();
	} // putFixBbsItem

	@RequestMapping(value = "/getFixBbsItem/{bbsName}", method = RequestMethod.GET)
	public @ResponseBody String getFixBbsItems(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "bbsName") String bbsName) {
		List<BbsItem> bbsItems = fixItemService.getFixedItems(BbsNames.valueOf(bbsName.toUpperCase()), request);
		if (bbsItems == null) {
			logger.info("getFixedItems() has no result for bbsName : {}", bbsName);
			return new JSONObject().put("success", 1).put("count", 0).toString();
		} // if

		JSONObject retJson=new JSONObject();
		retJson.put("success", 1);
		retJson.put("count", bbsItems.size());
		
		for (BbsItem bbsItem : bbsItems)
			retJson.append("data", bbsItem.toJSON());

//		return new JSONObject().put("success", "1").put("data", dataArr).toString();
		return retJson.toString();
	} // getFixBbsItems

	@RequestMapping(value = "/removeFixBbsItem/{bbsName}/{num}", method = RequestMethod.GET)
	public @ResponseBody String getFixBbsItems(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "bbsName") String bbsName,
			@PathVariable(value = "num") String num) {
		long numLong;

		try {
			numLong = Long.parseLong(num);
		} catch (NumberFormatException e) {
			String msg=String.format("invalid num : %s", num);
			logger.error(msg);
			return new JSONObject().put("success", 0).put("desc", msg).toString();
		} // if

		boolean result = fixItemService.removeFixedItem(BbsNames.valueOf(bbsName.toUpperCase()), numLong, request);
		if (!result) {
			String msg=String.format("failed to remove FixedItem for bbsName : %s, num  : %s", bbsName, numLong);
			logger.warn(msg);
			return new JSONObject().put("success", 0).put("desc", msg).toString();
		} // if

		return new JSONObject().put("success", 1).toString();
	} // getFixBbsItems
} // class