
package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.book.dto.Book;
import fr.d2factory.libraryapp.book.repository.BookRepository;
import fr.d2factory.libraryapp.member.Member;


import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import static java.time.temporal.ChronoUnit.DAYS;


public class LibraryImpl implements  Library {


    private Logger LOGGER = Logger.getLogger("Borrow");

    BookRepository bookRepository= BookRepository.getInstanceBookRepository();




    public Book borrowBook(ISBN isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException
    {

        List<Book> listOfBooksByMember= bookRepository.haveBooksByMember(member);


        if(listOfBooksByMember != null) {
            for (int i = 0; i < listOfBooksByMember.size(); i++) {
                LocalDate now = LocalDate.now();
                LocalDate borrowDate = bookRepository.findBorrowedBookDate(listOfBooksByMember.get(i));
                int nbOfBorrowBookDays = (int) DAYS.between(borrowDate, now);

                if (member.hasLateBook(nbOfBorrowBookDays)) {
                    LOGGER.info("The member who want to pay the book has a late book "  );

                    throw new HasLateBooksException();
                }


            }

        }

        LOGGER.info("Searching if the book is available"  );

        Book bookAvaialble= bookRepository.findBook(isbnCode);


        if(bookAvaialble != null)
        {
            LOGGER.info("The book" +bookAvaialble.getTitle()+" is available");
            bookRepository.saveBookBorrow(bookAvaialble, borrowedAt, member);
            return bookAvaialble;
        }

        LOGGER.info("The book is already borrowed by a member" );


        return null;

    }




    public void returnBook(Book book, Member member)
    {

        LocalDate borrowedAt= bookRepository.findBorrowedBookDate(book);
        LocalDate now= LocalDate.now();
        int nbOfDays=(int) DAYS.between(borrowedAt,now);
        try {
            member.payBook(nbOfDays, book);
            bookRepository.removeBookFromBorrowedBooks(book,member);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

}
