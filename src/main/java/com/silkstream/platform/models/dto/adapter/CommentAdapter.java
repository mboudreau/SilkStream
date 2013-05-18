package com.silkstream.platform.models.dto.adapter;


import com.silkstream.platform.models.db.Comment;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.models.dto.CommentItem;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service("CommentAdapter")
public class CommentAdapter {
	@Inject
	UserAdapter userAdapter;

	public CommentItem buildCompleteCommentItem(Comment comment, User user) {


		if (comment != null) {
			CommentItem commentItem = new CommentItem();
			commentItem.setId(comment.getId());
			commentItem.setTargetId(comment.getTargetId());
			if (user != null) {
				commentItem.setUser(userAdapter.buildCompleteUserItem(user));
			}
			commentItem.setMessage(comment.getMessage());
			commentItem.setCreadtedDate(comment.getCreadtedDate());
			commentItem.setRating(comment.getRating());
			commentItem.setUpdatedDate(comment.getUpdatedDate());
			commentItem.setFlag(comment.getFlag());
			return commentItem;
		}
		return null;
	}

	public Comment buildCompleteComment(CommentItem commentItem) {
		Comment comment = new Comment();
		if (commentItem != null) {
			comment.setCreadtedDate(commentItem.getCreadtedDate());
			comment.setFlag(commentItem.getFlag());
			comment.setId(commentItem.getId());
			comment.setMessage(commentItem.getMessage());
			comment.setRating(commentItem.getRating());
			comment.setTargetId(commentItem.getTargetId());
			comment.setUpdatedDate(commentItem.getUpdatedDate());
			comment.setUserid(commentItem.getUser().getId());
		}
		return comment;
	}
}
