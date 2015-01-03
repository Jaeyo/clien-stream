package org.jaeyo.clien_stream.service;

import java.util.UUID;

public abstract class AbstractService {
	public String generatePersistenceSessionId(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	} //generatePersistenceSessionId
} //class