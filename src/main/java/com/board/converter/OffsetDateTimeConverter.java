package com.board.converter;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Objects;

public final class OffsetDateTimeConverter {

	public OffsetDateTimeConverter() {}
	
	public static OffsetDateTime toOffsetDateTime(Timestamp time) {
		return Objects.nonNull(time)?OffsetDateTime.ofInstant(time.toInstant(), ZoneOffset.UTC): null;
	}
}
