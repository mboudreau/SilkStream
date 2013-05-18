package com.silkstream.platform.models.dto.adapter;

import com.silkstream.platform.models.db.Place;
import com.silkstream.platform.models.db.Tivity;
import com.silkstream.platform.models.db.User;
import com.silkstream.platform.models.dto.PlaceItem;
import com.silkstream.platform.models.dto.TivityItem;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service("answerAdapter")
public class TivityAdapter {

	@Inject
	PlaceAdapter placeAdapter;
	@Inject
	UserAdapter userAdapter;

	public TivityItem buildCompleteTivityItem(Tivity answer, User user, Place location) {
		return this.buildCompleteTivityItem(answer, user, placeAdapter.buildCompletePlaceItem(location));
	}


	public TivityItem buildCompleteTivityItem(Tivity tivity, User user, PlaceItem placeItem) {
		TivityItem item = new TivityItem();

		if (user != null) {
			item.setUser(userAdapter.buildCompleteUserItem(user));
		}

		if (tivity != null) {
			item.setId(tivity.getId());
			item.setActivity(tivity.getActivity());
			item.setLocation(placeItem);
			item.setPromoted(tivity.getPromoted());
			item.setName(tivity.getName());
			item.setDescription(tivity.getDescription());
			item.setRating(tivity.getRating());
			item.setCreatedDate(tivity.getCreatedDate());
			item.setUpdateDate(tivity.getUpdatedDate());
			item.setStartTime(tivity.getStartTime());
			item.setEndTime(tivity.getEndTime());
			item.setMinimum(tivity.getMinimum());
			item.setMaximum(tivity.getMaximum());
			item.setCost(tivity.getCost());
			item.setRSVP(tivity.getRSVP());
			item.setFavoritedUserIds(tivity.getFavoritedUser());
			item.setActive(tivity.getMinimum() != null && tivity.getRSVP() != null && tivity.getRSVP().size() >= tivity.getMinimum());
			item.setEnabled(tivity.getEnabled());
			if (tivity.getFavoritedUser() != null) {
				item.setFavoriteCount(tivity.getFavoritedUser().size());
			} else
				item.setFavoriteCount(0);
		}
		return item;
	}

	public Tivity buildTivity(TivityItem item) {
		Tivity tivity = new Tivity();

		if (item != null) {
			tivity.setPromoted(item.isPromoted());
			tivity.setActivity(item.getActivity());
			tivity.setCreatedDate(item.getCreatedDate());
			tivity.setName(item.getName());
			tivity.setDescription(item.getDescription());
			tivity.setEndTime(item.getEndTime());
			tivity.setFavoritedUser(item.getFavoritedUserIds());
			tivity.setId(item.getId());
			tivity.setRSVP(item.getRSVP());
			tivity.setMinimum(item.getMinimum());
			tivity.setMaximum(item.getMaximum());
			tivity.setCost(item.getCost());
			if (item.getLocation() != null) {
				tivity.setLocationId(item.getLocation().getId());
			}
			tivity.setRating(item.getRating());
			tivity.setUpdatedDate(item.getUpdateDate());
			tivity.setStartTime(item.getStartTime());
			if (item.getUser() != null) {
				tivity.setUserId(item.getUser().getId());
			}
		}
		return tivity;
	}
}
