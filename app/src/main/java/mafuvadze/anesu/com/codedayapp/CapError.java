package mafuvadze.anesu.com.codedayapp;

/**
 * Created by Angellar Manguvo on 11/25/2015.
 */
public class CapError
{
    String except, error;
    public CapError(String except, String error)
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
