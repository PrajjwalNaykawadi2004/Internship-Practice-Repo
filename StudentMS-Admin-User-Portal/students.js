async function loadStudents(){

    try{

        const response = await fetch("/students");

        const students = await response.json();

        displayStudents(students);


        document.getElementById("searchStudent")
        .addEventListener("keyup",function(){

            const value = this.value.toLowerCase();


            const filtered = students.filter(student =>
                student.name.toLowerCase().includes(value)
            );


            displayStudents(filtered);

        });


    }
    catch(error){

        console.log(error);

        alert("Unable to load students");

    }

}



function displayStudents(students){


    let rows="";


    students.forEach(student=>{


        rows += `

        <tr>

            <td>${student.id}</td>

            <td>${student.name}</td>

            <td>${student.email}</td>

            <td>${student.department}</td>

            <td>${student.course}</td>

            <td>${student.city}</td>

            <td>${student.age}</td>


            <td>


            <button class="edit-btn"
            onclick="editStudent(${student.id})">

            <i class="fa-solid fa-pen"></i>

            </button>



            <button class="delete-btn"
            onclick="deleteStudent(${student.id})">

            <i class="fa-solid fa-trash"></i>

            </button>


            </td>


        </tr>

        `;


    });



    document.getElementById("studentTable").innerHTML = rows;


}




function editStudent(id){

    alert("Edit Student ID : "+id);

}




async function deleteStudent(id){


    if(confirm("Are you sure you want to delete this student?")){


        const response = await fetch("/students/"+id,{

            method:"DELETE"

        });



        if(response.ok){


            alert("Student Deleted Successfully");

            loadStudents();


        }
        else{


            alert("Delete Failed");


        }


    }


}




function logout(){

    window.location.href="Login.html";

}

loadStudents();