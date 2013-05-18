package com.silkstream.platform.models.search.deprecated.index;

import java.util.ArrayList;

public class IndexItemResponse {
	private String status;
	private int adds;
	private int deletes;
	private ArrayList<IndexItemResponseMessage> errors;
	private ArrayList<IndexItemResponseMessage> warnings;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAdds() {
		return adds;
	}

	public void setAdds(int adds) {
		this.adds = adds;
	}

	public int getDeletes() {
		return deletes;
	}

	public void setDeletes(int deletes) {
		this.deletes = deletes;
	}

	public ArrayList<IndexItemResponseMessage> getErrors() {
		return errors;
	}

	public void setErrors(ArrayList<IndexItemResponseMessage> errors) {
		this.errors = errors;
	}

	public ArrayList<IndexItemResponseMessage> getWarnings() {
		return warnings;
	}

	public void setWarnings(ArrayList<IndexItemResponseMessage> warnings) {
		this.warnings = warnings;
	}
}
