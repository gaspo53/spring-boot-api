package com.example.api.entities;

import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Simple key-value entity to store in MongoDB (String data).<br>
 * Used as a general storage for caching data.
 *
 */
@Document(collection="cache_custom_data")
public class CacheCustomData{

    @Id
    private String key;
    
    private Object value;
    
    @Indexed(expireAfterSeconds=0)
    private Date expiresAt;
    
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("CacheData [");
		if (this.key != null) {
			builder.append("key=");
			builder.append(this.key);
			builder.append(", ");
		}
		if (this.value != null) {
			builder.append("value=");
			builder.append(this.value);
			builder.append(", ");
		}
		if (this.getExpiresAt() != null) {
			builder.append("expiresAt=");
			builder.append(this.getExpiresAt());
			builder.append(", ");
		}
		builder.append("expireAfterSeconds=");
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Expires the item after received seconds.
	 * @param seconds
	 */
	public void expiresAfter(int seconds){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, seconds);
        Date expiry = calendar.getTime();
        this.expiresAt = expiry;
	}
	
    //Getters and setters
    public String getKey() {
        return this.key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public void setValue(Object value) {
        this.value = value;
    }

	public Date getExpiresAt() {
		return this.expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

}
