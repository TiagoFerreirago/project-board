package com.board.service;

import java.sql.Connection;
import java.sql.SQLException;

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
	
}
