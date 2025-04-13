package com.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.board.dto.BoardColumnInfoDTO;
import com.board.dto.CardDetailsDTO;
import com.board.exception.CardBlockedException;
import com.board.exception.CardFinishedException;
import com.board.exception.EntityNotFoundException;
import com.board.model.BoardColumnKindEnum;
import com.board.model.CardEntity;
import com.board.persistence.dao.CardDAO;

public class CardService {

	private Connection connection;

	public CardService(Connection connection) {
		this.connection = connection;
	}
	
	public CardEntity create(CardEntity cardEntity) throws SQLException {
		
		try{
			CardDAO dao = new CardDAO(connection);
			dao.insert(cardEntity);
			connection.commit();
			return cardEntity;
		}
		catch(SQLException ex) {
			connection.rollback();
			throw ex;
		}
	}
	
	public void moveToNextColumn(Long cardId, List<BoardColumnInfoDTO> boardColumns) throws SQLException {
		try {
			CardDAO dao = new CardDAO(connection);
			var optional = dao.findById(cardId);
			CardDetailsDTO dto = optional.orElseThrow(() -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId)));
			if(dto.blocked()) {
				String message = "O card %s está bloqueado, é necesário desbloquea-lo para mover".formatted(cardId);
				throw new CardBlockedException(message);
			}
			var correntColumn = boardColumns.stream().filter(
					bc -> bc.id().equals(dto.columnId()))
					.findFirst().orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board"));
			if(correntColumn.kind().equals(BoardColumnKindEnum.FINAL)) {
				throw new CardFinishedException("O card já foi finalizado");
			}
			var nextColumn = boardColumns.stream()
					.filter(bc -> bc.order() == correntColumn.order() + 1)
					.findFirst().orElseThrow();
			dao.moveToColumn(nextColumn.id(), cardId); 
			connection.commit();
		}
		catch(SQLException ex) {
			connection.rollback();
			throw ex;
		}

	}
	
}
