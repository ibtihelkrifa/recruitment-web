package fr.d2factory.libraryapp.member;



import fr.d2factory.libraryapp.book.Book;

import java.util.logging.Logger;

public class Resident extends Member {



    private Logger LOGGER = Logger.getLogger("resident");

    public Resident(int id) {
        super(id);
    }

    public void payBook(int numberOfDays, Book bookToPayFor) throws Exception {
        if(numberOfDays < 0) throw new Exception("NumberOfDays Should be > 0");



        int numberOfEarlyDays ;
        int numberOfLateDays = 0 ;
        if(numberOfDays<=60 && numberOfDays> 0)
        {
            numberOfEarlyDays= numberOfDays;
        }
        else
        {
            numberOfLateDays =  numberOfDays -60 ;
            numberOfEarlyDays = 60 ;
        }


        float moneyToPay=numberOfEarlyDays*10 + numberOfLateDays * 20;
        LOGGER.info("resident " + this.getId() + "is going to pay for book for a number of days"+ numberOfDays +" will cost " + moneyToPay );
        this.setWallet(this.getWallet()-moneyToPay);
        LOGGER.info("resident" +this.getId() +" money left in walled = " + this.getWallet());


    }

    public Boolean hasLateBook(int numberOfDays)
    {
        return numberOfDays > 60;

    }


}
