package com.silkstream.platform.models.dto.google.place;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

@JsonSerialize(
		include = JsonSerialize.Inclusion.NON_NULL,
		typing = JsonSerialize.Typing.STATIC
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GooglePlaceAutocompleteItem {
	private String description;
	private String id;
	private String reference;
	private List<Object> matched_substrings;
	private List<GooglePlaceAutocompleteItemTerm> terms;
	private List<String> types;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getReference() {
		return reference;
	}

	public List<Object> getMatched_substrings() {
		return matched_substrings;
	}

	public List<GooglePlaceAutocompleteItemTerm> getTerms() {
		return terms;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public void setMatched_substrings(List<Object> matched_substrings) {
		this.matched_substrings = matched_substrings;
	}

	public void setTerms(List<GooglePlaceAutocompleteItemTerm> terms) {
		this.terms = terms;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}
}
