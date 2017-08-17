
import java.util.ArrayList;
import javaca5.Person;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PersonTest 
{
    
    @Test
    public void testAdd()
    {      
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("test"); 
                        
        Person p = new Person(5.2, "queryName", "John", 12345, imageUrls , "www.somthing.com");
        
        ArrayList results = p.getImageUrls();  // get back the list of img urls
             
        assertTrue(p.getScore() == 5.2);
        assertTrue(p.getQueryName().equals("queryName"));
        assertTrue(p.getName().equals("John"));
        assertTrue(p.getId() == 12345);
        assertTrue(results.contains("test"));
        assertTrue(p.getPersonLink().equals("www.somthing.com"));
        assertTrue(p.getMyComments().isEmpty());
        assertTrue(p.getMyRating() == -1);
    }
   
    @Test
    public void testToStrings()
    {
        ArrayList<String> imageUrls = new ArrayList<>();
        Person p = new Person(5.2, "queryJohn", "John", 12345, imageUrls , "www.john.com"); 

        String personString = "\nName : " + p.getName() + ", Id :" 
                + p.getId() + ", Rating : Not Set" + "\nComments : No Comments"
                + "\npersonLink :\n\t" + p.getPersonLink() 
                + "\nNo Image Urls" + "\n";       
        
        assertEquals(p.toPersonString(), personString);
        
        String listString = String.format("%-6s %-30s %-20s %-20s %-20s", "("+1+")", p.getName(), p.getId(), "Not Set", p.getScore());
        assertEquals(p.toListString(1), listString);
        
    }
    
    @Test
    public void testCompares()
    {
        
        ArrayList<String> imageUrls = new ArrayList<>();
        
        Person p = new Person(5.2, "queryJohn", "John", 12345, imageUrls , "www.john.com"); 
        Person p2 = new Person(14.2, "queryMike", "mike", 12445, imageUrls , "www.mike.com");
        Person p3 = new Person(10, "queryRyan", "ryan", 11442, imageUrls , "www.ryan.com");
        Person p4 = new Person(5.2, "queryJohn", "John", 12345, imageUrls , "www.john.com"); 
        
        p3.setMyRating(10);
        p4.setMyRating(50);
        
        //test default score comparator
        assertEquals(0, p.compareTo(p4));
        assertTrue(p.compareTo(p2) > 0);
        assertTrue(p3.compareTo(p) < 0);
        
        //test custom name comparator
        assertTrue(Person.nameComparator().compare(p, p2) < 0);
        assertTrue(Person.nameComparator().compare(p2, p) > 0);
        assertTrue(Person.nameComparator().compare(p, p4) == 0);
        
        //test id comparator
        assertTrue(Person.idComparator().compare(p, p3) > 0);
        assertTrue(Person.idComparator().compare(p3, p) < 0);
        assertTrue(Person.idComparator().compare(p, p4) == 0);
        
        //test rating comparator
        assertEquals(0, Person.ratingComparator().compare(p, p2));
        assertTrue(Person.ratingComparator().compare(p3, p4) < 0);
        assertTrue(Person.ratingComparator().compare(p4, p3) > 0);
    }
}
