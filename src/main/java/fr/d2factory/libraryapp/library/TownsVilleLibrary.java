package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class TownsVilleLibrary implements Library {
    BookRepository bR;

    public TownsVilleLibrary(BookRepository bR) {
        this.bR = bR;
    }

    @Override
    public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException
    {
        int MaxDay=0;
        ArrayList<LocalDate> Dates=new ArrayList<LocalDate>();
        Collection<LocalDate> values = bR.getBorrowedBooks().values();
        for(LocalDate v :values) {
            Dates.add(v);
        }
        Dates.sort(null);
        if(Dates.size()>=1) {
            LocalDate date = Dates.get(Dates.size()-1);
            MaxDay=borrowedAt.getDayOfYear()-date.getDayOfYear()+((borrowedAt.getYear()-date.getYear())*365);
        }

        if(member instanceof Resident) {
            if(MaxDay>60) throw new HasLateBooksException();
            else{
                Book book=bR.findBook(isbnCode);
                try {
                    bR.saveBookBorrow(book,borrowedAt);
                }
                catch( NullPointerException e) {
                    return null;
                }

                return book;
            }

        }

        if(member instanceof Student) {
            if(MaxDay>30) throw new HasLateBooksException();
            else {
                Book book=bR.findBook(isbnCode);
                try {
                    bR.saveBookBorrow(book,borrowedAt);
                }
                catch( NullPointerException e) {
                    return null;
                }

                return book;
            }
        }

        return null;

    }

    @Override
    public void returnBook(Book book, Member member)
    {
        LocalDate actualDate=LocalDate.now();
        bR.getAvailableBooks().put(book.getIsbn(),book);
        LocalDate dateReturn = bR.getBorrowedBooks().remove(book);
        int numberOfDays = actualDate.getDayOfYear()-dateReturn.getDayOfYear()+((actualDate.getYear()-dateReturn.getYear())*365);
        if(member instanceof Resident) {
            member.payBook(numberOfDays);
        }

        if(member instanceof Student) {
            member.payBook(numberOfDays);
        }
    }
}
