import java.io.*;
import java.util.ArrayList;

public class FileStudentDAO
{
    static String fileName = "students.txt";

    // Insert Student
    public static void insertStudent(Student s)
    {
        try
        {
            // Duplicate Check
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String line;

            while ((line = br.readLine()) != null)
            {
                String[] data = line.split(",");

                if(data[0].equalsIgnoreCase(s.getName()))
                {
                    System.out.println("Student Already Exists!");

                    br.close();
                    return;
                }
            }

            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));

            bw.write(s.getName() + "," + s.getAge() + "," + s.getCourse());

            bw.newLine();

            bw.close();

            System.out.println("Student Inserted Successfully!");
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // Display Students
    public static void displayStudents()
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String line;

            System.out.println("\nName\tAge\tCourse");

            while((line = br.readLine()) != null)
            {
                String[] data = line.split(",");

                System.out.println(data[0] + "\t" + data[1] + "\t" + data[2]);
            }

            br.close();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    } 

    // Update Student Course
    public static void updateStudent(String name, String newCourse)
    {
        try
        {
            File inputFile = new File(fileName);
            File tempFile = new File("temp.txt");

            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

            String line;
            
            while((line = br.readLine()) != null)
            {
                String[] data = line.split(",");

                if(data[0].equalsIgnoreCase(name))
                {
                    data[2] = newCourse;

                    System.out.println("Student Updated Successfully!");
                }

                bw.write(data[0] + "," + data[1] + "," + data[2]);
                bw.newLine();
            }

            br.close();
            bw.close();

            inputFile.delete();

            tempFile.renameTo(inputFile);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Delete Student
    public static void deleteStudent(String name)
    {
        try
        {
            File inputFile = new File(fileName);
            File tempFile = new File("temp.txt");

            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

            String line;

            while((line = br.readLine()) != null)
            {
                String[] data = line.split(",");

                if(data[0].equalsIgnoreCase(name))
                {
                    System.out.println("Student Deleted Successfully!");
                    continue;
                }

                bw.write(line);
                bw.newLine();
            }

            br.close();
            bw.close();

            inputFile.delete();

            tempFile.renameTo(inputFile);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}