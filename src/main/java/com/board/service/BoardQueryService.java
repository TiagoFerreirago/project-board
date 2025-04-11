package com.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.board.dto.BoardDetailsDTO;
import com.board.model.BoardEntity;
import com.board.persistence.dao.BoardColumnDAO;
import com.board.persistence.dao.BoardDAO;

public class BoardQueryService {

	
	private Connection connection;

	public BoardQueryService(Connection connection) {
		this.connection = connection;
	}
	
	public Optional<BoardEntity> findById(Long id) throws SQLException{
		
		BoardDAO dao = new BoardDAO(connection);
		BoardColumnDAO columnDao = new BoardColumnDAO(connection);
		Optional<BoardEntity>optional = dao.findById(id);
		if(optional.isPresent()) {
			BoardEntity entity = optional.get();
			entity.setBoardColumns(columnDao.findByBoardId(entity.getId()));
			return Optional.of(entity);
		}
		return Optional.empty();
		
		
	}
	
	public Optional<BoardDetailsDTO> showBoardDetails(Long id) throws SQLException{
		BoardDAO dao = new BoardDAO(connection);
		BoardColumnDAO columnDao = new BoardColumnDAO(connection);
		var optional = dao.findById(id);
		if(optional.isPresent()) {
			BoardEntity entity = optional.get();
			var columns = columnDao.findByBoardIdWithDetails(id);
			BoardDetailsDTO dto = new BoardDetailsDTO(entity.getId(), entity.getName(), columns);
			return Optional.of(dto);
		}
		return Optional.empty();
	}
	
	
}
