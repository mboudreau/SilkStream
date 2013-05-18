package com.silkstream.platform.comparator;


import com.silkstream.platform.models.dto.google.ResultInfo;

import java.util.Comparator;

public class ResultInfoComparator implements Comparator<ResultInfo> {

	public int compare(ResultInfo item1, ResultInfo item2) {
		if(item1.getDistance()!=null && item2.getDistance()!=null){
		return item1.getDistance().compareTo(item2.getDistance());
		}
		return 0;
	}

}