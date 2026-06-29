import java.util.ArrayList;

public class Main
{
    public static void main(String[] args)
    {
        ArrayList<Student> students = new ArrayList<>();
    
        // Insert Students
        students.add(new Student("Rahul", 22, "Python"));
        students.add(new Student("Priya", 20, "Java"));
        students.add(new Student("Rahul", 22, "Python"));       // Duplicate

        // Insert into File
        for(Student s : students)
        {
            FileStudentDAO.insertStudent(s);
        }

        // Update Rahul's Course

        FileStudentDAO.updateStudent("Rahul","Java Full Stack");

        // Delete

        FileStudentDAO.deleteStudent("Priya");

        // Display Students

        System.out.println("\n====== Student Records =====");

        FileStudentDAO.displayStudents();
    }
}