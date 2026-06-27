import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Student> students = new ArrayList<>();

        students.add(new Student("Rahul", 22, "Python"));
        students.add(new Student("Priya", 20, "Java"));

        // Insert
        for (Student s : students) {
            StudentDAO.insertStudent(s);
        }

        // Display
        System.out.println("----- Students After Insert -----");
        StudentDAO.displayStudents();

        // Update
        StudentDAO.updateStudent(1, "Java Full Stack");

        System.out.println("\n----- After Update -----");
        StudentDAO.displayStudents();

        // Delete
        StudentDAO.deleteStudent(2);

        System.out.println("\n----- After Delete -----");
        StudentDAO.displayStudents();
    }
}