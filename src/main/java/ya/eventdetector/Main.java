package ya.eventdetector;

import ya.eventdetector.dbversion.EventCounterImpl;
import ya.eventdetector.treeversion.EventCounterImplTree;

public class Main {
    public static void main(String[] args) {
        EventCounterImplTree eventCounterImplTree = new EventCounterImplTree();

        for (int i = 0; i < 200000; i++) {
            eventCounterImplTree.addEvent();
        }

        System.out.println(eventCounterImplTree.getCountOfEventsPerMinute());
        System.out.println(eventCounterImplTree.getCountOfEventsPerHour());
        System.out.println(eventCounterImplTree.getCountOfEventsPerDay());

        EventCounterImpl eventCounterImpl = new EventCounterImpl();

        for (int i = 0; i < 200000; i++) {
            eventCounterImpl.addEvent();
        }

        System.out.println(eventCounterImpl.getCountOfEventsPerMinute());
        System.out.println(eventCounterImpl.getCountOfEventsPerHour());
        System.out.println(eventCounterImpl.getCountOfEventsPerDay());

    }
}
