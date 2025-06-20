package com.devops.library_management_system;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.prometheus.client.exporter.PushGateway;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;

public class LibraryMain {
    static final Logger logger = Logger.getLogger(LibraryMain.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

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

        Library library = new Library();
        library.addBook(new Book(101, "Java Programming", "John Doe"));
        library.addBook(new Book(102, "Data Structures", "Jane Smith"));
        library.addBook(new Book(103, "Algorithms", "Bob Johnson"));

        String result = "";
        boolean success = false;

        // Increment operation counter with operation tag
        Counter.builder("library_cli_operations_total")
               .description("Library CLI operations by type")
               .tags("operation", String.valueOf(operation))
               .register(prometheusRegistry)
               .increment();

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
                Counter.builder("library_cli_success_total")
                       .description("Successful operations by type")
                       .tags("operation", String.valueOf(operation))
                       .register(prometheusRegistry)
                       .increment();
            }
        }

        try {
            PushGateway pg = new PushGateway("localhost:9091");
            pg.pushAdd(prometheusRegistry.getPrometheusRegistry(), "library-cli-job");
        } catch (IOException e) {
            logger.error("Failed to push metrics to Prometheus PushGateway: " + e.getMessage());
        }
    }
}
