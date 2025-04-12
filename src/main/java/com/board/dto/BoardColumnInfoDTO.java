package com.board.dto;

import com.board.model.BoardColumnKindEnum;

public record BoardColumnInfoDTO(Long id,
		int order,
		BoardColumnKindEnum kind) {

}
