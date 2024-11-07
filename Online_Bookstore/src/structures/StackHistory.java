package structures;

import models.Order;

import java.util.List;

public class StackHistory {
    private Node top; // Pointer to the top node in the stack

    // Inner class Node represents each element in the stack, holding order data and a link to the next node
    private class Node {
        Order data; // The order data stored in the node
        Node next;  // Pointer to the next node

        public Node(Order data) {
            this.data = data;
            this.next = null; // Initialize next as null since it's the last node when created
        }
    }

    // Adds a new order to the top of the stack
    public void push(Order order) {
        Node newNode = new Node(order); // Create a new node with the order data
        newNode.next = top; // Link the new node to the previous top of the stack
        top = newNode; // Move the top pointer to the new node
    }

    // Removes and returns the order from the top of the stack
    public Order pop() {
        if (top == null) return null; // If the stack is empty, return null
        Order order = top.data; // Retrieve the data from the top node
        top = top.next; // Move the top pointer to the next node in the stack
        return order;
    }

    // Checks if the stack is empty
    public boolean isEmpty() {
        return top == null;
    }

    // Displays up to a specified number of the most recent orders in the stack
    public void displayHistory(int limit) {
        if (isEmpty()) {
            System.out.println("There is no order history.");
            return;
        }

        System.out.println("Displaying the latest " + limit + " orders in LIFO order:");

        Node current = top; // Start from the top of the stack
        int count = 0;

        // Iterate through the stack and display each order up to the specified limit
        while (current != null && count < limit) {
            System.out.println(current.data); // Assume Order has a toString() method for display
            current = current.next;
            count++;
        }

        if (count < limit) {
            System.out.println("Only " + count + " orders in the history.");
        }
    }
}
