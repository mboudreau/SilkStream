package com.silkstream.platform.models.dto;

import java.util.List;

public class Invites extends DTO{
	private List<UserItem> list;
	private String message;

	public List<UserItem> getList() {
		return list;
	}

	public void setList(List<UserItem> list) {
		this.list = list;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
