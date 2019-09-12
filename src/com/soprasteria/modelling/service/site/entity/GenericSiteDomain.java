package com.soprasteria.modelling.service.site.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.soprasteria.modelling.framework.annotation.Column;
import com.soprasteria.modelling.framework.annotation.Entity;
import com.soprasteria.modelling.framework.entity.Location;
import com.soprasteria.modelling.framework.entity.domain.MongoAnnoDomain;
import com.soprasteria.modelling.framework.util.Tool;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Entity(name = "site")
public class GenericSiteDomain extends MongoAnnoDomain {
	public final static String STATUS_NORMAL		=	"N"; 
	public final static String STATUS_TEST			=	"T";
	public final static String STATUS_STAGING		=	"S";
	public final static String STATUS_IDLE			=	"I";
	public final static String STATUS_ERROR			=	"E";
	
	@Column
	private String name;							// the display name of the entity
	@Column (group = "device", isKey = true)
	private String device_ref;						// the foreign key to the meter / device
	@Column
	protected String status;					// the status. N for normal, E for error, I for idle
	@Column
	protected String type;
	@Column
	protected String remark;						// the message to describe the status of the entity
	@Column
	protected String icon;
	
	@Column (group = "geo")
	private String geo_address;
	@Column (group = "geo")
	private String geo_latlng;						// lat,lng
	@Column 
	private String t_latlng;	
	@Column (group = "geo")
	private String geo_elevation;
	@Column (group = "geo")
	private String geo_ref;							// the reference to indicate the geo location of the site, e.g. junction id, block id
	@Column(group="geo")
	private byte[] geo_mapimg;
	@Column
	private HashMap<String, Object> tag;			// the fields to be grouped or searched for
	@Column
	private HashMap<String, Object> optional;		// other fields
	@Column
	private HashMap<String, DBObject> datapoint;	// the data points
	
	private Location loc;
	
	public GenericSiteDomain(DBObject doc) throws Exception {
		super(doc);
		if(getOid() != null) setOid(getOid().toString()); // convert all siteid to be string format
		if(status == null) status = STATUS_NORMAL;
		if(tag == null) tag = new HashMap<>();
		if(optional == null) optional = new HashMap<>();
		if(datapoint == null) datapoint = new HashMap<>();
	}
	
	public GenericSiteDomain(String siteId, String deviceRef) {
		super();
//		try {
//			if(siteId != null)
//				setOid(new ObjectId(siteId));
//		} catch (Exception e) {
//			setOid(siteId);
//		}
		if(siteId != null) 
			setOid(siteId); 
		else if(getOid() != null)
			setOid(getOid().toString()); // convert all siteid to be string format
		if(deviceRef != null) setDeviceRef(deviceRef);
		status = STATUS_NORMAL;
		icon = "siteicons/defaultsite.png";
		tag= new HashMap<>();
		optional= new HashMap<>();
		datapoint= new HashMap<>();
	}
	
	public void setAddress(String address) {geo_address = address;}
	public String getAddress() {return geo_address;}
	public void setLatLng(String latlng) throws Exception {loc = new Location(latlng);geo_latlng = latlng;}
	public Location getLoc() throws Exception {if(loc == null && geo_latlng != null) return new Location(geo_latlng); return loc;}
	public String getIcon() {return icon;}
	public void setIcon(String icon){this.icon = icon;};
	public void setName(String name) {this.name = name;}
	public String getName() {return name;}
	public void setDeviceRef(String device_ref) {this.device_ref = device_ref;}
	public String getDeviceRef() {return device_ref;}
	public String getGeo_elevation() {return geo_elevation;}
	public void setGeo_elevation(String geo_elevation) {this.geo_elevation = geo_elevation;}
	public String getGeo_ref() {return geo_ref;}
	public void setGeo_ref(String geo_ref) {this.geo_ref = geo_ref;}
	
	public String getT_latlng() {
		return t_latlng;
	}

	public void setT_latlng(String t_latlng) {
		this.t_latlng = t_latlng;
	}

	public String getStatus() {return status;}
	public void setStatus(String status) {this.status = status;}
	public String getRemark() {return remark;}
	public void setMap(byte[] geo_mapimg) {this.geo_mapimg = geo_mapimg;}
	public byte[] getMap() {return this.geo_mapimg;}

	public void addTag(String tagName, String tagValue) {
		if(tag == null) tag = new HashMap<>();
		tag.put(tagName, Tool.convert2Printable(tagValue));
	}
	
	public void addTagfrom2(String tagName, String tagValue1, String tagValue2) {
		if(tag == null) tag = new HashMap<>();
		if(Tool.isEmpty(tagValue1)) {
			addTag(tagName, tagValue2);
		} else tag.put(tagName, tagValue1); 
	}
	
	public void addOptional(String optionalName, String optionalValue) {
		if(optional == null) optional = new HashMap<>();
		optional.put(optionalName, optionalValue);
	}
	
	public void addOptional(Map<String, Object> optional) {
		if(this.optional == null) this.optional = new HashMap<>();
		this.optional.putAll(optional);
	}
	
	public void addTags(Map<String, Object> tags) {
		if(this.tag == null) this.tag = new HashMap<>();
		this.tag.putAll(tags);;
	}
	
	public HashMap<String, Object> getTags() {
		return tag;
	}
	
	public Object getTag(String tagName) {
		return tag.get(tagName);
	}

	public HashMap<String, Object> getOptionals() {
		return optional;
	}
	
	public Object getOptional(String fieldname) {
		return optional.get(fieldname);
	}

	public DataPointDomain getDatapoint(String type) throws Exception {
		DBObject obj = datapoint.get(type);
		if(obj != null) return new DataPointDomain(obj);
		return null;
	}
	
	public List<DataPointDomain> getDatapoints() {
		List<DataPointDomain> result = new ArrayList<>();
		for (DBObject b : datapoint.values()) {
			try {
				result.add(new DataPointDomain(b));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public void addDatapoint(DataPointDomain dp) throws Exception {
		if(datapoint == null) datapoint = new HashMap<>();
		datapoint.put(dp.getType(), dp.toDBObject());
	}
	
	public boolean containKeyword(String keyword) {
		if(!Tool.isEmpty(keyword) && keyword.length()>1) {
			String searchcontent = getTags().toString()+"  "+getName()+"  "+getAddress();
			if(searchcontent.toLowerCase().contains(keyword.toLowerCase())) return true;
			else return false;
		} else return true;
	}
	
	@SuppressWarnings("rawtypes")
	public DBObject toSetStmt() throws Exception {
		updateLastMod();
		DBObject doc1 = new BasicDBObject();
		DBObject doc2 = new BasicDBObject();
		
		List<Field> fields = Tool.getFields(getClass());
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				field.setAccessible(true);
				Column column = field.getAnnotation(Column.class);
				Object value = field.get(this);
				if(!column.isNull() && value == null) throw new Exception ("Non-nullable field ("+field.getName()+") indicated");
				
				if(field.getName().equals("_id")) continue;
				if(field.getName().equals("created")) continue;
				if(value instanceof Map) {
					if (((Map)value).isEmpty()) continue;
				}
				if(value == null) continue;
				
				doc2.put(field.getName().toLowerCase(), value);
			}
		}

		doc1.put("$set", doc2);
		return doc1;
	}
}
