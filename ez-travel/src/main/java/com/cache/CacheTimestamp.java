package com.cache;

import java.util.Date;

public class CacheTimestamp implements Comparable<CacheTimestamp>{

	private String key;
	private Date timestamp;

	public CacheTimestamp() {
		super();

	}

	public CacheTimestamp(String key, Date timestamp) {
		super();
		this.key = key;
		this.timestamp = timestamp;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Timestamp [key=" + key + ", timestamp=" + timestamp + "]";
	}

	@Override
	public int compareTo(CacheTimestamp o) {
		
		Date TimestampB = o.getTimestamp();

		if (this.timestamp.before(TimestampB))
			return -1;
		if (this.timestamp.after(TimestampB))
			return 1;
		return 0;
	}

}
