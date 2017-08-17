package javaca5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class MainApp
{

    static Scanner in = new Scanner(System.in);
    private static String url = "http://api.tvmaze.com/search/people?q=";
    private static PersonStore personStore = new PersonStore();
    private static int sortOrder = 0;

    public static void main(String[] args)
    {

        System.out.println("Welcome to the tv movie database");
        System.out.println("This Project was created by Michael Grinnell and Ryan Dullaghan");
        System.out.println("For Object Orientatied Programming Ca5");
        System.out.println("Lecturer : Derek Flood\n");

        promptEnterKey();//Press enter to continue method

        loadFromFile();
        mainMenu();
    }

    public static void mainMenu()
    {
        boolean finished = false;
        while (!finished) 
        {
            System.out.println("\t\t\t" + getDateTime());//Print formated Date    
            System.out.println("\t //****************** Main Menu ******************\\\\");
            
            System.out.println("\t//    Please choose from the following Options\t   \\\\\n");
            System.out.println("(1)Search for person, (2)List all Persons, (3)Edit person, (0)Save and exit");

            int choice = Validation.validateInt("");//ensures correct input of an int
            switch (choice) 
            {
                case 0:
                    saveToFile();
                    System.out.println("\tFile Saved");
                    System.out.println("\tGoodbye, Have a nice day");
                    finished = true;
                    break;

                case 1:
                    getSearchName();
                    break;

                case 2:
                    System.out.println("\t//*************************** All Local People ***************************\\\\");
                    listFoundPersons(personStore.getPeopleList());
                    break;

                case 3:
                    editPersonMenu();
                    break;

                default:
                    System.out.println(choice + " is not a valid option");
                    break;
            }
        }
    }

    public static void editPersonMenu()
    {
        System.out.println("To edit a person you can either enter an id or a name to search");
        String searchTearm = in.nextLine();

        if (Validation.isNumeric(searchTearm)) // if search term is a number it will search for ID
        {
            Person p = personStore.findById(Long.parseLong(searchTearm)); // will return null if id not found          
            if (p != null)
            {
                editPerson(p);
            } 
            else 
            {
                System.out.println("ID has not been found");
            }
        } 
        else 
        {
            searchForPersonsLocal(searchTearm);
        }
    }

    public static void getSearchName() 
    {
        System.out.println("\t\t //*****Search Person*****\\\\");
        System.out.println("\t\tPlease enter a name to search");
        String searchTearm = in.nextLine();
        searchForPersonsLocal(searchTearm);
    }

    public static void searchForPersonsLocal(String searchTearm) 
    {

        ArrayList<Person> found = new ArrayList<>();
        for (Person p : personStore.getPeopleList())// all people in local store without duplicates
        {
            if (Pattern.compile(Pattern.quote(searchTearm),
                    Pattern.CASE_INSENSITIVE).matcher(p.getName()).find())//uses regex.Pattern to ignore case
            {
                found.add(p);
            }
        }
        System.out.println("\t//*************************** Reults for " + searchTearm + " ***************************\\\\");
        if (found.isEmpty())
        {
            searchForPersonsApi(searchTearm);//If no results in local store will search api
        } 
        else 
        {
            listFoundPersons(found);
        }
    }

    public static void searchForPersonsApi(String searchTearm) 
    {

        ArrayList<Person> foundPersons = new ArrayList<>();
        try 
        {
            String encode = URLEncoder.encode(searchTearm, "UTF-8");
            URL website = new URL(url + encode);
            InputStream siteIn = website.openStream();
            JsonReader reader = Json.createReader(siteIn);
            JsonArray allResults = reader.readArray();

            for (int i = 0; i < allResults.size(); i++) 
            {

                Person p = getPersonFromJsonArray(allResults, i, searchTearm);
                foundPersons.add(p);
            }
            personStore.addPersons(searchTearm, foundPersons);

            //Sort List by Score
            Collections.sort(foundPersons);
            listFoundPersons(foundPersons);
            saveToFile();

        } 
        catch (MalformedURLException ex) 
        {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (UnsupportedEncodingException ex) 
        {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Person getPersonFromJsonArray(JsonArray allResults, int index, String searchTearm) 
    {
        JsonObject obj = allResults.getJsonObject(index);

        double score = obj.getJsonNumber("score").doubleValue();

        JsonObject person = obj.getJsonObject("person");

        long id = person.getJsonNumber("id").longValue();

        String personLink = person.getString("url");

        String name = person.getString("name");

        JsonObject imageUrls;
        ArrayList<String> imgUrls = new ArrayList<>();
        if (person.get("image") instanceof JsonObject) 
        {
            imageUrls = person.getJsonObject("image");
            if (!imageUrls.isEmpty())
            {
                if (imageUrls.containsKey("medium")) 
                {
                    String medium = imageUrls.getString("medium");
                    imgUrls.add(medium);
                }
                if (imageUrls.containsKey("original"))
                {
                    String original = imageUrls.getString("original");
                    imgUrls.add(original);
                }
            }
        }
        Person p = new Person(score, searchTearm, name, id, imgUrls, personLink);

        return p;
    }

    public static void listFoundPersons(List<Person> people)
    {

        int choice;
        ArrayList<Long> foundIds = new ArrayList<>();//ArrayList for Ids of people found
        if (!people.isEmpty())//if there people have been found
        {
            String heading = String.format("%-6s %-29s %-22s %-18s %-20s", "|Option|", "  |Name|", "   |ID|", "|Rating|", " |SearchScore|");
            System.out.println(heading);

            int i = 0;
            for (Person p : people) 
            {
                i++;
                foundIds.add(p.getId());// adds person id to arraylist of found ids
                System.out.println(p.toListString(i));
            }
            
            choice = Validation.validateInt("\n\t--Enter option number of person to view and edit,\n"
                    + "\t--Type " + (foundIds.size() + 1) + " to exit,\n"
                    + "\t--Type " + (foundIds.size() + 2) + " To Export to html\n"
                    + "\t--Press (0) to change the sort order");
            
        }
        else// if no people have been found
        {
            System.out.println("\t***** No Persons found *****");
            System.out.println("\tYou will Be returned to the main menu");
            choice = 1;// to exit this menu
        }

        if (choice == 0)// change the sort order of the list
        {
            sortPersons(people);
            listFoundPersons(people);
        }
        else if ((choice <= foundIds.size()) && (choice > 0))//if choice is an option on the list, ie greater than 0 and less than the size of the results
        {
            editPerson(personStore.findById(foundIds.get(choice - 1), people));//choice-1 is index of person id in arraylist found ids
            
        }
        else if (choice > (foundIds.size() + 2) || choice < 0)//If choice is outside the range
        {
            System.out.println("\t" +choice + " is not a valid option");
            listFoundPersons(people);//recursion if choice is invalid
        }
        else if (choice == (foundIds.size() + 2)) 
        {
            exportToHtml(people);
        }
    }

    public static List sortPersons(List people) 
    {

        if (sortOrder == 8)
        {
            sortOrder = 0;//If sort order reaches 8, it is reset to 0
        }
        sortOrder++;// then increments to 1, so it can reach 8 but will be reset next turn

        
        switch (sortOrder)// sort Order will cycle between 1 and 8
        {
            case 1:
                System.out.println("\t//*************************** Sorted by name Ascending ***************************\\\\");
                Collections.sort(people, Person.nameComparator());
                break;
                
            case 2:
                System.out.println("\t//*************************** Sorted by name Decending ***************************\\\\");
                Collections.sort(people, Person.nameComparator());
                Collections.reverse(people);
                break;
                
            case 3:
                System.out.println("\t//*************************** Sorted by ID Ascending ***************************\\\\");
                Collections.sort(people, Person.idComparator());
                break;
                
            case 4:
                System.out.println("\t//*************************** Sorted by ID Decending ***************************\\\\");
                Collections.sort(people, Person.idComparator());
                Collections.reverse(people);
                break;
                
            case 5:
                System.out.println("\t//*************************** Sorted by Rating Ascending ***************************\\\\");
                Collections.sort(people, Person.ratingComparator());
                break;
                
            case 6:
                System.out.println("\t//*************************** Sorted by Rating Decending ***************************\\\\");
                Collections.sort(people, Person.ratingComparator());
                Collections.reverse(people);
                break;
                
            case 7:
                System.out.println("\t//*************************** Sorted by Score Ascending ***************************\\\\");
                Collections.sort(people);
                break;
                
            case 8:
                System.out.println("\t//*************************** Sorted by Score Decending ***************************\\\\");
                Collections.sort(people);
                Collections.reverse(people);
                break;
        }
        return people;
    }

    public static void editPerson(Person p) 
    {
        boolean finished = false;
        while (!finished) 
        {System.out.println("\t//*************************** Edit Person Menu ***************************\\\\");
            System.out.println(p.toPersonString());
            int choice = Validation.validateInt("\n\t\t****** Please choose an option ****** \n\t(1)Add Rating, (2)Add comments, (3)Remove comments, (0)Exit");

            switch (choice) 
            {
                case 0:
                    finished = true;
                    break;
                    
                case 1:
                    addRating(p);
                    break;
                    
                case 2:
                    addComments(p);
                    break;
                    
                case 3:
                    removeComments(p);
                    break;
                default:
                    System.out.println(choice + " is not a valid option");
                    break;
            }
        }

    }

    public static void addRating(Person p)
    {
        System.out.println("\t\t***** Add Rating *****");
        double rating = Validation.validateRating("\tPlease enter a rating for " + p.getName()); // validate input of double

        boolean confirmed = Validation.validateYesNo("\tAre you sure you wish to set " + p.getName() + "'s rating to " + rating + " Y/N");// confirm yes no

        if (confirmed) 
        {
            System.out.println("\t" +p.getName() + "'s rating has been set to " + rating);
            p.setMyRating(rating);
        }
        else 
        {
            System.out.println("\t" + p.getName() + "'s rating has not been changed");

            if (p.getMyRating() == -1) 
            {
                System.out.println("\tCurrent rating is Not set");
            } 
            else 
            {
                System.out.println("\tCurrent rating is " + p.getMyRating());
            }

        }

    }
    

    public static void addComments(Person p) 
    {
        System.out.println("\t\t***** Add Comments *****");
       
        ArrayList<String> comments = new ArrayList<>();
        
        String choice = Validation.validateComment("\tPlease enter comments for " + p.getName() + " Type exit to finish");
        

        while (!choice.equalsIgnoreCase("exit")) 
        {
            comments.add(choice);
            choice = Validation.validateComment("\tComment added");
        }

        boolean confirmed = Validation.validateYesNo("\tAre you sure you wish to add these comments to " + p.getName() + "\n\t" + comments + "\n\tY/N to confirm");

        if (confirmed) 
        {
            p.setMyComments(comments);
            System.out.println("\tYour comments have been saved");
        } 
        else 
        {
            System.out.println("\tComments have NOT been saved");
        }
    }

    public static void removeComments(Person p)
    {
        System.out.println("\t\t***** Remove Comments *****");
        if (p.getMyComments().size() > 0) //if there are any comments
        {
            boolean finished = false;
            ArrayList comments = p.getMyComments();

            while (!finished) 
            {
                System.out.println("\tWhich comment would you like to remove?");
                System.out.println("\tType 0 to exit");

                for (int i = 0; i < comments.size(); i++) 
                {
                    System.out.println("(" + (i + 1) + ") " + comments.get(i));//gives comments an option number starting at 1
                }
                
                int choice;
                if (p.getMyComments().isEmpty())//if all comments have been removed
                {
                   System.out.println("\t\t***** No Comments to remove *****");
                   choice = 0; // choice to exit
                }
                else
                {
                    choice = Validation.validateInt("");
                }               

                if (choice > 0 && choice < comments.size() + 1) 
                {
                    boolean confirmed = Validation.validateYesNo("\tAre you sure you wish to remove the following comment (Y/N) \n\t" + comments.get(choice - 1));

                    if (confirmed)
                    {
                        comments.remove(choice - 1);
                        System.out.println("\tComment removed");
                    }
                    else 
                    {
                        System.out.println("\tComment NOT removed");
                    }
                    p.setMyComments(comments);
                    
                }
                else if (choice == 0)
                {
                    System.out.println("\tYou will be returned to the previous menu");
                    finished = true;
                } 
                else 
                {
                    System.out.println("\tNot a valid option");
                }
            }
        } 
        else 
        {
            System.out.println("\tNo Comments to remove");
        }
    }

    public static void saveToFile() 
    {
        FileOutputStream fo = null;
        DataOutputStream dos = null;
        try
        {
            File file = new File("persons.dat");
            fo = new FileOutputStream(file);
            dos = new DataOutputStream(fo);

            for (Map.Entry<String, ArrayList<Person>> entry : personStore.getPeople().entrySet())
            {

                String queryName = entry.getKey();

                ArrayList<Person> persons = entry.getValue();
                String resultSize = String.valueOf(persons.size());

                writeToFile(pad(queryName, 24));
                writeToFile(pad(resultSize, 4));

                for (Person p : persons) 
                {
                    savePersonToFile(p);
                }
            }

        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally 
        {
            try
            {
                fo.close();
                dos.close();
            }
            catch (IOException ex) 
            {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void savePersonToFile(Person p)
    {
        //gets the string value from person, pads the word  then writes to file
        writeToFile(pad(String.valueOf(p.getScore()), 16));
        writeToFile(pad(p.getName(), 24));
        writeToFile(pad(String.valueOf(p.getId()), 16));
        writeToFile(pad(String.valueOf(p.getImageUrls().size()), 4));

        if (!(p.getImageUrls().isEmpty()))
        {
            for (int i = 0; i < p.getImageUrls().size(); i++)
            {
                writeToFile(pad(p.getImageUrls().get(i), 100));
            }
        }

        writeToFile(pad(p.getPersonLink(), 100));
        writeToFile(pad(String.valueOf(p.getMyRating()), 16));

        String numOfComments;

        if (p.getMyComments() == null) 
        {
            numOfComments = "0";
        }
        else 
        {
            numOfComments = String.valueOf(p.getMyComments().size());
        }

        writeToFile(pad(numOfComments, 4));

        if (!(p.getMyComments() == null)) 
        {
            for (int i = 0; i < p.getMyComments().size(); i++) 
            {
                writeToFile(pad(p.getMyComments().get(i), 100));
            }
        }
    }

    private static void writeToFile(String paddedWord)
    {
        try
        {
            File file = new File("persons.dat");
            FileOutputStream fo = new FileOutputStream(file, file.exists());//will append if file exists
            DataOutputStream dis = new DataOutputStream(fo);
            dis.writeChars(paddedWord);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String readString(DataInputStream dis, int size) throws IOException
    {
        
        byte[] makeBytes = new byte[size * 2];// 2 bytes per char
        dis.read(makeBytes);  // read size characters (including padding)
        return depad(makeBytes);
    }

    public static String depad(byte[] read) 
    {
        //word = word.replace("*", "");
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < read.length; i += 2)
        {
            char c = (char) (((read[i] & 0x00FF) << 8) + (read[i + 1] & 0x00FF));

            if (c != '*')
            {
                word.append(c);
            }
        }
        return word.toString();
    }

    public static String pad(String value, int i) 
    {
        int originalLength = value.length();
        StringBuilder padded = new StringBuilder();
        
        for (int j = 0; j < (i -originalLength); j++) 
        {
          padded.append("*");  
        }
        
        padded.append(value);
        
        return padded.toString();
    }

    //Why did the programmer quit his job?
    //Because he didn't get arrays
    public static void loadFromFile()
    {

        File file = new File("persons.dat");
        if (file.exists())
        {

            try 
            {

                FileInputStream fo = new FileInputStream(file);
                DataInputStream dis = new DataInputStream(fo);

                while (dis.available() != 0)
                {

                    try 
                    {
                        String queryName = readString(dis, 24);
                        int peopleResultsSize = Integer.parseInt(readString(dis, 4));

                        ArrayList<Person> people = new ArrayList<>();

                        for (int i = 0; i < peopleResultsSize; i++)
                        {

                            double score = Double.parseDouble(readString(dis, 16));
                            String name = readString(dis, 24);
                            long id = Long.parseLong(readString(dis, 16));
                            ArrayList<String> imageUrls = new ArrayList();
                            int imageUrlsSize = Integer.parseInt(readString(dis, 4));

                            for (int j = 0; j < imageUrlsSize; j++) 
                            {
                                imageUrls.add(readString(dis, 100));
                            }

                            String personLink = readString(dis, 100);
                            double myRating = Double.parseDouble(readString(dis, 16));

                            int commentsSize = Integer.parseInt(readString(dis, 4));
                            ArrayList<String> comments = new ArrayList<>();

                            for (int j = 0; j < commentsSize; j++)
                            {
                                comments.add(readString(dis, 100));
                            }

                            Person p = new Person(score, queryName, name, id, imageUrls, personLink);
                            p.setMyRating(myRating);
                            p.setMyComments(comments);

                            people.add(p);

                        }

                        personStore.addPersons(queryName, people);

                    } 
                    catch (IOException ex)
                    {
                        Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                dis.close();
                fo.close();
            } catch (IOException ex)
            {
                Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static String getDateTime() 
    {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
        String nowFormat = formatter.format(now);

        return nowFormat;
    }

    public static void promptEnterKey() 
    {
        System.out.println("Press \"ENTER\" to begin...");
        in.nextLine();
    }

    public static void exportToHtml(List<Person> people) 
    {
        System.out.println("\t\t***** Make HTML *****");
        StringBuilder fileName = new StringBuilder("HTML/");
        System.out.println("\tPlease enter the name of your HTML file, it will be appended with .html");

        fileName.append(in.next());
        fileName.append(".html");
        in.nextLine();//consume next line character

        System.out.println("\tPlease Enter a Title");
        String title = in.nextLine();
        MakeHTML html = new MakeHTML(fileName.toString(), title);
        html.MakeNewHTMLFile();
        System.out.println("\tFile made");
        for (Person p : people) 
        {
            html.appendPersonToHtml(p);
        }
        html.closeBody();
        System.out.println("\tFile succsessfully Exported");
    }

}
