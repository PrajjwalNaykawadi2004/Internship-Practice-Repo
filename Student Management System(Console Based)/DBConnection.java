import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection
{
    public static Connection getConnection()
    {
        try
        {
            String url = "jdbc:sqlserver://localhost:58319;databaseName=StudentDB;encrypt=true;trustServerCertificate=true";
            String user = "sa";
            String password = "1234";

            Connection con = DriverManager.getConnection(url, user, password);

            System.out.println("Database Connected Successfully!");
            return con;
        }

        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
