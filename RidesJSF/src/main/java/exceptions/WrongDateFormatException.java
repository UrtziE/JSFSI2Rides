package exceptions;
public class WrongDateFormatException extends Exception {
 private static final long serialVersionUID = 1L;
 
 public WrongDateFormatException()
  {
    super();
  }
  /**This exception is triggered if the event has already finished
  *@param s String of the exception
  */
  public WrongDateFormatException(String s)
  {
    super(s);
  }
}