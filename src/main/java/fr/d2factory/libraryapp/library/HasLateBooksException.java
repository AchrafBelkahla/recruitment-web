package fr.d2factory.libraryapp.library;

/**
 * This exception is thrown when a member who owns late books tries to borrow another book
 */
public class HasLateBooksException extends RuntimeException
{
    public HasLateBooksException() {
        super("this member hasn't returned a book, he cannot borrow again !");
    }
}
