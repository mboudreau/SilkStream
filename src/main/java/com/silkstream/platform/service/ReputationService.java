package com.silkstream.platform.service;


import org.springframework.stereotype.Service;

@Service("reputationService")
public class ReputationService extends BasicService {
	public final Long ANSWER_CREATED_POINTS = 25L;
	public final Long ANSWER_CREATED_POINTS_FIRSTTIME = 50L;
	public final Long ANSWER_CREATED_POINTS_SECONDTIME = 100L;
	public final Long ANSWER_CREATED_POINTS_THIRDTIME = 150L;
	public final Long USER_INVITE_WENT_THROUGH = 0L;
}
