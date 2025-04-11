package com.board.persistence.dao;

import java.time.OffsetDateTime;

public record CardDetailsDTO(Long id,
		boolean blocked,
		OffsetDateTime blockedAt,
		String blockReason,
		int blocksAmount,
		Long columnId,
		String columnName) {

}
