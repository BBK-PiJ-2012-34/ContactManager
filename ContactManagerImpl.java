import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class ContactManagerImpl implements ContactManager {
    private List<Meeting> meetingList = null;
    private Set<Contact> contactSet = null;

    public ContactManagerImpl() {
        // We're implementing the List interface as ArrayList
        // and the Set interface as a HashSet.
        this.meetingList = new ArrayList<Meeting>();
        this.contactSet = new HashSet<Contact>();
    }

    /**
     * Add a new meeting to be held in the future.
     *
     * @param contacts a list of contacts that will participate in the meeting.
     * @param date the date on which the meeting will take place.
     * @return the ID for the meeting.
     * @throws IllegalArgumentException if the meeting is set for a time in the past, or if any contact is unknown.
     */
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        // Exception thrown if time is in the past.
        if (!timeInFuture(date)) {
            throw new IllegalArgumentException("Meeting time is in the past.");
        }

        // Exception thrown if at least one ID doesn't exist.
        for (Contact contact : contacts) {
            int id = contact.getId();
            if (!allContactsExist(id)) {
                throw new IllegalArgumentException("Contact ID supplied does not exist.");
            }
        }

        // ID given to new meeting is current meeting list size + 1.
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
    @Override
    public PastMeeting getPastMeeting(int id) {
        // Check if meeting exists based on ID.
        if (!allMeetingsExist(id)) {
            return null;
        }

        Meeting meeting = getMeeting(id);

        // Paranoid null check.
        if (meeting != null) {
            // Throw exception if meeting is of the FutureMeetingImpl type.
            if (meeting instanceof FutureMeetingImpl) {
                throw new IllegalArgumentException("Meeting time is in the future.");
            }
        }

        // Downcast to PastMeeting before returning.
        return (PastMeeting) meeting;
    }

    /**
     * Returns the FUTURE meeting with the requested ID, or null if there is none.
     *
     * @param id the ID for the meeting.
     * @return the meeting with the requested ID, or null if there is none.
     * @throws IllegalArgumentException if there is a meeting with that ID happening in the past
     */
    @Override
    public FutureMeeting getFutureMeeting(int id) {
        // Check if meeting exists based on ID.
        if (!allMeetingsExist(id)) {
            return null;
        }

        Meeting meeting = getMeeting(id);

        // Paranoid null check.
        if (meeting != null) {
            // Throw exception if meeting is of the PastMeetingImpl type.
            if (meeting instanceof PastMeetingImpl) {
                throw new IllegalArgumentException("Meeting time is in the past.");
            }
        }

        // Downcast to FutureMeeting before returning.
        return (FutureMeeting) meeting;
    }

    /**
     * Returns the meeting with the requested ID, or null if there is none.
     *
     * @param id the ID for the meeting.
     * @return the meeting with the requested ID, or null if there is none.
     */
    @Override
    public Meeting getMeeting(int id) {
        for (Meeting aMeeting : this.meetingList) {
            if (aMeeting.getId() == id) {
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
    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        // Throw exception if contact does not exist based on ID.
        if (!allContactsExist(contact.getId())) {
            throw new IllegalArgumentException("Contact ID supplied does not exist.");
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
    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {
        // Note that as per Sergio, this interface method should have been named getMeetingList(Calendar date) as
        // it returns past and future meetings.

        List<Meeting> pastAndFutureMeetings = new ArrayList<Meeting>();

        for (Meeting meeting : this.meetingList) {
            Calendar meetingDate = meeting.getDate();
            if (calendarsEqual(meetingDate, date)) {
                pastAndFutureMeetings.add(meeting);
            }
        }

        // TODO: Sort chronologically.

        return pastAndFutureMeetings;
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
    @Override
    public List<PastMeeting> getPastMeetingList(Contact contact) {
        // Throw exception if contact does not exist based on ID.
        if (!allContactsExist(contact.getId())) {
            throw new IllegalArgumentException("Contact ID supplied does not exist.");
        }

        // Note: For some reason interface requires return type to be a List<PastMeeting>
        // while for the similar method for future meetings, it only requires a List<Meeting> return type.
        List<PastMeeting> pastMeetings = new ArrayList<PastMeeting>();

        for (Meeting meeting : this.meetingList) {
            if (meeting instanceof PastMeetingImpl) {
                Set<Contact> contacts = meeting.getContacts();
                if (contacts.contains(contact)) {
                    pastMeetings.add((PastMeeting) meeting);
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
     * @param date the date on which the meeting took place.
     * @param text messages to be added about the meeting.
     * @throws IllegalArgumentException if the list of contacts is empty, or any of the contacts does not exist.
     * @throws NullPointerException if any of the arguments is null.
     */
    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        // NOTE: This method as defined by the interface does NOT check for a date being in the FUTURE.
        // Therefore we can create with it a PAST MEETING THAT HAS A FUTURE DATE and NOT throw an exception.

        if (contacts.isEmpty()) {
            throw new IllegalArgumentException("Contact list is empty.");
        }

        // Throw exception of at least one of the contacts is not found.
        for (Contact contact : contacts) {
            if (!allContactsExist(contact.getId())) {
                throw new IllegalArgumentException("Contact ID supplied does not exist.");
            }
        }

        if (contacts == null || date == null || text == null) {
            throw new NullPointerException("Parameter(s) supplied is null.");
        }

        // ID given to new past meeting is current meeting list size + 1.
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
     * @param id the ID of the meeting.
     * @param text messages to be added about the meetings.
     * @throws IllegalArgumentException if the meeting does not exist.
     * @throws IllegalStateException if the meeting is set for a date in the future.
     * @throws NullPointerException if the notes are null.
     */
    @Override
    public void addMeetingNotes(int id, String text) {
        // Exception thrown if meeting does not exist.
        if (!allMeetingsExist(id)) {
            throw new IllegalArgumentException("Meeting ID does not exist.");
        }

        Meeting meeting = getMeeting(id);

        // Paranoid null check.
        if (meeting != null) {
            // This is slippery as per the interface spec and discussion. We need to check the date
            // and NOT the object type (i.e. checking that it is FutureMeetingImpl is NOT right).
            // This is important since using addNewPastMeeting we can actually create a PastMeeting
            // that has a FUTURE date without throwing an exception.

            // Use private method to check if meeting date is in the future and throw exception if it is.
            if (timeInFuture(meeting.getDate())) {
                throw new IllegalStateException("Meeting time is in the future.");
            }
        }

        if (text == null) {
            throw new NullPointerException("Notes are null.");
        }

        // If meeting is a FutureMeetingImpl type, convert it to PastMeetingImpl type.
        if (meeting instanceof FutureMeetingImpl) {
            // Keep references to FutureMeetingImpl object's state.
            int tempID = meeting.getId();
            Calendar tempDate = meeting.getDate();
            Set<Contact> tempContacts = meeting.getContacts();

            // Remove FutureMeetingImpl meeting object from meeting list.
            this.meetingList.remove(meeting);

            // Create new past meeting with future meeting's state.
            PastMeetingImpl tempMeeting = new PastMeetingImpl(tempID, tempDate, tempContacts, text);

            // Add the PastMeetingImpl meeting object to the meeting list.
            this.meetingList.add(tempMeeting);

            // As this was a converted future meeting object, we've already added the notes during
            // its conversion, so we just return.
            return;
        }

        // TODO: Check if this downcasting is a 'proper' way of doing it.
        PastMeetingImpl pastMeeting = (PastMeetingImpl) getMeeting(id);

        pastMeeting.setNotes(text);
    }

    /**
     * Create a new contact with the specified name and notes.
     *
     * @param name the name of the contact.
     * @param notes notes to be added about the contact.
     * @throws NullPointerException if the name or the notes are null.
     */
    @Override
    public void addNewContact(String name, String notes) {
        // Exception thrown if name or notes are null.
        if (name == null || notes == null) {
            throw new NullPointerException("Contact name or string cannot be null.");
        }

        // ID for new contact is current contact set size + 1.
        int contactId = this.contactSet.size() + 1;

        Contact newContact = new ContactImpl(contactId, name, notes);
        this.contactSet.add(newContact);
    }

    /**
     * Returns a list containing the contacts that correspond to the IDs.
     *
     * @param ids an arbitrary number of contact IDs.
     * @return a list containing the contacts that correspond to the IDs.
     * @throws IllegalArgumentException if any of the IDs does not correspond to a real contact.
     */
    @Override
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
            // Exception thrown if at least one ID doesn't exist.
            throw new IllegalArgumentException("Not all IDs supplied exist.");
        }
    }

    /**
     * Returns a list with the contacts whose name contains that string.
     *
     * @param name the string to search for.
     * @return a list with the contacts whose name contains that string.
     * @throws NullPointerException if the parameter is null.
     */
    @Override
    public Set<Contact> getContacts(String name) {
        // Exception thrown if name parameter is null.
        if (name == null) {
            throw new NullPointerException("Search string cannot be null.");
        }

        // Temporary set that holds contacts to return.
        Set<Contact> tempContactSet = new HashSet<Contact>();

        for (Contact contact : this.contactSet) {
            if (contact.getName().contains(name)) {
                tempContactSet.add(contact);
            }
        }

        // Note: Interface does not specify an exception for a null result, so we just
        // return an empty list in case there are no matches.
        return tempContactSet;
    }

    /**
     * Save all data to disk.
     * <p/>
     * This method must be executed when the program is closed and when/if the user requests it.
     */
    public void flush() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    // Private utility methods start here.

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
        // Check each ID to see if present in contacts.
        for (int id : ids) {
            // Flag set to true when an id is found.
            boolean foundFlag = false;

            // Check to see if current ID is present in any of the contacts.
            // If found, set foundFlag to true.
            for (Contact contact : contactSet) {
                if (id == contact.getId()) {
                    foundFlag = true;
                }
            }

            if (foundFlag == false) {
                // No need to check other IDs. If one is not found, return false.
                return false;
            }
        }

        // All IDs found, so return true.
        return true;
    }

    /**
     * Checks if provided meeting IDs exist.
     *
     * @param ids meeting IDs.
     * @return true if all IDs exist, otherwise false.
     */
    private boolean allMeetingsExist(int... ids) {
        // Check each ID to see if present in meetings.
        for (int id: ids) {
            // Flag set to true when an id is found.
            boolean foundFlag = false;

            // Check to see if current ID is present in any of the meetings.
            // If found, set foundFlag to true.
            for (Meeting meeting : meetingList) {
                if (id == meeting.getId()) {
                    foundFlag = true;
                }
            }

            if (foundFlag == false) {
                // No need to check other IDs. If one is not found, return false.
                return false;
            }
        }

        // All IDs found, so return true.
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

    /**
     * Compares if two Calendars have equal date (not time) portions.
     *
     * @param calendarFirst  first calendar to compare.
     * @param calendarSecond second calendar to compare.
     * @return true if the dates have equal date portions, otherwise false.
     */
    private boolean calendarsEqual(Calendar calendarFirst, Calendar calendarSecond) {
        if (calendarFirst.get(Calendar.YEAR) != calendarSecond.get(Calendar.YEAR)) {
            return false;
        } else if (calendarFirst.get(Calendar.MONTH) != calendarSecond.get(Calendar.MONTH)) {
            return false;
        } else if (calendarFirst.get(Calendar.DAY_OF_MONTH) != calendarSecond.get(Calendar.DAY_OF_MONTH)) {
            return false;
        }
        return true;
    }
}
