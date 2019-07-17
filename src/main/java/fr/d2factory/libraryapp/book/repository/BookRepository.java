package fr.d2factory.libraryapp.book.repository;

import com.sun.jmx.mbeanserver.Repository;

import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.book.dto.Book;
import fr.d2factory.libraryapp.member.Member;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The book repository emulates a database via 2 HashMaps
 */



public class BookRepository {


    private Logger LOGGER = Logger.getLogger("Book Repository");

    private BookRepository()
    {

    }
    /**
     * The book repository instance attribut
     */
    private static BookRepository INSTANCE = null;

    public static BookRepository getInstanceBookRepository()
    {
        if (INSTANCE == null)
        {
            synchronized(Repository.class)
            {
                if (INSTANCE == null)
                {   INSTANCE = new BookRepository();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * Every book have an only key ISBN
     */
    private static Map<ISBN, Book> availableBooks = new HashMap<>();

    /**
     * Every book is borrowed in a specific date
     */
    private static Map<Book, LocalDate> borrowedBooks = new HashMap<>();

    /**
     * Every memeber has a list a borrowed Books
     */

    private static Map<Member, List<Book>> membersHaveBooks= new HashMap<>();


    /**
     * add books user to set a list of books
     */

    public void addBooks(List<Book> books){

        for(int i=0; i< books.size(); i++ )
        {
            availableBooks.put( books.get(i).getIsbn(),books.get(i));
        }

    }

    /**
     * ISBN code is the considered as the id book, so we can find the book with its isbn
     */

    public   Book findBook(ISBN isbnCode) {

        return availableBooks.get(isbnCode);
    }


    /**
     * In this method we save the barrowed book in the map of booksbarrowed
     * we add the barrowed book to the list of books of the memebers who barrowed this book
     * This method use 3 parametres
     * @param book book borrowed
     * @param borrowedAt the date of borrow
     * @param member the member who borrowed the book
     */

    public   void saveBookBorrow(Book book, LocalDate borrowedAt, Member member){


        borrowedBooks.put(book,borrowedAt);
        LOGGER.info("the book" +book.getTitle() +" is borrowed");
        availableBooks.remove(book.getIsbn());

        if(membersHaveBooks.get(member) != null) {
            membersHaveBooks.get(member).add(book);
        }

        else

        {
            List<Book> listBooks= new ArrayList<>();
            listBooks.add(book);
            membersHaveBooks.put(member, listBooks);
        }
    }


    /**
     * This method return the date of barrow of the book
     * it took as a parameter
     * @param  book the book wanted
     */

    public  LocalDate findBorrowedBookDate(Book book) {

        System.out.println(borrowedBooks.size());
        return borrowedBooks.get(book);

    }

    /**
     * if a book was returned by the member, it will be removed from the barrowedbooks map
     * This method take as a parameter
     * @param book the book that will be removed from the borrowed books
     */


    public   void removeBookFromBorrowedBooks(Book book,Member member)
    {
        borrowedBooks.remove(book);

        availableBooks.put(book.getIsbn(),book);

        List<Book> listBooksOfTheMember= membersHaveBooks.get(member);
        listBooksOfTheMember.remove(book);

    }


    /**
     * Every member has a list of books
     * This method returns the list of books for  a determined memeber
     * it tooks as a parameter
     * @param member who has 0 or more borrowed books
     */

    public  List<Book> haveBooksByMember(Member member)
    {

        return membersHaveBooks.get(member);

    }

    /**
     * @return the list of the available Books
     */

    public List<Book> getAvailableBooks(){
        Collection<Book> bookCollection= availableBooks.values();
        List<Book> bookList;
        bookList=bookCollection.stream().collect(Collectors.toList());

        return bookList;
    }


}