package com.board.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import com.board.converter.OffsetDateTimeConverter;

public class BlockDAO {

	
	private Connection connection;

	public BlockDAO(Connection connection) {
		this.connection = connection;
	}
	
	public void block(String reason, Long cardId) throws SQLException {
		String sql = "INSERT INTO BLOCKS (blocked_at, block_reason, card_id) VALUES (?,?,?);";
		
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setTimestamp(1, OffsetDateTimeConverter.toTimeStamp(OffsetDateTime.now()));
			statement.setString(2, reason);
			statement.setLong(3, cardId);
			statement.executeUpdate();
			
		}
	}
	
	
}
