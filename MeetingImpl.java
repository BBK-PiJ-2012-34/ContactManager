import java.util.Calendar;
import java.util.Set;

/**
 * A class to represent meetings.
 *
 * Meetings have unique IDs, scheduled date and a list of participating contacts.
 */
public class MeetingImpl implements Meeting, Comparable<Meeting> {
    private int id = 0;
    private Calendar date = null;
    private Set<Contact> contacts = null;

    /**
     * Constructor
     *
     * @param id an ID for the meeting.
     * @param date date of the meeting.
     * @param contacts set of contacts for the meeting.
     */
    public MeetingImpl(int id, Calendar date, Set<Contact> contacts) {
        this.id = id;
        this.date = date;
        this.contacts = contacts;
    }

    /**
     * Returns the id of the meeting.
     *
     * @return the id of the meeting.
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Return the date of the meeting.
     *
     * @return the date of the meeting.
     */
    @Override
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
    @Override
    public Set<Contact> getContacts() {
        return this.contacts;
    }

    /**
     * Compares this object with the specified object for order.
     *
     * @param otherMeeting the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Meeting otherMeeting) {
        return this.date.compareTo(otherMeeting.getDate());
    }

    /**
     * Compares this object with the specified object for equality.
     *
     * @param o the object to be compared.
     * @return true if equal, otherwise false.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        int otherMeetingID = ((MeetingImpl) o).getId();
        Calendar otherMeetingDate = ((MeetingImpl) o).getDate();
        Set<Contact> otherMeetingContacts = ((MeetingImpl) o).getContacts();

        if (this.id == otherMeetingID) {
            if (this.date.equals(otherMeetingDate)) {
                if (this.contacts.equals(otherMeetingContacts)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a hash code value for the object. This method is supported for the benefit of hash tables.
     * Must be overridden since we're overriding Object.equals() as well and must ensure our hash is consistent.
     *
     * @return a hash code value for this object using an arbitrary algorithm based on its fields values and hash codes.
     */
    @Override public int hashCode() {
        int hashCode = 0;

        hashCode = this.id + this.contacts.hashCode() + this.date.hashCode();

        return hashCode;
    }
}
