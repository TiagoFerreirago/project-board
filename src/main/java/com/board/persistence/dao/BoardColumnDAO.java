package com.board.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.board.model.BoardColumnEntity;
import com.mysql.cj.jdbc.StatementImpl;

public class BoardColumnDAO {
	
	private  Connection connection;
	
	public BoardColumnDAO(Connection connection) {
		this.connection = connection;
	}
	
	public BoardColumnEntity insert(BoardColumnEntity entity) throws SQLException{
		
		 var sql = "INSERT INTO BOARDS_COLUMNS (name, `order`, kind, board_id) VALUES (?, ?, ?, ?);";
		 
		 try(PreparedStatement statement = connection.prepareStatement(sql)){
			 statement.setString(1, entity.getName());
			 statement.setInt(2, entity.getOrder());
			 statement.setString(3, entity.getKind().name());
			 statement.setLong(4, entity.getBoard().getId());
			 statement.executeUpdate();
			 
			 if(statement instanceof StatementImpl imp) {
				 entity.setId(imp.getLastInsertID());
			 }
			 return entity;
		 }
	}

	public List<BoardColumnEntity> findByBoardId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}



	
}
