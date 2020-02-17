package fr.d2factory.libraryapp.library;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

/**
 * Do not forget to consult the README.md :)
 */
public class LibraryTest {
    private BookRepository bookRepository=new BookRepository();
    private TownsVilleLibrary townsVilleLibrary = new TownsVilleLibrary(bookRepository);
    private static List<Book> books;


    @BeforeEach
    void setup() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        File booksJson = new File("src/test/resources/books.json");
        books = mapper.readValue(booksJson, new TypeReference<List<Book>>() {
        });
        bookRepository.addBooks(books);
    }

    @Test
    void member_can_borrow_a_book_if_book_is_available(){

        Member resident=new Resident(50);
        LocalDate actualDate=LocalDate.now();
        Book result = townsVilleLibrary.borrowBook(12345, resident,actualDate);
        assertEquals(null,result);
    }

    @Test
    void borrowed_book_is_no_longer_available(){
        Member student=new Student(1,50);
        LocalDate actualDate=LocalDate.now();
        townsVilleLibrary.borrowBook(12345,student,actualDate);
        Book result = bookRepository.findBook(12345);
        assertEquals(null,result);
    }

    @Test
    void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){
        Member resident=new Resident(50);

        LocalDate borrowedAt = LocalDate.of(2019, 12, 10);
        Book book = books.get(1);
        townsVilleLibrary.borrowBook(12345,resident,borrowedAt);
        townsVilleLibrary.returnBook(book,resident);
        assertEquals(resident.getWallet(),29, 0.00001);
    }
    @Test
    void students_pay_10_cents_the_first_30days(){
        Member student=new Student(1,50);

        LocalDate borrowedAt = LocalDate.of(2019, 12, 10);
        Book book = books.get(1);
        townsVilleLibrary.borrowBook(12345,student,borrowedAt);
        townsVilleLibrary.returnBook(book,student);
        assertEquals(student.getWallet(),29, 0.0002);

    }

    @Test
    void students_in_1st_year_are_not_taxed_for_the_first_15days(){
        Member student=new Student(1,50);

        LocalDate borrowedAt = LocalDate.of(2020, 12, 27);
        Book book = books.get(1);
        townsVilleLibrary.borrowBook(3326456467846L,student,borrowedAt);
        townsVilleLibrary.returnBook(book,student);
        assertEquals(student.getWallet(),20.0f, 0.0002);
    }

    @Test
    void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){
        Member resident=new Resident(50);

        LocalDate borrowedAt = LocalDate.of(2019, 11, 1);
        Book book = books.get(1);
        townsVilleLibrary.borrowBook(12345,resident,borrowedAt);
        townsVilleLibrary.returnBook(book,resident);
        assertEquals(resident.getWallet(),40.9f,0.0002);

    }

    @Test
    void members_cannot_borrow_book_if_they_have_late_books(){
        Member resident=new Resident(50);
        Book book1 = books.get(1);
        LocalDate borrowedAt = LocalDate.of(2019, 10, 1);
        bookRepository.saveBookBorrow(book1, borrowedAt);
        LocalDate borrowDate = LocalDate.of(2020, 01, 11);
        Assertions.assertThrows(HasLateBooksException.class, () -> {
            townsVilleLibrary.borrowBook(12345,resident,borrowDate);
        });
    }
}
