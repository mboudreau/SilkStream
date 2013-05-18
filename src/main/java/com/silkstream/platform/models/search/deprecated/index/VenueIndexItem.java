/*
package com.tivity.platform.models.search.deprecated.index;

import com.tivity.platform.enums.EnvironmentType;
import com.tivity.platform.enums.IndexType;
import com.tivity.platform.enums.deprecated.ModelType;
import com.tivity.platform.models.db.deprecated.Venue;
import com.tivity.platform.models.db.deprecated.VenueSpace;

import java.math.BigDecimal;
import java.util.*;

public class VenueIndexItem implements Indexable {
	private Venue model;
	private List<VenueSpace> venueSpaces;
	private EnvironmentType env;
	private IndexType type;
    private ModelType modelType;


    public VenueIndexItem(Venue model, List<VenueSpace> venueSpaces, IndexType type, EnvironmentType env) {
		this.model = model;
		this.venueSpaces = venueSpaces;
		this.type = type;
		this.env = env;
        this.modelType = ModelType.VENUE;
	}

	public BigDecimal getLat() {
		return this.model.getLocation().getLat();
	}

	public BigDecimal getLon() {
		return this.model.getLocation().getLon();
	}

	public String getName() {
		return this.model.getName();
	}

	*/
/*public int getRating() {
		return this.model.getRating();
	}*//*


	public Set<String> getActivityTypes() {
		Set<String> types = new HashSet<String>();
		for(VenueSpace vs:this.venueSpaces) {
			types.addAll(vs.getActivityTypes());
		}
		return types;
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
        fields.put("acttypes",getActivityTypes().toArray(new String[0])) ;
        fields.put("env",env.toString().toLowerCase());
        if(model.getLocation().getNortheast()!=null && model.getLocation().getLon() !=null){
        fields.put("lat",(long) ((180 + model.getLocation().getNortheast().doubleValue()) * 100000));
        fields.put("lon",(long) ((360 + model.getLocation().getLon().doubleValue()) * 100000));
        }else {
            fields.put("lat",(long) ((180 + 10) * 100000));
            fields.put("lon",(long) ((360 + 10) * 100000));
        }
        fields.put("name",model.getName().toLowerCase());
        fields.put("type", getModelType().toString());
        request.setFields(fields);
        return request;
    }
}
*/
