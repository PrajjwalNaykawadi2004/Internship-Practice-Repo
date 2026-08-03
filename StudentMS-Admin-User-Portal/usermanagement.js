async function loadUsers() {

    try {

        const response = await fetch("http://localhost:8082/auth/users", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token"),
                "Content-Type": "application/json"
            }
        });
        const users = await response.json();

        displayUsers(users);

        document.getElementById("searchUser")
            .addEventListener("keyup", function () {

                const value = this.value.toLowerCase();

                const filtered = users.filter(user =>
                    user.username.toLowerCase().includes(value)
                );

                displayUsers(filtered);

            });

    } catch (error) {

        console.error(error);
        alert("Unable to load users.");

    }

}

function displayUsers(users) {

    let rows = "";

    users.forEach(user => {

        const status =
            user.status === "Active"
                ? "<span class='status-active'>🟢 Active</span>"
                : "<span class='status-inactive'>🔴 Inactive</span>";

        rows += `

        <tr>

            <td>${user.id}</td>

            <td>${user.username}</td>

            <td>********</td>

            <td>${user.email || ""}</td>

            <td>${user.contact || ""}</td>

             <td>${user.address || ""}</td>

            <td>${status}</td>

            <td>${user.createdDate ? user.createdDate.split("T")[0] : ""}</td>

            <td>

                <button class="edit-btn"
                onclick="editUser(${user.id})">

                <i class="fa-solid fa-pen"></i>

                </button>

                <button class="delete-btn"
                onclick="deleteUser(${user.id})">

                <i class="fa-solid fa-trash"></i>

                </button>

            </td>

        </tr>

        `;

    });

    document.getElementById("userTable").innerHTML = rows;

}

async function editUser(id) {

    const username = prompt("Enter New Username");

    const password = prompt("Enter New Password");

    const contact = prompt("Enter Contact Number");

    const address = prompt("Enter Address");

    if (username == null || password == null || contact == null || address == null) {
        return;
    }

    const response = await fetch("http://localhost:8082/auth/update/" + id, {

        method: "PUT",

        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + localStorage.getItem("token")
        },

        body: JSON.stringify({
            username: username,
            password: password,
            contact: contact,
            address: address
        })

    });

    const result = await response.text();

    alert(result);

    loadUsers();
}

async function deleteUser(id){

    if(confirm("Are you sure you want to delete this user?")){

        const response = await fetch("http://localhost:8082/auth/delete/" + id, {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token"),
                "Content-Type": "application/json"
            }
        });
        if(response.ok){

            alert("User deleted successfully");
            loadUsers();

        }else{

            alert("Failed to delete user");

        }

    }

}

async function filterUsers() {

    const status = document.getElementById("statusFilter").value;

    let url = "http://localhost:8082/auth/users";

    if (status !== "") {
        url = "http://localhost:8082/auth/filter?status=" + status;
    }

    try {

        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + localStorage.getItem("token"),
                "Content-Type": "application/json"
            }
        });

        const users = await response.json();

        displayUsers(users);

    } catch (error) {

        console.log(error);
        alert("Unable to filter users.");

    }

}

function logout(){

    window.location.href="Login.html";

}

loadUsers();