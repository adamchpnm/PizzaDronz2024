package uk.ac.ed.inf;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import uk.ac.ed.inf.IO.RestInfoRetriever;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class RetrieverTests {
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Test
    public void allTestsPass() throws Exception {
        badURL();
        wrongURL();
        validURL();
    }

    @Test
    public void badURL() throws Exception {
        System.out.println("Checking with unavailable URL provided:");
        RestInfoRetriever retriever = new RestInfoRetriever();
        URL url = new URL("https://gogogoogogogog.com/");
        Exception exception = assertThrows(Exception.class, () -> retriever.connect(url));
        String expectedMessage = "Invalid URL entered - cannot be connected to";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println("Pass\n");
    }

    @Test
    public void wrongURL() {
        System.out.println("Checking with wrong URL provided:"+ANSI_BLACK);
        String[] wrongURLargs = { "2025-01-20", "https://github.com/adamchpnm/"};
        Exception exception = assertThrows(Exception.class, () -> App.main(wrongURLargs));
        String expectedMessage = "Incorrect URL for rest end point";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        System.out.println(ANSI_YELLOW+"Pass\n");
    }

    @Test
    public void validURL() throws IOException {
        RestInfoRetriever retriever = new RestInfoRetriever();
        URL url = new URL("https://ilp-rest-2024.azurewebsites.net/");
        testRetriever(retriever,url);
    }

    public void testRetriever(RestInfoRetriever retriever, URL url) throws IOException {
        ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>) retriever.jsonReader(new URL(url + "/restaurants"), "Restaurant");
        ArrayList<NamedRegion> noZones = ((ArrayList<NamedRegion>)retriever.jsonReader(new URL(url + "/noFlyZones"), "NamedRegion"));
        NamedRegion central = ((ArrayList<NamedRegion>) retriever.jsonReader(new URL(url + "/centralArea"), "Central")).get(0);
        ArrayList<Order> ordersAll = (ArrayList<Order>) retriever.jsonReader(new URL(url+ "/orders"), "Order");
        assertTrue(true);
    }

}
