async function changePassword() {

    const currentPassword = document.getElementById("currentPassword").value.trim();
    const newPassword = document.getElementById("newPassword").value.trim();
    const confirmPassword = document.getElementById("confirmPassword").value.trim();

    if (currentPassword === "" || newPassword === "" || confirmPassword === "") {
        alert("Please fill all fields");
        return;
    }

    const token = localStorage.getItem("token");

    try {

        const response = await fetch("http://localhost:8082/auth/change-password", {

            method: "PUT",

            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },

            body: JSON.stringify({
                currentPassword: currentPassword,
                newPassword: newPassword,
                confirmPassword: confirmPassword
            })

        });

        const result = await response.text();

        if (response.ok) {

            alert("Password Changed Successfully");

            document.getElementById("currentPassword").value = "";
            document.getElementById("newPassword").value = "";
            document.getElementById("confirmPassword").value = "";

            window.location.href = "Profile.html";

        } else {

            alert(result);

        }

    } catch (error) {

        console.error(error);
        alert("Server Error");

    }

}