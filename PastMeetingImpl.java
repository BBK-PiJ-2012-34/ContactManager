import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
    private String notes = null;

    public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
        super(id, date, contacts);
    }

    /**
     * Returns the notes from the meeting.
     *
     * If there are no notes, the empty string is returned.
     *
     * @return the notes from the meeting.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets notes for the meeting.
     *
     * @param notes notes to set.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
