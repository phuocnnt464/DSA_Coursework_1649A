package utils;

import models.Book;
import models.Order;
import structures.OrderQueue;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvUtility {
    // Reads a list of books from a CSV file and returns it as a List<Book>
    public List<Book> readBooks(String filePath) {
        List<Book> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String title = values[0];
                String author = values[1];
                int quantity = Integer.parseInt(values[2]);
                books.add(new Book(title, author, quantity)); // Add book to the list
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Reads a list of orders from a CSV file and adds them to the queue
    public void readOrders(OrderQueue orderQueue, String filePath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            br.readLine(); // Skip the header line
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", 4); // Split into 4 parts: name, address, orderID, books

                // Check if the line has at least 3 fields (customerName, shippingAddress, orderID)
                if (values.length < 3) {
                    System.out.println("Invalid line: " + line);
                    continue; // Skip this line and continue to the next
                }

                String customerName = values[0];
                String shippingAddress = values[1];
                String orderID = values[2];
                String booksData = values.length > 3 ? values[3] : ""; // Check if there is book data

                Order order = new Order(customerName, shippingAddress);
                order.setOrderID(orderID); // Set the order ID

                // Process the list of books if it exists
                if (!booksData.isEmpty()) {
                    String[] booksArray = booksData.split(";");
                    for (String bookEntry : booksArray) {
                        String[] bookDetails = bookEntry.split("-");
                        if (bookDetails.length == 3) { // Check if there are enough book details
                            String title = bookDetails[0];
                            String author = bookDetails[1];
                            int quantity = Integer.parseInt(bookDetails[2]);

                            Book book = new Book(title, author, quantity);
                            order.addBook(book); // Add book to the order
                        }
                    }
                }
                orderQueue.enqueue(order); // Add the order to the queue
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error in parsing book quantity in CSV file.");
            e.printStackTrace();
        }
    }

    // Writes a single order to a CSV file
    public void writeOrder(Order order, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), "UTF-8"))) {
            StringBuilder orderData = new StringBuilder();
            orderData.append(order.getCustomerName()).append(",")
                    .append(order.getShippingAddress()).append(",")
                    .append(order.getOrderID()).append(",");

            // Add information for each book in the format: title-author-quantity
            for (Book book : order.getBooks()) {
                orderData.append(book.getTitle()).append("-")
                        .append(book.getAuthor()).append("-")
                        .append(book.getQuantity()).append(";");
            }

            // Remove the trailing semicolon
            if (!order.getBooks().isEmpty()) {
                orderData.setLength(orderData.length() - 1);
            }

            orderData.append("\n");
            bw.write(orderData.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Updates the books CSV file with the current list of books
    public void updateBooksFile(List<Book> books, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"))) {
            // Write header line
            bw.write("Title,Author,Quantity");
            bw.newLine();

            // Write each book to the file
            for (Book book : books) {
                bw.write(book.getTitle() + "," + book.getAuthor() + "," + book.getQuantity());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Writes an order's history to a CSV file
    public void writeOrderHistory(Order order, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), "UTF-8"))) {
            StringBuilder sb = new StringBuilder();
            sb.append(order.getCustomerName()).append(",");
            sb.append(order.getShippingAddress()).append(",");
            sb.append(order.getOrderID()).append(",");

            // Append details of each book in the format title-author-quantity
            for (Book book : order.getBooks()) {
                sb.append(book.getTitle()).append("-");
                sb.append(book.getAuthor()).append("-");
                sb.append(book.getQuantity()).append(";");
            }
            // Remove trailing semicolon if there are books
            if (!order.getBooks().isEmpty()) {
                sb.setLength(sb.length() - 1); // Remove final ";"
            }
            sb.append("\n");
            bw.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Error writing order history to CSV file: " + e.getMessage());
        }
    }

    // Reads the order history from a CSV file and returns it as a List<Order>
    public List<Order> readOrderHistory(String filePath) {
        List<Order> orderHistory = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            String line;
            br.readLine(); // Skip the header line (if there is one)
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", 4); // Split data: name, address, orderID, books

                // Check if the line has at least 3 fields
                if (values.length < 3) {
                    System.out.println("Invalid line: " + line);
                    continue;
                }

                String customerName = values[0];
                String shippingAddress = values[1];
                String orderID = values[2];
                String booksData = values.length > 3 ? values[3] : "";

                Order order = new Order(customerName, shippingAddress);
                order.setOrderID(orderID); // Set the order ID

                // Process the list of books if it exists
                if (!booksData.isEmpty()) {
                    String[] booksArray = booksData.split(";");
                    for (String bookEntry : booksArray) {
                        String[] bookDetails = bookEntry.split("-");
                        if (bookDetails.length == 3) {
                            String title = bookDetails[0].trim();
                            String author = bookDetails[1].trim();
                            int quantity = Integer.parseInt(bookDetails[2].trim());

                            Book book = new Book(title, author, quantity);
                            order.addBook(book); // Add book to the order
                        }
                    }
                }

                orderHistory.add(order);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error in parsing book quantity in CSV file.");
            e.printStackTrace();
        }
        return orderHistory;
    }
}
