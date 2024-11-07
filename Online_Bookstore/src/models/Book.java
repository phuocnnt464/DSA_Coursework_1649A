package models;

public class Book {
    private String title;    // Title of the book
    private String author;   // Author of the book
    private int quantity;    // Quantity of the book in stock or for the order

    // Constructor to initialize the title, author, and quantity of the book
    public Book(String title, String author, int quantity) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }

    // Getter for the book title
    public String getTitle() {
        return title;
    }

    // Getter for the author's name
    public String getAuthor() {
        return author;
    }

    // Getter for the quantity of the book
    public int getQuantity() {
        return quantity;
    }

    // Setter to update the quantity of the book
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Overrides toString to provide a readable string representation of the book, including title, author, and quantity
    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
