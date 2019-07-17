package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.book.dto.Book;
import fr.d2factory.libraryapp.book.repository.BookRepository;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.MemberFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import com.google.common.io.Resources;

import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static fr.d2factory.libraryapp.member.MemberType.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.Assert.*;

public class LibraryTest {
    private Library library ;
    private BookRepository bookRepository;

    @Before
    public void setup(){
        //TODO instantiate the library and the repository
        this.library= new LibraryImpl();
        this.bookRepository= BookRepository.getInstanceBookRepository();
        //TODO add some test books (use BookRepository#addBooks)
        //TODO to help you a file called books.json is available in src/test/resources

        JSONParser jsonParser= new JSONParser();
        try {

            List<Book> bookList= new ArrayList<>();

            Object obj = jsonParser.parse(new FileReader(Resources.getResource("books.json").getPath()));
            JSONArray JsonbookList = (JSONArray) obj;

            for (JSONObject Jsonbook : (Iterable<JSONObject>) JsonbookList) {
                String title = (String) Jsonbook.get("title");
                String author = (String) Jsonbook.get("author");
                JSONObject ISBNJSONObject = (JSONObject) Jsonbook.get("isbn");
                Long isbnCode = (Long) ISBNJSONObject.get("isbnCode");
                Book book = new Book(author, title, new ISBN(isbnCode));
                bookList.add(book);

            }

            bookRepository.addBooks(bookList);


        }


        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }


    }

    /**
     * This test is used just to verify if the BookRepository have just one instance in our program
     */
    @Test
    public void verify_that_the_sigleton_repositry_of_the_book_returns_always_the_same_object()
    {
        BookRepository bookRepository= BookRepository.getInstanceBookRepository();
        BookRepository bookRepository1=BookRepository.getInstanceBookRepository();

        assertEquals(bookRepository,bookRepository1);
    }


    /**
     * This test is used just to verify if the factory does not return the same instance
     */

    @Test
    public void verify_that_the_factory_does_not_return_the_same_object()
    {
        Member m= MemberFactory.createMember(STUDENT);
        Member m1=MemberFactory.createMember(RESIDENT);
        assertNotEquals(m,m1);
    }


    /**
     *
     */

    @Test
    public void verify_add_books_works_well_and_find_book_added()
    {

        List<Book> bookList= new ArrayList<>();
        Book b=new Book("La peau de chagrin", "Balzac", new ISBN(465789453149L));
        bookList.add(b);
        bookRepository.addBooks(bookList);
        assertEquals(b,bookRepository.findBook(b.getIsbn()));

    }






    @Test
    public void member_can_borrow_a_book_if_book_is_available(){


        Member member= MemberFactory.createMember(RESIDENT);
        List<Book> availableBooks =bookRepository.getAvailableBooks();
        Book book=library.borrowBook(availableBooks.get(0).getIsbn(), member, LocalDate.now());
        assertNotNull(book);

    }


    @Test
    public void borrowed_book_is_no_longer_available(){


        Member member1= MemberFactory.createMember(STUDENT);
        List<Book> availableBooks =bookRepository.getAvailableBooks();
        Book book=library.borrowBook(availableBooks.get(0).getIsbn(),member1,LocalDate.now());
        Book b2= library.borrowBook(book.getIsbn(),member1,LocalDate.now());
        assertNull(b2);

    }


    @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){
        Member resident= MemberFactory.createMember(RESIDENT);
        List<Book> availableBooks =bookRepository.getAvailableBooks();
        Book book=library.borrowBook(availableBooks.get(0).getIsbn(),resident,LocalDate.of(2019,7,11));
        int nbjours= (int) DAYS.between(LocalDate.of(2019, 7,11), LocalDate.now());
        assert resident != null;
        resident.setWallet(500);
        library.returnBook(book,resident);

        assertEquals(500-nbjours*10,resident.getWallet(),0);


    }



    @Test
    public void students_pay_10_cents_the_first_30days(){


        Member student= MemberFactory.createMember(STUDENT);
        List<Book> availableBooks =bookRepository.getAvailableBooks();
        Book book=library.borrowBook(availableBooks.get(0).getIsbn(),student,LocalDate.of(2019, 7,11));
        int nbjours= (int) DAYS.between(LocalDate.of(2019,7,11), LocalDate.now());
        assert student != null;
        student.setWallet(500);
        library.returnBook(book,student);

        assertEquals(500-nbjours*10,student.getWallet(),0);
    }



    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days(){


        Member resident= MemberFactory.createMember(STUDENT_FIRST_YEAR);
        List<Book> availableBooks =bookRepository.getAvailableBooks();
        Book book=library.borrowBook(availableBooks.get(0).getIsbn(),resident,LocalDate.of(2019,7,11));
        assert resident != null;
        resident.setWallet(500);
        library.returnBook(book,resident);

        assertEquals(500,resident.getWallet(),0);

    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days(){


        Member student= MemberFactory.createMember(STUDENT);
        List<Book> availableBooks =bookRepository.getAvailableBooks();
        Book book=library.borrowBook(availableBooks.get(0).getIsbn(),student,LocalDate.of(2019,6,14));
        int nbjours= (int) DAYS.between(LocalDate.of(2019,6,14), LocalDate.now());
        assert student != null;
        student.setWallet(1500);
        library.returnBook(book,student);

        assertEquals(1500-(30*10)-(nbjours-30)*15,student.getWallet(),0);
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){


        Member resident= MemberFactory.createMember(RESIDENT);
        List<Book> availableBooks =bookRepository.getAvailableBooks();
        Book book=library.borrowBook(availableBooks.get(0).getIsbn(),resident,LocalDate.of(2019,5,14));
        assert resident != null;
        resident.setWallet(1500);
        library.returnBook(book,resident);

        assertEquals(1500-(60*10)-(4*20),resident.getWallet(),0);

    }

    @Test(expected = HasLateBooksException.class)
    public void members_cannot_borrow_book_if_they_have_late_books(){

        Member resident;
        resident = MemberFactory.createMember(RESIDENT);

        List<Book> availableBooks =bookRepository.getAvailableBooks();
        library.borrowBook(availableBooks.get(0).getIsbn(),resident,LocalDate.of(2019,5,14));

        library.borrowBook(availableBooks.get(1).getIsbn(),resident,LocalDate.now());

    }
}