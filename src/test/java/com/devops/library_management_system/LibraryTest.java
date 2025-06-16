package com.devops.library_management_system;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LibraryTest {
    private Library library;

    @Before
    public void setUp() {
        library = new Library();
        library.addBook(new Book(101, "Test Book 1", "Author 1"));
        library.addBook(new Book(102, "Test Book 2", "Author 2"));
    }

    // 1. Test adding books
    @Test
    public void testAddBook() {
        assertEquals(2, library.getTotalBooks());
        library.addBook(new Book(103, "Test Book 3", "Author 3"));
        assertEquals(3, library.getTotalBooks());
    }

    @Test
    public void testAddBookWithDuplicateId() {
        // Adding a book with an existing ID should not throw, but may overwrite or ignore
        // This depends on your implementation. If you want to prevent duplicates, add logic.
        // Current test assumes duplicates are allowed.
        library.addBook(new Book(101, "Duplicate Book", "Duplicate Author"));
        assertEquals(3, library.getTotalBooks()); // May be 2 or 3, depending on logic
        // For strict testing, add logic to prevent duplicates in Library class and test here.
    }

    @Test
    public void testAddBookWithNegativeId() {
        library.addBook(new Book(-1, "Negative ID Book", "Author"));
        assertEquals(3, library.getTotalBooks());
    }

    // 2. Test issuing and returning books
    @Test
    public void testIssueBook() {
        assertTrue(library.issueBook(101));
        assertFalse(library.issueBook(101)); // Already issued
        assertEquals(1, library.getAvailableBooks());
    }

    @Test
    public void testIssueBookThatDoesNotExist() {
        assertFalse(library.issueBook(999)); // Non-existent book
    }

    @Test
    public void testReturnBook() {
        library.issueBook(101);
        assertTrue(library.returnBook(101));
        assertFalse(library.returnBook(101)); // Already returned
        assertEquals(2, library.getAvailableBooks());
    }

    @Test
    public void testReturnBookThatDoesNotExist() {
        assertFalse(library.returnBook(999)); // Non-existent book
    }

    @Test
    public void testIssueAllBooks() {
        library.issueBook(101);
        library.issueBook(102);
        assertEquals(0, library.getAvailableBooks());
    }

    // 3. Test finding books
    @Test
    public void testFindBookById() {
        Book book = library.findBookById(101);
        assertNotNull(book);
        assertEquals("Test Book 1", book.getTitle());

        Book notFound = library.findBookById(999);
        assertNull(notFound);
    }

    // 4. Test available and total books
    @Test
    public void testGetAvailableBooks() {
        assertEquals(2, library.getAvailableBooks());
        library.issueBook(101);
        assertEquals(1, library.getAvailableBooks());
    }

    @Test
    public void testGetTotalBooks() {
        assertEquals(2, library.getTotalBooks());
        library.addBook(new Book(103, "Test Book 3", "Author 3"));
        assertEquals(3, library.getTotalBooks());
    }

    // 5. Test edge cases and state after operations
    @Test
    public void testStateAfterMultipleOperations() {
        library.issueBook(101);
        assertEquals(1, library.getAvailableBooks());
        library.returnBook(101);
        assertEquals(2, library.getAvailableBooks());
        library.addBook(new Book(103, "Test Book 3", "Author 3"));
        assertEquals(3, library.getTotalBooks());
        assertEquals(3, library.getAvailableBooks()); // All books available
        library.issueBook(103);
        assertEquals(2, library.getAvailableBooks());
    }
}

