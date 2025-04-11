package com.board.dto;

import com.board.model.BoardColumnKindEnum;

public record BoardColumnDTO(Long id,
		String name,
		BoardColumnKindEnum kind,
		int cardsAmout) {

}
