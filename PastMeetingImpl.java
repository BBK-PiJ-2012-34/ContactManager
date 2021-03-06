import java.util.Calendar;
import java.util.Set;

/**
 * PastMeetingImpl - Implements PastMeeting interface.
 *
 * It includes your notes about what happened and what was agreed.
 */
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
    private String notes = null;

    /**
     * Constructor
     *
     * @param id an ID for the meeting.
     * @param date date of the meeting.
     * @param contacts set of contacts for the meeting.
     * @param text meeting notes text.
     */
    public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts, String text) {
        super(id, date, contacts);
        setNotes(text);
    }

    /**
     * Returns the notes from the meeting.
     *
     * If there are no notes, the empty string is returned.
     *
     * @return the notes from the meeting.
     */
    @Override
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
