package com.silkstream.platform.models.search.deprecated.index;

import com.silkstream.platform.enums.IndexType;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Map;

public class IndexItemRequest {
	private String id;
	private IndexType type;
	private String lang;
	private long version;
	private Map<String, Object> fields;

	public IndexItemRequest() {
		setLang("en");
		setVersion(System.currentTimeMillis() / 1000);
	}

    @JsonSerialize()
	public IndexType getType() {
		return type;
	}

	public void setType(IndexType type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public Map<String, Object> getFields() {
		return fields;
	}

	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}
}
