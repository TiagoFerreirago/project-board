package com.board.model;

public record BoardColumnInfoDTO(Long id,
		int order,
		BoardColumnKindEnum kind) {

}
