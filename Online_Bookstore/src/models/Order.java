package models;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String customerName;  // Customer's name
    private String shippingAddress;  // Shipping address for the order
    private List<Book> books;  // List of books in this order
    private String orderID;  // Unique order ID

    // Constructor to initialize customer name, shipping address, an empty list of books, and generate an order ID
    public Order(String customerName, String shippingAddress) {
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.books = new ArrayList<>();  // Initialize an empty list of books
        this.orderID = generateOrderID();  // Generate a unique order ID
    }

    // Generates a unique order ID based on the current timestamp
    private String generateOrderID() {
        return "ORD" + System.currentTimeMillis();
    }

    // Adds a book to the order's book list
    public void addBook(Book book) {
        books.add(book);
    }

    // Getter for the customer's name
    public String getCustomerName() {
        return customerName;
    }

    // Getter for the shipping address
    public String getShippingAddress() {
        return shippingAddress;
    }

    // Getter for the order ID
    public String getOrderID() {
        return orderID;
    }

    // Getter for the list of books in this order
    public List<Book> getBooks() {
        return books;
    }

    // Setter to manually set or update the order ID (e.g., if loading from a file)
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    // Overrides toString to provide a readable string representation of the order, including customer name, address, books, and order ID
    @Override
    public String toString() {
        return "Order{" +
                "customerName='" + customerName + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", books=" + books +
                ", orderID='" + orderID + '\'' +
                '}';
    }
}
