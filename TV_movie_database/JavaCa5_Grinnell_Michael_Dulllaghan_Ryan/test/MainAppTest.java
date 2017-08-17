
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import static javaca5.MainApp.addComments;
import static javaca5.MainApp.addRating;
import static javaca5.MainApp.pad;
import static javaca5.MainApp.readString;
import static javaca5.MainApp.removeComments;
import javaca5.Person;
import javaca5.Validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


public class MainAppTest
{
    


    
    @Test
    public void testAddRating()
    {
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("test");               
        Person p = new Person(5.2, "queryName", "John", 12345, imageUrls , "www.somthing.com");
        
        double input1 = 54.3;
        String input2 = "Y";
        String simulatedUserInput = input1 + System.getProperty("line.separator")
        + input2 + System.getProperty("line.separator");
       
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes(StandardCharsets.UTF_8)));
        Validation.reset();
        assertEquals(-1, p.getMyRating(),0);//default rating -1
        addRating(p);      
        assertEquals(54.3, p.getMyRating(),0);
          

    }  

    
    @Test
    public void testAddComments()
    {
        
        ArrayList<String> imageUrls = new ArrayList<>();//needed to create person object
        imageUrls.add("test");               
        Person p = new Person(100, "Chuck", "Chuck Norris", 12345, imageUrls , "www.google.com");
        
        String input1 = "Chuck Norris can access private methods.";
        String input2 = "Chuck Norris can unit test entire applications with a single assert";
        String input3 = "<Chuck Norris can delete the recycling bin.>";//this will not be added to comments as it contains <>       
        String input4 = "exit";
        String input5 = "Y";
        
        String simulatedUserInput =
                  input1 + System.getProperty("line.separator")
                + input2 + System.getProperty("line.separator") 
                + input3 + System.getProperty("line.separator")
                + input4 + System.getProperty("line.separator")
                + input5 + System.getProperty("line.separator");
               
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes(StandardCharsets.UTF_8)));
        Validation.reset();//resets scanner
        assertTrue(p.getMyComments().isEmpty());
        
        addComments(p);
        
        assertEquals(2, p.getMyComments().size());
        assertEquals(input1, p.getMyComments().get(0));
        assertEquals(input2, p.getMyComments().get(1));
        
    }
    
    @Test
    public void testRemoveComments()
    {
        
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("test");               
        Person p = new Person(100, "Chuck", "Chuck Norris", 12345, imageUrls , "www.google.com");
        
        String comment1 = "Chuck Norris can dereference a NULL pointer.";
        String comment2 = "Chuck Norris can write to an input stream.";
        String comment3 = "I ran out of Chuck Norris jokes";      
        
        ArrayList<String> comments = new ArrayList<>();
        
        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);
        
        p.setMyComments(comments);
        assertEquals(comments, p.getMyComments());//ensures correct comments have been added
        assertEquals(3, p.getMyComments().size()); //length of 3
        
        int input1 = 2; //comment number to remove the second comment with index of 1
        String input2 = "Y"; //Y to confirm       
        int input3 = 0; //0 to finish
        
        String simulatedUserInput =
                  input1 + System.getProperty("line.separator")
                + input2 + System.getProperty("line.separator") 
                + input3 + System.getProperty("line.separator");
        
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes(StandardCharsets.UTF_8)));
        Validation.reset();
        
        removeComments(p);
        
        
        assertEquals(2, p.getMyComments().size());
        assertEquals(comment1, p.getMyComments().get(0));
        assertEquals(comment3, p.getMyComments().get(1));
        assertFalse(p.getMyComments().contains(comment2));
    }
        
    @Test(expected=NullPointerException.class)
    public void testAddRatingNullException()
    {
        Person p2 = null;        
        addRating(p2);  
    }
    
    @Test(expected=NullPointerException.class)
    public void testAddCommentsNullException()
    {
        Person p2 = null;        
        addComments(p2);  
    }
    
    @Test(expected=NullPointerException.class)
    public void testRemoveCommentsNullException()
    {
        Person p2 = null;        
        removeComments(p2);  
    }
    
    @Test
    public void testPad()
    {
        String word = "word";
        String paddedWordTen = "******word";
            
        String paddedword = pad(word,10);
        
        String negativePad = pad(word, -10);
        
        assertEquals(paddedWordTen, paddedword);
        assertEquals(word, negativePad);//negative numbers are treated the same as zero               
    }  
    
    @Test
    public void testReadString() throws IOException
    {
        String testString = "******test";
        
        //code expects a UCS-2 encoded string which has been superseded by UTF-16 
        InputStream stream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_16BE));
        DataInputStream dis = new DataInputStream(stream);
       
        String word = readString(dis, 10);
        
        assertEquals("test", word);
    }  
}
