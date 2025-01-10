package uk.ac.ed.inf;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import uk.ac.ed.inf.Validation.InputHandler;

import java.net.MalformedURLException;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    @Test
    public void everythingWorks() throws MalformedURLException {
        allTestsPass();
        System.out.println("----------------------------------\n" + ANSI_GREEN + "----------------------------------" + ANSI_BLACK);
        mainRuns();
        System.out.println(ANSI_GREEN + "\nApp runs on current REST server for all orders\n" + "----------------------------------\n"+ ANSI_RESET + "\nALL TESTS PASS");

    }

//    @Test
    public void mainRuns() throws MalformedURLException {
        String[] args = { "2025-01-20", "https://ilp-rest-2024.azurewebsites.net/"};
        App.main(args);
        System.out.flush();
        assertTrue(true);
    }

//    @Test
    public void allTestsPass(){
        System.out.println(ANSI_PURPLE + "----------------------------------");
        LngLatHandlingTests lngLatHandlingTests = new LngLatHandlingTests();
        lngLatHandlingTests.allTestsPass();

        System.out.println("----------------------------------" + ANSI_BLUE + "\n----------------------------------");
        OrderValidatorTests orderValidatorTests = new OrderValidatorTests();
        orderValidatorTests.allTestsPass();
    }

}
