package com.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.board.model.BoardColumnEntity;
import com.board.persistence.dao.BoardColumnDAO;

public class BoardColumnQueryService {

	private Connection connection;
	
	public BoardColumnQueryService(Connection connection) {

		this.connection = connection;
	}

	public Optional<BoardColumnEntity> findById(Long id) throws SQLException {

		var dao= new BoardColumnDAO(connection);
		return dao.findById(id);
	}


}
