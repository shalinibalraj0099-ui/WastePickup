const apiRoot = "/api";
const page = document.body.dataset.page;
let refreshPage = async () => {};

async function apiRequest(path, options = {}) {
    const response = await fetch(`${apiRoot}${path}`, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        }
    });
    const text = await response.text();
    let result = text;
    if (response.headers.get("content-type")?.includes("application/json") && text) {
        result = JSON.parse(text);
    }
    if (!response.ok) {
        throw new Error(typeof result === "string" ? result : result.message || "Request failed");
    }
    return result;
}

function escapeHtml(value) {
    return String(value ?? "").replace(/[&<>"']/g, character => ({
        "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
    })[character]);
}

function showMessage(message, isError = false, targetId = "formMessage") {
    const element = document.getElementById(targetId);
    if (!element) return;
    element.textContent = message;
    element.classList.toggle("error", isError);
}

function fillSelect(selectId, records, labelFor) {
    const select = document.getElementById(selectId);
    if (!select) return;
    const placeholder = select.options[0]?.outerHTML || '<option value="">Choose one</option>';
    select.innerHTML = placeholder + records.map(record =>
        `<option value="${record.id}">${escapeHtml(labelFor(record))}</option>`
    ).join("");
}

function deleteButton(resource, id, label) {
    return `<button class="button button-small button-danger" type="button" data-resource="${resource}" data-delete="${id}" aria-label="Delete ${label}">Delete</button>`;
}

async function loadDashboard() {
    const alertsElement = document.getElementById("reminderAlerts");
    const [zones, households, schedules, pickups, flaggedHouseholds] = await Promise.all([
        apiRequest("/zones"),
        apiRequest("/households"),
        apiRequest("/schedules"),
        apiRequest("/pickups"),
        apiRequest("/households/flagged")
    ]);
    document.getElementById("zoneCount").textContent = zones.length;
    document.getElementById("householdCount").textContent = households.length;
    document.getElementById("scheduleCount").textContent = schedules.length;
    document.getElementById("pickupCount").textContent = pickups.length;
    if (!alertsElement) return;
    alertsElement.innerHTML = flaggedHouseholds.map(household => {
        const phoneNumber = household.phoneNumber || "";
        const hasValidPhone = /^\+[1-9]\d{7,14}$/.test(phoneNumber);
        const message = `Waste pickup reminder for ${household.address}: please prepare your waste for your scheduled collection.`;
        const action = hasValidPhone
            ? `<div class="notification-actions">
                <a class="button button-small button-primary" data-sms-draft href="sms:${phoneNumber}?body=${encodeURIComponent(message)}">Send SMS</a>
                <button class="button button-small button-confirm" type="button" data-confirm-sms="${household.id}" hidden>Confirm sent</button>
            </div>`
            : `<button class="button button-small button-primary" type="button" data-set-phone="${household.id}">Add phone</button>`;
        return `
            <article class="reminder-alert">
                <span class="alert-mark" aria-hidden="true">!</span>
                <div><strong>Pickup reminder enabled</strong>
                <p>${escapeHtml(household.address)}${household.zone?.name ? ` · ${escapeHtml(household.zone.name)}` : ""}</p>
                <small>${escapeHtml(phoneNumber || "Phone number not set")} · Review and send from your messaging app</small></div>
                ${action}
            </article>
        `;
    }).join("") || '<p class="empty-alert">No households are currently flagged for reminders.</p>';
}

async function loadZones() {
    const zones = await apiRequest("/zones");
    document.getElementById("zoneTotal").textContent = zones.length;
    document.getElementById("zoneTable").innerHTML = zones.map(zone => `
        <tr><td>${zone.id}</td><td>${escapeHtml(zone.name)}</td><td>${deleteButton("zones", zone.id, "zone")}</td></tr>
    `).join("") || '<tr><td class="empty-row" colspan="3">No zones have been added.</td></tr>';
}

async function loadHouseholds() {
    const [zones, households] = await Promise.all([apiRequest("/zones"), apiRequest("/households")]);
    fillSelect("householdZone", zones, zone => zone.name);
    document.getElementById("householdTotal").textContent = households.length;
    document.getElementById("householdTable").innerHTML = households.map(household => `
        <tr><td>${household.id}</td><td>${escapeHtml(household.address)}</td>
        <td>${escapeHtml(household.phoneNumber || "-")}</td>
        <td>${escapeHtml(household.zone?.name || "-")}</td><td>${household.reminderFlag ? "On" : "Off"}</td>
        <td>${deleteButton("households", household.id, "household")}</td></tr>
    `).join("") || '<tr><td class="empty-row" colspan="6">No households have been added.</td></tr>';
}

async function loadSchedules() {
    const [zones, schedules] = await Promise.all([apiRequest("/zones"), apiRequest("/schedules")]);
    fillSelect("scheduleZone", zones, zone => zone.name);
    document.getElementById("scheduleTotal").textContent = schedules.length;
    document.getElementById("scheduleTable").innerHTML = schedules.map(schedule => `
        <tr><td>${schedule.id}</td><td>${escapeHtml(schedule.zone?.name || "-")}</td>
        <td>${escapeHtml(schedule.pickupDate)}</td>
        <td>${escapeHtml(schedule.startTime)} - ${escapeHtml(schedule.endTime)}</td>
        <td>${deleteButton("schedules", schedule.id, "schedule")}</td></tr>
    `).join("") || '<tr><td class="empty-row" colspan="5">No schedules have been added.</td></tr>';
}

