package com.silkstream.platform.service;

import com.silkstream.platform.enums.ActionType;
import com.silkstream.platform.enums.UserLogType;
import com.silkstream.platform.models.db.Comment;
import com.silkstream.platform.models.dto.CommentItem;
import com.silkstream.platform.models.dto.adapter.CommentAdapter;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("commentService")
public class CommentService extends BasicService {
	@Inject
	UserLogService userLogService;
	@Inject
	UserService userService;
	@Inject
	CloudSearchService searchService;
	@Inject
	CommentAdapter commentAdapter;


	public CommentItem create(String targetId, String message, String userId, UserLogType type) {
		if (targetId != null && userId != null) {
			Comment comment = new Comment();
			comment.setId(createId());
			comment.setMessage(message);
			comment.setUserid(userId);
			comment.setTarget(type);
			comment.setTargetId(targetId);
			comment.setCreadtedDate(now());
			comment.setUpdatedDate(now());
			mapper.clobber(comment);
			userLogService.create(UserLogType.ANSWER, comment.getId(), userId, ActionType.COMMENT, 0L);
			return commentAdapter.buildCompleteCommentItem(comment, userService.get(userId));
		}
		return null;
	}

	public void edit(CommentItem commentItem) {
		if (commentItem != null) {
			Comment comment = load(commentItem.getId());
			comment.setUpdatedDate(now());
			comment.setRating(commentItem.getRating());
			comment.setMessage(commentItem.getMessage());
			mapper.save(comment);
		}
	}

	public void delete(String id) {
		if (id != null) {
			delete(load(id));
		}
	}

	public void delete(Comment comment) {
		if (comment != null) {
			mapper.delete(comment);
		}
	}

	public void flag(String id) {
		if (id != null) {
			Comment comment = load(id);
			if (comment != null) {
				comment.setFlag(true);
				mapper.save(comment);
			}
		}
		// TODO: need to send an email here
	}

	public CommentItem get(String id) {
		if (id != null) {
			return commentAdapter.buildCompleteCommentItem(load(id), null);
		}
		return null;
	}

	public Comment load(String id) {
		return load(id,null);
	}

	public Comment load(String id,List<String> attributesToGet) {
		if (id != null) {
			return mapper.load(Comment.class, id,attributesToGet);
		}
		return null;
	}

	public List<CommentItem> getFromTargetId(String targetId) {
		if (targetId != null) {
			List<Comment> comments = mapper.scanWith(Comment.class, "ti", targetId);
			if (comments != null) {
				List<CommentItem> commentItems = new ArrayList<CommentItem>();
				for (Comment comment : comments) {
					commentItems.add(commentAdapter.buildCompleteCommentItem(comment, userService.get(comment.getUserid())));
				}
				return commentItems;
			}
		}
		return null;
	}
}
