package javaca5;

import java.util.Scanner;

/**
 * We have place the methods for validation of input in this Class Scanner
 * object could not be global for testing purposes
 */
public class Validation
{

    private static Scanner in;
    //Takes a String to print a message, User must enter a valid double to be returned
    public static double validateDouble(String str) 
    {
        if(in == null)
        {
         in = new Scanner(System.in);
        }
        double d = 0;
        boolean valid = false;
        while (!valid) 
        {
            System.out.println(str);
            if (!in.hasNextDouble())
            {
                System.out.println("Not a valid number");
                in.nextLine();
            }
            else
            {
                d = in.nextDouble();
                valid = true;
            }
        }
        return d;
    }
    
    //Takes a String to print a message, User must enter a valid Int to be returned
    public static int validateInt(String str)
    {
        if(in == null)
        {
         in = new Scanner(System.in);
        }
        int i = 0;
        boolean valid = false;
        while (!valid) 
        {
            System.out.println(str);
            if (!in.hasNextInt())
            {
                System.out.println("Not a valid number");
                in.next();
            } 
            else 
            {
                i = in.nextInt();
                in.nextLine();
                valid = true;
            }
        }
        
        return i;
    }

    //Takes a String to print a message, 
    //User must enter a valid Y or N for Yes or No a corisponding boolean is returned
    public static boolean validateYesNo(String str)
    {
        if(in == null)
        {
         in = new Scanner(System.in);
        }
        boolean YesNo = false;
        boolean valid = false;

        while (!valid) 
        {
            System.out.println(str);
            String choice = in.next();
            if (choice.equalsIgnoreCase("Y")) 
            {
                valid = true;
                YesNo = true;
            }
            else if (choice.equalsIgnoreCase("N")) 
            {
                valid = true;
                YesNo = false;
            }
            else 
            {
                System.out.println("Invalid input");
            }
        }
        return YesNo;
    }
    public static void reset()
    {
        in = new Scanner(System.in);
    }
    //ensures rating is a valid double between 0 and 100
    public static double validateRating(String str)
    {
        if(in == null)
        {
         in = new Scanner(System.in);
        }
        
        double d = 0;
        boolean valid = false;
        while (!valid)
        {
            System.out.println(str);
            if (!in.hasNextDouble()) 
            {
                System.out.println("Not a valid number");
                in.nextLine();
            }
            else 
            {
                d = in.nextDouble();

                if (d >= 0 && d <= 100) 
                {
                    valid = true;
                } 
                else 
                {
                    System.out.println("Rating must be between 0 and 100");
                }
            }
        }
        return d;
    }
    
    //comments cannot contain special characters as when being exported to html it was possible to add code such as <h1> or <br>
    public static String validateComment(String str)
    {
        
        
        if(in == null)
        {
         in = new Scanner(System.in);
        }
          
        String comment = "";
        boolean valid = false;
        while (!valid)
        {
            System.out.println(str);
            comment = in.nextLine();
            if (!comment.matches("[a-zA-Z0-9.\\s\\r\\n?|\\n]*")) 
            {
                str = "Comments cannot contain special charaters";  
                
            }
            else 
            {   
                valid = true;   
            }
        }
        return comment;
    }
    
    //Uses regEx to check if input is numeric
    public static boolean isNumeric(String input)
    {
        return input.matches("[-+]?\\d+(\\.\\d+)?");
    }
}
