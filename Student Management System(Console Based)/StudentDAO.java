import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentDAO
{
    public static void insertStudent(Student s)
    {
        try
        {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO Student(Name, Age, Course) VALUES(?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, s.getName());
            ps.setInt(2, s.getAge());
            ps.setString(3, s.getCourse());

            ps.executeUpdate();

            System.out.println("Student Inserted Successfully!");

            con.close();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void displayStudents()
    {
        try
        {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM Student";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("\nID\tName\tAge\tCourse");

            while(rs.next())
            {
                System.out.println(
                rs.getInt("ID") + "\t" +
                rs.getString("Name") + "\t" +
                rs.getInt("Age") + "\t" +
                rs.getString("Course"));
            }

            con.close();
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void updateStudent(int id, String course)
    {
        try
        {
            Connection con = DBConnection.getConnection();

            String sql = "UPDATE Student SET Course = ? WHERE ID = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, course);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0)
            {
                System.out.println("Student Updated Successfully!");
            }
            else
            {
                System.out.println("Student Not Found!");
            }

            con.close();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteStudent(int id)
    {
        try
        {
            Connection con = DBConnection.getConnection();

            String query = "DELETE FROM STUDENT WHERE ID = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if(rows > 0)
            {
                System.out.println("Student Deleted Successfully!");
            }
            else
            {
                System.out.println("Student not found!");
            }

            con.close();
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
