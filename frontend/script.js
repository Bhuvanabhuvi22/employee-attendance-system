const API_BASE = "http://localhost:8081/api/attendance";

// Load all records when the page opens
document.addEventListener("DOMContentLoaded", loadAttendance);

function formatTime(isoString) {
    if (!isoString) return "-";
    const d = new Date(isoString);
    return d.toLocaleTimeString();
}

async function loadAttendance() {
    try {
        const res = await fetch(API_BASE);
        if (!res.ok) throw new Error("Failed to load attendance records");
        const records = await res.json();
        renderTable(records);
    } catch (err) {
        console.error(err);
        alert("Could not reach the server. Is the backend running on port 8081?");
    }
}

function renderTable(records) {
    const tableBody = document.getElementById("tableBody");
    tableBody.innerHTML = "";

    records.forEach((record, index) => {
        const row = tableBody.insertRow();
        row.insertCell(0).innerHTML = index + 1;
        row.insertCell(1).innerHTML = record.empCode;
        row.insertCell(2).innerHTML = record.empName;
        row.insertCell(3).innerHTML = formatTime(record.inTime);
        row.insertCell(4).innerHTML = formatTime(record.outTime);
        row.insertCell(5).innerHTML = record.status;
    });
}

async function checkIn() {
    const code = document.getElementById("empCode").value.trim();
    const name = document.getElementById("empName").value.trim();

    if (code === "" || name === "") {
        alert("Enter Employee Code and Name");
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/checkin`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ empCode: code, empName: name })
        });

        const data = await res.json();

        if (!res.ok) {
            alert(data.message || "Check-in failed");
            return;
        }

        clearInputs();
        loadAttendance();
    } catch (err) {
        console.error(err);
        alert("Could not reach the server. Is the backend running on port 8081?");
    }
}

async function checkOut() {
    const code = document.getElementById("empCode").value.trim();

    if (code === "") {
        alert("Enter Employee Code");
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/checkout`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ empCode: code })
        });

        const data = await res.json();

        if (!res.ok) {
            alert(data.message || "Check-out failed");
            return;
        }

        clearInputs();
        loadAttendance();
    } catch (err) {
        console.error(err);
        alert("Could not reach the server. Is the backend running on port 8081?");
    }
}

function clearInputs() {
    document.getElementById("empCode").value = "";
    document.getElementById("empName").value = "";
}
