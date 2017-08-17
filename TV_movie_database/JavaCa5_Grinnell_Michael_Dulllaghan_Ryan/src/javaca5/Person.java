package javaca5;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.io.Serializable;

public class Person implements Comparable<Person>, Serializable 
{

    private double score; // value indicating the quality of match for search query
    private String queryName; // query name used to retrieve this Actor
    private String name;
    private long id;
    private ArrayList<String> imageUrls;
    private String personLink;
    private double myRating;
    private ArrayList<String> myComments;

    public Person(double score, String queryName, String name, long id, ArrayList<String> imageUrls, String personLink) 
    {
        this.score = score;
        this.queryName = queryName;
        this.name = name;
        this.id = id;
        this.imageUrls = imageUrls;
        this.personLink = personLink;
        myRating = -1;
        myComments = new ArrayList<>();
    }

    public double getScore()
    {
        return score;
    }

    public void setScore(double score)
    {
        this.score = score;
    }

    public String getQueryName()
    {
        return queryName;
    }

    public void setQueryName(String queryName)
    {
        this.queryName = queryName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getId() 
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public ArrayList<String> getImageUrls()
    {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls)
    {
        this.imageUrls = imageUrls;
    }

    public String getPersonLink() 
    {
        return personLink;
    }

    public void setPersonLink(String personLink) 
    {
        this.personLink = personLink;
    }

    public double getMyRating()
    {
        return myRating;
    }

    public void setMyRating(double myRating) 
    {
        this.myRating = myRating;
    }

    public ArrayList<String> getMyComments()
    {
        return myComments;
    }

    public void setMyComments(ArrayList<String> myComments) 
    {
        this.myComments = myComments;
    }

    public void addComments(String Comments)
    {
        myComments.add(Comments);
    }

    //Search score and query name have been removed from equals and hashcode 
    //So the same person will be considered equal
    @Override
    public int hashCode() 
    {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.name);
        hash = 73 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 73 * hash + Objects.hashCode(this.imageUrls);
        hash = 73 * hash + Objects.hashCode(this.personLink);
        hash = 73 * hash + (int) (Double.doubleToLongBits(this.myRating) ^ (Double.doubleToLongBits(this.myRating) >>> 32));
        hash = 73 * hash + Objects.hashCode(this.myComments);
        return hash;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) 
        {
            return true;
        }
        if (this.getId() == getId()) 
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
        final Person other = (Person) obj;

        if (this.id != other.id) 
        {
            return false;
        }
        if (Double.doubleToLongBits(this.myRating) != Double.doubleToLongBits(other.myRating)) 
        {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) 
        {
            return false;
        }
        if (!Objects.equals(this.personLink, other.personLink))
        {
            return false;
        }
        if (!Objects.equals(this.myComments, other.myComments)) 
        {
            return false;
        }
        return Objects.equals(this.imageUrls, other.imageUrls);
    }

    @Override
    public String toString() 
    {
        return "Person{" + "score=" + score + ", queryName=" + queryName + ", name=" + name + ", id=" + id + ", imageUrls=" + imageUrls + ", personLink=" + personLink + ", myRating=" + myRating + ", myComments=" + myComments + '}';
    }

    public String toPersonString() 
    {
        StringBuilder imageLinks = new StringBuilder();
        StringBuilder allComments = new StringBuilder();//StringBuilder is much more efficiant than +=

        String rating;
        String comments;

    //Checks if information is set, prints appropriate info 
        if (!(imageUrls.isEmpty()))
        {
            imageLinks.append("\n\tImageUrls : "); //Places each url on new line
            for (int i = 0; i < imageUrls.size(); i++)
            {
                imageLinks.append("\n\t\t");
                imageLinks.append(imageUrls.get(i));
            }
        }
        else
        {
            imageLinks.append("\n\tNo Image Urls");
        }

        if (!(myRating == -1)) //-1 is the default value when rating not set 
        {
            rating = "Rating : " + myRating;
        } 
        else 
        {
            rating = "Rating : Not Set";
        }

        if (!(myComments.isEmpty())) 
        {
            allComments.append("\n\tComments :");

            for (String c : myComments) 
            {
                allComments.append("\n\t\t");
                allComments.append(c);
                
            }
            comments = allComments.toString();
        }
        else 
        {
            comments = "\n\tComments : No Comments";
        }

        return "\n\tName : " + name + ",\tId :" + id + ",\t" + rating + comments + "\n\tpersonLink :\n\t\t" + personLink + imageLinks.toString() + "\n";
    }

    public String toListString(int option)
    {
        String rating = "Not Set";
        if (!(myRating == -1))
        {
            rating = String.valueOf(myRating);
        }
        return String.format("%-10s %-30s %-20s %-20s %-20s", "  (" + option + ")", name, id, rating, score);
    }

    //Default comparator by score
    @Override
    public int compareTo(Person p) 
    {
        return Double.compare(p.score, score);
    }
    
    //Custom comparator by name
    public static Comparator<Person> nameComparator() 
    {
        return (Person o1, Person o2) -> o1.getName().compareToIgnoreCase(o2.getName());
    }
    
    //Custom comparator by person id
    public static Comparator<Person> idComparator()
    {
        return (Person o1, Person o2) -> Long.compare(o1.getId(), o2.getId());
    }
    
    //Custom comparator by person rating
    public static Comparator<Person> ratingComparator()
    {
        return (Person o1, Person o2) -> Double.compare(o1.getMyRating(), o2.getMyRating());
    }
}
