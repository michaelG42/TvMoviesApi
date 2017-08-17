import java.io.File;
import javaca5.MakeHTML;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class MakeHTMLTest 
{
    private MakeHTML testHTML = new MakeHTML("TestFile","TheTitle");
     
    @Test
    public void testGet()
    {                         
        assertEquals( true, testHTML.getFileName().equals("TestFile"));
        assertTrue( testHTML.getHead().contains("TheTitle"));
    }
    
    @Test
    public void testSet()
    {        
        testHTML.setFileName("NewFileName");
        testHTML.setHead("NewHead");
       
        assertEquals( true, testHTML.getFileName().equals("NewFileName"));
        assertEquals( true, testHTML.getHead().equals("\n<head>NewHead</head>\n"));
    }
    
    @Test
    public void testMake()
    {
        testHTML.MakeNewHTMLFile();
        
        File file = new File("TestFile");
        assertTrue(file.exists());
    }
}
