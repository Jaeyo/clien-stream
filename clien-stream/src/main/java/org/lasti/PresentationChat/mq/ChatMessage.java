package org.lasti.PresentationChat.mq;

import java.io.Serializable;

public class ChatMessage implements Serializable {
	private static final long serialVersionUID = -3823888765690586279L;
	private String senderNickname;
	private String msg;

	public ChatMessage() {
	}

	public ChatMessage(String senderNickname, String msg) {
		this.senderNickname = senderNickname;
		this.msg = msg;
	}

	public String getSenderNickname() {
		return senderNickname;
	}

	public void setSenderNickname(String senderNickname) {
		this.senderNickname = senderNickname;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
} // class