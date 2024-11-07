package utils;

import models.Book;
import java.text.Collator;
import java.util.List;
import java.util.Locale;

public class SortingUtility {

    // Sort books by title using the Quick Sort algorithm with Vietnamese language support
    public void sortBooksByTitle(List<Book> books) {
        Collator collator = Collator.getInstance(new Locale("vi", "VN")); // Set up a Collator for Vietnamese sorting
        quickSort(books, 0, books.size() - 1, collator, "title"); // Sort books by title
    }

    // Sort books by author using the Quick Sort algorithm with Vietnamese language support
    public void sortBooksByAuthor(List<Book> books) {
        Collator collator = Collator.getInstance(new Locale("vi", "VN")); // Set up a Collator for Vietnamese sorting
        quickSort(books, 0, books.size() - 1, collator, "author"); // Sort books by author
    }

    // Quick Sort algorithm with Collator for Vietnamese sorting, based on the chosen criteria (title or author)
    private void quickSort(List<Book> books, int low, int high, Collator collator, String sortCriteria) {
        // Base case: if the low index is less than the high index, continue sorting
        if (low < high) {
            // Partition the list and get the pivot index
            int pivotIndex = partition(books, low, high, collator, sortCriteria);

            // Recursively sort the left and right subarrays
            quickSort(books, low, pivotIndex - 1, collator, sortCriteria);
            quickSort(books, pivotIndex + 1, high, collator, sortCriteria);
        }
    }

    // Partition method for the Quick Sort algorithm
    private int partition(List<Book> books, int low, int high, Collator collator, String sortCriteria) {
        // Select the last element as the pivot
        Book pivotBook = books.get(high);
        int i = low - 1;  // Initialize i as the low index - 1

        // Compare elements with the pivot
        for (int j = low; j < high; j++) {
            int comparison;

            // Compare by title or author based on the sortCriteria
            if (sortCriteria.equals("title")) {
                comparison = collator.compare(books.get(j).getTitle(), pivotBook.getTitle());
            } else {
                comparison = collator.compare(books.get(j).getAuthor(), pivotBook.getAuthor());
            }

            // If the element is less than or equal to the pivot, swap it
            if (comparison <= 0) {
                i++; // Move the smaller element index forward
                Book temp = books.get(i);
                books.set(i, books.get(j)); // Swap elements at i and j
                books.set(j, temp);
            }
        }

        // Place the pivot in the correct sorted position
        Book temp = books.get(i + 1);
        books.set(i + 1, books.get(high));
        books.set(high, temp);

        // Return the pivot index
        return i + 1;
    }
}
