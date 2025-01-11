package uk.ac.ed.inf;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import uk.ac.ed.inf.Validation.OrderValidator;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Random;


public class OrderValidatorTests {
    Pizza[] pizzas = {new Pizza("Margarita", 1000), new Pizza("Calzone", 1400), new Pizza("Meat Lover", 1400),
        new Pizza("Vegan Delight", 1100), new Pizza( "Super Cheese", 1400), new Pizza("All Shrooms", 900), new Pizza("24HOURS", 900), new Pizza("7DAYS", 900)};

    Restaurant[] restaurants = {
            new Restaurant("Civerinos Slice", new LngLat(-3.1912869215011597, 55.945535152517735),
                    new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY}, Arrays.copyOfRange(pizzas,0,2)),
            new Restaurant("Sora Lella Vegan Restaurant",new LngLat(-3.202541470527649,55.943284737579376),
                    new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY,DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY},  Arrays.copyOfRange(pizzas,2,4) ),
            new Restaurant("Domino's Pizza - Edinburgh - Southside", new LngLat(-3.1838572025299072,55.94449876875712),
                    new DayOfWeek[]{DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},Arrays.copyOfRange(pizzas,4,6)),
            new Restaurant("ALWAYS OPEN", new LngLat(-3.1838572025299072,55.94449876875712),
                    new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},Arrays.copyOfRange(pizzas,7,8))};

    OrderValidator testInstance = new OrderValidator();
    LocalDate orderDate = LocalDate.of(2023,10,6);
    Order testOrder = new Order("200", orderDate, OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2500, Arrays.copyOfRange(pizzas,0,2),
            new CreditCardInformation("1111222233334444", "11/25", "919"));

    Order ALWAYSORDER = new Order("100", orderDate, OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1000, Arrays.copyOfRange(pizzas,7,8),
            new CreditCardInformation("1111222233334444", "11/25", "919"));


    public void resetTestOrder(){
        testOrder = new Order("200", orderDate, OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 2500, Arrays.copyOfRange(pizzas,0,2),
                new CreditCardInformation("1111222233334444", "11/25", "919"));
        ALWAYSORDER = new Order("100", orderDate, OrderStatus.UNDEFINED, OrderValidationCode.UNDEFINED, 1000, Arrays.copyOfRange(pizzas,7,8),
                new CreditCardInformation("1111222233334444", "11/25", "919"));
    }

    @Test
    public void allTestsPass() {
        System.out.println("Checking for valid order:");
        validOrder();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for invalid card number (length):");
        invalidCardNum_Length();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for invalid card number (characters):");
        invalidCardNum_Characters();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for invalid expiry date (expired):");
        invalidExpiry_Expired();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for invalid expiry date (format):");
        invalidExpiry_Format();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for invalid expiry date (characters):");
        invalidExpiry_Characters();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for invalid expiry date (close to valid):");
        invalidExpiry_CloseToValid();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for valid expiry date (close to invalid):");
        validExpiry_CloseToInvalid();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for invalid CVV (length):");
        invalidCVV_Length();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for invalid CVV (characters):");
        invalidCVV_Characters();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for pizza that exists:");
        pizzaExist();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for pizza that doesn't exist:");
        pizzaDoesntExist();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for null pizza:");
        pizzaDoesntExist_null();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for pizzas from the same restaurant:");
        pizzasFromSameRestaurant();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for pizzas not from the same restaurant:");
        notFromSameRestaurant();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for invalid number of pizzas:");
        invalidNumberOfPizzas();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for total added correctly:");
        totalAddedCorrectly();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for total not added correctly:");
        totalNotAddedCorrectly();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for restaurant open:");
        restaurantOpen();
        System.out.println("Pass\n");
        resetTestOrder();

        System.out.println("Checking for restaurant closed:");
        restaurantClosed();
        System.out.println("Pass");
    }



    @Test
    public void validOrder(){
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(testOrder.getOrderValidationCode(), OrderValidationCode.NO_ERROR);
    }

    @RepeatedTest(100)
    public void invalidCardNum_Length(){
        String badNum = generateRandomCardBadLength();
        testOrder.getCreditCardInformation().setCreditCardNumber(badNum);
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.CARD_NUMBER_INVALID, testOrder.getOrderValidationCode());
    }
    @RepeatedTest(100)
    public void invalidCardNum_Characters(){
        String badChar = generateRandomCardChars();
        testOrder.getCreditCardInformation().setCreditCardNumber(badChar);
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.CARD_NUMBER_INVALID, testOrder.getOrderValidationCode());
    }

    @RepeatedTest(100)
    public void invalidExpiry_Expired(){
        String badExp = generateRandomDay();
        testOrder.getCreditCardInformation().setCreditCardExpiry(badExp);
        testOrder.setOrderDate(LocalDate.now());
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.EXPIRY_DATE_INVALID, testOrder.getOrderValidationCode());
    }
    @Test
    public void invalidExpiry_Format(){
        testOrder.getCreditCardInformation().setCreditCardExpiry("1224");
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.EXPIRY_DATE_INVALID, testOrder.getOrderValidationCode());
    }
    @RepeatedTest(100)
    public void invalidExpiry_Characters(){
        String badChar = generateRandomExpChars();
        testOrder.getCreditCardInformation().setCreditCardExpiry(badChar);
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.EXPIRY_DATE_INVALID, testOrder.getOrderValidationCode());
    }
    @RepeatedTest(100)
    public void invalidExpiry_CloseToValid(){
        LocalDate date = generateRandomDayJan();
        ALWAYSORDER.setOrderDate(date);
        ALWAYSORDER.getCreditCardInformation().setCreditCardExpiry("12/24");
        testInstance.validateOrder(ALWAYSORDER, restaurants);
        assertSame(OrderValidationCode.EXPIRY_DATE_INVALID, ALWAYSORDER.getOrderValidationCode());
    }
    @RepeatedTest(100)
    public void validExpiry_CloseToInvalid(){
        LocalDate date = generateRandomDayJan();
        ALWAYSORDER.setOrderDate(date);
        ALWAYSORDER.getCreditCardInformation().setCreditCardExpiry("01/25");
        testInstance.validateOrder(ALWAYSORDER, restaurants);
        assertSame(OrderValidationCode.NO_ERROR, ALWAYSORDER.getOrderValidationCode());
    }

    @RepeatedTest(100)
    public void invalidCVV_Length(){
        //Resetting other credit info
        String badCVV = generateRandomCVVBadLength();
        testOrder.setCreditCardInformation(new CreditCardInformation("1111222233334444","11/25", badCVV ));
        testOrder.setOrderDate(orderDate);
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.CVV_INVALID, testOrder.getOrderValidationCode());
    }
    @RepeatedTest(100)
    public void invalidCVV_Characters(){
        String charCVV = generateRandomCVVChars();
        testOrder.setCreditCardInformation(new CreditCardInformation("1111222233334444", "10/26", charCVV));
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.CVV_INVALID, testOrder.getOrderValidationCode());
    }

    @Test
    public void pizzaExist(){
        testOrder.setPizzasInOrder(new Pizza[]{new Pizza("All Shrooms", 900)});
        testOrder.setPriceTotalInPence(1000);
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.NO_ERROR, testOrder.getOrderValidationCode());
    }
    @Test
    public void pizzaDoesntExist(){
        testOrder.setPizzasInOrder(new Pizza[]{new Pizza("All Shrooms", 900), new Pizza("Yummers", 130000)});
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.PIZZA_NOT_DEFINED, testOrder.getOrderValidationCode());
    }
    @Test
    public void pizzaDoesntExist_null(){
        testOrder.setPizzasInOrder(new Pizza[]{new Pizza("",0)});
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.PIZZA_NOT_DEFINED, testOrder.getOrderValidationCode());
    }
    @Test
    public void pizzasFromSameRestaurant(){
        testOrder.setPizzasInOrder(Arrays.copyOfRange(pizzas,2,4));
        testOrder.setPriceTotalInPence(2600);
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.NO_ERROR, testOrder.getOrderValidationCode());
    }
    @Test
    public void notFromSameRestaurant(){
        testOrder.setPizzasInOrder(new Pizza[]{new Pizza("Margarita", 1000),new Pizza("Meat Lover", 1400 )});
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS, testOrder.getOrderValidationCode());
    }
    @Test
    public void invalidNumberOfPizzas(){
        testOrder.setPizzasInOrder(pizzas);
        testOrder.setPriceTotalInPence(7300);
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED,testOrder.getOrderValidationCode());
    }

    @Test
    public void totalAddedCorrectly(){
        testOrder.setPizzasInOrder(new Pizza[]{new Pizza( "Super Cheese", 1400), new Pizza("All Shrooms", 900)});
        testOrder.setPriceTotalInPence(2400);
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.NO_ERROR, testOrder.getOrderValidationCode());
    }
    @Test
    public void totalNotAddedCorrectly(){
        testOrder.setPriceTotalInPence(2700);
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.TOTAL_INCORRECT, testOrder.getOrderValidationCode());
    }

    @Test
    public void restaurantOpen(){
        testOrder.getCreditCardInformation().setCreditCardExpiry("11/26");
        testOrder.setOrderDate(LocalDate.of(2023,11,11));
        testOrder.setPizzasInOrder(new Pizza[] {new Pizza("Margarita", 1000), new Pizza("Calzone", 1400)});
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.NO_ERROR, testOrder.getOrderValidationCode());
    }
    @Test
    public void restaurantClosed(){
        testOrder.setOrderDate(LocalDate.of(2023, 10,11));
        testInstance.validateOrder(testOrder, restaurants);
        assertSame(OrderValidationCode.RESTAURANT_CLOSED, testOrder.getOrderValidationCode());
    }

    private static String generateRandomCardGood() {
        String zeroToNine = "0123456789";
        Random rand=new Random();
        StringBuilder res=new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int randIndex=rand.nextInt(zeroToNine.length());
            res.append(zeroToNine.charAt(randIndex));
        }
        return res.toString();
    }

    private static String generateRandomCardChars() {
        String zeroToNine = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVxXyYzZ";
        Random rand=new Random();
        StringBuilder res=new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int randIndex=rand.nextInt(zeroToNine.length());
            res.append(zeroToNine.charAt(randIndex));
        }
        return res.toString();
    }

    private static String generateRandomCardBadLength() {
        String zeroToNine = "0123456789";
        Random rand=new Random();
        int howLong = rand.nextInt(50);
        if (howLong == 16){
            howLong ++;
        }
        StringBuilder res=new StringBuilder();
        for (int i = 0; i < howLong; i++) {
            int randIndex=rand.nextInt(zeroToNine.length());
            res.append(zeroToNine.charAt(randIndex));
        }
        return res.toString();
    }

    private static String generateRandomCVVChars() {
        String zeroToNine = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVxXyYzZ";
        Random rand=new Random();
        StringBuilder res=new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int randIndex=rand.nextInt(zeroToNine.length());
            res.append(zeroToNine.charAt(randIndex));
        }
        return res.toString();
    }

    private static String generateRandomCVVBadLength() {
        String zeroToNine = "0123456789";
        Random rand = new Random();
        int howLong = rand.nextInt(50);
        if (howLong == 3) {
            howLong++;
        }
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < howLong; i++) {
            int randIndex = rand.nextInt(zeroToNine.length());
            res.append(zeroToNine.charAt(randIndex));
        }
        return res.toString();
    }

    private static String generateRandomExpChars() {
        String zeroToNine = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVxXyYzZ";
        Random rand=new Random();
        StringBuilder res=new StringBuilder();
        for (int i = 0; i < 2; i++) {
            int randIndex=rand.nextInt(zeroToNine.length());
            res.append(zeroToNine.charAt(randIndex));
        }
        res.append('/');
        for (int i = 0; i < 2; i++) {
            int randIndex=rand.nextInt(zeroToNine.length());
            res.append(zeroToNine.charAt(randIndex));
        }
        return res.toString();
    }

    private static LocalDate generateRandomDayJan() {
        Random rand=new Random();
        int day = rand.nextInt(1, 31);
        return LocalDate.of(2025,1,day);
    }

    public static String generateRandomDay() {
        LocalDate start = LocalDate.of(2000, Month.JANUARY, 1);
        LocalDate end = LocalDate.of(2024, Month.DECEMBER, 31);
        long days = ChronoUnit.DAYS.between(start, end);
        LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + 1));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        return randomDate.format(formatter);
    }





}
