package api.repositories;

import api.interfaces.IPlayerRepository;
import api.misc.Utilities;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import restmodels.RESTPlayer;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class PlayerRepository implements IPlayerRepository {

    private static final Logger log = LoggerFactory.getLogger(Utilities.class);

    private Properties propertiesFile;
    private Connection connection;
    private boolean loaded;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PlayerRepository(){
        FileInputStream input = null;

        try{
            input = new FileInputStream("database.prop");
            propertiesFile = new Properties();
            propertiesFile.load(input);
            loaded = true;
        }
        catch (Exception e) {
            log.error("Exception in property file!", e);
            loaded = false;
        }
    }

    public RESTPlayer getPlayerByUsername(String username) {
        if(!loaded)
            return null;

        try {
            DriverManager.registerDriver(new SQLServerDriver());
            connection = DriverManager.getConnection(propertiesFile.getProperty("url"), propertiesFile.getProperty("username"), propertiesFile.getProperty("password"));
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Players WHERE Username = ?");
            statement.setString(1, username);

            ResultSet sqlResult = statement.executeQuery();
            if (!sqlResult.isBeforeFirst()) {
                return null;
            }

            while (sqlResult.next()) {
                return new RESTPlayer(sqlResult.getInt("Id"), sqlResult.getString("Username"), sqlResult.getString("Password"));
            }

        } catch (SQLException e) {
            log.error("SQL Exception!", e);
        }
        return null;
    }

    public RESTPlayer signInPlayer(RESTPlayer player) {
        if(!loaded)
            return null;

        RESTPlayer storedPlayer = getPlayerByUsername(player.getUsername());
        if(storedPlayer == null || !Utilities.verifyPassword(player.getPassword(), storedPlayer.getPassword()) || !storedPlayer.getUsername().equals(player.getUsername())){
            return null;
        }

        return storedPlayer;
    }

    public RESTPlayer registerPlayer(RESTPlayer player) {
        if(!loaded)
            return null;

        try{
            DriverManager.registerDriver(new SQLServerDriver());
            connection = DriverManager.getConnection(propertiesFile.getProperty("url"), propertiesFile.getProperty("username"), propertiesFile.getProperty("password"));

            if(getPlayerByUsername(player.getUsername()) == null){

                PreparedStatement statement = connection.prepareStatement("INSERT INTO Players (Username, Password) VALUES (?, ?)");
                statement.setString(1, player.getUsername());
                statement.setString(2, Utilities.hashPassword(player.getPassword()));
                statement.executeUpdate();

                return getPlayerByUsername(player.getUsername());
            }

        } catch (SQLException e) {
            log.error("SQL Exception!", e);
        }

        return null;
    }
}
