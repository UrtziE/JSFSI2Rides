package exceptions;
public class AtriNullException extends Exception {
 private static final long serialVersionUID = 1L;
 
 public AtriNullException ()
  {
    super();
  }
  
  public AtriNullException (String s)
  {
    super(s);
  }
}
