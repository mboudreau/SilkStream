package com.silkstream.platform.web;

import com.silkstream.platform.models.dto.SearchResults;
import com.silkstream.platform.models.dto.UserLogItem;
import com.silkstream.platform.models.dto.adapter.UserLogAdapter;
import com.silkstream.platform.service.CloudSearchService;
import com.silkstream.platform.service.UserLogService;
import javax.inject.Inject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/api/log", produces = "application/json")
public class UserLogController extends BasicController {
	@Inject
	protected UserLogService userLogService;
    @Inject
    UserLogAdapter userLogAdapter;
	@Inject
	protected CloudSearchService cloudSearchService;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@PreAuthorize("hasRole('USER')")
	@ResponseBody
	public UserLogItem get(@PathVariable("id") String id) {
        try {
		    UserLogItem userLogItem = userLogAdapter.buildCompleteUserLogItem(userLogService.load(id), null, null);
            return userLogItem;
        }
        catch (Exception e) {
        }
        return null;
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET, value = "/user/{id}")
	@ResponseBody
	public SearchResults<UserLogItem> getLogsFromUser(@PathVariable("id") String id) {
		return userLogService.loadLogsFromUser(id);
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public void delete(@PathVariable String id){
		userLogService.delete(id);
		cloudSearchService.deleteItem(id);
	}
}