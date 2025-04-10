package com.board.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.board.model.BoardEntity;
import com.mysql.cj.jdbc.StatementImpl;

public class BoardDAO {
	
	@Autowired
	private Connection connection;
	
	
	public BoardEntity insert(BoardEntity entity) throws SQLException {
		var sql = "INSERT INTO BOARDS (name) VALUES (?);";
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			
			statement.setString(1, entity.getName());
			statement.executeUpdate();
			if(statement instanceof StatementImpl imp) {
				entity.setId(imp.getLastInsertID());
			}
		}
		return entity;
	}
	
	public void delete(Long id) throws SQLException {
		var sql = "DELETE FROM BOARDS WHERE id = ?;";
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setLong(1, id);
			statement.executeUpdate();
		}
	}
	
	public Optional<BoardEntity> findById(Long id) throws SQLException{
		var sql = "SELECT id, name FROM BOARDS WHERE id = ?;";
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setLong(1, id);
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				BoardEntity board = new BoardEntity();
				board.setId(resultSet.getLong("id"));
				board.setName(resultSet.getString("name"));
				return Optional.of(board);
			}
			return Optional.empty();
		}
	}
	
	public boolean exists(Long id) throws SQLException {
		var sql = "SELECT 1 FROM BOARDS WHERE id = ?;";
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setLong(1, id);
			statement.executeQuery();
			return statement.getResultSet().next();
		}
	}

}
