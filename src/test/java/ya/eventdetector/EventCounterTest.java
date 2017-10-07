package ya.eventdetector;

import org.junit.Before;
import org.junit.Test;
import ya.eventdetector.dbversion.EventCounterImpl;
import ya.eventdetector.treeversion.EventCounterImplTree;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventCounterTest {
    private EventCounterImplTree eventCounter;
    private EventCounterImpl eventCounterImpl;

    private final static int NUMBER_OF_CYCLES = 500000;
    private final static int NUMBER_OF_CYCLES_WITH_DB = 100000;
    private final static int BIG_NUMBER = 10000000;
    private final static int BIG_NUMBER_WITH_DB = 1000001;
    private final static String HOUR = "HOUR";
    private final static String DAY = "DAY";

    @Before
    public void init() {
        eventCounter = new EventCounterImplTree();
        eventCounterImpl = new EventCounterImpl();
    }

    @Test
    public void testSimpleEvents() {
        eventCounter.addEvent();
        eventCounter.addEvent();
        eventCounter.addEvent();
        eventCounter.addEvent();
        eventCounter.addEvent();
        assertEquals(5L, eventCounter.getCountOfEventsPerMinute());
        assertEquals(5L, eventCounter.getCountOfEventsPerHour());
        assertEquals(5L, eventCounter.getCountOfEventsPerDay());

        eventCounterImpl.addEvent();
        eventCounterImpl.addEvent();
        eventCounterImpl.addEvent();
        eventCounterImpl.addEvent();
        eventCounterImpl.addEvent();
        assertEquals(5L, eventCounterImpl.getCountOfEventsPerMinute());
        assertEquals(5L, eventCounterImpl.getCountOfEventsPerHour());
        assertEquals(5L, eventCounterImpl.getCountOfEventsPerDay());
    }

    @Test
    public void testSimpleEventsInCycle() {
        for (int i = 0; i < NUMBER_OF_CYCLES; i++) {
            eventCounter.addEvent();
            eventCounter.addEvent();
        }

        assertEquals(2 * NUMBER_OF_CYCLES, eventCounter.getCountOfEventsPerMinute());
        assertEquals(2 * NUMBER_OF_CYCLES, eventCounter.getCountOfEventsPerHour());
        assertEquals(2 * NUMBER_OF_CYCLES, eventCounter.getCountOfEventsPerDay());


        for (int i = 0; i < NUMBER_OF_CYCLES_WITH_DB; i++) {
            eventCounterImpl.addEvent();
            eventCounterImpl.addEvent();
        }
        assertEquals(2 * NUMBER_OF_CYCLES_WITH_DB, eventCounterImpl.getCountOfEventsPerDay());
        assertEquals(2 * NUMBER_OF_CYCLES_WITH_DB, eventCounterImpl.getCountOfEventsPerHour());
        assertEquals(2 * NUMBER_OF_CYCLES_WITH_DB, eventCounterImpl.getCountOfEventsPerMinute());
    }

    @Test
    public void testDifferentEvents() throws InterruptedException {
        eventCounter.addEvent();
        eventCounter.addSpecialEvent(HOUR);
        eventCounter.addSpecialEvent(HOUR);
        eventCounter.addSpecialEvent(DAY);
        eventCounter.addSpecialEvent(DAY);
        eventCounter.addSpecialEvent(DAY);
        assertEquals(1, eventCounter.getCountOfEventsPerMinute());
        assertTrue(eventCounter.getCountOfEventsPerHour() <= 3);
        assertTrue(eventCounter.getCountOfEventsPerHour() <= 6);
        eventCounter.addEvent();
        sleep(2000);
        assertEquals(2, eventCounter.getCountOfEventsPerMinute());
        assertEquals(2, eventCounter.getCountOfEventsPerHour());
        assertEquals(4, eventCounter.getCountOfEventsPerDay());
    }

    @Test
    public void testSmallStress() {
        for (int i = 0; i < BIG_NUMBER; i++) {
            eventCounter.addEvent();
        }
        assertEquals(BIG_NUMBER, eventCounter.getCountOfEventsPerMinute());
        assertEquals(BIG_NUMBER, eventCounter.getCountOfEventsPerHour());
        assertEquals(BIG_NUMBER, eventCounter.getCountOfEventsPerDay());

        for (int i = 0; i < BIG_NUMBER_WITH_DB; i++) {
            eventCounterImpl.addEvent();
        }

        assertEquals(BIG_NUMBER_WITH_DB, eventCounterImpl.getCountOfEventsPerHour());
        assertEquals(BIG_NUMBER_WITH_DB, eventCounterImpl.getCountOfEventsPerDay());
    }

    @Test
    public void testBigStress() {
        for (int i = 0; i < BIG_NUMBER*3; i++) {
            eventCounter.addEvent();
        }
        assertEquals(BIG_NUMBER*3, eventCounter.getCountOfEventsPerHour());
        assertEquals(BIG_NUMBER*3, eventCounter.getCountOfEventsPerDay());
    }
}
