package org.ssglobal.revalida.codes.comparator;

import java.sql.Timestamp;
import java.util.Comparator;

import org.ssglobal.revalida.codes.dto.PostsDTO;

public class TimestampComparator implements Comparator<PostsDTO> {

	@Override
	public int compare(final PostsDTO p1, final PostsDTO p2) {

		if (p1.getTimestamp() == null && p2.getTimestamp() == null) {
			return 0;
		} else if (p1.getTimestamp() == null) {
			return 1;
		} else if (p2.getTimestamp() == null) {
			return -1;
		} else {
			Timestamp ts1 = Timestamp.valueOf(p1.getTimestamp());
			Timestamp ts2 = Timestamp.valueOf(p2.getTimestamp());
			return ts2.compareTo(ts1);
		}
	}
}
