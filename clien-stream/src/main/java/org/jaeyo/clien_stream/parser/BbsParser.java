package org.jaeyo.clien_stream.parser;

import java.util.ArrayList;

import org.jaeyo.clien_stream.entity.BbsItem;

public interface BbsParser {
	public ArrayList<BbsItem> parsePage(String bbsName, int page);
} //interface