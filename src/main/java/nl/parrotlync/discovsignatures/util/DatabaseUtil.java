package nl.parrotlync.discovsignatures.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseUtil {
    private final String host, database, username, password;
    private Connection connection;

    public DatabaseUtil(String host, String username, String password, String database) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    public void connect() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) { return; }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) { return; }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s?autoReconnect=true&useSSL=false", host, 3306, database), username, password);
        }
    }

    public List<String> getSignatures(UUID uuid) throws SQLException, ClassNotFoundException {
        connect();
        List<String> signatures = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM dsignatures WHERE owner = ?");
        statement.setString(1, uuid.toString());
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            signatures.add(result.getString("signature"));
        }
        return signatures;
    }

    public void addSignature(UUID uuid, String signature) throws SQLException, ClassNotFoundException {
        connect();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO dsignatures (owner, signature) VALUES (?, ?)");
        statement.setString(1, uuid.toString());
        statement.setString(2, signature);
        statement.execute();
    }

    public void removeSignature(UUID uuid, String signature) throws SQLException, ClassNotFoundException {
        connect();
        PreparedStatement statement = connection.prepareStatement("DELETE FROM dsignatures WHERE owner = ? AND signature = ?");
        statement.setString(1, uuid.toString());
        statement.setString(2, signature);
        statement.execute();
    }
}
