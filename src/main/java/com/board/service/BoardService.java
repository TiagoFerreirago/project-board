package com.board.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;

import com.board.model.BoardEntity;
import com.board.persistence.dao.BoardDAO;

public class BoardService {

	 
	 private  Connection connection;
	 
	 public BoardService(Connection connection) {
			this.connection = connection;
		}
	 
	 /*public BoardEntity insert(BoardEntity entity) throws SQLException {
	 
		 
	 
	 }*/
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
