package fr.d2factory.libraryapp.member;


public class Student extends Member {

    private int yearOfStudy;
    public Student(int yearOfStudy,float wallet) {
        super(wallet);
        this.yearOfStudy=yearOfStudy;
    }
    @Override
    public void payBook(int numberOfDays) {
        float Total=0;

        //if 1st year student
        if(yearOfStudy==1) {
            int days = Math.max(0, numberOfDays-15);
            Total=(float) (days*0.1);
        }
        else {
            Total=(float) (numberOfDays*0.1);
        }

        if(this.getWallet()<Total) throw (new InsufficientFundsException("STUDENT HAS NO MONEY !!!!!!"));
        this.setWallet(this.getWallet()-Total);

    }

}
