package structures;

import models.Order;
import utils.CsvUtility;

public class OrderQueue {
    private Node front, rear; // Pointers to the front and rear nodes of the queue
    private CsvUtility csvUtility; // Utility for writing orders to CSV
    private String filePath; // File path for saving orders to CSV

    // Constructor that initializes OrderQueue with CsvUtility and a file path to save orders to CSV
    public OrderQueue(CsvUtility csvUtility, String filePath) {
        this.csvUtility = csvUtility;
        this.filePath = filePath;
    }

    // No-parameter constructor to initialize an empty OrderQueue
    public OrderQueue() {
        this.csvUtility = null;
        this.filePath = null;
    }

    // Inner class Node represents each element in the queue, holding order data and a link to the next node
    private class Node {
        Order data; // The order data stored in the node
        Node next;  // Pointer to the next node

        public Node(Order data) {
            this.data = data;
            this.next = null; // Initialize next as null since it's the last node when created
        }
    }

    // Adds a new order to the end of the queue
    public void enqueue(Order order) {
        Node newNode = new Node(order); // Create a new node with the order data
        if (rear == null) {
            // If the queue is empty, set both front and rear to the new node
            front = rear = newNode;
        } else {
            // Otherwise, link the new node at the end of the queue
            rear.next = newNode;
            rear = newNode;
        }
    }

    // Removes and returns the order at the front of the queue
    public Order dequeue() {
        if (front == null) return null; // If the queue is empty, return null
        Order order = front.data; // Retrieve the data from the front node
        front = front.next; // Move the front pointer to the next node
        if (front == null) rear = null; // If queue is now empty, set rear to null
        return order;
    }

    // Checks if the queue is empty
    public boolean isEmpty() {
        return front == null;
    }

    // Displays up to a specified number of the most recent orders in the queue
    public void displayLimitedOrders(int limit) {
        if (isEmpty()) {
            System.out.println("There are currently no orders in the queue.");
            return;
        }

        System.out.println("Displaying the latest " + limit + " orders in FIFO order:");
        Node current = front; // Start from the front of the queue
        int count = 0;

        // Iterate through the queue and display each order up to the specified limit
        while (current != null && count < limit) {
            System.out.println(current.data); // Assume Order has a toString() method for display
            current = current.next;
            count++;
        }

        if (count < limit) {
            System.out.println("Only " + count + " orders in the queue.");
        }
    }
}
