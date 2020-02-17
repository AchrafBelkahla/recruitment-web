package fr.d2factory.libraryapp.member;



public class Resident extends Member{

    public Resident(float wallet) {
        super(wallet);

    }
    @Override
    public void payBook(int numberOfDays) throws InsufficientFundsException{

        float Total=0;
        int lateDays = Math.max(0,numberOfDays-60);
        if(lateDays==0)
            Total=(float) (numberOfDays*0.1);
        else
            Total=(float) ((numberOfDays*0.1)+(lateDays*0.2));


        if(this.getWallet()<Total) throw (new InsufficientFundsException("RESIDENT HAS NO MONEY !!!!!"));
        this.setWallet(this.getWallet()-Total);

    }

}
