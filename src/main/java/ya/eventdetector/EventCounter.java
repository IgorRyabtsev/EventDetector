package ya.eventdetector;

public interface EventCounter {
    void addEvent();
    long getCountOfEventsPerMinute();
    long getCountOfEventsPerHour();
    long getCountOfEventsPerDay();

}
