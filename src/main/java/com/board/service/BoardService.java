package com.board.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.board.model.BoardEntity;
import com.board.persistence.dao.BoardColumnDAO;
import com.board.persistence.dao.BoardDAO;

public class BoardService {

	 
	 private  Connection connection;
	 
	 public BoardService(Connection connection) {
			this.connection = connection;
		}
	 
	 public BoardEntity insert(BoardEntity entity) throws SQLException {
	 
		 BoardDAO dao = new BoardDAO(connection);
		 BoardColumnDAO daoColumn = new BoardColumnDAO(connection);
		 
		 try {
			 dao.insert(entity);
			 var columns = entity.getBoardColumns().stream().map(c -> {
				
				 c.setBoard(entity);
				 return c;
			 }).toList();
			 
			 for (var column : columns) {
				 daoColumn.insert(column);
			 }
			 connection.commit();
		 }
		 catch(SQLException ex) {
			 connection.rollback();
			 throw ex;
		 }
		 return entity;
	 
	 }
	 public boolean delete(Long id) throws SQLException {
		 
		 BoardDAO dao = new BoardDAO(connection);
		 try {
			 if(!dao.exists(id)) {
				 return false;
			 }
			 dao.delete(id);
			 connection.commit();
			 return true;
		 }
		 catch(SQLException e) {
			 connection.rollback();
			 throw e;
		 }
	 }
	
}
