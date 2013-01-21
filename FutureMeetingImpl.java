import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl implements FutureMeeting {
    private int id = 0;
    private Calendar date = null;
    private Set<Contact> contacts = null;

    public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
        this.id = id;
        this.date = date;
        this.contacts = contacts;
    }

    /**
     * Returns the id of the meeting.
     *
     * @return the id of the meeting.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Return the date of the meeting.
     *
     * @return the date of the meeting.
     */
    public Calendar getDate() {
        return this.date;
    }

    /**
     * Return the details of people that attended the meeting.
     *
     * The list contains a minimum of one contact (if there were just two people: the user and the contact) and may
     * contain an arbitrary number of them.
     *
     * @return the details of people that attended the meeting.
     */
    public Set<Contact> getContacts() {
        return this.contacts;
    }
}