async function loadPickups() {
    const [households, pickups] = await Promise.all([apiRequest("/households"), apiRequest("/pickups")]);
    fillSelect("pickupHousehold", households, household => household.address);
    document.getElementById("pickupTotal").textContent = pickups.length;
    document.getElementById("pickupTable").innerHTML = pickups.map(pickup => `
        <tr><td>${pickup.id}</td><td>${escapeHtml(pickup.household?.address || "-")}</td>
        <td>${escapeHtml(pickup.pickupTime?.replace("T", " ") || "-")}</td><td><span class="score">${pickup.score}</span></td>
        <td>${deleteButton("pickups", pickup.id, "pickup record")}</td></tr>
    `).join("") || '<tr><td class="empty-row" colspan="5">No pickup records have been added.</td></tr>';
}

function bindForm(id, submit) {
    const form = document.getElementById(id);
    if (!form) return;
    form.addEventListener("submit", async event => {
        event.preventDefault();
        try {
            await submit(form);
            form.reset();
            showMessage("Saved successfully.");
            await refreshPage();
        } catch (error) {
            showMessage(error.message, true);
        }
    });
}

if (page === "dashboard") {
    refreshPage = loadDashboard;
} else if (page === "zones") {
    refreshPage = loadZones;
    bindForm("zoneForm", form => apiRequest("/zones", {
        method: "POST", body: JSON.stringify({ name: form.elements.name.value.trim() })
    }));
} else if (page === "households") {
    refreshPage = loadHouseholds;
    bindForm("householdForm", form => apiRequest("/households", {
        method: "POST",
        body: JSON.stringify({
            address: form.elements.address.value.trim(),
            phoneNumber: form.elements.phoneNumber.value.trim(),
            reminderFlag: form.elements.reminderFlag.checked,
            zone: { id: Number(form.elements.zoneId.value) }
        })
    }));
} else if (page === "schedules") {
    refreshPage = loadSchedules;
    bindForm("scheduleForm", form => apiRequest("/schedules", {
        method: "POST",
        body: JSON.stringify({
            pickupDate: form.elements.pickupDate.value,
            startTime: form.elements.startTime.value,
            endTime: form.elements.endTime.value,
            zone: { id: Number(form.elements.zoneId.value) }
        })
    }));
} else if (page === "pickups") {
    refreshPage = loadPickups;
    const pickupTime = document.getElementById("pickupTime");
    pickupTime.value = new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 16);
    bindForm("pickupForm", form => apiRequest("/pickups", {
        method: "POST",
        body: JSON.stringify({
            household: { id: Number(form.elements.householdId.value) },
            pickupTime: form.elements.pickupTime.value,
            score: Number(form.elements.score.value)
        })
    }));
}

document.addEventListener("click", async event => {
    const smsDraftLink = event.target.closest("[data-sms-draft]");
    if (smsDraftLink) {
        const confirmButton = smsDraftLink.parentElement.querySelector("[data-confirm-sms]");
        if (confirmButton) confirmButton.hidden = false;
        showMessage(
            "After sending the message in your SMS app, return here and confirm it was sent.",
            false,
            "pageMessage"
        );
        return;
    }
    const confirmSmsButton = event.target.closest("[data-confirm-sms]");
    if (confirmSmsButton) {
        confirmSmsButton.disabled = true;
        try {
            const result = await apiRequest(
                `/households/${confirmSmsButton.dataset.confirmSms}/notification/confirm`,
                { method: "POST" }
            );
            await refreshPage();
            showMessage(`${result}. Carrier delivery is not verified.`, false, "pageMessage");
        } catch (error) {
            confirmSmsButton.disabled = false;
            showMessage(error.message, true, "pageMessage");
        }
        return;
    }
    const phoneButton = event.target.closest("[data-set-phone]");
    if (phoneButton) {
        const phoneNumber = window.prompt("Enter the household phone in international format, such as +14155552671:");
        if (phoneNumber === null) return;
        const normalizedPhone = phoneNumber.trim();
        if (!/^\+[1-9]\d{7,14}$/.test(normalizedPhone)) {
            showMessage("Use international format, for example +14155552671.", true, page === "dashboard" ? "pageMessage" : "formMessage");
            return;
        }
        try {
            const household = await apiRequest(`/households/${phoneButton.dataset.setPhone}`);
            household.phoneNumber = normalizedPhone;
            await apiRequest(`/households/${household.id}`, {
                method: "PUT",
                body: JSON.stringify(household)
            });
            await refreshPage();
            showMessage("Phone number saved.", false, page === "dashboard" ? "pageMessage" : "formMessage");
        } catch (error) {
            showMessage(error.message, true, page === "dashboard" ? "pageMessage" : "formMessage");
        }
        return;
    }
    const button = event.target.closest("[data-delete]");
    if (!button) return;
    try {
        await apiRequest(`/${button.dataset.resource}/${button.dataset.delete}`, { method: "DELETE" });
        showMessage("Deleted successfully.");
        await refreshPage();
    } catch (error) {
        showMessage(error.message, true);
    }
});

refreshPage().catch(error => showMessage(error.message, true, "pageMessage"));