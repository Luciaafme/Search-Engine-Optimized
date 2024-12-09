package control.metadata;

import control.interfaces.MetadataStoreManager;
import model.Metadata;

import java.io.File;
import java.sql.*;

public class MetadataStoreSqlite implements MetadataStoreManager {

	private final String databaseUrl;

	public MetadataStoreSqlite(String generaldatamartDirectory) {

		String metadataDatamartDirectory = createMetadataDatamartDirectory(generaldatamartDirectory);

		this.databaseUrl = "jdbc:sqlite:" + metadataDatamartDirectory + File.separator + "metadata.db";

		createDatabase();
		createTable();
	}

	private String createMetadataDatamartDirectory(String generalDatamartDirectory) {
		File metadataDatamartDirectory = new File(generalDatamartDirectory, "metadata");
		if (!metadataDatamartDirectory.exists()) {
			metadataDatamartDirectory.mkdirs();
		}
		return metadataDatamartDirectory.toString();
	}

	private void createDatabase() {
		try (Connection conn = DriverManager.getConnection(databaseUrl)) {
			if (conn != null) {
				System.out.println("Database created or successfully connected at: " + databaseUrl);
			}
		} catch (SQLException e) {
			System.err.println("Error creating the database: " + e.getMessage());
		}
	}

	private void createTable() {
		String sql = """
				CREATE TABLE IF NOT EXISTS Metadata (
				    bookID TEXT PRIMARY KEY,
				    name TEXT NOT NULL,
				    author TEXT NOT NULL,
				    year INTEGER NOT NULL,
				    language TEXT NOT NULL,
				    downloadLink TEXT NOT NULL
				);
				""";

		try (Connection conn = DriverManager.getConnection(databaseUrl);
			 Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			System.err.println("Error creating the table: " + e.getMessage());
		}
	}

	@Override
	public void update(Metadata metadata) {
		String sql = """
				INSERT INTO Metadata (bookID, name, author, year, language, downloadLink)
				VALUES (?, ?, ?, ?, ?, ?)
				ON CONFLICT(bookID) DO NOTHING;
				""";

		try (Connection conn = DriverManager.getConnection(databaseUrl);
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			int i = 1;
			for (String data : metadata.toList()) {
				pstmt.setString(i, data);
				i++;
			}
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error saving the book: " + e.getMessage());
		}
	}
}
