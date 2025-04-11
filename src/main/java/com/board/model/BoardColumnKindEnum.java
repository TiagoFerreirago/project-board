package com.board.model;

import java.util.stream.Stream;

public enum BoardColumnKindEnum {

	INITIAL, FINAL, CANCEL, PENDING;
	
	public static BoardColumnKindEnum findByName(String name) {
		return Stream.of(BoardColumnKindEnum.values()).filter( p -> p.name().equals(name))
				.findFirst().orElseThrow();
		
	}
}
