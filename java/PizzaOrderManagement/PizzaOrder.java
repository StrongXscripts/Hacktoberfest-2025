package PizzaOrderManagement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PizzaOrder {
    private static int orderCounter = 1000;
    private int orderId;
    private String customerName;
    private String phoneNumber;
    private String pizzaType;
    private String size;
    private int quantity;
    private double price;
    private LocalDateTime orderTime;
    private String status;

    public PizzaOrder(String customerName, String phoneNumber, String pizzaType, String size, int quantity) {
        this.orderId = ++orderCounter;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.pizzaType = pizzaType;
        this.size = size;
        this.quantity = quantity;
        this.orderTime = LocalDateTime.now();
        this.status = "PENDING";
        this.price = calculatePrice(pizzaType, size, quantity);
    }

    private double calculatePrice(String pizzaType, String size, int quantity) {
        double basePrice = 0;


        switch (pizzaType.toLowerCase()) {
            case "margherita": basePrice = 12.99; break;
            case "pepperoni": basePrice = 15.99; break;
            case "vegetarian": basePrice = 14.99; break;
            case "hawaiian": basePrice = 16.99; break;
            case "meat lovers": basePrice = 19.99; break;
            case "bbq chicken": basePrice = 17.99; break;
            default: basePrice = 12.99; break;
        }


        double sizeMultiplier = 1.0;
        switch (size.toLowerCase()) {
            case "small": sizeMultiplier = 0.8; break;
            case "medium": sizeMultiplier = 1.0; break;
            case "large": sizeMultiplier = 1.3; break;
            case "extra large": sizeMultiplier = 1.6; break;
        }

        return basePrice * sizeMultiplier * quantity;
    }


    public int getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPizzaType() { return pizzaType; }
    public String getSize() { return size; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDateTime getOrderTime() { return orderTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Order #%d | %s | %s | %s %s Pizza (x%d) | $%.2f | %s | %s",
                orderId, customerName, phoneNumber, size, pizzaType, quantity, price, status, orderTime.format(formatter));
    }
}