package com.board.ui;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.board.persistence.config.ConnectionConfig;

@Component
public class Main implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {

		ConnectionConfig.getConnection();
		new MainMenu().execute();
	}

}
