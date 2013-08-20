package uk.co.revthefox.foxbot.database;

import uk.co.revthefox.foxbot.FoxBot;

import java.io.File;
import java.sql.*;

public class Database
{
    private FoxBot foxbot;

    Connection connection = null;

    public Database(FoxBot foxbot)
    {
        this.foxbot = foxbot;
    }

    public void connect()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");

            File path = new File("data");

            if (!path.exists())
            {
                path.mkdirs();
            }
            connection = DriverManager.getConnection("jdbc:sqlite:data/bot.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS tells (sender string, receiver string, text string, used boolean)");
        }
        catch(SQLException | ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
    }

    public void addTell(String sender, String receiver, String message)
    {
        try
        {
            PreparedStatement statement;
            connection.setAutoCommit(false);
            statement = connection.prepareStatement("INSERT INTO tells (sender, receiver, text, used) VALUES (?,?,?, 'false');");
            statement.setString(1, sender);
            statement.setString(2, receiver);
            statement.setString(3, message);
            statement.executeUpdate();
            connection.commit();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public void disconnect()
    {
        if (connection != null)
        {
            try
            {
                connection.close();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
            return;
        }
        System.out.println("Database is already disconnected!");
    }
}