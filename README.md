# MediQueue – Hospital Emergency Patient Management System

## 1. Project Overview

MediQueue is a web-based Hospital Emergency Patient Management System designed to organize patient records and manage emergency queues based on priority levels. Instead of processing patients only in arrival order, the system displays an estimated service order based on the assigned priority.

The project demonstrates the practical application of data structures and web development technologies in healthcare management. It provides a user-friendly dashboard for registering patients, searching records, tracking service status, and managing the emergency queue.

This is an academic project intended for educational purposes. It does not replace professional medical triage or clinical decision-making.

## 2. Objectives

- Register and maintain patient information.
- Organize patients according to emergency priority.
- Display the estimated patient service order.
- Search patient records efficiently.
- Update patient service status.
- Demonstrate priority queues, queues, maps, and sorting concepts.
- Provide a simple and responsive user interface.

## 3. Technologies Used

- **HTML5:** Structures the web pages and patient registration forms.
- **CSS3:** Provides styling, layout, colors, and responsive design.
- **JavaScript:** Implements patient registration, searching, sorting, queue management, and status updates.
- **Java (conceptual backend):** The related Java implementation demonstrates data structures using `PriorityQueue`, `Queue`, and `HashMap`.

**Note:** The current HTML, CSS, and JavaScript frontend operates independently. It is not yet connected to the Java application.

## 4. Project Structure

```text
MediQueue/
│
├── index.html
├── style.css
├── script.js
└── README.md
```

### File Descriptions

- `index.html` – Contains the dashboard, patient registration form, emergency queue, and patient records table.
- `style.css` – Defines the visual design, status indicators, priority badges, and responsive layout.
- `script.js` – Handles patient data, queue ordering, searching, sorting, dashboard statistics, and status updates.
- `README.md` – Documents the project, features, setup, and usage.

## 5. Key Features

### Patient Registration

Users can enter a patient's name, age, medical condition, and priority level. The application assigns a unique patient ID automatically.

### Priority-Based Emergency Queue

Patients are displayed in the following illustrative priority order:

1. Critical
2. High
3. Moderate
4. Low

Patients with the same priority are ordered by arrival time.

### Patient Record Management

The application displays patient details, including ID, name, age, medical condition, priority, and service status.

### Patient Search

Users can search records by patient ID, name, medical condition, priority, or status.

### Service Status Management

Each patient's status can be updated to one of the following:

- Waiting
- In Service
- Completed
- Cancelled

### Dashboard Statistics

The dashboard displays the total number of patients, critical-priority patients, waiting patients, and completed patients.

### Responsive User Interface

The application adjusts its layout for desktop computers, tablets, and mobile devices.

## 6. Data Structures and Algorithms

The project demonstrates the following concepts:

| Data Structure / Algorithm | Purpose |
|---|---|
| Priority Queue | Selects the next waiting patient according to priority. |
| Queue (FIFO) | Maintains arrival order among patients with the same priority. |
| HashMap | Provides efficient patient record lookup using patient ID in the Java implementation. |
| ArrayList | Temporarily stores records for sorting and display in Java. |
| Comparator | Sorts patients by priority and arrival order. |
| JavaScript Map | Stores patient records in the frontend. |
| JavaScript Arrays | Manage waiting queues and sort patient records. |

## 7. How to Run the Project

### Requirements

- A modern web browser such as Google Chrome, Microsoft Edge, or Firefox.
- A text editor or IDE such as Visual Studio Code.

### Steps

1. Create a folder named `MediQueue`.
2. Create `index.html`, `style.css`, and `script.js` inside the folder.
3. Paste the corresponding source code into each file.
4. Save all files.
5. Open `index.html` in your web browser.
6. Register sample patients and test the queue, search, and status management features.

No web server or external library is required for the current frontend version.

## 8. How to Use

1. Open the MediQueue dashboard.
2. Enter the patient's name, age, and medical condition.
3. Select a priority level.
4. Click **Register Patient**.
5. View the patient in the emergency queue and patient records table.
6. Use the search field to find a particular patient.
7. Update the patient's status using the dropdown menu.
8. Click **Serve Next Patient** to move the highest-priority waiting patient to In Service.

## 9. Limitations

- Patient records are stored in browser memory and are lost when the page is refreshed.
- The frontend does not use a database or persistent storage.
- The Java implementation and web frontend are separate applications.
- No authentication or role-based access control is implemented.
- The queue does not calculate actual waiting times.
- Priority levels are illustrative and are not based on a validated medical triage protocol.

## 10. Future Enhancements

- Integrate the frontend with a Java backend using Spring Boot or Java Servlets.
- Store patient records in a database such as MySQL.
- Add authentication for authorized hospital staff.
- Implement audit logs and secure patient data access.
- Add notifications for queue updates.
- Add estimated waiting-time calculations using historical service data.
- Implement clinically approved triage workflows under professional supervision.

## 11. Educational Disclaimer

MediQueue is developed as an academic project to demonstrate programming concepts, data structures, and basic patient queue management. It is not intended for real-world clinical use. Actual triage and treatment decisions must be made by qualified healthcare professionals.

## 12. Conclusion

MediQueue demonstrates how priority queues, queues, hash-based record retrieval, and sorting can be applied to a hospital emergency management scenario. Its user-friendly interface simplifies patient registration, record searching, queue visualization, and service status tracking. The project provides a foundation for future development into a more comprehensive healthcare management application.

---

**Project Name:** MediQueue  
**Project Type:** Academic Web Application  
**Frontend:** HTML, CSS, JavaScript  
**Related Java Concepts:** PriorityQueue, Queue, HashMap, Sorting Algorithms  
**Status:** Frontend prototype
