package com.board.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.board.dto.CardDetailsDTO;
import com.board.persistence.dao.CardDAO;

public class CardQueryService {

	private Connection connection;

	public CardQueryService(Connection connection) {
		this.connection = connection;
	}
	
	public Optional<CardDetailsDTO> findById(Long id) throws SQLException{
		CardDAO dao = new CardDAO(connection);
		return dao.findById(id);
	}
}
