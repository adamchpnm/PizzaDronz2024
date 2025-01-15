package uk.ac.ed.inf;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import uk.ac.ed.inf.Validation.LngLatHandler;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;

import java.util.HashMap;
import java.util.Map;

public class LngLatHandlingTests {

    LngLatHandler testInstance = new LngLatHandler();
    LngLat forestHill = new LngLat(-3.192473,55.946233);
    LngLat buccleuch = new LngLat(-3.184319,55.942617);
    LngLat kfc = new LngLat(-3.184319,55.946233);
    LngLat meadows = new LngLat(-3.192473,55.942617);

    LngLat[] region = {forestHill, meadows, buccleuch, kfc};
    LngLat[] multisideRegion = {new LngLat(3.10,55.90), new LngLat(3.11, 55.94), new LngLat(3.15, 55.89), new LngLat(3.23, 55.94), new LngLat(3.25,55.95)};
    NamedRegion central = new NamedRegion("Central Area", region);
    NamedRegion pentagon = new NamedRegion("Pentagon", multisideRegion);

    Map<Double, LngLat> map = getMapNext();
    LngLat oldPoint = new LngLat(3.11111,52.222222);

    Map<Double, LngLat> mapFar = getMapFar();

    double nextTestCount = 0.0;
    @Test
    public void allTestsPass() {
        System.out.println("Checking for correct distance calculation:");
        correctDistanceCalculated();
        System.out.println("Pass\n");

        System.out.println("Checking for being close to a point:");
        closeToPoint();
        System.out.println("Pass\n");

        System.out.println("Checking for not being close to a point:");
        notCloseToPoint();
        System.out.println("Pass\n");

        System.out.println("Checking for being close to being close:");
        closeToBeingClose();
        System.out.println("Pass\n");

        System.out.println("Checking for being within a region:");
        withinRegion();
        System.out.println("Pass\n");

        System.out.println("Checking for not being within a region:");
        notWithinRegion();
        System.out.println("Pass\n");

        System.out.println("Checking for being within a multi-point region:");
        withinMultiPointRegion();
        System.out.println("Pass\n");

        System.out.println("Checking for not being within a multi-point region:");
        notWithinMultiPointRegion();
        System.out.println("Pass\n");

        System.out.println("Checking for the next point being correct:");
        for (double i = 0.0; i < 360.0; i += 22.5) {
            System.out.println("    Compass direction: "+i);
            nextTestCount = i;
            nextPointCorrect();
            System.out.println("    Pass\n");
        }
        nextTestCount = 0.0;

        System.out.println("Checking for multi-step point (heuristics) being correct (W):");
        for (double i = 0.0; i < 360.0; i += 22.5) {
            System.out.println("    Compass direction: "+i);
            nextTestCount = i;
            farPointCorrect();
            System.out.println("    Pass\n");
        }
    }


    @Test
    public void correctDistanceCalculated(){
        double result = testInstance.distanceTo(forestHill, buccleuch);
        double expected = Math.sqrt((Math.pow(-3.192473 + 3.184319, 2))+(Math.pow(55.946233-55.942617, 2)));
        assertEquals(result,expected,0.00);
    }

    @Test
    public void closeToPoint(){
        LngLat closePoint = new LngLat(-3.192472, 55.946232);
        assertTrue(testInstance.isCloseTo(forestHill, closePoint));
    }

    @Test
    public void notCloseToPoint(){
        LngLat notClosePoint = new LngLat(-3.92472, 55.46232);
        assertFalse(testInstance.isCloseTo(forestHill, notClosePoint));
    }

    @Test
    public void closeToBeingClose(){
        LngLat nearlyClose = new LngLat(-3.199999, 56.125465);
        assertFalse(testInstance.isCloseTo(forestHill, nearlyClose));
    }


    @Test
    public void withinRegion(){
        LngLat withinRegion = new LngLat(-3.192163,55.945233);
        assertTrue(testInstance.isInRegion(withinRegion, central));
    }

    @Test
    public void notWithinRegion(){
        LngLat notWithinRegion = new LngLat(-3.261934, 55.943617);
        assertFalse(testInstance.isInRegion(notWithinRegion, central));
    }

    @Test
    public void withinMultiPointRegion(){
        LngLat withinRegion = new LngLat(3.17, 55.91);
        assertTrue(testInstance.isInRegion(withinRegion, pentagon));
    }

    @Test
    public void notWithinMultiPointRegion(){
        LngLat notWithin = new LngLat(3.105, 55.895);
        assertFalse(testInstance.isInRegion(notWithin, pentagon));
    }

    @Test
    public void nextPointCorrect(){
        assertEquals(testInstance.nextPosition(oldPoint, nextTestCount), map.get(nextTestCount));
    }

    @Test
    public void farPointCorrect(){
        assertEquals(testInstance.nextFarPosition(oldPoint, nextTestCount), mapFar.get(nextTestCount));
    }

    public Map getMapNext(){
        Map<Double, LngLat> map = new HashMap<>();
        map.put(0.0, new LngLat(3.11126,52.222222));
        map.put(22.5, new LngLat(3.1112485819298765, 52.22227940251486));
        map.put(45.0, new LngLat(3.111216066017178, 52.22232806601718));
        map.put(67.5, new LngLat(3.111167402514855, 52.22236058192988));
        map.put(90.0, new LngLat(3.11111,52.222372));
        map.put(112.5, new LngLat(3.1110525974851453, 52.22236058192988));
        map.put(135.0, new LngLat(3.111003933982822, 52.22232806601718));
        map.put(157.5, new LngLat(3.1109714180701236, 52.22227940251486));
        map.put(180.0, new LngLat(3.11096,52.222222));
        map.put(202.5, new LngLat(3.1109714180701236, 52.22216459748515));
        map.put(225.0, new LngLat(3.111003933982822, 52.22211593398283));
        map.put(247.5, new LngLat(3.1110525974851453, 52.22208341807013));
        map.put(270.0, new LngLat(3.11111, 52.222072000000004));
        map.put(292.5, new LngLat(3.111167402514855, 52.22208341807013));
        map.put(315.0, new LngLat(3.111216066017178, 52.22211593398283));
        map.put(337.5, new LngLat(3.1112485819298765, 52.22216459748515));
        return map;
    }

    public Map getMapFar() {
        Map<Double, LngLat> map = new HashMap<>();
        map.put(0.0, new LngLat(3.11156, 52.222222));
        map.put(22.5, new LngLat(3.11152574578963, 52.22239420754457));
        map.put(45.0, new LngLat(3.111428198051534, 52.22254019805153));
        map.put(67.5, new LngLat(3.1112822075445643, 52.22263774578963));
        map.put(90.0, new LngLat(3.11111, 52.222672));
        map.put(112.5, new LngLat(3.110937792455436, 52.22263774578963));
        map.put(135.0, new LngLat(3.110791801948466, 52.22254019805153));
        map.put(157.5, new LngLat(3.11069425421037, 52.22239420754457));
        map.put(180.0, new LngLat(3.11066, 52.222222));
        map.put(202.5, new LngLat(3.11069425421037, 52.22204979245544));
        map.put(225.0, new LngLat(3.110791801948466, 52.22190380194847));
        map.put(247.5, new LngLat(3.110937792455436, 52.22180625421037));
        map.put(270.0, new LngLat(3.11111, 52.221772));
        map.put(292.5, new LngLat(3.1112822075445643, 52.22180625421037));
        map.put(315.0, new LngLat(3.111428198051534, 52.22190380194847));
        map.put(337.5, new LngLat(3.11152574578963, 52.22204979245544));
        return map;
    }
}
