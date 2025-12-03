package exceptions;
public class DriverNotInDBException extends Exception {
 private static final long serialVersionUID = 1L;
 
 public DriverNotInDBException ()
  {
    super();
  }
  
  public DriverNotInDBException (String s)
  {
    super(s);
  }
}
