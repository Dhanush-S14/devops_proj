package com.devops.library_management_system;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class LibraryMain {
    static final Logger logger = Logger.getLogger(LibraryMain.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        
        if (args.length < 3) {
            logger.error("Usage: java -jar library-system.jar <bookId> <memberId> <operation>");
            return;
        }

        int bookId = Integer.parseInt(args[0]);
        int memberId = Integer.parseInt(args[1]);
        int operation = Integer.parseInt(args[2]);

        logger.info("Book ID: " + bookId);
        logger.info("Member ID: " + memberId);
        logger.info("\n1: Add Book.\n2: Issue Book.\n3: Return Book.\n4: Check Availability.\n5: View All Books.\n6: Exit.");
        logger.info("\nYour choice: " + operation);

        // Initialize library with sample books
        Library library = new Library();
        library.addBook(new Book(101, "Java Programming", "John Doe"));
        library.addBook(new Book(102, "Data Structures", "Jane Smith"));
        library.addBook(new Book(103, "Algorithms", "Bob Johnson"));

        String result = "";
        boolean success = false;

        switch (operation) {
            case 1:
                library.addBook(new Book(bookId, "Sample Book " + bookId, "Author " + memberId));
                result = "Book added successfully";
                success = true;
                break;
            case 2:
                success = library.issueBook(bookId);
                result = success ? "Book issued successfully" : "Book not available";
                break;
            case 3:
                success = library.returnBook(bookId);
                result = success ? "Book returned successfully" : "Book was not issued";
                break;
            case 4:
                Book book = library.findBookById(bookId);
                if (book != null) {
                    result = "Book available: " + book.isAvailable();
                    success = true;
                } else {
                    result = "Book not found";
                }
                break;
            case 5:
                result = "Total books: " + library.getTotalBooks() + 
                        ", Available: " + library.getAvailableBooks();
                success = true;
                break;
            default:
                result = "Invalid operation";
        }

        if (operation >= 1 && operation <= 5) {
            logger.info("Result: " + result);
            if (success) {
                logger.info("Operation completed successfully");
            }
        }
    }
}

