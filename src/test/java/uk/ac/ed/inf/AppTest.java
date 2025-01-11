package uk.ac.ed.inf;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    @Test
    public void everythingWorks() throws Exception {
        allTestsPass();
        System.out.println("----------------------------------\n" + ANSI_GREEN + "----------------------------------");
        appEmpty();
        appRuns();
        System.out.println(ANSI_GREEN + "\nAPP RUNS SUCCESSFULLY\n" + "----------------------------------\n"+ ANSI_RESET + "\nALL TESTS PASS");

    }

    @Test
    public void appRuns() throws Exception {
        //normal day
        System.out.println("Checking app runs normally:" + ANSI_BLACK);
        String[] args = { "2025-01-20", "https://ilp-rest-2024.azurewebsites.net/"};
        App app = new App();
        app.main(args);
        System.out.println(ANSI_GREEN );
        assertTrue(true);
    }

    @Test
    public void appEmpty() throws Exception{
        System.out.println("Checking app runs with empty day:" + ANSI_BLACK);
        String[] empty = new String[]{"2000-01-01", "https://ilp-rest-2024.azurewebsites.net/"};
        App app = new App();
        app.main(empty);
        System.out.println(ANSI_GREEN );
        assertTrue(true);
    }

    @Test
    public void allTestsPass() throws Exception {
        System.out.println(ANSI_PURPLE + "----------------------------------");
        LngLatHandlingTests lngLatHandlingTests = new LngLatHandlingTests();
        lngLatHandlingTests.allTestsPass();

        System.out.println("----------------------------------" + ANSI_BLUE + "\n----------------------------------");
        OrderValidatorTests orderValidatorTests = new OrderValidatorTests();
        orderValidatorTests.allTestsPass();

        System.out.println("----------------------------------" + ANSI_YELLOW + "\n----------------------------------");
        InputHandlerTests inputHandlerTests = new InputHandlerTests();
        inputHandlerTests.allTestsPass();
        RetrieverTests retrieverTests = new RetrieverTests();
        retrieverTests.allTestsPass();
    }



}
