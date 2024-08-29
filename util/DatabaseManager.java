package mealplanner.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
	private static final String DB_URL = "jdbc:postgresql:meals_db";
	private static final String USER = "postgres";
	private static final String PASS = "1111";

	public DatabaseManager() {
		try {
			createDatabaseIfNotExists();
			try (Connection conn = getConnection();
			     Statement stmt = conn.createStatement()) {
				// Create tables if they don't exist
				stmt.execute("CREATE TABLE IF NOT EXISTS meals (" +
						"meal_id INTEGER PRIMARY KEY," +
						"category VARCHAR NOT NULL," +
						"meal VARCHAR NOT NULL)");

				stmt.execute("CREATE TABLE IF NOT EXISTS ingredients (" +
						"ingredient_id INTEGER PRIMARY KEY," +
						"meal_id INTEGER," +
						"ingredient VARCHAR NOT NULL," +
						"FOREIGN KEY (meal_id) REFERENCES meals(meal_id))");

				stmt.execute("CREATE TABLE IF NOT EXISTS plan (" +
						"id SERIAL PRIMARY KEY," +
						"day VARCHAR NOT NULL," +
						"category VARCHAR NOT NULL," +
						"meal_id INTEGER," +
						"FOREIGN KEY (meal_id) REFERENCES meals(meal_id))");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, USER, PASS);
	}

	private void createDatabaseIfNotExists() {
		try (Connection conn = DriverManager.getConnection("jdbc:postgresql:", USER, PASS);
		     Statement stmt = conn.createStatement()) {
			stmt.execute("CREATE DATABASE meals_db");
		} catch (SQLException e) {
			if (!"42P04".equals(e.getSQLState())) { // 42P04 - database already exists
				e.printStackTrace();
			}
		}
	}
}
