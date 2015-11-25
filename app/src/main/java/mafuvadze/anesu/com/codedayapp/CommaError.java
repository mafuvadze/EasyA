package mafuvadze.anesu.com.codedayapp;

/**
 * Created by Angellar Manguvo on 11/25/2015.
 */
public class CommaError
{
    String except, error;
    public CommaError(String except, String error)
    {
        this.except = except;
        this.error = error;
    }

    @Override
    public String toString()
    {
        return error;
    }
}
