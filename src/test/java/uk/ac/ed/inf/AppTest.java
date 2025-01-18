package uk.ac.ed.inf;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

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

//    remove comment and run to run all tests at once
    @Test
    public void everythingWorks() throws Exception {
        System.out.println(ANSI_RESET + "Running all unit tests");
        allUnitTestsPass();
        System.out.println("----------------------------------\n" + ANSI_RESET + "Running all system tests\n"+ ANSI_GREEN + "----------------------------------");
        runAllSystemTests();
        System.out.println(ANSI_GREEN + "\nAPP RUNS SUCCESSFULLY\n" + "----------------------------------\n"+ ANSI_RESET + "\nALL TESTS PASS");
    }

    @Test
    public void runAllSystemTests() throws Exception {
        EmptyDaySystemTest();
        NormalDaySystemTest();
        SystemLevelErrorTestBADURL();
        SystemLevelErrorTestDisconnectURL();
    }

    @RepeatedTest(10)
    public void NormalDaySystemTest() throws Exception {
        //normal day
        System.out.println("Checking app runs normally:" + ANSI_BLACK);
        String[] args = { generateRandomDayAfter().toString(), "https://ilp-rest-2024.azurewebsites.net/"};
        long start = System.currentTimeMillis();
        App.main(args);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println(ANSI_BLUE+"Complete app run through in " + timeElapsed + "ms"+ANSI_GREEN+"\n----------------------------------");
        assertTrue(true);
    }

    @RepeatedTest(10)
    public void EmptyDaySystemTest() throws Exception{
        System.out.println("Checking app runs with empty day:" + ANSI_BLACK);
        String[] empty = new String[]{generateRandomDayBefore().toString(), "https://ilp-rest-2024.azurewebsites.net/"};
        long start = System.currentTimeMillis();
        App.main(empty);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println(ANSI_BLUE+"Complete app run through in " + timeElapsed + "ms"+ANSI_GREEN+"\n----------------------------------");
        assertTrue(true);
    }

    @Test
    public void SystemLevelErrorTestBADURL(){
        System.out.println("Checking app throws proper error:" + ANSI_BLACK);
        String[] bad = new String[]{generateRandomDayAfter().toString(), "BADURLINPUT"};
        long start = System.currentTimeMillis();
        Exception exception = assertThrows(Exception.class, () -> App.main(bad));
        String expectedMessage = "Invalid URL provided";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println(ANSI_BLUE+"Error thrown and returned in " + timeElapsed + "ms"+ANSI_GREEN+"\n----------------------------------");
    }

    @Test
    public void SystemLevelErrorTestDisconnectURL(){
        System.out.println("Checking app throws proper error:" + ANSI_BLACK);
        String[] bad = new String[]{generateRandomDayAfter().toString(), "https://gogogoogogogog.com/"};
        long start = System.currentTimeMillis();
        Exception exception = assertThrows(Exception.class, () -> App.main(bad));
        String expectedMessage = "Invalid URL entered - cannot be connected to";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println(ANSI_BLUE+"Error throw and returned in " + timeElapsed + "ms"+ANSI_GREEN+"\n----------------------------------");
    }

    @Test
    public void allUnitTestsPass() throws Exception {
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

    public static LocalDate generateRandomDayAfter() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(25);
        return generateRandomDay(start, end);
    }

    public static LocalDate generateRandomDayBefore() {
        LocalDate end = LocalDate.now().minusDays(1);
        LocalDate start = end.minusDays(25);
        return generateRandomDay(start, end);
    }

    public static LocalDate generateRandomDay(LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end);
        return start.plusDays(new Random().nextInt((int) days + 1));
    }



}
