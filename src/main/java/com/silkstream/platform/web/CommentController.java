package com.silkstream.platform.web;

import com.silkstream.platform.models.dto.CommentItem;
import com.silkstream.platform.models.dto.adapter.CommentAdapter;
import com.silkstream.platform.models.db.UserLog;
import com.silkstream.platform.service.CloudSearchService;
import com.silkstream.platform.service.CommentService;
import com.silkstream.platform.service.UserLogService;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/comment", produces = "application/json")
public class CommentController extends BasicController {
	@Inject
	protected UserLogController userlogController;
	@Inject
	protected CommentService commentService;
	@Inject
	protected UserLogService userLogService;
	@Inject
	protected CommentAdapter commentAdapter;
	@Inject
	protected CloudSearchService cloudSearchService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/{id}", method = RequestMethod.PUT, consumes = "application/json")
    @PreAuthorize("hasRole('USER')")
    public void edit(@RequestBody CommentItem commentItem) {
        commentService.edit(commentItem);
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/flag")
    @PreAuthorize("hasRole('USER')")
    public void flag(@PathVariable("id") String id) {
        commentService.flag(id);
    }

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public CommentItem get(@PathVariable("id") String id){
		return commentAdapter.buildCompleteCommentItem(commentService.load(id),null);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(@PathVariable String id){
		commentService.delete(id);
		cloudSearchService.deleteItem(id);
		List<UserLog> userLogs = userLogService.getAllLogsOnTarget(id);
		for (UserLog userLog : userLogs){
			userLogService.delete(userLog.getId());
			cloudSearchService.deleteItem(userLog.getId());
		}
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/delete/target/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteFromTargetId(@PathVariable String id){
		List<CommentItem> commentItems = commentService.getFromTargetId(id);
		for (CommentItem commentItem : commentItems){
			delete(commentItem.getId());
		}
	}
}
