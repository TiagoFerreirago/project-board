package com.board.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.board.model.BoardColumnEntity;
import com.board.model.BoardColumnKindEnum;
import com.board.model.BoardEntity;
import com.board.persistence.config.ConnectionConfig;
import com.board.service.BoardQueryService;
import com.board.service.BoardService;

public class MainMenu{
	
	private Scanner scanner = new Scanner(System.in).useDelimiter("\n");

	public void execute() throws SQLException{
		System.out.println("Bem vindo ao gerenciador de boards, escolha a opção desejada");
        var option = -1;
        while (true){
            System.out.println("1 - Criar um novo board");
            System.out.println("2 - Selecionar um board existente");
            System.out.println("3 - Excluir um board");
            System.out.println("4 - Sair");
            option = scanner.nextInt();
            switch (option){
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção inválida, informe uma opção do menu");
            }
        }
    }
	
	private void deleteBoard() throws SQLException {
		System.out.println("Informe o id do board que será excluido");
		Long id = scanner.nextLong();
		try(var connection = ConnectionConfig.getConnection()){
			BoardService service = new BoardService(connection);
			if(service.delete(id)) {
				System.out.printf("O board %s foi excluido\n", id);
			}
			
			else {
				System.out.printf("Não foi encontrado um board com id %s\n", id);
			}
		}
	}

	private void createBoard() throws SQLException {
		BoardEntity entity = new BoardEntity();
		System.out.println("Informe o nome do seu board");
        entity.setName(scanner.next());
        System.out.println("Por padrão o board possui 3 colunas, deseja adicionar mais colunas? se sim informe quantas, senão digite 0");
        int addittionColumns = scanner.nextInt();
        
        List<BoardColumnEntity> columns = new ArrayList<>();
        
        System.out.println("Informe o nome da coluna inicial do board");
        
        String columnName = scanner.next();
        BoardColumnEntity initialColumn = createColumn(columnName, BoardColumnKindEnum.INITIAL,0);
        columns.add(initialColumn);
        
        for(int i = 0; i < addittionColumns; i++) {
        	  System.out.println("Informe o nome da coluna de tarefa pendente do board");
        	  String pendingColumnName = scanner.next();
        	  BoardColumnEntity pendingColumn = createColumn(pendingColumnName,BoardColumnKindEnum.PENDING,i + 1);
        	  columns.add(pendingColumn);
        }
        
        System.out.println("Informe o nome da coluna final");
        String finalColumnName = scanner.next();
        BoardColumnEntity finalColumn = createColumn(finalColumnName, BoardColumnKindEnum.FINAL, addittionColumns + 1);
        columns.add(finalColumn);
        
        System.out.println("Informe o nome da coluna de cancelamento do baord");
        String cancelColumnName = scanner.next();
        BoardColumnEntity cancelColumn = createColumn(cancelColumnName, BoardColumnKindEnum.CANCEL, addittionColumns + 2);
        columns.add(cancelColumn);
        
        entity.setBoardColumns(columns);
        
        try (var connection = ConnectionConfig.getConnection()){
        	BoardService boardService = new BoardService(connection);
        	boardService.insert(entity);
        }
        
	}

	private BoardColumnEntity createColumn(String columnName, BoardColumnKindEnum kind, int order) {

		BoardColumnEntity boardColumn  = new BoardColumnEntity();
		boardColumn.setName(columnName);
		boardColumn.setKind(kind);
		boardColumn.setOrder(order);
		return boardColumn;
	}

	private void selectBoard() throws SQLException {
		 System.out.println("Informe o id do board que deseja selecionar");
		 Long id = scanner.nextLong();
		 try(var connection = ConnectionConfig.getConnection()){
			 BoardQueryService queryService = new BoardQueryService(connection);
	         var optional = queryService.findById(id);
	         optional.ifPresentOrElse(b -> new BoardMenu(b).execute(),
	        		 () -> System.out.printf("Não foi encontrado um board com id %s\n", id));
		 }
	}


}
