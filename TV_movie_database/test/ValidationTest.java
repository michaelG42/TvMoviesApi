
import java.io.ByteArrayInputStream;
import javaca5.Validation;
import org.junit.Test;
import static org.junit.Assert.*;


public class ValidationTest 
{
    @Test
    public void testValidateDouble()
    {       
    String inputData = "5.3";
    System.setIn(new ByteArrayInputStream(inputData.getBytes()));//User input is replaced using setIn
    Validation.reset();
    double input = Validation.validateDouble("Testing validate double");
    assertEquals(5.3, input, 0);        
    }
    
    @Test
    public void testValidateInt()
    {       
    String inputData = "2" + System.getProperty("line.separator");//had to add new line as in.nexline is called after in.nextInt
    System.setIn(new ByteArrayInputStream(inputData.getBytes()));
    Validation.reset();
    int input = Validation.validateInt("Testing validate int");
    
    assertEquals(2, input);        
    }
                   
    @Test
    public void testValidateYesNo()
    {       
    String inputYes = "y";
    String inputNo = "n";
    
    System.setIn(new ByteArrayInputStream(inputYes.getBytes()));
    Validation.reset();
    boolean inputTrue = Validation.validateYesNo("Testing validate YesNo True");
    assertTrue(inputTrue);
    
    System.setIn(new ByteArrayInputStream(inputNo.getBytes()));
    Validation.reset();
    boolean inputFalse = Validation.validateYesNo("Testing validate YesNo False");
    assertFalse(inputFalse);  
    }
    
    @Test
    public void testValidateIsNumeric()
    {       
    Validation.reset();
    boolean inputTrue = Validation.isNumeric("56.5");
    assertTrue(inputTrue);
    
    boolean inputFalse = Validation.isNumeric("notNumeric");
    assertFalse(inputFalse); 
    }
        
    @Test
    public void testValidateRating()
    {       
    String inputData = "55.3";
    System.setIn(new ByteArrayInputStream(inputData.getBytes()));
    Validation.reset();
    double input = Validation.validateRating("Testing validate rating");
    assertEquals(55.3, input, 0);        
    }
        
    @Test(expected=java.util.NoSuchElementException.class)
    public void testValidateExceptionsDouble()
    {       
    String inputDouble = "NotDouble";   
    System.setIn(new ByteArrayInputStream(inputDouble.getBytes()));
    Validation.reset();
    Validation.validateDouble("Testing validate Double exception");  
    }
    
    @Test(expected=java.util.NoSuchElementException.class)
    public void testValidateExceptionsRating()
    {       
    String inputDouble = "400.8";//double not in range   
    System.setIn(new ByteArrayInputStream(inputDouble.getBytes()));
    Validation.reset();
    Validation.validateRating("Testing validate Rating exception");  
    }
    
    @Test(expected=java.util.NoSuchElementException.class)
    public void testValidateExceptionsInt()
    {       
    String inputInt = "5.6"; 
    System.setIn(new ByteArrayInputStream(inputInt.getBytes()));
    Validation.reset();
    Validation.validateInt("Testing validate int exception");
    }
    
    @Test(expected=java.util.NoSuchElementException.class)
    public void testValidateExceptionsYesNo()
    {       
    String inputYesNo = "Maybe";  
    System.setIn(new ByteArrayInputStream(inputYesNo.getBytes()));
    Validation.reset();
    Validation.validateYesNo("Testing validate YesNo exception");
    }
    
}
