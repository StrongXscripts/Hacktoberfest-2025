package PizzaOrderManagement;

import java.util.*;

public class PizzaOrderManagement {
    private Queue<PizzaOrder> orderQueue;
    private List<PizzaOrder> allOrders;
    private List<PizzaOrder> deliveredOrders;
    private Scanner scanner;


    private int totalCustomers;
    private double totalRevenue;

    public PizzaOrderManagement() {
        this.orderQueue = new LinkedList<>();
        this.allOrders = new ArrayList<>();
        this.deliveredOrders = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.totalCustomers = 0;
        this.totalRevenue = 0.0;
    }

    public void displayMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(" PIZZA ORDER MANAGEMENT SYSTEM ");
        System.out.println("=".repeat(60));
        System.out.println("1. Place New Order");
        System.out.println("2. Process Next Order (Kitchen)");
        System.out.println("3. Mark Order as Ready");
        System.out.println("4. Deliver Order");
        System.out.println("5. View All Pending Orders");
        System.out.println("6. Search Order by ID");
        System.out.println("7. View Order Statistics");
        System.out.println("8. View Pizza Menu & Prices");
        System.out.println("9. Customer Order History");
        System.out.println("10. Revenue Report");
        System.out.println("11. Cancel Order");
        System.out.println("0. Exit");
        System.out.println("=".repeat(60));
        System.out.print("Enter your choice: ");
    }

    public void displayPizzaMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(" PIZZA MENU & PRICES ");
        System.out.println("=".repeat(50));
        System.out.println("Pizza Types:");
        System.out.println("1. Margherita - $12.99 (base price)");
        System.out.println("2. Pepperoni - $15.99 (base price)");
        System.out.println("3. Vegetarian - $14.99 (base price)");
        System.out.println("4. Hawaiian - $16.99 (base price)");
        System.out.println("5. Meat Lovers - $19.99 (base price)");
        System.out.println("6. BBQ Chicken - $17.99 (base price)");
        System.out.println("\nSizes & Multipliers:");
        System.out.println("1. Small (80% of base price)");
        System.out.println("2. Medium (100% of base price)");
        System.out.println("3. Large (130% of base price)");
        System.out.println("4. Extra Large (160% of base price)");
        System.out.println("=".repeat(50));
    }

    public void placeOrder() {
        System.out.println("\n--- PLACE NEW ORDER ---");

        System.out.print("Customer Name: ");
        String customerName = scanner.nextLine();

        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();

        displayPizzaMenu();
        System.out.print("Pizza Type: ");
        String pizzaType = scanner.nextLine();

        System.out.print("Size (Small/Medium/Large/Extra Large): ");
        String size = scanner.nextLine();

        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        PizzaOrder order = new PizzaOrder(customerName, phoneNumber, pizzaType, size, quantity);


        orderQueue.offer(order);
        allOrders.add(order);
        totalCustomers++;

        System.out.println("\nOrder placed successfully!");
        System.out.println("Order Details: " + order);
        System.out.println("Estimated wait time: " + (orderQueue.size() * 15) + " minutes");
    }

    public void processNextOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("\nNo pending orders to process!");
            return;
        }

        PizzaOrder order = orderQueue.peek();
        if (order.getStatus().equals("PENDING")) {
            order.setStatus("IN_KITCHEN");
            System.out.println("\nProcessing order in kitchen:");
            System.out.println(order);
        } else {
            System.out.println("\nCurrent order is already being processed!");
        }
    }

    public void markOrderReady() {
        if (orderQueue.isEmpty()) {
            System.out.println("\nNo orders in queue!");
            return;
        }

        PizzaOrder order = orderQueue.peek();
        if (order.getStatus().equals("IN_KITCHEN")) {
            order.setStatus("READY");
            System.out.println("\n Order is ready for delivery:");
            System.out.println(order);
        } else {
            System.out.println("\n Order is not in kitchen yet!");
        }
    }

    public void deliverOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("\n No orders to deliver!");
            return;
        }

        PizzaOrder order = orderQueue.poll();
        order.setStatus("DELIVERED");
        deliveredOrders.add(order);
        totalRevenue += order.getPrice();

        System.out.println("\n Order delivered successfully!");
        System.out.println(order);
        System.out.println("Thank you for choosing our pizza service!");
    }

    public void viewPendingOrders() {
        System.out.println("\n--- PENDING ORDERS ---");
        if (orderQueue.isEmpty()) {
            System.out.println("No pending orders.");
            return;
        }

        System.out.println("Orders in queue: " + orderQueue.size());
        int position = 1;
        for (PizzaOrder order : orderQueue) {
            System.out.println(position + ". " + order);
            position++;
        }
    }

    public void searchOrderById() {
        System.out.print("\nEnter Order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        PizzaOrder foundOrder = null;
        for (PizzaOrder order : allOrders) {
            if (order.getOrderId() == orderId) {
                foundOrder = order;
                break;
            }
        }

        if (foundOrder != null) {
            System.out.println("\n Order Found:");
            System.out.println(foundOrder);
        } else {
            System.out.println("\nOrder not found!");
        }
    }

    public void viewStatistics() {
        System.out.println("\n--- ORDER STATISTICS ---");
        System.out.println("Total Customers Served: " + totalCustomers);
        System.out.println("Pending Orders: " + orderQueue.size());
        System.out.println("Delivered Orders: " + deliveredOrders.size());
        System.out.println("Total Orders: " + allOrders.size());
        System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue));

        if (!allOrders.isEmpty()) {
            double avgOrderValue = totalRevenue / deliveredOrders.size();
            System.out.println("Average Order Value: $" + String.format("%.2f", avgOrderValue));
        }
    }

    public void customerOrderHistory() {
        System.out.print("\nEnter customer phone number: ");
        String phoneNumber = scanner.nextLine();

        List<PizzaOrder> customerOrders = new ArrayList<>();
        for (PizzaOrder order : allOrders) {
            if (order.getPhoneNumber().equals(phoneNumber)) {
                customerOrders.add(order);
            }
        }

        if (customerOrders.isEmpty()) {
            System.out.println("\n No orders found for this customer.");
        } else {
            System.out.println("\n--- CUSTOMER ORDER HISTORY ---");
            System.out.println("Customer: " + customerOrders.get(0).getCustomerName());
            System.out.println("Phone: " + phoneNumber);
            System.out.println("Total Orders: " + customerOrders.size());

            for (PizzaOrder order : customerOrders) {
                System.out.println(order);
            }
        }
    }

    public void revenueReport() {
        System.out.println("\n--- REVENUE REPORT ---");
        System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue));
        System.out.println("Orders Delivered: " + deliveredOrders.size());

        if (!deliveredOrders.isEmpty()) {

            Map<String, Integer> pizzaCount = new HashMap<>();
            Map<String, Double> pizzaRevenue = new HashMap<>();

            for (PizzaOrder order : deliveredOrders) {
                String type = order.getPizzaType();
                pizzaCount.put(type, pizzaCount.getOrDefault(type, 0) + order.getQuantity());
                pizzaRevenue.put(type, pizzaRevenue.getOrDefault(type, 0.0) + order.getPrice());
            }

            System.out.println("\nPizza Sales Report:");
            for (String type : pizzaCount.keySet()) {
                System.out.printf("%s: %d sold, $%.2f revenue%n",
                        type, pizzaCount.get(type), pizzaRevenue.get(type));
            }
        }
    }

    public void cancelOrder() {
        System.out.print("\nEnter Order ID to cancel: ");
        int orderId = scanner.nextInt();
        scanner.nextLine();


        Iterator<PizzaOrder> iterator = orderQueue.iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            PizzaOrder order = iterator.next();
            if (order.getOrderId() == orderId) {
                iterator.remove();
                order.setStatus("CANCELLED");
                found = true;
                System.out.println("\nOrder cancelled successfully!");
                System.out.println(order);
                break;
            }
        }

        if (!found) {
            System.out.println("\nOrder not found in pending queue or already processed!");
        }
    }

    public void run1() {
        System.out.println(" Welcome to Pizza Order Management System! ");

        int choice;
        do {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: placeOrder(); break;
                case 2: processNextOrder(); break;
                case 3: markOrderReady(); break;
                case 4: deliverOrder(); break;
                case 5: viewPendingOrders(); break;
                case 6: searchOrderById(); break;
                case 7: viewStatistics(); break;
                case 8: displayPizzaMenu(); break;
                case 9: customerOrderHistory(); break;
                case 10: revenueReport(); break;
                case 11: cancelOrder(); break;
                case 0: System.out.println("\n Thank you for using Pizza Order Management System!"); break;
                default: System.out.println("\nInvalid choice! Please try again.");
            }

            if (choice != 0) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }

        } while (choice != 0);

        scanner.close();
    }

    public static void main(String[] args) {
        PizzaOrderManagement system = new PizzaOrderManagement();
        system.run1();
    }
}