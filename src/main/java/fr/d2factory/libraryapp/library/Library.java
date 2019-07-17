package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.book.dto.Book;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.MemberType;

import java.time.LocalDate;

/**
 * The library class is in charge of stocking the books and managing the return delays and members
 *
 */
public interface Library {

    /**
     * A member is borrowing a book from our library.
     *
     * @param isbnCode the isbn code of the book
     * @param member the member who is borrowing the book
     * @param borrowedAt the date when the book was borrowed
     *
     * @return the book the member wishes to obtain if found
     * @throws HasLateBooksException in case the member has books that are late
     *
     * @see ISBN
     * @see Member
     */
    Book borrowBook(ISBN isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException;

    /**
     * A member returns a book to the library.
     * We should calculate the tarif and probably charge the member
     *
     * @param book the {@link Book} they return
     * @param member the {@link Member} who is returning the book
     *
     * @see Member#payBook(int,Book)
     */
    void returnBook(Book book, Member member);
}
