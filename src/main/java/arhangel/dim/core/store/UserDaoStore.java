package arhangel.dim.core.store;

import arhangel.dim.core.User;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;


/**
 * Created by Алексей on 20.05.2016.
 */
public class UserDaoStore implements UserStore {
    private static Logger log = Logger.getLogger(UserDaoStore.class.getName());
    DaoFactory daoFactory =  DaoFactory.getInstance();

    @Override
    public User addUser(User user) {

        User newUser = null;
        try {
            log.info("Creating new customer with login=" + user.getName());
            log.trace("Opening connection");
            Connection conn = daoFactory.connect();

            log.trace("Creating prepared statement");
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "insert into Usertable (login, pwd) values(?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());

            int affectedRows;
            affectedRows = preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();

            newUser = new User(generatedKeys.getString("login"), generatedKeys.getString("pwd"));
            newUser.setId(generatedKeys.getLong(1));

            // Добавление нового пользователя в базовый чат
            preparedStatement = conn.prepareStatement(
                    "insert into user_chat (user_id, chat_id) values(?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, newUser.getId());
            preparedStatement.setLong(2, 1);

            affectedRows = preparedStatement.executeUpdate();

            generatedKeys.close();
            log.trace("result set closed");

            preparedStatement.close();
            log.trace("prepared statement closed");

            conn.close();
            log.trace("Connection closed");
        } catch (SQLException e) {
            e.getMessage();
        }
        return newUser;
    }

    @Override
    public User getUser(String login, String pass) {

        Connection connection =  daoFactory.connect();
        User userFound = null;

        try {
            connection = daoFactory.connect();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select id, login, pwd from Usertable where login = ? and pwd = ?");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, pass);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userFound = new User(resultSet.getString("login"), resultSet.getString("pwd"));
                userFound.setId(Long.parseLong(resultSet.getString("id")));
            } else {
                preparedStatement = connection.prepareStatement("select id, login, pwd from Usertable where login = ?");
                preparedStatement.setString(1, login);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return  userFound = new User(resultSet.getString("login"), null);
                } else {
                    return null;
                }
            }

            resultSet.close();

            preparedStatement.close();

            connection.close();
        } catch (SQLException e) {
            e.getMessage();
        }
        return userFound;
    }

    public User getUserById(Long id) {
        Connection conn =  daoFactory.connect();
        User userFound = null;

        try {
            conn = daoFactory.connect();

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select id, login, pwd from Usertable where id = ?");
            preparedStatement.setLong(1, id);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userFound = new User(resultSet.getString("login"), resultSet.getString("pwd"));
                userFound.setId(Long.parseLong(resultSet.getString("id")));
                return userFound;
            }

            resultSet.close();

            preparedStatement.close();

            conn.close();
        } catch (SQLException e) {
            e.getMessage();
        }
        return userFound;
    }

    public User updateUser(User user) {
        return null;
    }
}
