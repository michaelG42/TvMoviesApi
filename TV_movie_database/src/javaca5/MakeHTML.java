package javaca5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MakeHTML {

    private String fileName;
    private String head;

    public MakeHTML(String FileName, String title)
    {
        this.fileName = FileName;
        this.head
                = "    <head>\n"
                + "        <title>" + title + "</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "        <link rel=\"stylesheet\" type=\"text/css\" href=\"Style.css\">\n"
                + "    </head>\n<body>\n";
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String FileName) 
    {
        this.fileName = FileName;
    }

    public String getHead()
    {
        return head;
    }

    public void setHead(String head)
    {
        this.head = "\n<head>" + head + "</head>\n";
    }

    public void appendPersonToHtml(Person p)
    {
        String imageUrl = "noImage.jpg";//if no images used this default one
        String rating = "Not Set";

        //Formats score to 2 decimal places
        DecimalFormat df = new DecimalFormat("#.##");
        double score = Double.valueOf(df.format(p.getScore()));

        StringBuilder comments = new StringBuilder();

        if (p.getMyComments() != null)
        {
            for (String comment : p.getMyComments()) 
            {
                comments.append(comment);
                comments.append("<br>");
            }
        } 
        else
        {
            comments.append("No Comments");
        }

        if (p.getMyRating() > 0) 
        {
            rating = String.valueOf(p.getMyRating());
        }
        if (!p.getImageUrls().isEmpty()) 
        {
            imageUrl = p.getImageUrls().get(0);
        }
        //adds Person details to html fieldset to be written to file
        String template
                = "        \n<fieldset class = container>\n"
                + "        <legend>\n"
                + "            " + p.getName() + "\n"
                + "        </legend>\n"
                + "        <img src = " + imageUrl + "  alt =\"" + p.getName() + "\" >\n"
                + "        <a href = " + p.getPersonLink() + " > View On TvMaze </a>\n"
                + "        <h3>ID : " + p.getId() + "</h3>\n"
                + "        <h3>Rating : " + rating + "</h3>\n"
                + "        <h4>Query Name : " + p.getQueryName() + "</h4>\n"
                + "        <h4>Search Score : " + score + "</h4>\n"
                + "        <fieldset class = comments>\n"
                + "         <legend> Comments </legend> \n"
                + "          <p>\n"
                + "              " + comments + "\n"
                + "          </p>\n"
                + "        </fieldset>\n"
                + "        </fieldset>\n";

        FileWriter fw = null;
        try
        {
            fw = new FileWriter(fileName, true);//true means it will append
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println(template);
            out.flush();
            out.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(MakeHTML.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally 
        {
            try
            {
                fw.close();
            } 
            catch (IOException ex)
            {
                Logger.getLogger(MakeHTML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void closeBody()
    {
        FileWriter fw = null;
        try
        {
            fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println("</body>");
            out.flush();
            out.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(MakeHTML.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                fw.close();
            } 
            catch (IOException ex)
            {
                Logger.getLogger(MakeHTML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void MakeNewHTMLFile()
    {
        File htmlFile = new File(fileName);
        BufferedWriter writer = null;
        try 
        {
            writer = new BufferedWriter(new FileWriter(htmlFile));
            writer.write(head);
            writer.close();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(MakeHTML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
