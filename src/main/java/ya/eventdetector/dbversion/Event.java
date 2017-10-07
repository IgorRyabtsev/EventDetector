package ya.eventdetector.dbversion;

import java.io.Serializable;

// It may be photo, as an example
public class Event implements Serializable {
    private long id;
    private long timeInMillis;

    public Event() {
    }

    public Event(long id, long timeInMillis) {
        this.id = id;
        this.timeInMillis = timeInMillis;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != event.id) return false;
        return timeInMillis == event.timeInMillis;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (timeInMillis ^ (timeInMillis >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", timeInMillis=" + timeInMillis +
                '}';
    }
}
