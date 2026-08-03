async function login() {

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    if (username === "" || password === "") {
        alert("Please enter Username and Password");
        return;
    }

    try {

        const response = await fetch("http://localhost:8082/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        const result = await response.json();

        if (response.ok) {

            localStorage.setItem("token", result.token);
            localStorage.setItem("refreshToken", result.refreshToken);
            localStorage.setItem("role", result.role);
            localStorage.setItem("username",username);

            alert(result.role);
            alert("Login Successful");

            if (result.role === "ADMIN") {
                window.location.href = "AdminDashboard.html";
            } else {
                window.location.href = "UserDashboard.html";
            }

        } else {

            alert(result.message || "Invalid Username or Password");

        }

    } catch (error) {

        console.log(error);
        alert("Server Error");

    }
}

document.getElementById("loginForm").addEventListener("submit", function (e) {
    e.preventDefault();
    login();
});