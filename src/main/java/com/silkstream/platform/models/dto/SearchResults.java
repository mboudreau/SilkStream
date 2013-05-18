package com.silkstream.platform.models.dto;

import java.util.List;


public class SearchResults<T> extends DTO {
	private List<T> list;
	private Integer count;
	private Integer itemPerPage;
	private SearchOptions options;

	public SearchResults() {
	}

	public SearchResults(List<T> list, Integer count, Integer itemPerPage, SearchOptions options) {
		this.list = list;
		this.count = count;
		this.itemPerPage = itemPerPage;
		this.options = options;
	}

	public SearchResults(List<T> list) {
		this(list, null, null, null);
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getItemPerPage() {
		return itemPerPage;
	}

	public void setItemPerPage(Integer itemPerPage) {
		this.itemPerPage = itemPerPage;
	}

	public SearchOptions getOptions() {
		return options;
	}

	public void setOptions(SearchOptions options) {
		this.options = options;
	}
}
