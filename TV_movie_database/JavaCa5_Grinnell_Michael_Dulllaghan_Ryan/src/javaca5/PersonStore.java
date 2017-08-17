package javaca5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PersonStore
{

    HashMap<String, ArrayList<Person>> people = new HashMap<>();

    public HashMap<String, ArrayList<Person>> getPeople() 
    {
        return people;
    }

    public List<Person> getPeopleList() //returns all people in one list without duplicates
    {
        //originally iterated through the hashmap adding all persons to one list
        //then checked if there were duplicates using .equals method and removed them
        //then found a way to do it all in one line wich I thought was more impressive
        return new ArrayList<>(new LinkedHashSet<>(people.values().stream().flatMap(List::stream).collect(Collectors.toList())));
    }

    public void setPeople(HashMap<String, ArrayList<Person>> people) 
    {
        this.people = people;
    }

    public void addPersons(String qName, ArrayList<Person> p) 
    {
        people.put(qName, p);
    }

    public Person findById(long id) 
    {
        Person found = null;
        
        for (Map.Entry<String, ArrayList<Person>> entry : people.entrySet()) 
        {
            ArrayList<Person> value = entry.getValue();
            for (Person p : value)
            {
                if (p.getId() == id) 
                {
                    found = p;
                }
            }
        }

        if (found != null) 
        {
            return found;
        } 
        else 
        {
            return null;
        }
    }

    public Person findById(long id, List<Person> people) 
    {
        Person found = null;
        for (Person p : people)
        {
            if (p.getId() == id)
            {
                found = p;
            }
        }
        return found;
    }

    @Override
    public int hashCode() 
    {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.people);
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) 
        {
            return true;
        }
        if (obj == null) 
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final PersonStore other = (PersonStore) obj;
        return Objects.equals(this.people, other.people);
    }

    @Override
    public String toString() 
    {
        return "PersonStore{" + "people=" + people + '}';
    }

}
