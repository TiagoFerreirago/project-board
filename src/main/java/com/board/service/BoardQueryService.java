package com.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

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
	
	
}
