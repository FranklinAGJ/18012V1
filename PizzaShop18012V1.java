import java.util.Scanner;
import java.util.InputMismatchException;


class Pizza {
    private final String name;
    private final double basePrice;
    private final int size;

    public Pizza(String name, double basePrice, int size) {
        this.name = name;
        this.basePrice = basePrice;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getSize() {
        return size;
    }

    public String getSizeAsString() {
        switch (size) {
            case 1: return "Small";
            case 2: return "Medium";
            case 3: return "Large";
            default: return "Unknown";
        }
    }
}

class Topping {
    private final String name;
    private final double price;

    public Topping(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class PizzaOrder {
    private final Pizza pizza;
    private final int size;
    private final int quantity;
    private final Topping[] toppings;

    public PizzaOrder(Pizza pizza, int size, int quantity, Topping[] toppings) {
        this.pizza = pizza;
        this.size = size;
        this.quantity = quantity;
        this.toppings = toppings;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public int getQuantity() {
        return quantity;
    }

    public double calculateTotal() {
        double total = pizza.getBasePrice() * quantity;
        for (Topping topping : toppings) {
            if (topping != null) {
                total += topping.getPrice() * quantity;
            }
        }
        return total;
    }
}

class Customer {
    private String name;
    private String phone;

    public Customer(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}


class Menu {
    private final String[] pizzaMenu = {"Cheese Burst Pizza", "Veggie Pizza", "Paneer Pizza", "Pepperoni Pizza"};
    private final double[] pizzaPrices = {250, 150, 200, 400};
    private final String[] toppingMenu = {"Mushrooms", "Onions", "Bell Peppers", "Olives", "Bacon"};
    private final double[] toppingPrices = {20, 30, 25, 10, 50};

    public void displayMenu() {
        System.out.println("\n--- Pizza Menu ---");
        for (int i = 0; i < pizzaMenu.length; i++) {
            System.out.printf("%d. %s - INR %.2f%n", i + 1, pizzaMenu[i], pizzaPrices[i]);
        }
        System.out.printf("%d. Exit%n", 6);
        System.out.println("\n--- Available Toppings ---");
        for (int i = 0; i < toppingMenu.length; i++) {
            System.out.printf("%d. %s - INR %.2f%n", i + 1, toppingMenu[i], toppingPrices[i]);
        }
    }

    public int getPizzaCount() {
        return pizzaMenu.length;
    }

    public double getPizzaPrice(int index) {
        return pizzaPrices[index];
    }

    public String[] getToppingMenu() {
        return toppingMenu;
    }

    public double getToppingPrice(int index) {
        return toppingPrices[index];
    }

    public String[] getPizzaMenu() {
        return pizzaMenu;
    }
}

class Shop {
    private final String USERNAME = "pizza";
    private final String PASSWORD = "pizza";
    private final Menu menu = new Menu();
    private final OrderManager orderManager = new OrderManager();
    private final Scanner scanner;

    public Shop(Scanner scanner) {
        this.scanner = scanner;
    }

    public boolean login() {
        System.out.print("--- Login ---\nEnter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        return USERNAME.equals(username) && PASSWORD.equals(scanner.nextLine());
    }

    public double runShop() {
        double totalBill = 0;
        menu.displayMenu();
        while (true) {
            int choice = getUserChoice();
            if (choice == -1) {
                System.out.println("Exiting the program. Thank you for visiting Pizza Palace!");
                break;
            }
            if (choice >= 1 && choice <= menu.getPizzaCount()) {
                int size = getPizzaSize();
                int quantity = getPizzaQuantity();
                totalBill += orderManager.processOrder(choice, size, quantity, menu, scanner);
            } else {
                System.out.println("Invalid choice. Please select a number between 1 and " + menu.getPizzaCount() + ".");
            }
        }
        return totalBill;
    }

    private int getUserChoice() {
        while (true) {
            System.out.print("Select a pizza by number or enter 6 to exit: ");
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 6) return -1;
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    private int getPizzaSize() {
        while (true) {
            System.out.println("Choose pizza size:");
            System.out.println("1. Small\n2. Medium\n3. Large");
            System.out.print("Enter your choice (1, 2, or 3): ");
            try {
                int size = scanner.nextInt();
                scanner.nextLine();
                if (size >= 1 && size <= 3) return size;
                System.out.println("Invalid size! Please choose 1, 2, or 3.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    private int getPizzaQuantity() {
        while (true) {
            System.out.print("Enter the quantity: ");
            try {
                int quantity = scanner.nextInt();
                scanner.nextLine();
                if (quantity > 0) return quantity;
                System.out.println("Quantity must be a positive integer.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid quantity.");
                scanner.next();
            }
        }
    }

    public String getCustomerDetails(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public String getPhoneNumber() {
        while (true) {
            System.out.print("Enter your phone number: ");
            String phone = scanner.nextLine();
            if (phone.matches("\\d{10}")) return phone;
            System.out.println("Invalid phone number! Enter a valid 10-digit number.");
        }
    }

    public void printOrderSummary() {
        orderManager.printOrderSummary();
    }

    public void printBill(Customer customer, double totalBill) {
        System.out.printf("\n--- Bill ---\nCustomer Name: %s\nPhone Number: %s\nTotal Amount: INR %.2f%n", customer.getName(), customer.getPhone(), totalBill);
    }

    public int getOrderCount() {
        return orderManager.getOrderCount();
    }
}

class OrderManager {
    private final PizzaOrder[] orders = new PizzaOrder[50];
    private int orderCount = 0;

    public double processOrder(int pizzaChoice, int size, int quantity, Menu menu, Scanner scanner) {
        String pizzaName = menu.getPizzaMenu()[pizzaChoice - 1];
        double pizzaPrice = menu.getPizzaPrice(pizzaChoice - 1);
        Pizza pizza = new Pizza(pizzaName, pizzaPrice, size);
        Topping[] toppings = addToppingsToOrder(menu, scanner);
        PizzaOrder order = new PizzaOrder(pizza, size, quantity, toppings);

        orders[orderCount++] = order;
        return order.calculateTotal();
    }

    private Topping[] addToppingsToOrder(Menu menu, Scanner scanner) {
        Topping[] selectedToppings = new Topping[5];
        int toppingCount = 0;

        while (true) {
            System.out.println("Choose a topping (or type 0 to finish):");
            for (int i = 0; i < menu.getToppingMenu().length; i++) {
                System.out.printf("%d. %s - INR %.2f%n", i + 1, menu.getToppingMenu()[i], menu.getToppingPrice(i));
            }

            try {
                int toppingChoice = scanner.nextInt();
                scanner.nextLine();
                if (toppingChoice == 0) break;
                if (toppingChoice > 0 && toppingChoice <= menu.getToppingMenu().length) {
                    selectedToppings[toppingCount++] = new Topping(menu.getToppingMenu()[toppingChoice - 1], menu.getToppingPrice(toppingChoice - 1));
                } else {
                    System.out.println("Invalid topping choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scanner.next();
            }
        }
        return selectedToppings;
    }

    public void printOrderSummary() {
        System.out.println("\n--- Order Summary ---");
        for (int i = 0; i < orderCount; i++) {
            PizzaOrder order = orders[i];
            System.out.printf("Pizza: %s | Size: %s | Quantity: %d | Total Price: INR %.2f%n", order.getPizza().getName(), order.getPizza().getSizeAsString(), order.getQuantity(), order.calculateTotal());
        }
    }

    public int getOrderCount() {
        return orderCount;
    }
}


public class PizzaShop18012V1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Shop shop = new Shop(scanner);
        
        System.out.println("Welcome to Pizza Palace!");
        if (!shop.login()) {
            System.out.println("Invalid login credentials. Exiting.");
            return;
        }
        
        Customer customer = new Customer(shop.getCustomerDetails("Enter your name: "), shop.getPhoneNumber());
        double totalBill = shop.runShop();
        
        if (shop.getOrderCount() > 0) {
            shop.printOrderSummary();
            shop.printBill(customer, totalBill);
        } else {
            System.out.println("No order placed.");
        }
    }
}
