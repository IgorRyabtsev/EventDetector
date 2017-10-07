package ya.eventdetector.dbversion;

import ya.eventdetector.EventCounter;
import ya.eventdetector.dbversion.db.SimpleDataBaseImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static ya.eventdetector.util.Utils.*;

public class EventCounterImpl implements EventCounter {

    private final List<Event> events;
    private final int MAX_RECORDS_IN_MEMORY = 1000000;
    private final long DATABASE_CLEAR_PERIOD = 10000000;
    private long eventId;
    private long fileName;
    private long filesNumber;
    private long leastNumberOfFile;

    public EventCounterImpl() {
        this.events = new ArrayList<>();
        eventId = 0;
        fileName = 0;
        filesNumber = 0;
        leastNumberOfFile = 0;
    }

    @Override
    public void addEvent() {
        synchronized (events) {
            final long currentTimeInMillis = getCurrentTimeInMillis();
            if (eventId != 0 && eventId % DATABASE_CLEAR_PERIOD == 0) {
                clearDataBase(currentTimeInMillis);
            }
            if (events.size() >= MAX_RECORDS_IN_MEMORY) {
                SimpleDataBaseImpl.writeEventsToDatabase(events, fileName + DB_EXTENSION);
                events.clear();
                ++fileName;
                ++filesNumber;
            }
            events.add(new Event(eventId, currentTimeInMillis));
            ++eventId;
        }
    }

    //delete unnecessary files in DB. We'll delete files, which has last element, detected more than 1 day before.
    private void clearDataBase(long currentTimeInMillis) {
        synchronized (events) {
            for (long i = leastNumberOfFile; i < filesNumber; ++i) {
                final ArrayList<Event> eventsFromDB =
                        (ArrayList<Event>) SimpleDataBaseImpl.readEventsFromDatabase(i + DB_EXTENSION);

                if (eventsFromDB.get(eventsFromDB.size() - 1).getTimeInMillis() < currentTimeInMillis - DAY) {
                    SimpleDataBaseImpl.removeEvents(i + DB_EXTENSION);
                    ++leastNumberOfFile;
                    continue;
                }
                return;
            }
        }
    }

    @Override
    public long getCountOfEventsPerMinute() {
        return getCountOfEventsByMillis(getCurrentTimeInMillis() - MINUTE);
    }

    @Override
    public long getCountOfEventsPerHour() {
        return getCountOfEventsByMillis(getCurrentTimeInMillis() - HOUR);
    }

    @Override
    public long getCountOfEventsPerDay() {
        return getCountOfEventsByMillis(getCurrentTimeInMillis() - DAY);
    }

    private long getCountOfEventsByMillis(long minNecessaryTime) {

        long allEvents = 0;
        //calculate in-memory array
        synchronized (events) {
            allEvents += events.size() - lowerBound(events, minNecessaryTime);
            //go throw all files
            for (long i = leastNumberOfFile; i < filesNumber; ++i) {
                ArrayList<Event> eventsFromDB =
                        (ArrayList<Event>) SimpleDataBaseImpl.readEventsFromDatabase(i + DB_EXTENSION);

                if (eventsFromDB.get(0).getTimeInMillis() >= minNecessaryTime) {
                    allEvents += eventsFromDB.size();
                    continue;
                }

                allEvents += lowerBound(eventsFromDB, minNecessaryTime);
            }

            return allEvents;
        }
    }

    //bin search to find necessary time
    private int lowerBound(List<Event> events, long timeInMillis) {
        int left = 0, right = events.size();
        while (left != right) {
            int middle = (left + right) >> 1;
            if (events.get(middle).getTimeInMillis() >= timeInMillis)
                right = middle;
            else
                left = middle + 1;
        }
        return left;
    }

    private long getCurrentTimeInMillis() {
        final Calendar data = Calendar.getInstance();
        return data.getTimeInMillis();
    }

    //just for testing
    public void addSpecialEvent(final long timeLimit) {
        synchronized (events) {
            long timeInMillis = getCurrentTimeInMillis() - timeLimit;
            events.add(new Event(eventId, timeInMillis));
            ++eventId;
        }
    }
}
