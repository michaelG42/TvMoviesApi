
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javaca5.Person;
import javaca5.PersonStore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PersonStoreTest 
{
       
    @Test
    public void testAdd()
    {
        PersonStore testPersonStore = new PersonStore();
        ArrayList<String> ArrayList = new ArrayList<>();
        ArrayList.add("ImageUrls"); 
        
        Person p = new Person(5.2, "queryJohn", "John", 12345, ArrayList , "www.john.com");
        Person p2 = new Person(14.2, "queryMike", "mike", 12445, ArrayList , "www.mike.com");
        Person p3 = new Person(10, "queryRyan", "ryan", 11442, ArrayList , "www.ryan.com");

        ArrayList<Person> people = new ArrayList<>();
        
        people.add(p);
        people.add(p2);
        people.add(p3);
        
        testPersonStore.addPersons("Key", people);
        
        HashMap<String, ArrayList<Person>> hmap = testPersonStore.getPeople();
        
        List<Person> testPeople = testPersonStore.getPeopleList(); 
        
        assertEquals(hmap,testPersonStore.getPeople());
        assertEquals(people,testPeople);
        assertTrue(hmap.containsKey("Key"));
        
        
        HashMap<String, ArrayList<Person>> hmap2 = null;       
        testPersonStore.setPeople(hmap2);//tests setPeople
        
        assertTrue(hmap2 == null);
    }
    
    @Test
    public void testFindById()
    {
        PersonStore testPersonStore = new PersonStore();
        
        ArrayList<String> ArrayList = new ArrayList<>();
        ArrayList.add("ImageUrls"); 
        
        Person p = new Person(5.2, "queryJohn", "John", 12345, ArrayList , "www.john.com");
        Person p2 = new Person(14.2, "queryMike", "mike", 12445, ArrayList , "www.mike.com");
        Person p3 = new Person(10, "queryRyan", "ryan", 11442, ArrayList , "www.ryan.com");

        ArrayList<Person> people = new ArrayList<>();
        
        people.add(p);
        people.add(p2);
        people.add(p3);
        
        testPersonStore.addPersons("Key", people);
        
        Person found = testPersonStore.findById(12345);
        Person notFound = testPersonStore.findById(1101001);
        
        assertEquals(found,p);
        assertTrue(notFound == null);
        
        //testing overloaded method
        found = testPersonStore.findById(11442,people);
        notFound = testPersonStore.findById(4545345,people);
        
        assertEquals(found,p3);
        assertTrue(notFound == null);       
    }
    
}
