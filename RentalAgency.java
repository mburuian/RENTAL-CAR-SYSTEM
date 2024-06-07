import java.io.*;
import java.util.*;

public class RentalAgency {
    private Map<String, Car> cars = new HashMap<>();
    private Map<String, Customer> customers = new HashMap<>();
    private List<String> rentals = new ArrayList<>();
    private static final String CUSTOMER_FILE = "customers.txt";
    private static final String CAR_FILE = "cars.txt";

    public RentalAgency() {
        loadCustomers();
        loadCars();
    }

    public void addCar(Car car) {// this ffunction will allow me to add more cars into th rental system
        cars.put(car.getLicensePlate(), car);
        saveCars();
    }

    public void addCustomer(Customer customer) {// this ffunction will allow me to add more customers into th rental
                                                // system
        customers.put(customer.getId(), customer);
        saveCustomers();
    }

    // a fuction to look for a car
    public Car findCar(String licensePlate) {
        return cars.get(licensePlate);
    }

    // a fuction to look for a customer
    public Customer findCustomer(String id) {
        return customers.get(id);
    }

    // this cheks whether the car is rented or not
    public boolean rentCar(String licensePlate, String customerId) {
        Car car = cars.get(licensePlate);
        Customer customer = customers.get(customerId);

        if (car != null && customer != null && !car.isRented()) {
            car.rent();
            rentals.add("Car " + car.getModel() + " rented by " + customer.getName());
            saveCars();
            return true;
        }
        return false;
    }

    public boolean returnCar(String licensePlate) {
        Car car = cars.get(licensePlate);

        if (car != null && car.isRented()) {
            car.returnCar();
            rentals.add("Car " + car.getModel() + " returned.");
            saveCars();
            return true;
        }
        return false;
    }

    public List<String> getRentals() {
        return rentals;
    }

    public Map<String, Car> getCars() {
        return cars;
    }

    public Map<String, Customer> getCustomers() {
        return customers;
    }

    public List<Car> getAvailableCars() {
        List<Car> availableCars = new ArrayList<>();
        for (Car car : cars.values()) {
            if (!car.isRented()) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    private void saveCustomers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
            for (Customer customer : customers.values()) {
                writer.write(customer.getId() + "," + customer.getName());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCustomers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    customers.put(parts[0], new Customer(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            // File might not exist on first run, this is not an error
        }
    }

    private void saveCars() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAR_FILE))) {
            for (Car car : cars.values()) {
                writer.write(car.getLicensePlate() + "," + car.getModel() + "," + car.isRented());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCars() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CAR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Car car = new Car(parts[0], parts[1]);
                    if (Boolean.parseBoolean(parts[2])) {
                        car.rent();
                    }
                    cars.put(parts[0], car);
                }
            }
        } catch (IOException e) {
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RentalAgency agency = new RentalAgency();

        while (true) {
            System.out.println("\nCar Rental System Menu:");
            System.out.println("1. Add a Customer");
            System.out.println("2. Add a Car");
            System.out.println("3. Rent a Car");
            System.out.println("4. Return a Car");
            System.out.println("5. Show Rental Transactions");
            System.out.println("6. Show All Rented Cars");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Customer ID: ");
                    String customerId = scanner.nextLine();
                    System.out.print("Enter Customer Name: ");
                    String customerName = scanner.nextLine();
                    agency.addCustomer(new Customer(customerId, customerName));
                    System.out.println("Customer added successfully!");
                    break;
                case 2:
                    System.out.print("Enter Car License Plate: ");
                    String licensePlate = scanner.nextLine();
                    System.out.print("Enter Car Model: ");
                    String carModel = scanner.nextLine();
                    agency.addCar(new Car(licensePlate, carModel));
                    System.out.println("Car added successfully!");
                    break;
                case 3:
                    System.out.println("Available cars:");
                    for (Car car : agency.getAvailableCars()) {
                        System.out.println("License Plate: " + car.getLicensePlate() + ", Model: " + car.getModel());
                    }
                    System.out.print("Enter Car License Plate: ");
                    String rentLicensePlate = scanner.nextLine();
                    System.out.print("Enter Customer ID: ");
                    String rentCustomerId = scanner.nextLine();
                    if (agency.rentCar(rentLicensePlate, rentCustomerId)) {
                        System.out.println("Car rented successfully!");
                    } else {
                        System.out.println("Car rental failed!");
                    }
                    break;
                case 4:
                    System.out.print("Enter Car License Plate to Return: ");
                    String returnLicensePlate = scanner.nextLine();
                    if (agency.returnCar(returnLicensePlate)) {
                        System.out.println("Car returned successfully!");
                    } else {
                        System.out.println("Car return failed!");
                    }
                    break;
                case 5:
                    System.out.println("Rental Transactions:");
                    for (String rental : agency.getRentals()) {
                        System.out.println(rental);
                    }
                    break;
                case 6:
                    System.out.println("Rented Cars:");
                    for (Car car : agency.getCars().values()) {
                        if (car.isRented()) {
                            System.out.println(car);
                        }
                    }
                    break;
                case 7:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

class Customer {
    private String id;
    private String name;

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

class Car {
    private String licensePlate;
    private String model;
    private boolean isRented;

    public Car(String licensePlate, String model) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.isRented = false;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getModel() {
        return model;
    }

    public boolean isRented() {
        return isRented;
    }

    public void returnCar() {
        this.isRented = false;
    }

    public void rent() {
        this.isRented = true;
    }
}
