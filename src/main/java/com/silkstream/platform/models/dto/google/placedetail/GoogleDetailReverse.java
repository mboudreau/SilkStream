package com.silkstream.platform.models.dto.google.placedetail;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleDetailReverse {
	private Object html_attributions;
	private String status;
	private GoogleDetailResult result;

	public Object getHtml_attributions() {
		return html_attributions;
	}

	public void setHtml_attributions(Object html_attributions) {
		this.html_attributions = html_attributions;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public GoogleDetailResult getResult() {
		return result;
	}

	public void setResult(GoogleDetailResult result) {
		this.result = result;
	}
}
