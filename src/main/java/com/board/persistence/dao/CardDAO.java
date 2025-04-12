package com.board.persistence.dao;

import java.sql.Connection;

public class CardDAO {

	
	private Connection connection;

	public CardDAO(Connection connection) {
		this.connection = connection;
	}
	
	public CardDetailsDTO findById(Long id) {
		
		return null;
	}
	
	
}
