package ya.eventdetector;

import org.junit.Before;
import org.junit.Test;
import ya.eventdetector.dbversion.EventCounterImpl;
import ya.eventdetector.treeversion.EventCounterImplTree;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EventCounterMultithreadingTest {
    private EventCounterImplTree eventCounter;
    private EventCounterImpl eventCounterImpl;

    private final static int NUMBER_OF_CYCLES = 10000;
    private final static int BIG_NUMBER = 100001;

    @Before
    public void init() {
        eventCounter = new EventCounterImplTree();
        eventCounterImpl = new EventCounterImpl();
    }

    @Test
    public void testThreeThreads() throws InterruptedException {
        Thread threadFirst = new Thread(() -> {
            for (int i = 0; i < NUMBER_OF_CYCLES; i++) {
                eventCounter.addEvent();
                eventCounterImpl.addEvent();
            }
        });
        Thread threadSecond = new Thread(() -> {
            for (int i = 0; i < NUMBER_OF_CYCLES; i++) {
                eventCounter.addEvent();
                eventCounterImpl.addEvent();
            }
        });
        Thread threadThird = new Thread(() -> {
            for (int i = 0; i < NUMBER_OF_CYCLES; i++) {
                eventCounter.addEvent();
                eventCounterImpl.addEvent();
            }
        });

        threadFirst.start();
        threadSecond.start();
        threadThird.start();

        threadFirst.join();
        threadSecond.join();
        threadThird.join();
        assertEquals(NUMBER_OF_CYCLES * 3, eventCounter.getCountOfEventsPerMinute());
        assertEquals(NUMBER_OF_CYCLES * 3, eventCounterImpl.getCountOfEventsPerHour());
    }

    @Test
    public void testTenThreads() throws InterruptedException {
        List<Thread> threads = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(() -> {
                for (int j = 0; j < NUMBER_OF_CYCLES; j++) {
                    eventCounter.addEvent();
                    eventCounterImpl.addEvent();
                }
            }));
        }

        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        assertEquals(NUMBER_OF_CYCLES * 10, eventCounter.getCountOfEventsPerMinute());
        assertEquals(NUMBER_OF_CYCLES * 10, eventCounterImpl.getCountOfEventsPerMinute());
    }

    @Test
    public void testStressThreeThreads() throws InterruptedException {
        Thread threadFirst = new Thread(() -> {
            for (int i = 0; i < BIG_NUMBER; i++) {
                eventCounter.addEvent();
                eventCounterImpl.addEvent();
            }
        });
        Thread threadSecond = new Thread(() -> {
            for (int i = 0; i < BIG_NUMBER; i++) {
                eventCounter.addEvent();
                eventCounterImpl.addEvent();
            }
        });
        Thread threadThird = new Thread(() -> {
            for (int i = 0; i < BIG_NUMBER; i++) {
                eventCounter.addEvent();
                eventCounterImpl.addEvent();
            }
        });

        threadFirst.start();
        threadSecond.start();
        threadThird.start();

        threadFirst.join();
        threadSecond.join();
        threadThird.join();

        assertEquals(BIG_NUMBER * 3, eventCounter.getCountOfEventsPerHour());
        assertEquals(BIG_NUMBER * 3, eventCounterImpl.getCountOfEventsPerHour());
    }

}
