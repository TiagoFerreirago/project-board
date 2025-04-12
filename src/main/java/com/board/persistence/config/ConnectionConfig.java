package com.board.persistence.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionConfig {
	
	public ConnectionConfig() {}

	public static Connection getConnection() throws SQLException {
		var url = "jdbc:mysql://localhost/databoard";
        var user = "root";
        var password = "91349808";
        var connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false);
        return connection;
	}
}
