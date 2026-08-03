if (!localStorage.getItem("token")) {
    alert("Please Login First");
    window.location.href = "Login.html";
}

document.addEventListener("DOMContentLoaded", function () {

    loadAdminProfile();

});

async function loadAdminProfile() {

    try {

        const token = localStorage.getItem("token");
        const username = localStorage.getItem("username");

        const response = await fetch("http://localhost:8082/auth/users", {

            method: "GET",

            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }

        });

        if (!response.ok) {

            alert("Session Expired!");

            localStorage.clear();

            window.location.href = "Login.html";

            return;
        }

        const users = await response.json();

        const admin = users.find(user => user.username === username);

        if (!admin) {

            alert("Admin Not Found");

            return;
        }

        //document.getElementById("profileTitle").innerText = admin.username;
        document.getElementById("profileUsername").innerText = admin.username;
        document.getElementById("profileEmail").innerText = admin.email || "";
        document.getElementById("profileContact").innerText = admin.contact || "";
        document.getElementById("profileAddress").innerText = admin.address || "";
        document.getElementById("profileRole").innerText =
            admin.role ? admin.role.roleName : "";
        document.getElementById("profileStatus").innerText = admin.status || "";
        document.getElementById("profileCreatedDate").innerText =
            admin.createdDate ? admin.createdDate.split("T")[0] : "";

        document.getElementById("adminWelcome").innerText =
            "Welcome " + admin.username;

        if (admin.profilePhoto) {

            const photo =
                "http://localhost:8082/uploads/" + admin.profilePhoto;

            document.getElementById("profileImage").src = photo;
            document.getElementById("adminTopPhoto").src = photo;

        } else {

            document.getElementById("profileImage").src =
                "images/default-user.png";

            document.getElementById("adminTopPhoto").src =
                "images/default-user.png";
        }

    } catch (error) {

        console.error(error);

        alert(error);

    }

}

function logout() {

    localStorage.clear();

    window.location.href = "Login.html";

}

async function uploadProfilePhoto() {

    const fileInput = document.getElementById("profilePhotoFile");

    if (fileInput.files.length === 0) {
        alert("Please Select Photo");
        return;
    }

    const username = localStorage.getItem("username");
    const token = localStorage.getItem("token");

    const response = await fetch("http://localhost:8082/auth/users", {
        headers: {
            "Authorization": "Bearer " + token
        }
    });

    const users = await response.json();

    const admin = users.find(user => user.username === username);

    if (!admin) {
        alert("Admin Not Found");
        return;
    }

    const formData = new FormData();
    formData.append("file", fileInput.files[0]);

    const uploadResponse = await fetch(
        "http://localhost:8082/auth/uploadPhoto/" + admin.id,
        {
            method: "POST",
            headers: {
                "Authorization": "Bearer " + token
            },
            body: formData
        }
    );

    const result = await uploadResponse.text();

    alert(result);

    fileInput.value = "";

    const fileName = document.getElementById("fileName");
    if (fileName) {
        fileName.innerText = "";
    }

    loadAdminProfile();
}

function openUpdateProfile() {

    document.getElementById("updateEmail").value =
        document.getElementById("profileEmail").innerText;

    document.getElementById("updateContact").value =
        document.getElementById("profileContact").innerText;

    document.getElementById("updateAddress").value =
        document.getElementById("profileAddress").innerText;

    document.getElementById("updateProfileModal").style.display = "flex";
}

function closeUpdateProfile() {

    document.getElementById("updateProfileModal").style.display = "none";
}

async function saveProfile() {

    const username = localStorage.getItem("username");
    const token = localStorage.getItem("token");

    const email = document.getElementById("updateEmail").value.trim();
    const contact = document.getElementById("updateContact").value.trim();
    const address = document.getElementById("updateAddress").value.trim();

    if (email === "" || contact === "" || address === "") {
        alert("Please fill all fields");
        return;
    }

    try {

        const response = await fetch(
            "http://localhost:8082/auth/update-profile/" + username,
            {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + token
                },
                body: JSON.stringify({
                    email: email,
                    contact: contact,
                    address: address
                })
            }
        );

        const result = await response.text();

        if (response.ok) {

            alert("Profile Updated Successfully");

            closeUpdateProfile();

            loadAdminProfile();

        } else {

            alert(result);

        }

    } catch (error) {

        console.error(error);

        alert("Server Error");

    }
}

document.getElementById("profilePhotoFile").addEventListener("change", function () {

    const fileName = document.getElementById("fileName");

    if (!fileName) return;

    if (this.files.length > 0) {
        fileName.innerText = this.files[0].name;
    } else {
        fileName.innerText = "";
    }

});