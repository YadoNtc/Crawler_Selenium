package db;

import java.sql.*;

public class ConnectionProvider {

    private static Connection connection;
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/crawlerjavaquiz";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            if (connection == null) {
                connection = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public void insert(String sql, Object... parameters) {
        PreparedStatement statement = null;
        try {
            if (connection == null) {
                connection = getConnection();
            }

            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParameter(statement, parameters);
            statement.executeUpdate();

            statement.getGeneratedKeys();

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void setParameter(PreparedStatement statement, Object[] parameters) {
        try {
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                int index = i + 1;
                if (parameter instanceof String) {
                    statement.setString(index, (String) parameter);
                } else if (parameter instanceof Integer) {
                    statement.setInt(index, (Integer) parameter);
                } else if (parameter == null) {
                    statement.setNull(index, Types.NVARCHAR);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
