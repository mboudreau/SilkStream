package com.silkstream.platform.comparator;


import com.silkstream.platform.models.dto.TivityItem;

import java.util.Comparator;

public class TivityItemComparator implements Comparator<TivityItem> {

	public int compare(TivityItem item1, TivityItem item2) {
		if (item1.getDistance() != null && item2.getDistance() != null) {
			return item1.getDistance().compareTo(item2.getDistance());
		}
		return 0;
	}

}