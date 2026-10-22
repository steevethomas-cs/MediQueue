
"use strict";

const patients = new Map();
const priorityQueues = new Map([
    [1, []],
    [2, []],
    [3, []],
    [4, []]
]);

let nextId = 1;
let arrivalCounter = 0;
let toastTimer;

const form = document.getElementById("patientForm");
const table = document.getElementById("patientTable");
const queueList = document.getElementById("queueList");
const searchInput = document.getElementById("searchInput");
const toast = document.getElementById("toast");

document.getElementById("currentDate").textContent =
    new Date().toLocaleDateString("en-IN", {
        day: "numeric",
        month: "short",
        year: "numeric"
    });

function priorityName(priority) {
    return {
        1: "Critical",
        2: "High",
        3: "Moderate",
        4: "Low"
    }[priority];
}

function escapeHTML(value) {
    return String(value).replace(/[&<>"']/g, char => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#39;"
    })[char]);
}

function showToast(message) {
    toast.textContent = message;
    toast.style.display = "block";

    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => {
        toast.style.display = "none";
    }, 3000);
}

function isWaiting(patient) {
    return patient.status === "Waiting";
}

function sortByPriority(a, b) {
    return a.priority - b.priority ||
           a.arrivalOrder - b.arrivalOrder;
}

function addToQueue(patient) {
    if (!isWaiting(patient)) return;

    priorityQueues.get(patient.priority).push(patient);
}

function removeFromQueue(patient) {
    for (const queue of priorityQueues.values()) {
        const index = queue.indexOf(patient);

        if (index !== -1) {
            queue.splice(index, 1);
        }
    }
}

function getWaitingPatients() {
    return [...patients.values()]
        .filter(isWaiting)
        .sort(sortByPriority);
}

// Register a patient
form.addEventListener("submit", event => {
    event.preventDefault();

    const name = document.getElementById("patientName")
        .value.trim();

    const age = Number(document.getElementById("patientAge").value);

    const condition = document.getElementById("patientCondition")
        .value.trim();

    const priority = Number(
        document.getElementById("patientPriority").value
    );

    if (!name || !condition) {
        showToast("Please enter the patient's name and condition.");
        return;
    }

    if (!Number.isInteger(age) || age < 0 || age > 130) {
        showToast("Please enter a valid age.");
        return;
    }

    if (![1, 2, 3, 4].includes(priority)) {
        showToast("Please select a valid priority.");
        return;
    }

    const patient = {
        id: nextId++,
        name,
        age,
        condition,
        priority,
        status: "Waiting",
        arrivalOrder: arrivalCounter++
    };

    patients.set(patient.id, patient);
    addToQueue(patient);

    form.reset();
    document.getElementById("patientPriority").value = "3";

    renderAll();
    showToast(`Patient #${patient.id} registered successfully.`);
});

// Display all patient records and support searching
function renderPatients() {
    const query = searchInput.value.trim().toLowerCase();

    const filtered = [...patients.values()]
        .filter(patient => {
            const searchable = [
                patient.id,
                patient.name,
                patient.condition,
                priorityName(patient.priority),
                patient.status
            ].join(" ").toLowerCase();

            return searchable.includes(query);
        })
        .sort(sortByPriority);

    if (filtered.length === 0) {
        table.innerHTML = `
            <tr>
                <td colspan="7" class="empty">
                    No matching patient records.
                </td>
            </tr>`;
        return;
    }

    table.innerHTML = filtered.map(patient => {
        const statusClass = patient.status
            .toLowerCase().replace(/\s+/g, "-");

        return `
            <tr>
                <td>#${patient.id}</td>
                <td><strong>${escapeHTML(patient.name)}</strong></td>
                <td>${patient.age}</td>
                <td>${escapeHTML(patient.condition)}</td>
                <td>
                    <span class="priority-badge priority-${patient.priority}-badge">
                        ${priorityName(patient.priority)}
                    </span>
                </td>
                <td>
                    <span class="status-badge status-${statusClass}">
                        ${patient.status}
                    </span>
                </td>
                <td>
                    <select class="action-select"
                        data-patient-id="${patient.id}"
                        aria-label="Update status for patient ${patient.id}">
                        ${["Waiting", "In Service", "Completed", "Cancelled"]
                            .map(status => `
                                <option value="${status}"
                                    ${patient.status === status ? "selected" : ""}>
                                    ${status}
                                </option>
                            `).join("")}
                    </select>
                </td>
            </tr>`;
    }).join("");
}

// Render the emergency service queue
function renderQueue() {
    const waiting = getWaitingPatients();

    if (waiting.length === 0) {
        queueList.innerHTML =
            '<p class="empty">No patients in the queue.</p>';
        return;
    }

    queueList.innerHTML = waiting.map((patient, index) => `
        <div class="queue-item priority-${patient.priority}">
            <strong>${index + 1}. ${escapeHTML(patient.name)}
                <span style="float:right">#${patient.id}</span>
            </strong>
            <small>
                ${priorityName(patient.priority)} priority
                &bull; ${escapeHTML(patient.condition)}
            </small>
        </div>
    `).join("");
}

// Update dashboard counters
function renderStats() {
    const all = [...patients.values()];

    document.getElementById("totalPatients").textContent =
        all.length;

    document.getElementById("criticalPatients").textContent =
        all.filter(p => p.priority === 1 && p.status !== "Cancelled")
           .length;

    document.getElementById("waitingPatients").textContent =
        all.filter(isWaiting).length;

    document.getElementById("completedPatients").textContent =
        all.filter(p => p.status === "Completed").length;
}

function renderAll() {
    renderStats();
    renderQueue();
    renderPatients();
}

// Update a patient's status
table.addEventListener("change", event => {
    const select = event.target.closest(".action-select");
    if (!select) return;

    const id = Number(select.dataset.patientId);
    const patient = patients.get(id);

    if (!patient) return;

    const newStatus = select.value;
    const allowedStatuses = [
        "Waiting", "In Service", "Completed", "Cancelled"
    ];

    if (!allowedStatuses.includes(newStatus)) return;

    const wasWaiting = isWaiting(patient);

    if (!wasWaiting && newStatus === "Waiting") {
        patient.arrivalOrder = arrivalCounter++;
    }

    patient.status = newStatus;

    removeFromQueue(patient);

    if (isWaiting(patient)) {
        addToQueue(patient);
    }

    renderAll();
    showToast(`Patient #${id} status updated to ${newStatus}.`);
});

// Serve the next waiting patient
document.getElementById("serveNextBtn")
    .addEventListener("click", () => {
        const waiting = getWaitingPatients();

        if (waiting.length === 0) {
            showToast("No waiting patients available.");
            return;
        }

        const patient = waiting[0];

        patient.status = "In Service";
        removeFromQueue(patient);

        renderAll();

        showToast(
            `Patient #${patient.id} (${patient.name}) moved to In Service.`
        );
    });

// Search as the user types
searchInput.addEventListener("input", renderPatients);

// Initial dashboard
renderAll();
