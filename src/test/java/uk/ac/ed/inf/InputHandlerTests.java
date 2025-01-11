package uk.ac.ed.inf;
import org.junit.Test;
import uk.ac.ed.inf.Validation.InputHandler;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InputHandlerTests {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    @Test
    public void allTestsPass() throws Exception {
        normalArgument();
        noArguments();
        oneArgument();
        incorrectDate();
        incorrectURL();
    }

    @Test
    public void normalArgument() throws Exception {
        System.out.println("Checking normal input:");
        String[] args = { "2025-01-20", "https://ilp-rest-2024.azurewebsites.net/"};
        assertDoesNotThrow(() -> {
            InputHandler noErrorInstance = new InputHandler(args);
        });
        InputHandler testInstance = new InputHandler(args);
        assertEquals(args[0],testInstance.getOrderDate().toString());
        assertEquals(args[1],testInstance.getWebsite().toString());
        System.out.println("Pass\n");
    }

    @Test
    public void noArguments(){
        System.out.println("Checking no input:");
        String[] noArgs = new String[]{};
        assertDoesNotThrow(() -> {
            InputHandler noErrorInstance = new InputHandler(noArgs);
        });
        System.out.println("Pass\n");
    }

    @Test
    public void oneArgument(){
        System.out.println("Checking one argument input:");
        String[] oneArg = new String[]{"2025-01-20"};
        Exception exception = assertThrows(Exception.class, () -> {
            InputHandler errorInstance = new InputHandler(oneArg);
        });
        String expectedMessage = "Incorrect number of arguments passed";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println("Pass\n");
    }

    @Test
    public void incorrectDate(){
        System.out.println("Checking input with incorrect date format:");
        String[] badDate = new String[]{"20-01-2025", "https://ilp-rest-2024.azurewebsites.net/"};
        Exception exception = assertThrows(Exception.class, () -> {
            InputHandler errorInstance = new InputHandler(badDate);
        });
        String expectedMessage = "Date not in Localdate format YYYY-MM-DD";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println("Pass\n");
    }

    @Test
    public void incorrectURL(){
        System.out.println("Checking input with incorrect URL format:");
        String[] badURL = new String[]{"2025-01-10", "htps:/ilp-rest-2024.azurewebsites.bet/"};
        Exception exception = assertThrows(Exception.class, () -> {
            InputHandler errorInstance = new InputHandler(badURL);
        });
        String expectedMessage = "Invalid URL provided";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println("Pass\n");
    }

}