package com.board.persistence.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.board.converter.OffsetDateTimeConverter;
import com.board.dto.CardDetailsDTO;

public class CardDAO {

	
	private Connection connection;

	public CardDAO(Connection connection) {
		this.connection = connection;
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
	                       COUNT(SELECT sub_b.id
	                               FROM BLOCKS sub_b
	                              WHERE sub_b.card_id = c.id) blocks_amount
	                  FROM CARDS c
	                  LEFT JOIN BLOCKS b
	                    ON c.id = b.card_id
	                   AND b.unblocked_at IS NULL
	                  INNER BOARDS_COLUMNS bc
	                     ON bc.id = c.board_column_id
	                  WHERE id = ?;
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
	                        resultSet.getString("b.block_reason").isEmpty(),
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
	}
