package com.board.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeExclude;
import org.apache.commons.lang3.builder.ToStringExclude;

public class BoardEntity {

	 private Long id;
	 private String name;
	 @ToStringExclude
	 @HashCodeExclude
	 @EqualsExclude
	 private List<BoardColumnEntity> boardColumns = new ArrayList<BoardColumnEntity>();
	 
	 

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<BoardColumnEntity> getBoardColumns() {
		return boardColumns;
	}
	public void setBoardColumns(List<BoardColumnEntity> boardColumns) {
		this.boardColumns = boardColumns;
	}
	 
	 
}
