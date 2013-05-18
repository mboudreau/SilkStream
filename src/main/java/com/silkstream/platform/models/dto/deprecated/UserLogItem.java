package com.silkstream.platform.models.dto.deprecated;

import com.silkstream.platform.models.db.Comment;
import com.silkstream.platform.models.dto.TivityItem;
import com.silkstream.platform.models.dto.RequestItem;

import java.util.List;


public class UserLogItem {
	private List<TivityItem> createdTivities;
	private List<RequestItem> createdRequests;
	private List<Comment> createdComments;
	private List<TivityItem> votedUpTivities;
	private List<TivityItem> votedDownTivities;
	private List<RequestItem> votedUpRequests;
	private List<RequestItem> votedDownRequests;

	public List<TivityItem> getCreatedTivities() {
		return createdTivities;
	}

	public void setCreatedTivities(List<TivityItem> createdTivities) {
		this.createdTivities = createdTivities;
	}

	public List<RequestItem> getCreatedRequests() {
		return createdRequests;
	}

	public void setCreatedRequests(List<RequestItem> createdRequests) {
		this.createdRequests = createdRequests;
	}

	public List<Comment> getCreatedComments() {
		return createdComments;
	}

	public void setCreatedComments(List<Comment> createdComments) {
		this.createdComments = createdComments;
	}

	public List<TivityItem> getVotedUpTivities() {
		return votedUpTivities;
	}

	public void setVotedUpTivities(List<TivityItem> votedUpTivities) {
		this.votedUpTivities = votedUpTivities;
	}

	public List<TivityItem> getVotedDownTivities() {
		return votedDownTivities;
	}

	public void setVotedDownTivities(List<TivityItem> votedDownTivities) {
		this.votedDownTivities = votedDownTivities;
	}

	public List<RequestItem> getVotedUpRequests() {
		return votedUpRequests;
	}

	public void setVotedUpRequests(List<RequestItem> votedUpRequests) {
		this.votedUpRequests = votedUpRequests;
	}

	public List<RequestItem> getVotedDownRequests() {
		return votedDownRequests;
	}

	public void setVotedDownRequests(List<RequestItem> votedDownRequests) {
		this.votedDownRequests = votedDownRequests;
	}
}
