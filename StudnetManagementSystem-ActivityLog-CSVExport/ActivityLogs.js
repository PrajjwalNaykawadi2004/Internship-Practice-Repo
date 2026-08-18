// ================= AUTH CHECK =================

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


// ================= PAGINATION =================

let currentPage = 0;
const pageSize = 10;
let totalPages = 0;


// ================= LOAD ACTIVITY LOGS =================

async function loadLogs(page = 0) {

    const username =
        document.getElementById("usernameSearch").value.trim();

    const action =
        document.getElementById("actionFilter").value;

    let url =
        "http://localhost:8082/activity-logs" +
        "?page=" + page +
        "&size=" + pageSize;

    if (username !== "") {
        url += "&username=" +
            encodeURIComponent(username);
    }

    if (action !== "") {
        url += "&action=" +
            encodeURIComponent(action);
    }

    try {

        const response = await fetch(url, {

            method: "GET",

            headers: {
                "Authorization": "Bearer " + token,
                "Content-Type": "application/json"
            }

        });


        if (!response.ok) {

            if (response.status === 403 ||
                response.status === 401) {

                alert("Access Denied / Session Expired");

                localStorage.removeItem("token");

                window.location.href = "Login.html";

                return;
            }

            throw new Error("Failed to load activity logs");
        }


        const data = await response.json();


        // Save pagination information

        currentPage = data.number;

        totalPages = data.totalPages;


        // Display logs

        displayLogs(data.content);


        // Update pagination

        updatePagination(data);


    }
    catch (error) {

        console.error("Activity Log Error:", error);

        document.getElementById("activityTable").innerHTML = `
            <tr>
                <td colspan="8">
                    Unable to load activity logs.
                </td>
            </tr>
        `;

    }

}


// ================= DISPLAY LOGS =================

function displayLogs(logs) {

    let rows = "";


    if (!logs || logs.length === 0) {

        rows = `
            <tr>
                <td colspan="8">
                    No activity logs found.
                </td>
            </tr>
        `;

        document.getElementById("activityTable").innerHTML = rows;

        return;
    }


    logs.forEach(log => {

        let actionClass = "";


        if (log.action === "LOGIN") {

            actionClass = "status-active";

        }
        else if (
            log.action === "DELETE"
        ) {

            actionClass = "status-inactive";

        }
        else {

            actionClass = "status-active";

        }


        rows += `

        <tr>

            <td>
                ${log.id ?? ""}
            </td>

            <td>
                ${log.username ?? ""}
            </td>

            <td>
                <span class="${actionClass}">
                    ${log.action ?? ""}
                </span>
            </td>

            <td>
                ${log.entityName ?? ""}
            </td>

            <td>
                ${log.entityId ?? ""}
            </td>

            <td>
                ${log.description ?? ""}
            </td>

            <td>
                ${log.ipAddress ?? ""}
            </td>

            <td>
                ${
                    log.timestamp
                        ? formatDate(log.timestamp)
                        : ""
                }
            </td>

        </tr>

        `;

    });


    document.getElementById("activityTable").innerHTML = rows;

}


// ================= DATE FORMAT =================

function formatDate(timestamp) {

    const date = new Date(timestamp);

    if (isNaN(date.getTime())) {
        return timestamp;
    }

    return date.toLocaleString();

}


// ================= PAGINATION UI =================

function updatePagination(data) {

    const pageInfo =
        document.getElementById("pageInfo");

    const previousBtn =
        document.getElementById("previousBtn");

    const nextBtn =
        document.getElementById("nextBtn");


    pageInfo.innerText =
        "Page " +
        (data.number + 1) +
        " of " +
        Math.max(data.totalPages, 1);


    previousBtn.disabled =
        data.first;


    nextBtn.disabled =
        data.last;

}


// ================= PREVIOUS PAGE =================

function previousPage() {

    if (currentPage > 0) {

        loadLogs(currentPage - 1);

    }

}


// ================= NEXT PAGE =================

function nextPage() {

    if (currentPage < totalPages - 1) {

        loadLogs(currentPage + 1);

    }

}


// ================= CLEAR FILTERS =================

function clearFilters() {

    document.getElementById("usernameSearch").value = "";

    document.getElementById("actionFilter").value = "";

    loadLogs(0);

}


// ================= EXPORT CSV =================

async function exportCSV() {

    const username =
        document.getElementById("usernameSearch").value.trim();

    const action =
        document.getElementById("actionFilter").value;


    let url =
        "http://localhost:8082/activity-logs/export";


    const params = [];


    if (username !== "") {

        params.push(
            "username=" +
            encodeURIComponent(username)
        );

    }


    if (action !== "") {

        params.push(
            "action=" +
            encodeURIComponent(action)
        );

    }


    if (params.length > 0) {

        url += "?" + params.join("&");

    }


    try {

        const response = await fetch(url, {

            method: "GET",

            headers: {

                "Authorization":
                    "Bearer " + token

            }

        });


        if (!response.ok) {

            alert("Unable to export activity logs.");

            return;
        }


        const blob =
            await response.blob();


        const downloadUrl =
            window.URL.createObjectURL(blob);


        const link =
            document.createElement("a");


        link.href = downloadUrl;

        link.download =
            "activity_logs.csv";


        document.body.appendChild(link);

        link.click();

        link.remove();


        window.URL.revokeObjectURL(
            downloadUrl
        );

    }
    catch (error) {

        console.error(
            "CSV Export Error:",
            error
        );

        alert("Unable to export CSV.");

    }

}


// ================= LOGOUT =================

function logout() {

    localStorage.removeItem("token");

    localStorage.removeItem("role");

    localStorage.removeItem("username");

    window.location.href =
        "Login.html";

}


// ================= PAGE LOAD =================

window.addEventListener(
    "DOMContentLoaded",
    function () {

        loadLogs(0);

    }
);