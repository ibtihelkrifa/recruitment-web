package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.book.dto.Book;
import fr.d2factory.libraryapp.library.Library;


/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public abstract class Member {
    /**
     * An initial sum of money the member has
     */
    private float wallet;

    private int id ;



    /**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
    public abstract void payBook(int numberOfDays, Book bookToPayFor) throws Exception;

    public abstract Boolean hasLateBook(int numberOfDays) ;

    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Member(int id) {
        this.id = id;
    }



}
