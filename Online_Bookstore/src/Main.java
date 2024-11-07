import models.Book;
import models.Order;
import structures.OrderQueue;
import structures.StackHistory;
import utils.CsvUtility;
import utils.SearchUtility;
import utils.SortingUtility;

import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize the system and necessary data structures
        CsvUtility csvUtility = new CsvUtility();
        OrderQueue orderQueue = new OrderQueue(csvUtility, "./src/data/orders.csv");
        StackHistory stackHistory = new StackHistory();
        SearchUtility searchUtility = new SearchUtility();
        SortingUtility sortingUtility = new SortingUtility();

        // Read data from CSV
        csvUtility.readOrders(orderQueue, "./src/data/orders.csv");

        // Read available books from CSV for order selection
        List<Book> availableBooks = csvUtility.readBooks("./src/data/books.csv");

        // Read order history from order_history.csv and load it into StackHistory
        List<Order> orderHistory = csvUtility.readOrderHistory("./src/data/order_history.csv");
        for (Order order : orderHistory) {
            stackHistory.push(order);
        }

        Scanner scanner = new Scanner(System.in);

        // Display menu
        boolean running = true;
        while (running) {
            System.out.println("=== Online Bookstore System ===");
            System.out.println("1. Add Order");
            System.out.println("2. View Order History");
            System.out.println("3. Sort Books");
            System.out.println("4. Search Orders");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            // Check if input is an integer
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                scanner.next(); // Clear invalid input
                continue; // Skip current loop iteration and show the menu again
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (choice) {
                case 1:
                    // Create new order
                    System.out.println("Enter customer name:");
                    String customerName = scanner.nextLine();
                    System.out.println("Enter shipping address:");
                    String shippingAddress = scanner.nextLine();
                    Order order = new Order(customerName, shippingAddress);

                    // Add books to order
                    boolean addingBooks = true;
                    while (addingBooks) {
                        System.out.println("Select a book to add to the order:");
                        for (int i = 0; i < availableBooks.size(); i++) {
                            System.out.println((i + 1) + ". " + availableBooks.get(i));
                        }
                        System.out.println("Enter the book number or 0 to finish:");
                        int bookChoice = scanner.nextInt();
                        if (bookChoice == 0) {
                            addingBooks = false;
                        } else if (bookChoice > 0 && bookChoice <= availableBooks.size()) {
                            System.out.println("Enter quantity:");
                            int quantity = scanner.nextInt();
                            scanner.nextLine(); // Clear buffer

                            Book selectedBook = availableBooks.get(bookChoice - 1);

                            if (selectedBook.getQuantity() >= quantity) {
                                // Create a copy of the book with desired quantity and add it to the order
                                Book bookForOrder = new Book(selectedBook.getTitle(), selectedBook.getAuthor(), quantity);
                                order.addBook(bookForOrder);
                                System.out.println("Book added to the order.");

                                // Reduce quantity in availableBooks
                                selectedBook.setQuantity(selectedBook.getQuantity() - quantity);

                                // Remove book from availableBooks if out of stock
                                if (selectedBook.getQuantity() == 0) {
                                    availableBooks.remove(bookChoice - 1);
                                }

                                // Update books.csv file with new quantities
                                csvUtility.updateBooksFile(availableBooks, "./src/data/books.csv");
                            } else {
                                System.out.println("Insufficient quantity. Available quantity: " + selectedBook.getQuantity());
                            }
                        } else {
                            System.out.println("Invalid choice.");
                        }
                    }

                    // Add order to the queue
                    orderQueue.enqueue(order);
                    stackHistory.push(order); // Add order to StackHistory
                    csvUtility.writeOrder(order, "./src/data/orders.csv"); // Save order to CSV
                    csvUtility.writeOrderHistory(order, "./src/data/order_history.csv"); // Save to order history file
                    System.out.println("Order added to the system.");

                    // Display up to 5 most recent orders after adding a new order
                    orderQueue.displayLimitedOrders(5);
                    break;
                case 2:
                    // View order history
                    System.out.println("Order history:");
                    stackHistory.displayHistory(5);
                    break;
                case 3:
                    // Sort books
                    if (availableBooks.isEmpty()) {
                        System.out.println("The book list is empty.");
                    } else {
                        System.out.println("Choose sorting criterion:");
                        System.out.println("1. Sort by book title");
                        System.out.println("2. Sort by author name");
                        System.out.print("Choice: ");
                        int sortChoice = scanner.nextInt();
                        scanner.nextLine(); // Clear buffer

                        if (sortChoice == 1) {
                            sortingUtility.sortBooksByTitle(availableBooks);
                            System.out.println("Books sorted by title.");
                        } else if (sortChoice == 2) {
                            sortingUtility.sortBooksByAuthor(availableBooks);
                            System.out.println("Books sorted by author.");
                        } else {
                            System.out.println("Invalid choice.");
                            break;
                        }

                        // Display sorted book list
                        System.out.println("Sorted book list:");
                        for (Book book : availableBooks) {
                            System.out.println(book);
                        }
                    }
                    break;
                case 4:
                    // Search orders
                    System.out.println("Choose search method:");
                    System.out.println("1. Search by Order ID");
                    System.out.println("2. Search by Customer Name");
                    System.out.print("Choice: ");
                    int searchChoice = scanner.nextInt();
                    scanner.nextLine(); // Clear buffer

                    if (searchChoice == 1) {
                        // Search by Order ID
                        System.out.println("Enter Order ID:");
                        String searchOrderID = scanner.nextLine();
                        Order foundOrderById = searchUtility.searchOrderByID(orderQueue, searchOrderID);
                        if (foundOrderById != null) {
                            System.out.println("Order details:");
                            System.out.println(foundOrderById);
                        } else {
                            System.out.println("No order found with the entered ID.");
                        }
                    } else if (searchChoice == 2) {
                        // Search by Customer Name
                        System.out.println("Enter Customer Name:");
                        String searchCustomerName = scanner.nextLine();
                        List<Order> matchingOrdersByName = searchUtility.searchOrdersByCustomerName(orderQueue, searchCustomerName);

                        if (matchingOrdersByName.isEmpty()) {
                            System.out.println("No orders found for the entered customer name.");
                        } else {
                            System.out.println("List of orders matching the customer name:");
                            for (Order matchOrder : matchingOrdersByName) {
                                System.out.println(matchOrder);
                            }
                        }
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        scanner.close();
    }
}
