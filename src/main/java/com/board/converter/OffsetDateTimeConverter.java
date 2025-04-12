package com.board.converter;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public final class OffsetDateTimeConverter {

	public OffsetDateTimeConverter() {}
	
	public static OffsetDateTime toOffsetDateTime(Timestamp time) {
		return OffsetDateTime.ofInstant(time.toInstant(), ZoneOffset.UTC);
	}
}
