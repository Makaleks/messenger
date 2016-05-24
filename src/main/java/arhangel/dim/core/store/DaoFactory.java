package arhangel.dim.core.store;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by Алексей on 18.05.2016.
 */

public class DaoFactory {
    private static DaoFactory daoFactory = new DaoFactory();

    Connection connection;

    public Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://178.62.140.149:5432/PhilSk",
                    "trackuser", "trackuser");
        } catch (Exception e) {
            e.getMessage();
        }
        return connection;
    }

    public void disconnect() {
        try {
            if (connection.isClosed()) {
                System.out.println("connection is already closed");
            } else {
                connection.close();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static DaoFactory getInstance() {
        return daoFactory;
    }

    public UserDaoStore getUserDao() {
        return new UserDaoStore();
    }

}