window.sendOTP = async function()
{
    const email = document.getElementById("email").value.trim();

    if(email === "")
    {
        alert("Please Enter Email");
        return;
    }

    const response = await fetch(
        "http://localhost:8082/auth/forgot-password",
        {
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            body: JSON.stringify({
                email: email
            })
        }
    );

    const result = await response.text();

    alert(result);

    document.getElementByID("otpSection").style.display = "block";
}

window.verifyOTP = async function()
{
    const email = document.getElementById("email").value.trim();

    const otp = document.getElementById("otp").value.trim();

    if(otp === "")
    {
        alert("Please Enter OTP");
        return;
    }


    const response = await fetch(
        "http://localhost:8082/auth/verify-otp",
        {
            method:"POST",

            headers:{
                "Content-Type":"application/json"
            },

            body: JSON.stringify({
                email: email,
                otp: otp
            })
        }
    );


    const result = await response.text();

    alert(result);


    if(response.ok)
    {
        document.getElementById("passwordSection").style.display = "block";
    }
}

window.resetPassword = async function()
{
    alert("Reset Password Clicked");

    const email = document.getElementById("email").value.trim();

    const otp = document.getElementById("otp").value.trim();

    const newPassword = document.getElementById("newPassword").value.trim();

    const confirmPassword = document.getElementById("confirmPassword").value.trim();


    if(newPassword === "" || confirmPassword === "")
    {
        alert("Please Enter Password");
        return;
    }


    const response = await fetch(
        "http://localhost:8082/auth/reset-password",
        {
            method:"POST",

            headers:{
                "Content-Type":"application/json"
            },

            body: JSON.stringify({
                email: email,
                otp: otp,
                newPassword: newPassword,
                confirmPassword: confirmPassword
            })
        }
    );


    const result = await response.text();

    alert(result);
}

