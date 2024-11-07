package utils;

import models.Order;
import structures.OrderQueue;

import java.util.ArrayList;
import java.util.List;

public class SearchUtility {

    // Search for an order by Order ID using a linear search
    public Order searchOrderByID(OrderQueue orderQueue, String orderID) {
        OrderQueue tempQueue = new OrderQueue();  // Temporary queue to hold checked elements
        Order foundOrder = null;

        // Check each element in the queue one by one
        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.dequeue();

            // Check if the current order's ID matches the search ID
            if (order.getOrderID().equals(orderID)) {
                foundOrder = order; // If found, store the order
            }

            tempQueue.enqueue(order);  // Store the checked order in a temporary queue to maintain original data
        }

        // Restore the original queue by moving items back from the temporary queue
        while (!tempQueue.isEmpty()) {
            orderQueue.enqueue(tempQueue.dequeue());
        }

        // Return the found order, or null if no match was found
        return foundOrder;
    }

    // Search for orders by customer name (partial string match)
    public List<Order> searchOrdersByCustomerName(OrderQueue orderQueue, String partialName) {
        OrderQueue tempQueue = new OrderQueue();  // Temporary queue to hold checked elements
        List<Order> matchingOrders = new ArrayList<>();  // List to store all matching orders

        // Loop through each order in the queue
        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.dequeue();

            // Check if the customer name contains the partial string (case-insensitive)
            if (order.getCustomerName().toLowerCase().contains(partialName.toLowerCase())) {
                matchingOrders.add(order);  // Add matching orders to the result list
            }

            tempQueue.enqueue(order);  // Store the checked order in a temporary queue to maintain original data
        }

        // Restore the original queue by moving items back from the temporary queue
        while (!tempQueue.isEmpty()) {
            orderQueue.enqueue(tempQueue.dequeue());
        }

        // Return the list of matching orders
        return matchingOrders;
    }
}
