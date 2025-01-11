package uk.ac.ed.inf;

import uk.ac.ed.inf.IO.FileCreator;
import uk.ac.ed.inf.IO.RestInfoRetriever;
import uk.ac.ed.inf.Pathing.FlightPath;
import uk.ac.ed.inf.Pathing.Move;
import uk.ac.ed.inf.Pathing.PathFinder;
import uk.ac.ed.inf.Validation.InputHandler;
import uk.ac.ed.inf.Validation.OrderValidator;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.data.*;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

/**
 * Main class - startpoint of application
 *
 *
 * ross points
 * - possible easier way to make files and directories!
 *
 * - maybe pull URL into a function?
 * - line 107 for loop can maybe be done in a stream
 * - 3 different file creation methods
 * - account for dates w/ no orders
 * - do central area checks
 * - make sure to just return null in the case that an order has no valid path instead of throwing an exception
 */
public class App 
{
    private static ArrayList<Order> orders = new ArrayList<>();
    private static ArrayList<Order> ordersAll;
    private static ArrayList<Restaurant> restaurants;
    private static ArrayList<NamedRegion> noZones;
    private static NamedRegion central;
    private static ArrayList<LngLat> route = new ArrayList<>();
    private static ArrayList<Move> allMoves = new ArrayList<>();
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * Main start point in application- will call classes from across the application to validate commandline arguments,
     * process orders, route to restaurants and write results to files
     * @param args = commandline arguments
     */

    public static void main(String[] args ) throws Exception
    {
        System.out.println("Welcome to PizzaDronz flightpathing and logging system");

        //Validate commandline arguments and get order date and URL
        InputHandler inputs = new InputHandler(args);
        LocalDate orderDate = inputs.getOrderDate();
        URL url = inputs.getWebsite();
        final DayOfWeek day = orderDate.getDayOfWeek();

        System.out.println("Connecting to rest server...");
        // Create RestInformationRetriever object to obtain  data from the rest service
        RestInfoRetriever retriever = new RestInfoRetriever();
        retriever.connect(url);

        //Get data from respective endpoints
        try {
            restaurants = ((ArrayList<Restaurant>)retriever.jsonReader(new URL(url + "/restaurants"), "Restaurant"));
            noZones = ((ArrayList<NamedRegion>)retriever.jsonReader(new URL(url + "/noFlyZones"), "NamedRegion"));
            central = ((ArrayList<NamedRegion>) retriever.jsonReader(new URL(url + "/centralArea"), "Central")).get(0);
            ordersAll = (ArrayList<Order>) retriever.jsonReader(new URL(url+ "/orders"), "Order");
//            orders = (ArrayList<Order>) retriever.jsonReader(new URL(url+ "/orders/" + orderDate), "Order");
        }
        catch(Exception e){errorMessage("Incorrect URL for rest end point");}
        System.out.println("Connected to rest server");

        //Filter for only orders on specified date
        for (Order e: ordersAll) {
            if (e.getOrderDate().equals(orderDate)){
                orders.add(e);
            }
        }
        if (orders.size() == 0){
            System.err.println("No orders found for specified date. Result files will be empty");
        }
        //Instantiate file creator with given order date
        FileCreator fileGenerator = new FileCreator(orderDate);

        //Filter restaurants to only consider restaurants that are open that day
        Restaurant[] filteredRestaurants = Arrays
                .stream(restaurants.toArray(new Restaurant[0]))
                .filter(r-> Arrays.stream(r.openingDays()).toList().contains(day))
                .toArray(Restaurant[]::new);

        //Configure flightRouter with only valid restaurants and NamedRegions
        PathFinder flightRouter = new PathFinder(filteredRestaurants, noZones.toArray(new NamedRegion[0]), central);

        //Generate route and store path for each restaurant
        System.out.println("Generating routes...");
        flightRouter.createAllRoutes();
        System.out.println("Routes generated");

        OrderValidator validator = new OrderValidator();
        HashMap<Pizza, Restaurant> restaurantsAndItems = new HashMap<>();

        //Create map of all unique pizzas and their accompanying restaurant - used when obtaining restaurant for an order
        for(Restaurant restaurant: filteredRestaurants){
            List<Pizza> pizzas = new ArrayList(Arrays.stream(restaurant.menu()).toList());
            pizzas.forEach(p -> restaurantsAndItems.put(p, restaurant));
        }

        for(Order currentOrder : orders){
            currentOrder = validator.validateOrder(currentOrder,filteredRestaurants);
            if(currentOrder.getOrderStatus() == OrderStatus.VALID_BUT_NOT_DELIVERED){
                if (currentOrder.getPizzasInOrder().length != 0) {
                    List<LngLat> rawRoute = flightRouter.getRoute(restaurantsAndItems.get(currentOrder.getPizzasInOrder()[0]));
                    //If none empty order or route empty (no route was found for that restaurant), order won't be delivered even if valid
                    if (!rawRoute.isEmpty()) {
                        route.addAll(rawRoute);
                        allMoves.addAll(new FlightPath(currentOrder.getOrderNo(), rawRoute, flightRouter.getGraph()).getProcessedRoute());
                        currentOrder.setOrderStatus(OrderStatus.DELIVERED);
                    }
                }
            }
        }
        System.out.println("Generating results files...");

        //Create and populate result files for Deliveries, Flightpath and Drone respectively
        fileGenerator.createFile(orders);
        fileGenerator.createFile(route,true);
        fileGenerator.createFile(allMoves);

        System.out.println("----------------------------------\n" + ANSI_GREEN + "Orders processed successfully and routes generated: see results file for paths and processed orders" + ANSI_RESET);
    }

    public static void errorMessage(String message) throws Exception {
        System.err.println(message + ". Program terminating...");
        throw new Exception(message);
    }
}