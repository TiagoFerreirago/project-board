package com.board.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.board.dto.BoardColumnDTO;
import com.board.model.BoardColumnEntity;
import com.board.model.BoardColumnKindEnum;
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

	public List<BoardColumnEntity> findByBoardId(Long id) throws SQLException {
		List<BoardColumnEntity> entities = new ArrayList<BoardColumnEntity>();
		String sql = "SELECT id, name, `order`, kind FROM BOARDS_COLUMNS WHERE board_id = ? ORDER BY `order`";
		
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setLong(1, id);
			ResultSet resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				BoardColumnEntity entity = new BoardColumnEntity();
				entity.setId(resultSet.getLong("id"));
				entity.setName(resultSet.getString("name"));
				entity.setOrder(resultSet.getInt("order"));
				entity.setKind(BoardColumnKindEnum.findByName(resultSet.getString("kind")));
				entities.add(entity);
			}
		}
		return entities;
	}
	
	public List<BoardColumnDTO> findByBoardIdWithDetails(Long id) throws SQLException{
		List<BoardColumnDTO> columns = new ArrayList<BoardColumnDTO>();
		String sql =  """
                SELECT bc.id,
                bc.name,
                bc.kind,
                COUNT(SELECT c.id
                        FROM CARDS c
                       WHERE c.board_column_id = bc.id) cards_amount
           FROM BOARDS_COLUMNS bc
          WHERE board_id = ?
          ORDER BY `order`
         """;
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            
            while(resultSet.next()) {
            	BoardColumnDTO dto = new BoardColumnDTO(resultSet.getLong("bc.id"),
                        resultSet.getString("bc.name"),
                        BoardColumnKindEnum.findByName(resultSet.getString("bc.kind")),
                        resultSet.getInt("cards_amount"));
            	columns.add(dto);
            }
            return columns;
		}
	}



	
}
