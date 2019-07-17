package fr.d2factory.libraryapp.member;


import fr.d2factory.libraryapp.book.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Student extends Member {


    private boolean isFirstYear;
    private Logger LOGGER = Logger.getLogger("student");

    private List<Book> usedBooks = new ArrayList<>();


    public Student(int id,boolean isFirstYear) {
        super(id);
        this.isFirstYear = isFirstYear;
    }


    public void payBook(int numberOfDays,Book bookToPayFor) throws  Exception
    {
        if(numberOfDays < 0) throw new Exception("NumberOfDays Should be > 0");


        int numberOfEarlyDays = 0 ;
        int numberOfLateDays = 0 ;

        if(numberOfDays <= 30  && numberOfDays > 0 ){
            if(isFirstYear && !usedBooks.contains(bookToPayFor)){
                if(numberOfDays > 15 ) numberOfEarlyDays = numberOfDays - 15 ;
            }
            else numberOfEarlyDays = numberOfDays;
        }
        else if (numberOfDays > 30){

            numberOfLateDays = numberOfDays - 30 ;


            if(isFirstYear && !usedBooks.contains(bookToPayFor)){
                numberOfEarlyDays = 30 - 15 ;
            }
            else numberOfEarlyDays = 30;

        }

        float moneyToPay= numberOfEarlyDays * 10 +numberOfLateDays * 15;
        usedBooks.add(bookToPayFor);
        LOGGER.info("student is going to pay for book for a number of days"+ numberOfDays +" will cost " + moneyToPay );
        this.setWallet(this.getWallet()-moneyToPay);
        LOGGER.info("student money left in walled = " + this.getWallet());

    }

    public Boolean hasLateBook(int numberOfDays)
    {
        return numberOfDays>30;
    }

}
