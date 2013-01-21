import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class ContactManagerImpl implements ContactManager {
    // Fields for future and past meetings
    private List<Meeting> meetingList = null;

    // TODO: Use this?
    private List<FutureMeeting> futureMeetingList = null;
    private List<PastMeeting> pastMeetingList = null;

    private Set<Contact> contactSet = null;

    public ContactManagerImpl() {
        this.meetingList = new ArrayList<Meeting>();

        // TODO: Use this?
        this.futureMeetingList = new ArrayList<FutureMeeting>();
        this.pastMeetingList = new ArrayList<PastMeeting>();

        this.contactSet = new HashSet<Contact>();
    }

    /**
     * Add a new meeting to be held in the future.
     *
     * @param contacts a list of contacts that will participate in the meeting.
     * @param date     the date on which the meeting will take place.
     * @return the ID for the meeting.
     * @throws IllegalArgumentException if the meeting is set for a time in the past, or if any contact is unknown.
     */
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        // Check if time is valid.
        if ( ! timeInFuture(date)) {
            throw new IllegalArgumentException("Meeting time is in the past!");
        }

        // Check if contacts are valid.
        for (Contact contact : contacts) {
            if ( ! allContactsExist(contact.getId())) {
                throw new IllegalArgumentException("Contact ID supplied does not exist!");
            }
        }

        // ID for new meeting is current meeting list size + 1.
        int meetingId = meetingList.size() + 1;

        // Create meeting.
        FutureMeeting futureMeeting = new FutureMeetingImpl(meetingId, date, contacts);

        // Add meeting to meeting list.
        this.meetingList.add(futureMeeting);

        return meetingId;
    }

    /**
     * Returns the PAST meeting with the requested ID, or null if there is none.
     *
     * @param id the ID for the meeting.
     * @return the meeting with the requested ID, or null if there is none.
     * @throws IllegalArgumentException if there is a meeting with that ID happening in the future.
     */
    public PastMeeting getPastMeeting(int id) {
        // Check if meeting exists based on ID.
        if (! allMeetingsExist(id)) {
            return null;
        }

        Meeting meeting = getMeeting(id);

        // Check if meeting is a future type.
        if (meeting instanceof FutureMeetingImpl) {
            throw new IllegalArgumentException("Meeting time is in the future!");
        }

        // Downcast to PastMeeting.
        return (PastMeeting)meeting;
    }

    /**
     * Returns the FUTURE meeting with the requested ID, or null if there is none.
     *
     * @param id the ID for the meeting.
     * @return the meeting with the requested ID, or null if there is none.
     * @throws IllegalArgumentException if there is a meeting with that ID happening in the past
     */
    public FutureMeeting getFutureMeeting(int id) {
        // Check if meeting exists based on ID.
        if (! allMeetingsExist(id)) {
            return null;
        }

        Meeting meeting = getMeeting(id);

        // Check if meeting is a past type.
        if (meeting instanceof PastMeetingImpl) {
            throw new IllegalArgumentException("Meeting time is in the past!");
        }

        // Downcast to FutureMeeting
        return (FutureMeeting)meeting;
    }

    /**
     * Returns the meeting with the requested ID, or null if there is none.
     *
     * @param id the ID for the meeting.
     * @return the meeting with the requested ID, or null if there is none.
     */
    public Meeting getMeeting(int id) {
        for (Meeting aMeeting : this.meetingList) {
            if ( aMeeting.getId() == id ) {
                return aMeeting;
            }
        }
        return null;
    }

    /**
     * Returns the list of future meetings scheduled with this contact.
     *
     * If there are none, the returned list will be empty. Otherwise, the list will be chronologically sorted and will
     * not contain any duplicates.
     *
     * @param contact one of the user's contacts.
     * @return the list of the future meeting(s) scheduled with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist.
     */
    public List<Meeting> getFutureMeetingList(Contact contact) {
        if ( ! allContactsExist(contact.getId())) {
            throw new IllegalArgumentException("Contact ID supplied does not exist!");
        }

        List<Meeting> futureMeetings = new ArrayList<Meeting>();
        for (Meeting meeting : this.meetingList) {
            if (meeting instanceof FutureMeetingImpl) {
                Set<Contact> contacts = meeting.getContacts();
                if (contacts.contains(contact)) {
                    futureMeetings.add(meeting);
                }
            }
        }

        // TODO: Sort chronologically.

        return futureMeetings;
    }

    /**
     * Returns the list of meetings that are scheduled for, or that took place on, the specified date.
     *
     * If there are none, the returned list will be empty. Otherwise, the list will be chronologically sorted and will
     * not contain any duplicates.
     *
     * @param date the date.
     * @return the list of meetings.
     */
    public List<Meeting> getFutureMeetingList(Calendar date) {
        // Note that as per Sergio, this interface method should have been named getMeetingList(Calendar date) as
        // it returns past and future meetings.




        return null;
    }

    /**
     * Returns the list of past meeting in which this contact has participated.
     *
     * If there are none, the returned list will be empty. Otherwise, the list will be chronologically sorted and will
     * not contain any duplicates.
     *
     * @param contact one of the user's contacts.
     * @return the list of past meeting(s) scheduled with this contact (maybe empty).
     * @throws IllegalArgumentException if the contact does not exist.
     */
    public List<PastMeeting> getPastMeetingList(Contact contact) {
        if ( ! allContactsExist(contact.getId())) {
            throw new IllegalArgumentException("Contact ID supplied does not exist!");
        }

        // Note: For some reason interface requires return type to be a List<PastMeeting>
        // while for the similar method for future meetings, it only requires a List<Meeting> return type.
        List<PastMeeting> pastMeetings = new ArrayList<PastMeeting>();
        for (Meeting meeting : this.meetingList) {
            if (meeting instanceof PastMeetingImpl) {
                Set<Contact> contacts = meeting.getContacts();
                if (contacts.contains(contact)) {
                    pastMeetings.add((PastMeeting)meeting);
                }
            }
        }

        // TODO: Sort chronologically.

        return pastMeetings;
    }

    /**
     * Create a new record for a meeting that took place in the past.
     *
     * @param contacts a list of participants.
     * @param date     the date on which the meeting took place.
     * @param text     messages to be added about the meeting.
     * @throws IllegalArgumentException if the list of contacts is empty, or any of the contacts does not exist.
     * @throws NullPointerException if any of the arguments is null.
     */
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        // Check if time is valid.
        if ( timeInFuture(date)) {
            throw new IllegalArgumentException("Meeting time is in the future!");
        }

        // Check if contacts are valid.
        for (Contact contact : contacts) {
            if ( ! allContactsExist(contact.getId())) {
                throw new IllegalArgumentException("Contact ID supplied does not exist!");
            }
        }

        if (contacts == null | date == null | text == null) {
            throw new NullPointerException("Data supplied is null.");
        }

        // ID for past meeting is current meeting list size + 1.
        int meetingId = meetingList.size() + 1;

        // Create past meeting.
        PastMeeting pastMeeting = new PastMeetingImpl(meetingId, date, contacts, text);

        // Add past meeting to meeting list.
        this.meetingList.add(pastMeeting);
    }

    /**
     * Add notes to a meeting.
     *
     * This method is used when a future meeting takes place, and is then converted to a past meeting (with notes).
     *
     * It can be also used to add notes to a past meeting at a later date.
     *
     * @param id   the ID of the meeting.
     * @param text messages to be added about the meetings.
     * @throws IllegalArgumentException if the meeting does not exist.
     * @throws IllegalStateException if the meeting is set for a date in the future.
     * @throws NullPointerException if the notes are null.
     */
    public void addMeetingNotes(int id, String text) {
        // Check if meeting exists based on ID.
        if (! allMeetingsExist(id)) {
            throw new IllegalArgumentException("Meeting ID does not exist!");
        }

        Meeting meeting = getMeeting(id);

        // Check if meeting is not a future type.
        if (meeting instanceof FutureMeetingImpl) {
            throw new IllegalStateException("Meeting time is in the future!");
        }

        if (text == null) {
            throw new NullPointerException("Notes are null!");
        }

        // TODO: Check if this downcasting is a 'proper' way of doing it.
        PastMeetingImpl pastMeeting = (PastMeetingImpl) getMeeting(id);

        pastMeeting.setNotes(text);
    }

    /**
     * Create a new contact with the specified name and notes.
     *
     * @param name  the name of the contact.
     * @param notes notes to be added about the contact.
     * @throws NullPointerException if the name or the notes are null.
     */
    public void addNewContact(String name, String notes) {
        if (name == null | notes == null) {
            throw new NullPointerException("Contact name or string cannot be null!");
        }

        // ID for new contact is current contact set size + 1.
        int contactId = this.contactSet.size() + 1;

        Contact newContact = new ContactImpl(contactId, name, notes);
        this.contactSet.add(newContact);

        System.out.println("Contact added. ID: " + contactId);
    }

    /**
     * Returns a list containing the contacts that correspond to the IDs.
     *
     * @param ids an arbitrary number of contact IDs.
     * @return a list containing the contacts that correspond to the IDs.
     * @throws IllegalArgumentException if any of the IDs does not correspond to a real contact.
     */
    public Set<Contact> getContacts(int... ids) {
        // Check if all contact IDs supplied are valid.
        if (allContactsExist(ids)) {
            // Temporary set that holds contacts to return.
            Set<Contact> tempContactSet = new HashSet<Contact>();

            for (Contact contact : this.contactSet) {
                for (int id : ids) {
                    if (contact.getId() == id) {
                        tempContactSet.add(contact);
                    }
                }
            }
            return tempContactSet;
        } else {
            throw new IllegalArgumentException("Not all IDs supplied exist!");
        }
    }

    /**
     * Returns a list with the contacts whose name contains that string.
     *
     * @param name the string to search for.
     * @return a list with the contacts whose name contains that string.
     * @throws NullPointerException if the parameter is null.
     */
    public Set<Contact> getContacts(String name) {
        if (name == null) {
            throw new NullPointerException("Search string cannot be null!");
        }

        // Temporary set that holds contacts to return.
        Set<Contact> tempContactSet = new HashSet<Contact>();

        for (Contact contact : this.contactSet) {
            if (contact.getName().contains(name)) {
                tempContactSet.add(contact);
            }
        }

        return tempContactSet;
    }

    /**
     * Save all data to disk.
     *
     * This method must be executed when the program is closed and when/if the user requests it.
     */
    public void flush() {
        //To change body of implemented methods use File | Settings | File Templates.
    }




    /**
     * Returns a single contact that corresponds to the ID.
     *
     * @param id a contact ID.
     * @return a Contact that corresponds to the ID or null if not found.
     */
    private Contact getContact(int id) {
        for (Contact contact : this.contactSet) {
            if (contact.getId() == id) {
                return contact;
            }
        }
        return null;
    }

    /**
     * Checks if provided contact IDs exist.
     *
     * @param ids contact IDs.
     * @return true if all IDs exist, otherwise false.
     */
    private boolean allContactsExist(int... ids) {
        // If any of the ids provided is higher than any possible id number for a contact (based on set size)
        // then throw an exception.
        int contactSetSize = contactSet.size();

        for (int id : ids) {
            if (id > contactSetSize + 1) {
               return false;
            }
        }

        return true;
    }

    /**
     * Checks if provided meeting IDs exist.
     *
     * @param ids meeting IDs.
     * @return true if all IDs exist, otherwise false.
     */
    private boolean allMeetingsExist(int... ids) {
        // If any of the ids provided is higher than any possible id number for a meeting (based on list size)
        // then throw an exception.
        int meetingListSize = meetingList.size();

        for (int id : ids) {
            if (id > meetingListSize + 1) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if provided date is in the future.
     *
     * @param date date to check.
     * @return true if the date is in the future, otherwise false.
     */
    private boolean timeInFuture(Calendar date) {
        Calendar rightNow = Calendar.getInstance();

        if (rightNow.before(date)) {
            return true;
        } else {
            return false;
        }
    }
}
