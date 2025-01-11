package uk.ac.ed.inf;
import org.junit.Test;
import uk.ac.ed.inf.IO.RestInfoRetriever;

import java.net.URL;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RetrieverTests {
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Test
    public void allTestsPass() throws Exception {
        badURL();
        wrongURL();
    }

    @Test
    public void badURL() throws Exception {
        System.out.println("Checking with unavailable URL provided:");
        RestInfoRetriever retriever = new RestInfoRetriever();
        URL url = new URL("https://gogogoogogogog.com/");
        Exception exception = assertThrows(Exception.class, () -> {
            retriever.connect(url);
        });
        String expectedMessage = "Invalid URL entered - cannot be connected to";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println("Pass\n");
    }

    @Test
    public void wrongURL() throws Exception {
        System.out.println("Checking with wrong URL provided:"+ANSI_BLACK);
        String[] wrongURLargs = { "2025-01-20", "https://github.com/adamchpnm/PizzaDronz2024/"};
        App app = new App();
        Exception exception = assertThrows(Exception.class, () -> {
            app.main(wrongURLargs);
        });
        String expectedMessage = "Incorrect URL for rest end point";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(ANSI_YELLOW+"Pass\n");
    }

}
