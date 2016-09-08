package com.cache;

import java.util.Comparator;
import java.util.Date;

public class CacheTimestampComperator implements Comparator<CacheTimestamp> {

	@Override
	public int compare(CacheTimestamp o1, CacheTimestamp o2) {

		Date TimestampA = o1.getTimestamp();
		Date TimestampB = o2.getTimestamp();

		if (TimestampA.before(TimestampB))
			return -1;
		if (TimestampA.after(TimestampB))
			return 1;
		return 0;
	}

}
