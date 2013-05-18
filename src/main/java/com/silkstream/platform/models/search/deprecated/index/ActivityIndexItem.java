/*
package com.tivity.platform.models.search.deprecated.index;

import com.tivity.platform.enums.EnvironmentType;
import com.tivity.platform.enums.IndexType;
import com.tivity.platform.enums.deprecated.ModelType;
import com.tivity.platform.models.db.deprecated.Activity;
import com.tivity.platform.models.db.deprecated.VenueSpace;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActivityIndexItem implements Indexable {
	private Activity model;
	private VenueSpace venueSpace;
	private EnvironmentType env;
	private IndexType type;
    private ModelType modelType;

	public ActivityIndexItem(Activity model, VenueSpace venueSpace, IndexType type, EnvironmentType env) {
		this.model = model;
		this.venueSpace = venueSpace;
		this.type = type;
		this.env = env;
        this.modelType = ModelType.ACTIVITY;
	}

	public BigDecimal getLat() {
		return this.venueSpace.getLocation().getLat();
	}

	public BigDecimal getLon() {
		return this.venueSpace.getLocation().getLon();
	}

	public String getName() {
		return this.model.getName();
	}

	*/
/*public int getRating() {
		return this.model.getRating();
	}*//*


	public Set<String> getActivityTypes() {
		return this.model.getTypes();
	}

	public EnvironmentType getEnv() {
		return env;
	}

	public IndexType getType() {
		return type;
	}

    public ModelType getModelType() {
        return modelType;
    }

    public IndexItemRequest getRequest() {
        IndexItemRequest request = new IndexItemRequest();
        request.setId(model.getValue());
        request.setType(type);
        Map<String,Object> fields = new HashMap<String,Object>();
        fields.put("acttypes",venueSpace.getActivityTypes().toArray(new String[0])) ;
        fields.put("env",env.toString().toLowerCase());
        fields.put("lat",(long) ((180 + venueSpace.getLocation().getNortheast().doubleValue()) * 100000));
        fields.put("lon",(long) ((360 + venueSpace.getLocation().getLon().doubleValue()) * 100000));
        fields.put("name",model.getName().toLowerCase());
        fields.put("type", getModelType().toString().toLowerCase());
        request.setFields(fields);
        return request;
    }
}
*/
