document.addEventListener("DOMContentLoaded", function () {

    const token = localStorage.getItem("token");
    const username = localStorage.getItem("username");

    if (!token || !username) {

        alert("Please Login First");

        window.location.href = "Login.html";

        return;
    }

    loadProfile();

});

async function loadProfile() {

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

            alert("Session Expired! Please Login Again.");

            localStorage.removeItem("token");
            localStorage.removeItem("refreshToken");
            localStorage.removeItem("role");
            localStorage.removeItem("username");

            window.location.href = "Login.html";

            return;
        }

        const users = await response.json();

        const userData = users.find(
            user => user.username === username
        );

        if (!userData) {

            alert("User Profile Not Found");

            return;
        }

        document.getElementById("username").innerText =
            "Welcome " + userData.username;

        document.getElementById("profileUsername").innerText =
            userData.username || "";

        document.getElementById("profileEmail").innerText =
            userData.email || "";

        document.getElementById("profileContact").innerText =
            userData.contact || "";

        document.getElementById("profileAddress").innerText =
            userData.address || "";

        document.getElementById("profileRole").innerText =
            userData.role
                ? userData.role.roleName
                : "";

        document.getElementById("profileStatus").innerText =
            userData.status || "";

        document.getElementById("profileCreatedDate").innerText =
            userData.createdDate
                ? userData.createdDate.split("T")[0]
                : "";

        const profileImage = document.getElementById("profileImage");

        if(userData.profilePhoto)
        {
            profileImage.src = "http://localhost:8082/uploads/" + userData.profilePhoto;
        }
        else
        {
            profileImage.src = "/images/default-user.png";
        }
    }
    catch (error) {

        console.error(error);

        alert("Unable to load profile details.");

    }

}

function logout() {

    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("role");
    localStorage.removeItem("username");

    alert("Logout Successful");

    window.location.href = "Login.html";
}

async function uploadProfilePhoto()
{
    const fileInput = document.getElementById("profilePhotoFile");

    if(fileInput.files.length === 0)
    {
        alert("Please Select Photo");
        return;
    }

    const username = localStorage.getItem("username");
    const token = localStorage.getItem("token");

    const response = await fetch("http://localhost:8082/auth/users");

    const users = await response.json();

    const currentUser = users.find(u => u.username === username);

    if(!currentUser)
    {
        alert("User Not Found");
        return;
    }

    const formData = new FormData();

    formData.append("file", fileInput.files[0]);

    const uploadResponse = await fetch(
        "http://localhost:8082/auth/uploadPhoto/" + currentUser.id,
        {
            method:"POST",
            headers:{
                "Authorization":"Bearer " + token
            },
            body:formData
        });

    const result = await uploadResponse.text();

    alert(result);

    fileInput.value = "";

    document.getElementById("fileName").innerText = "";

    loadProfile();
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

            loadProfile();

        } else {

            alert(result || "Profile update failed");

        }

    } catch (error) {

        console.error(error);
        alert("Server Error");

    }
}

document.getElementById("profilePhotoFile").addEventListener("change", function () {

    if (this.files.length > 0) {

        document.getElementById("fileName").innerText =
            this.files[0].name;

    }
    else {

        document.getElementById("fileName").innerText = "";

    }

});