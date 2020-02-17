package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository  {
    private Map<ISBN, Book> availableBooks = new HashMap<>();
    private Map<Book, LocalDate> borrowedBooks = new HashMap<>();

    public BookRepository() {}

    public Map<ISBN, Book> getAvailableBooks() {
        return availableBooks;
    }

    public void setAvailableBooks(Map<ISBN, Book> availableBooks) {
        this.availableBooks = availableBooks;
    }

    public Map<Book, LocalDate> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(Map<Book, LocalDate> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public void addBooks(List<Book> books){

        for(Book book:books) {
            availableBooks.put(book.isbn,book);
        }
    }

    public Book findBook(long isbnCode) {
        Iterator<Entry<ISBN, Book>> itr = availableBooks.entrySet().iterator();

        while(itr.hasNext())
        {
            Map.Entry<ISBN, Book> entry = itr.next();
            long isb = entry.getKey().isbnCode;
            if(isb==isbnCode)
                return entry.getValue();

        }
        return null;
    }

    public void saveBookBorrow(Book book, LocalDate borrowedAt){

        //remove the book from available books and add it to borrowed ones
        borrowedBooks.put(book, borrowedAt);
        availableBooks.remove(book.isbn);

    }

    public LocalDate findBorrowedBookDate(Book book) {
        Iterator<Entry<Book, LocalDate>> itr = borrowedBooks.entrySet().iterator();

        while(itr.hasNext())
        {
            Map.Entry<Book, LocalDate> entry = itr.next();
            long isb = entry.getKey().isbn.isbnCode;
            if(isb==book.isbn.isbnCode) {return entry.getValue();}

        }
        return null;
    }





}
