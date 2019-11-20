package exceptions;

public class InputException extends Exception{

    public InputException(String s) {
        super(s);
    }

    public InputException(){
        super("Please try again!");
    }
}
