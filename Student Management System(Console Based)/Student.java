public class Student
{
    private String name;
    private int Age;
    private String course;

    public Student(String name, int Age, String course)
    {
        this.name = name;
        this.Age = Age;
        this.course = course;
    }

    public String getName()
    {
        return name;
    }

    public int getAge()
    {
        return Age;
    }

    public String getCourse()
    {
        return course;
    }
}