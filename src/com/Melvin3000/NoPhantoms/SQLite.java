package com.Melvin3000.NoPhantoms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;

public class SQLite {

	private static Connection conn = null;

	public static void connect() {
		String database = "jdbc:sqlite:plugins/NoPhantoms/NoPhantoms.sqlite";
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(database);

			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("PRAGMA user_version;");
			int user_version = rs.getInt("user_version");
			rs.close();

			switch(user_version) {
			/* Database does not exist */
			case 0:
				NoPhantoms.instance.getLogger().info("Database not yet created. Creating ...");
				String create = "CREATE TABLE nophantoms "
					+ "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "uuid BLOB);"
					+ "CREATE INDEX idx_nophantoms_ ON nophantoms (uuid);"
					+ "PRAGMA user_version = 1;";

				st.executeUpdate(create);
				break;
			}
		} catch (Exception e) {
			NoPhantoms.instance.getLogger().info(e.getMessage());
		}
	}

	public static void close() {
		try {
			conn.close();
		} catch(Exception e) {
			NoPhantoms.instance.getLogger().info(e.getMessage());
		}
	}

	public static boolean containsUUID(UUID uuid) {
		boolean ret = false;
		String query = "SELECT * FROM nophantoms WHERE uuid = ?;";
		try {
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, uuid.toString());
			ResultSet rs = st.executeQuery();
			ret = rs.next();
			rs.close();
			st.close();
		} catch(Exception e) {
			NoPhantoms.instance.getLogger().info(e.getMessage());
		}
		return ret;
	}

	public static void insertUUID(UUID uuid) {
		String query = "INSERT INTO nophantoms (uuid) VALUES (?);";
		execute(uuid, query);
	}

	public static void deleteUUID(UUID uuid) {
		String query = "DELETE FROM nophantoms WHERE uuid = ?;";
		execute(uuid, query);
	}

	private static void execute(UUID uuid, String query) {
		try {
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, uuid.toString());
			st.executeUpdate();
			st.close();
		} catch (Exception e) {
			NoPhantoms.instance.getLogger().info(e.getMessage());
		}
	}
}
