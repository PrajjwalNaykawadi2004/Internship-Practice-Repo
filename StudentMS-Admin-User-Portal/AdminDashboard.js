const token = localStorage.getItem("token");
const role = localStorage.getItem("role");

if (!token) {
    alert("Please Login First");
    window.location.href = "Login.html";
}

if (role !== "ADMIN") {
    alert("Access Denied");
    window.location.href = "UserDashboard.html";
}

if (!localStorage.getItem("token")) {
    alert("Please Login First");
    window.location.href = "Login.html";
}

async function loadDashboard() {

    const token = localStorage.getItem("token");

    if (!token) {
        alert("Please login first!");
        window.location.href = "Login.html";
        return;
    }

    try {

        const response = await fetch("http://localhost:8082/auth/users", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            alert("Unauthorized! Please login again.");
            localStorage.removeItem("token");
            window.location.href = "Login.html";
            return;
        }

        const users = await response.json();

        const adminUsername = localStorage.getItem("username");

        const admin = users.find(user => user.username === adminUsername);

        if(admin){

            document.getElementById("adminName").innerText = "Welcome" + admin.username;

            document.getElementById("welcomeText").innerText =
                "Welcome " + admin.username + " 👋";

            if(admin.profilePhoto){

                document.getElementById("adminProfileImage").src =
                    "http://localhost:8082/uploads/" + admin.profilePhoto;

            }else{

                document.getElementById("adminProfileImage").src =
                    "images/default-user.png";

            }

        }

        const total = users.length;
        const active = users.filter(user => user.status === "Active").length;
        const inactive = total - active;

        // Cards
        document.getElementById("totalUsers").innerText = total;
        document.getElementById("activeUsers").innerText = active;
        document.getElementById("inactiveUsers").innerText = inactive;

        // Pie Chart
        new Chart(document.getElementById("pieChart"), {
            type: "pie",
            data: {
                labels: ["Active", "Inactive"],
                datasets: [{
                    data: [active, inactive],
                    backgroundColor: ["#10b981", "#ef4444"],
                    borderWidth: 2,
                    borderColor: "#ffffff"
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: "bottom"
                    }
                }
            }
        });

        // Bar Chart
        new Chart(document.getElementById("barChart"), {
            type: "bar",
            data: {
                labels: ["Total", "Active", "Inactive"],
                datasets: [{
                    label: "Users",
                    data: [total, active, inactive],
                    backgroundColor: ["#2563eb", "#10b981", "#f97316"],
                    borderRadius: 8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });

        // Recent Users
        let rows = "";

        users.slice(0, 5).forEach(user => {

            const badge =
                user.status === "Active"
                    ? "<span style='color:green;font-weight:bold;'>● Active</span>"
                    : "<span style='color:red;font-weight:bold;'>● Inactive</span>";

           rows += `
           <tr>
               <td>${user.username}</td>
               <td>${badge}</td>
               <td>${user.createdDate ? user.createdDate.split("T")[0] : ""}</td>
           </tr>
           `;
        });

        document.getElementById("recentTable").innerHTML = rows;

    } catch (error) {
        console.log(error);
        alert("Unable to load dashboard.");
    }
}

loadDashboard();

function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("username");

    alert("Logout Successful");

    window.location.href = "Login.html";
}
