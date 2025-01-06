package uk.ac.ed.inf.IO;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static uk.ac.ed.inf.App.errorMessage;

/**
 * Class for obtaining information from the Rest Server
 */
public class RestInfoRetriever {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Simple constructor for the retriever, updates the mapper with required modules for deserializing
     */
    public RestInfoRetriever(){
        mapper.findAndRegisterModules();
    }

    /**
     * Establishes HTTP connection with the website by performing the get request
     * @param url = the provided url to the rest server
     */
    public void connect(URL url){
        try{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() != 200) {
                throw new IOException("HttpResponseCode: " + conn.getResponseCode());
            }
        }
        catch(IOException e){
            errorMessage("Invalid URL entered - cannot be connected to");
        }
    }
    /**
     * Reads JSON from the provided site and deserializes before returning an ArrayList of objects
     * @param site = the endpoint of the rest server intended to read and deserialize
     * @param obj = a string to classify what object to deserialize
     * @return an arraylist of the object eg an arrayList of orders,restaurants or namedRegions
     */
    public ArrayList<?> jsonReader(URL site, String obj) throws IOException {
        //Write all the JSON data into a string using a scanner
        String jsonRead = "";
        Scanner scanner = new Scanner(site.openStream());
        while (scanner.hasNext()) {
            jsonRead += scanner.nextLine();
//            System.out.println(jsonRead);
        }
        scanner.close();

        //Remove all orders with the error "PRICE_FOR_PIZZA_INVALID" before parsing to order
        //Each order is of the form: {"orderNo":"...","orderDate":"...","orderStatus":"...","orderValidationCode":"...","priceTotalInPence":2400,"pizzasInOrder":[...],"creditCardInformation":{...}}#
        String toUse = "";
        String toAdd = "";
        int nester = 0;
//        if (jsonRead.contains("orderNo")){
//            for (char e: jsonRead.toCharArray()) {
//                toAdd += e;
//                if (e == '{'){
//                    nester += 1;
//                } else if (e == '}'){
//                    nester -= 1;
//                }
//                if (nester == 0){
//    //                System.out.println(toAdd);
//                    if (!(toAdd.contains("PRICE_FOR_PIZZA_INVALID")||toAdd.contains("EMPTY_ORDER"))) {
//                        toUse += toAdd;
//                    } else {
//                        toUse = toUse.substring(0, toUse.length() - 1);
//                    }
//                    toAdd = "";
//
//                }
//
//            }

//            toUse.replaceAll("\n", "");
//            toUse.replaceAll(",,", ",");
//            System.out.println(jsonRead);
//            System.out.println(toUse);
//        }
        //Depending on object type, get mapper to deserialize and return arraylist of required objects
        if(obj.equals("Order")){
            jsonRead = jsonRead.replaceAll("VALID","VALID_BUT_NOT_DELIVERED");
            jsonRead = jsonRead.replaceAll("INVALID_BUT_NOT_DELIVERED","INVALID");
            jsonRead = jsonRead.replaceAll("PRICE_FOR_PIZZA_INVALID","UNDEFINED");
            jsonRead = jsonRead.replaceAll("EMPTY_ORDER","UNDEFINED");
            return mapper.readValue(jsonRead, new TypeReference<ArrayList<Order>>(){});
        }
        else if(obj.equals("Restaurant")){
            return mapper.readValue(jsonRead, new TypeReference<ArrayList<Restaurant>>(){});
        }
        else if (obj.equals("NamedRegion")){
            return mapper.readValue(jsonRead, new TypeReference<ArrayList<NamedRegion>>(){});
        }
        else{
            //Only need one NamedRegion - central
            ArrayList<NamedRegion>  temp = new ArrayList<>(1);
            temp.add(mapper.readValue(jsonRead, new TypeReference<NamedRegion>(){}));
            return temp;
        }
    }
}
