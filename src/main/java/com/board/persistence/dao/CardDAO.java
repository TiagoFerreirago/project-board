package com.board.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import com.board.converter.OffsetDateTimeConverter;
import com.board.dto.CardDetailsDTO;
import com.board.model.CardEntity;
import com.mysql.cj.jdbc.StatementImpl;

public class CardDAO {

	
	private Connection connection;

	public CardDAO(Connection connection) {
		this.connection = connection;
	}
	
	public CardEntity insert(CardEntity cardEntity) throws SQLException {
		String sql = "INSERT INTO CARDS (title,description,board_column_id) VALUES (?,?,?);";
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setString(1, cardEntity.getTitle());
			statement.setString(2, cardEntity.getDescription());
			statement.setLong(3, cardEntity.getId());
			statement.executeQuery();
			
			if(statement instanceof StatementImpl imp) {
				cardEntity.setId(imp.getLastInsertID());
			}
		}
		return cardEntity;
	}
	
	public Optional<CardDetailsDTO> findById(Long id) throws SQLException {
		
		 var sql =
	                """
	                SELECT c.id,
	                       c.title,
	                       c.description,
	                       b.blocked_at,
	                       b.block_reason,
	                       c.board_column_id,
	                       bc.name,
	                       (SELECT COUNT(sub_b.id)
	                               FROM BLOCKS sub_b
	                              WHERE sub_b.card_id = c.id) blocks_amount
	                  FROM CARDS c
	                  LEFT JOIN BLOCKS b
	                    ON c.id = b.card_id
	                   AND b.unblocked_at IS NULL
	                  INNER JOIN BOARDS_COLUMNS bc
	                     ON bc.id = c.board_column_id
	                  WHERE c.id = ?;
	                """;
	        try(var statement = connection.prepareStatement(sql)){
	            statement.setLong(1, id);
	            statement.executeQuery();
	            ResultSet resultSet = statement.getResultSet();
	            if (resultSet.next()){
	                 CardDetailsDTO dto = new CardDetailsDTO(
	                        resultSet.getLong("c.id"),
	                        resultSet.getString("c.title"),
	                        resultSet.getString("c.description"),
	                        Objects.nonNull(resultSet.getString("b.block_reason")),
	                        OffsetDateTimeConverter.toOffsetDateTime(resultSet.getTimestamp("b.blocked_at")),
	                        resultSet.getString("b.block_reason"),
	                        resultSet.getInt("blocks_amount"),
	                        resultSet.getLong("c.board_column_id"),
	                        resultSet.getString("bc.name"));
	                return Optional.of(dto);
	            }
	
	            	return Optional.empty();
	        }
	        
	        
		}
	
	public void moveToColumn(Long columnId, Long cardId) throws SQLException {
		String sql = "UPDATE CARDS SET board_column_id = ? WHERE id = ?;";
		try(PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setLong(1, columnId);
			statement.setLong(2, cardId);
			statement.executeUpdate();
		}
	}
	}
