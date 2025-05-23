package com.board.ui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.board.dto.BoardColumnInfoDTO;
import com.board.model.BoardColumnEntity;
import com.board.model.BoardColumnKindEnum;
import com.board.model.BoardEntity;
import com.board.model.CardEntity;
import com.board.persistence.config.ConnectionConfig;
import com.board.service.BoardColumnQueryService;
import com.board.service.BoardQueryService;
import com.board.service.CardQueryService;
import com.board.service.CardService;

public class BoardMenu {
	
	private BoardEntity boardEntity;
	
	private Scanner scanner = new Scanner(System.in).useDelimiter("\n");

	public BoardMenu(BoardEntity boardEntity) {

		this.boardEntity = boardEntity;
	}

	public void execute() {
		
		try {
		System.out.printf("Bem vindo ao board %s, selecione a operação desejada\n", boardEntity.getId());
		
		int option = -1;
		
		while(option != 9) {
			 System.out.println("1 - Criar um card");
	         System.out.println("2 - Mover um card");
	         System.out.println("3 - Bloquear um card");
	         System.out.println("4 - Desbloquear um card");
	         System.out.println("5 - Cancelar um card");
	         System.out.println("6 - Ver board");
	         System.out.println("7 - Ver coluna com cards");
	         System.out.println("8 - Ver card");
	         System.out.println("9 - Voltar para o menu anterior um card");
	         System.out.println("10 - Sair");
	         option = scanner.nextInt();
	         
	         switch(option) {
	         
	         case 1 -> createCard();
             case 2 -> moveCardToNextColumn();
             case 3 -> blockCard();
             case 4 -> unblockCard();
             case 5 -> cancelCard();
             case 6 -> showBoard();
             case 7 -> showColumn();
             case 8 -> showCard();
             case 9 -> System.out.println("Voltando para o menu anterior");
             case 10 -> System.exit(0);
             default -> System.out.println("Opção inválida, informe uma opção do menu");
	         }
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
   }

	private void createCard() throws SQLException {
		
		CardEntity cardEntity = new CardEntity();
		 System.out.println("Informe o título do card");
		 cardEntity.setTitle(scanner.next());
	     System.out.println("Informe a descrição do card");
	     cardEntity.setDescription(scanner.next());
	     cardEntity.setBoardColumn(boardEntity.getInitialColumn());
	     try(Connection connection = ConnectionConfig.getConnection()){
	    	 new CardService(connection).create(cardEntity);
	     }
	}

	private void moveCardToNextColumn() throws SQLException {
		
		 System.out.println("Informe o id do card que deseja mover para a próxima coluna");
		 Long cardId = scanner.nextLong();
		 var boardColumnsInfo = boardEntity.getBoardColumns().stream()
				 .map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind())).toList();
		 try(var connection = ConnectionConfig.getConnection()){
			 new CardService(connection).moveToNextColumn(cardId, boardColumnsInfo);
		 }
		 catch(RuntimeException ex) {
			 System.out.println(ex.getMessage());
		 }
	}

	private void blockCard() throws SQLException {
	
		System.out.println("Informe o id do card que será bloqueado");
		Long cardId = scanner.nextLong();
		System.out.println("Informe o motivo do bloqueio do card");
		String reason = scanner.next();
		
		var boardColumnsInfo = boardEntity.getBoardColumns().stream()
				.map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
				.toList();
		try(var connection = ConnectionConfig.getConnection()){
			new CardService(connection).block(cardId, reason, boardColumnsInfo);
		}
		catch(RuntimeException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void unblockCard() throws SQLException {
	
		System.out.println("Informe o id do card que será desbloqueado");
        Long cardId = scanner.nextLong();
        System.out.println("Informe o motivo do desbloqueio do card");
		String reason = scanner.next();
		
		try(var connection = ConnectionConfig.getConnection()){
			new CardService(connection).unblock(cardId, reason);
		}
		catch(RuntimeException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void cancelCard() throws SQLException {
	
		System.out.println("Informe o id do card que deseja mover para a coluna de cancelamento");
		Long cardId = scanner.nextLong();
		BoardColumnEntity cancelColumn = boardEntity.getCancelColumn();
		var boardColumnsInfo = boardEntity.getBoardColumns().stream()
				.map(bc -> new BoardColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind())).toList();
		try(var connection = ConnectionConfig.getConnection()){
			new CardService(connection).cancel(cardId, cancelColumn.getId(), boardColumnsInfo);
		
		}
		catch(RuntimeException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void showBoard() throws SQLException {
		try(var connection = ConnectionConfig.getConnection()){
			var optional = new BoardQueryService(connection).showBoardDetails(boardEntity.getId());
			optional.ifPresent(b -> {
				 System.out.printf("Board [%s,%s]\n", b.id(), b.name());
				 b.columns().forEach( c -> 
				 System.out.printf("Coluna [%s] tipo: [%s] tem %s cards\n", c.name(), c.kind(), c.cardsAmout()));
			});
		}
		
	}

	private void showColumn() throws SQLException {
		
		var columnsIds = boardEntity.getBoardColumns().stream().map(BoardColumnEntity::getId).toList();
		
		Long selectedColumn = -1L;
		while(!columnsIds.contains(selectedColumn)) {
			System.out.printf("Escolha uma coluna do board %s\n", boardEntity.getName());
			boardEntity.getBoardColumns().forEach(c -> 
			System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getKind()));
			selectedColumn = scanner.nextLong();
		}
		try(var connection = ConnectionConfig.getConnection()){
			var column = new BoardColumnQueryService(connection).findById(selectedColumn);
			
			column.ifPresent(co -> {
				System.out.printf("Coluna %s tipo %s\n", co.getName(), co.getKind());
				
				co.getCards().forEach(ca -> System.out.printf("Card %s - %s\nDescrição: %s",
                        ca.getId(), ca.getTitle(), ca.getDescription()));
			});
			
		}
		
	}

	private void showCard() throws SQLException {

		 System.out.println("Informe o id do card que deseja visualizar");
		 Long selectedCardId = scanner.nextLong();
		 
		 try(Connection connection = ConnectionConfig.getConnection()){
			 new CardQueryService(connection).findById(selectedCardId).ifPresentOrElse( c -> {
                 System.out.printf("Card %s - %s.\n", c.id(), c.title());
                 System.out.printf("Descrição: %s\n", c.description());
                 System.out.println(c.blocked() ?
                         "Está bloqueado. Motivo: " + c.blockReason() :
                         "Não está bloqueado");
                 System.out.printf("Já foi bloqueado %s vezes\n", c.blocksAmount());
                 System.out.printf("Está no momento na coluna %s - %s\n", c.columnId(), c.columnName());}, 
					 () -> System.out.printf("Não existe um card com o id %s\n", selectedCardId));
		 }
	}
}
	
	


