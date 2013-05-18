package com.silkstream.platform.comparator;


import com.silkstream.platform.models.dto.PlaceItem;

import java.util.Comparator;

public class PlaceItemComparator implements Comparator<PlaceItem> {

	public int compare(PlaceItem item1, PlaceItem item2) {
		return item1.getDistance().compareTo(item2.getDistance());
	}

}