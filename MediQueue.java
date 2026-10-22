
import java.util.*;

class Patient {
    int id;
    String name;
    int age;
    String condition;
    int priority;
    String status;
    long arrivalOrder;

    Patient(int id, String name, int age, String condition,
            int priority, long arrivalOrder) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.condition = condition;
        this.priority = priority;
        this.arrivalOrder = arrivalOrder;
        this.status = "Waiting";
    }

    @Override
    public String toString() {
        return "ID: " + id
                + " | Name: " + name
                + " | Age: " + age
                + " | Condition: " + condition
                + " | Priority: " + priority
                + " | Status: " + status;
    }
}

public class MediQueue {

    // Stores all patient records for fast searching
    private static final Map<Integer, Patient> patientRecords =
            new HashMap<>();

    // Higher priority patients are served first.
    // Lower number means greater urgency.
    private static final PriorityQueue<Patient> emergencyQueue =
            new PriorityQueue<>(
                Comparator.comparingInt((Patient p) -> p.priority)
                          .thenComparingLong(p -> p.arrivalOrder)
            );

    // Patients with the same priority are managed in FIFO order.
    private static final Map<Integer, Queue<Patient>> priorityQueues =
            new HashMap<>();

    private static int nextId = 1;
    private static long arrivalCounter = 0;

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\n================================");
            System.out.println("       MEDIQUEUE SYSTEM");
            System.out.println(" Hospital Emergency Management");
            System.out.println("================================");
            System.out.println("1. Register Patient");
            System.out.println("2. Display All Patients");
            System.out.println("3. Search Patient");
            System.out.println("4. View Emergency Queue");
            System.out.println("5. Serve Next Patient");
            System.out.println("6. Update Patient Status");
            System.out.println("7. Sort Patients by Priority");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {
                case 1:
                    registerPatient();
                    break;
                case 2:
                    displayPatients();
                    break;
                case 3:
                    searchPatient();
                    break;
                case 4:
                    displayEmergencyQueue();
                    break;
                case 5:
                    serveNextPatient();
                    break;
                case 6:
                    updatePatientStatus();
                    break;
                case 7:
                    sortPatients();
                    break;
                case 8:
                    System.out.println("Exiting MediQueue. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 8);

        sc.close();
    }

    private static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            sc.next();
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    // Register a new patient
    private static void registerPatient() {

        System.out.println("\n--- Patient Registration ---");

        System.out.print("Enter patient name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        System.out.print("Enter age: ");
        int age = readInt();

        if (age < 0 || age > 130) {
            System.out.println("Invalid age.");
            return;
        }

        System.out.print("Enter medical condition: ");
        String condition = sc.nextLine().trim();

        if (condition.isEmpty()) {
            System.out.println("Condition cannot be empty.");
            return;
        }

        System.out.println("\nPriority Levels:");
        System.out.println("1 - Critical");
        System.out.println("2 - High");
        System.out.println("3 - Moderate");
        System.out.println("4 - Low");
        System.out.print("Enter priority (1-4): ");

        int priority = readInt();

        if (priority < 1 || priority > 4) {
            System.out.println("Priority must be between 1 and 4.");
            return;
        }

        Patient patient = new Patient(
                nextId++, name, age, condition,
                priority, arrivalCounter++
        );

        patientRecords.put(patient.id, patient);
        emergencyQueue.offer(patient);

        priorityQueues
                .computeIfAbsent(priority, k -> new LinkedList<>())
                .offer(patient);

        System.out.println("\nPatient registered successfully!");
        System.out.println(patient);
    }

    // Display all registered patients
    private static void displayPatients() {

        System.out.println("\n--- All Patient Records ---");

        if (patientRecords.isEmpty()) {
            System.out.println("No patient records found.");
            return;
        }

        List<Patient> patients =
                new ArrayList<>(patientRecords.values());

        patients.sort(Comparator.comparingInt(p -> p.id));

        for (Patient p : patients) {
            System.out.println(p);
        }
    }

    // Search a patient using HashMap
    private static void searchPatient() {

        System.out.print("\nEnter patient ID to search: ");
        int id = readInt();

        Patient patient = patientRecords.get(id);

        if (patient != null) {
            System.out.println("\nPatient Found:");
            System.out.println(patient);
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Display patients in estimated service order
    private static void displayEmergencyQueue() {

        System.out.println("\n--- Estimated Emergency Service Order ---");

        if (emergencyQueue.isEmpty()) {
            System.out.println("No patients are waiting.");
            return;
        }

        List<Patient> waiting = new ArrayList<>(emergencyQueue);
        waiting.sort(
                Comparator.comparingInt((Patient p) -> p.priority)
                          .thenComparingLong(p -> p.arrivalOrder)
        );

        int position = 1;

        for (Patient p : waiting) {
            if (p.status.equals("Waiting")) {
                System.out.println(position++ + ". " + p);
            }
        }

        System.out.println(
                "Note: This is an estimated order, not a clinical decision."
        );
    }

    // Serve the next waiting patient
    private static void serveNextPatient() {

        while (!emergencyQueue.isEmpty()) {

            Patient patient = emergencyQueue.poll();

            if (patient.status.equals("Waiting")) {

                patient.status = "In Service";

                Queue<Patient> queue =
                        priorityQueues.get(patient.priority);

                if (queue != null) {
                    queue.remove(patient);
                }

                System.out.println("\nNext patient for service:");
                System.out.println(patient);
                return;
            }
        }

        System.out.println("No waiting patients available.");
    }

    // Update patient status
    private static void updatePatientStatus() {

        System.out.print("\nEnter patient ID: ");
        int id = readInt();

        Patient patient = patientRecords.get(id);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("Current status: " + patient.status);
        System.out.println("1. Waiting");
        System.out.println("2. In Service");
        System.out.println("3. Completed");
        System.out.println("4. Cancelled");
        System.out.print("Choose new status: ");

        int choice = readInt();
        String oldStatus = patient.status;

        switch (choice) {
            case 1:
                patient.status = "Waiting";
                break;
            case 2:
                patient.status = "In Service";
                break;
            case 3:
                patient.status = "Completed";
                break;
            case 4:
                patient.status = "Cancelled";
                break;
            default:
                System.out.println("Invalid status choice.");
                return;
        }

        // Re-add to queues only when changed back to Waiting
        if (!oldStatus.equals("Waiting")
                && patient.status.equals("Waiting")) {

            patient.arrivalOrder = arrivalCounter++;
            emergencyQueue.offer(patient);

            priorityQueues
                    .computeIfAbsent(
                            patient.priority, k -> new LinkedList<>())
                    .offer(patient);
        }

        // Remove from active queues when no longer waiting
        if (oldStatus.equals("Waiting")
                && !patient.status.equals("Waiting")) {

            emergencyQueue.remove(patient);

            Queue<Patient> queue =
                    priorityQueues.get(patient.priority);

            if (queue != null) {
                queue.remove(patient);
            }
        }

        System.out.println("Patient status updated successfully.");
        System.out.println(patient);
    }

    // Sort and display patients by priority
    private static void sortPatients() {

        System.out.println("\n--- Patients Sorted by Priority ---");

        List<Patient> patients =
                new ArrayList<>(patientRecords.values());

        patients.sort(
                Comparator.comparingInt((Patient p) -> p.priority)
                          .thenComparingLong(p -> p.arrivalOrder)
        );

        for (Patient p : patients) {
            System.out.println(p);
        }
    }
}
