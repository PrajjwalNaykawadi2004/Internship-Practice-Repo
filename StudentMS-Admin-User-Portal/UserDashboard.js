const token = localStorage.getItem("token");
const role = localStorage.getItem("role");

if (!token) {
    alert("Please Login First");
    window.location.href = "Login.html";
}

if (role !== "USER") {
    alert("Access Denied");
    window.location.href = "AdminDashboard.html";
}

if (!localStorage.getItem("token")) {
    alert("Please Login First");
    window.location.href = "Login.html";
}

document.addEventListener("DOMContentLoaded", function () {

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Please Login First");
        window.location.href = "Login.html";
        return;
    }

    loadUser();

});

async function loadUser() {

    try {

        const token = localStorage.getItem("token");

        const response = await fetch("http://localhost:8082/auth/users", {

            method: "GET",

            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }

        });

        if (!response.ok) {

            alert("Session Expired! Please Login Again.");

            localStorage.removeItem("token");
            localStorage.removeItem("role");
            localStorage.removeItem("username");

            window.location.href = "Login.html";
            return;
        }

        const users = await response.json();

        const username = localStorage.getItem("username");

        document.getElementById("username").innerText =
                "Welcome " + (username || "User");

        let userData = users.find(user => user.username === username);

        if (userData) {

            document.getElementById("profileName").innerText =
                    userData.username;

            if(userData.profilePhoto)
            {
                document.getElementById("dashboardProfileImage").src =
                    "http://localhost:8082/uploads/" + userData.profilePhoto;
            }
            else
            {
                document.getElementById("dashboardProfileImage").src =
                    "images/default-user.png";
            }

            document.getElementById("userTable").innerHTML = `
                <tr>
                    <td>${userData.username}</td>
                    <td>${userData.role.roleName}</td>
                    <td>${userData.status}</td>
                    <td>${userData.createdDate ? userData.createdDate.split("T")[0] : ""}</td>
                </tr>
            `;
        }

    } catch (error) {

        console.log(error);
        alert("Unable to load user details");

    }
}

function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("username");

    alert("Logout Successful");

    window.location.href = "Login.html";
}