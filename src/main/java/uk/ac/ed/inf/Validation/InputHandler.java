package uk.ac.ed.inf.Validation;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import static uk.ac.ed.inf.App.errorMessage;

/**
 * Class for handling commandline arguments
 */

public class InputHandler {
    private static LocalDate orderDate;
    private static URL website;

    /**
     * Constructor - will immediately validate arguments
     * @param args = commandline arguments to be validated
     */

    public InputHandler(String[] args) {

        //Check for number of arguments provided
        if(args.length == 0){
            System.err.println("No arguments provided. Running with below default arguments:\nDate: "+LocalDate.now()+" (current date)\nWebsite: https://ilp-rest-2024.azurewebsites.net/ (default website)");
            String[] temp = { LocalDate.now().toString(), "https://ilp-rest-2024.azurewebsites.net/"};
            args = temp;
        } else if(args.length!= 2) {
            errorMessage("Incorrect number of arguments passed");
        }

        //Check date is in correct LocalDateTime format
        try{
            orderDate = LocalDate.parse(args[0]);
        }
        catch(DateTimeParseException e){
            errorMessage("Date not in Localdate format YYYY-MM-DD");
        }

        //Check URL is a valid URL
        try{
            website = new URL(args[1]);

        }
        catch(MalformedURLException e){
            errorMessage("Invalid URL provided");
        }
    }

    public LocalDate getOrderDate() {return orderDate;}
    public URL getWebsite() {return website;}
}
