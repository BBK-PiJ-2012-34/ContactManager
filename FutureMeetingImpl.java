import java.util.Calendar;
import java.util.Set;

/**
 * FutureMeetingImpl - Implements FutureMeeting interface.
 *
 * A meeting to be held in the future.
 */
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {
    /**
     * Constructor
     *
     * @param id an ID for the meeting.
     * @param date date of the meeting.
     * @param contacts set of contacts for the meeting.
     */
    public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
        super(id, date, contacts);
    }
}
