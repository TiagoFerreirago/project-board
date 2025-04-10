package com.board.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class BlockEntity {

	 private Long id;
	 private OffsetDateTime blockedAt;
	 private String blockReason;
	 private OffsetDateTime unblockedAt;
	 private String unblockReason;
	 private List<BoardColumnEntity> boardColumns = new ArrayList<BoardColumnEntity>();
	 
	 
	
	 public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public OffsetDateTime getBlockedAt() {
		return blockedAt;
	}
	public void setBlockedAt(OffsetDateTime blockedAt) {
		this.blockedAt = blockedAt;
	}
	public String getBlockReason() {
		return blockReason;
	}
	public void setBlockReason(String blockReason) {
		this.blockReason = blockReason;
	}
	public OffsetDateTime getUnblockedAt() {
		return unblockedAt;
	}
	public void setUnblockedAt(OffsetDateTime unblockedAt) {
		this.unblockedAt = unblockedAt;
	}
	public String getUnblockReason() {
		return unblockReason;
	}
	public void setUnblockReason(String unblockReason) {
		this.unblockReason = unblockReason;
	}
	public List<BoardColumnEntity> getBoardColumns() {
		return boardColumns;
	}
	public void setBoardColumns(List<BoardColumnEntity> boardColumns) {
		this.boardColumns = boardColumns;
	}
	 
	 
}
