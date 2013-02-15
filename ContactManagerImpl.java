import java.io.*;
import java.util.*;

/**
 * ContactManagerImpl - Implements ContactManager interface.
 *
 * A class to manage your contacts and meetings.
 */
public class ContactManagerImpl implements ContactManager {
    public static final String DATA_FILE = "contacts.txt";
    public static final String DELIMITER = "&";
    public static final String ATTENDEE_DELIMITER = "±";

    // Meeting lists.
    private List<PastMeeting> pastMeetingList = null;
    private List<FutureMeeting> futureMeetingList = null;

    // Caches ID integers used up so far for all meetings.
    private List<Integer> idIntegersList = null;

    // Caches ID integers used up so far for future meetings.
    private List<Integer> idFutureIntegersList = null;

    // Caches ID integers used up so far for past meetings.
    private List<Integer> idPastIntegersList = null;

    // Contacts set.
    private Set<Contact> contactSet = null;

    /**
     * Constructor
     * Note that it loads the data file if available.
     */
    public ContactManagerImpl() {
        // The List interface is implemented as ArrayList
        // and the Set interface as HashSet.
        this.contactSet = new HashSet<Contact>();

        this.pastMeetingList = new ArrayList<PastMeeting>();
        this.futureMeetingList = new ArrayList<FutureMeeting>();

        this.idIntegersList = new ArrayList<Integer>();
        this.idFutureIntegersList = new ArrayList<Integer>();
        this.idPastIntegersList = new ArrayList<Integer>();

        // Load contacts.txt if available.
        loadDataAsCSV();
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
        if (!Utilities.timeInFuture(date)) {
            throw new IllegalArgumentException("Meeting time is in the past.");
        }

        // Exception thrown if at least one ID doesn't exist.
        for (Contact contact : contacts) {
            int id = contact.getId();
            if (!allContactsExist(id)) {
                throw new IllegalArgumentException("Contact ID supplied does not exist.");
            }
        }

        // Get unique ID not used by other meetings.
        int meetingId = Utilities.createUniqueInteger(this.idIntegersList);

        // Cache meeting ID in ID lists.
        this.idFutureIntegersList.add(meetingId);
        this.idIntegersList.add(meetingId);

        // Create meeting.
        FutureMeeting futureMeeting = new FutureMeetingImpl(meetingId, date, contacts);

        // Add meeting to future meeting list.
        this.futureMeetingList.add(futureMeeting);

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
        if (!this.idIntegersList.contains(id)) {
            return null;
        }

        // Check if meeting is not in future based on future meetings ID list.
        if (this.idFutureIntegersList.contains(id)) {
            throw new IllegalArgumentException("Meeting time is in the future.");
        }

        Meeting meeting = getMeeting(id);

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
        if (!this.idIntegersList.contains(id)) {
            return null;
        }

        Meeting meeting = getMeeting(id);

        // Check if meeting is not in past based on past meetings ID list.
        if (this.idPastIntegersList.contains(id)) {
            throw new IllegalArgumentException("Meeting time is in the past.");
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
        // Check if ID is in past list and return past meeting.
        if (this.idPastIntegersList.contains(id)) {
            for (PastMeeting aPastMeeting : this.pastMeetingList) {
                if (aPastMeeting.getId() == id) {
                    return aPastMeeting;
                }
            }
        }

        // Check if ID is in future list and return future meeting.
        if (this.idFutureIntegersList.contains(id)) {
            for (FutureMeeting aFutureMeeting : this.futureMeetingList) {
                if (aFutureMeeting.getId() == id) {
                    return aFutureMeeting;
                }
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

        List<Meeting> meetingListForContact = new ArrayList<Meeting>();

        for (Meeting meeting : this.futureMeetingList) {
            Set<Contact> contacts = meeting.getContacts();
            if (contacts.contains(contact)) {
                meetingListForContact.add(meeting);
            }
        }

        meetingListForContact = Utilities.removeDuplicateItemsInList(meetingListForContact);

        // Chronological sort.
        // Here we make the ArrayList into explicit MeetingImpl ArrayList since Collections.compareTo() is implemented
        // in MeetingImpl and not part of the Meeting interface spec.
        // Create empty MeetingImpl ArrayList.
        List<MeetingImpl> sortedList = new ArrayList<MeetingImpl>();
        // Fill up list with meetings from Meeting ArrayList.
        for (Meeting meeting : meetingListForContact) {
            sortedList.add((MeetingImpl)meeting);
        }

        // Sort list.
        Collections.sort(sortedList);

        // Now convert back to Meeting ArrayList.
        meetingListForContact.clear();
        for (Meeting meeting : sortedList) {
            meetingListForContact.add(meeting);
        }

        return meetingListForContact;
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

        List<Meeting> pastAndFutureMeetingsForDateList = new ArrayList<Meeting>();

        // Get meetings from future list.
        for (Meeting meeting : this.futureMeetingList) {
            Calendar meetingDate = meeting.getDate();
            if (Utilities.calendarsEqual(meetingDate, date)) {
                pastAndFutureMeetingsForDateList.add(meeting);
            }
        }

        // Get meetings from past list.
        for (Meeting meeting : this.pastMeetingList) {
            Calendar meetingDate = meeting.getDate();
            if (Utilities.calendarsEqual(meetingDate, date)) {
                pastAndFutureMeetingsForDateList.add(meeting);
            }
        }

        pastAndFutureMeetingsForDateList = Utilities.removeDuplicateItemsInList(pastAndFutureMeetingsForDateList);

        // Chronological sort.
        // Here we make the ArrayList into explicit MeetingImpl ArrayList since Collections.compareTo() is implemented
        // in MeetingImpl and not part of the Meeting interface spec.
        // Create empty MeetingImpl ArrayList.
        List<MeetingImpl> sortedList = new ArrayList<MeetingImpl>();
        // Fill up list with meetings from Meeting ArrayList.
        for (Meeting meeting : pastAndFutureMeetingsForDateList) {
            sortedList.add((MeetingImpl)meeting);
        }

        // Sort list.
        Collections.sort(sortedList);

        // Now convert back to Meeting ArrayList.
        pastAndFutureMeetingsForDateList.clear();
        for (Meeting meeting : sortedList) {
            pastAndFutureMeetingsForDateList.add(meeting);
        }

        return pastAndFutureMeetingsForDateList;
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

        // Interface Definition Note: For some reason interface requires return type to be a List<PastMeeting>
        // while for the similar method for future meetings, it only requires a List<Meeting> return type.
        List<PastMeeting> pastMeetingsForContactList = new ArrayList<PastMeeting>();

        for (PastMeeting meeting : this.pastMeetingList) {
            Set<Contact> contacts = meeting.getContacts();
            if (contacts.contains(contact)) {
                pastMeetingsForContactList.add(meeting);
            }
        }

        pastMeetingsForContactList = Utilities.removeDuplicateItemsInList(pastMeetingsForContactList);

        // Chronological sort.
        // Here we make the ArrayList into explicit PastMeetingImpl ArrayList since Collections.compareTo() is implemented
        // in MeetingImpl and not part of the Meeting interface spec.
        // Create empty MeetingImpl ArrayList.
        List<PastMeetingImpl> sortedList = new ArrayList<PastMeetingImpl>();
        // Fill up list with meetings from Meeting ArrayList.
        for (PastMeeting meeting : pastMeetingsForContactList) {
            sortedList.add((PastMeetingImpl)meeting);
        }

        // Sort list.
        Collections.sort(sortedList);

        // Now convert back to PastMeeting ArrayList.
        pastMeetingsForContactList.clear();
        for (PastMeeting meeting : sortedList) {
            pastMeetingsForContactList.add(meeting);
        }

        return pastMeetingsForContactList;
    }

    /**
     * Create a new record for a meeting that took place in the past.
     *
     * @param contacts a list of participants.
     * @param date     the date on which the meeting took place.
     * @param text     messages to be added about the meeting.
     * @throws IllegalArgumentException if the list of contacts is empty, or any of the contacts does not exist.
     * @throws NullPointerException     if any of the arguments is null.
     */
    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        // NOTE: This method as defined by the interface does NOT check for a date being in the FUTURE.
        // Therefore we can create with it a PAST MEETING THAT HAS A FUTURE DATE and NOT throw an exception.

        if (contacts == null || date == null || text == null) {
            throw new NullPointerException("Parameter(s) supplied is null.");
        }

        if (contacts.isEmpty()) {
            throw new IllegalArgumentException("Contact list is empty.");
        }

        // Throw exception of at least one of the contacts is not found.
        for (Contact contact : contacts) {
            if (!allContactsExist(contact.getId())) {
                throw new IllegalArgumentException("Contact ID supplied does not exist.");
            }
        }

        // Get unique ID not used by other meetings.
        int meetingId = Utilities.createUniqueInteger(this.idIntegersList);

        // Cache meeting ID in ID lists.
        this.idPastIntegersList.add(meetingId);
        this.idIntegersList.add(meetingId);

        // Create past meeting.
        PastMeeting pastMeeting = new PastMeetingImpl(meetingId, date, contacts, text);

        // Add past meeting to past meeting list.
        this.pastMeetingList.add(pastMeeting);
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
     * @throws IllegalStateException    if the meeting is set for a date in the future.
     * @throws NullPointerException     if the notes are null.
     */
    @Override
    public void addMeetingNotes(int id, String text) {
        // Exception thrown if meeting does not exist.
        if (! this.idIntegersList.contains(id) ) {
            throw new IllegalArgumentException("Meeting ID does not exist.");
        }

        Meeting meeting = getMeeting(id);

        // This is slippery as per the interface spec and discussion. We need to check the date
        // and NOT the object type (i.e. checking that it is FutureMeetingImpl is NOT right).
        // This is important since using addNewPastMeeting we can actually create a PastMeeting
        // that has a FUTURE date without throwing an exception.

        // Use private method to check if meeting date is in the future and throw exception if it is.
        if (Utilities.timeInFuture(meeting.getDate())) {
            throw new IllegalStateException("Meeting time is in the future.");
        }

        if (text == null) {
            throw new NullPointerException("Notes are null.");
        }

        // If meeting is a FutureMeetingImpl type, convert it to PastMeetingImpl type.
        if (this.futureMeetingList.contains(meeting)) {
            // Keep references to FutureMeetingImpl object's state.
            int tempID = meeting.getId();
            Calendar tempDate = meeting.getDate();
            Set<Contact> tempContacts = meeting.getContacts();

            // Remove cached ID from future ID list.
            this.idFutureIntegersList.remove((Integer)tempID);

            // Remove FutureMeetingImpl meeting object from future meeting list.
            this.futureMeetingList.remove(meeting);

            // Create new past meeting with future meeting's state.
            PastMeeting tempMeeting = new PastMeetingImpl(tempID, tempDate, tempContacts, text);

            // Add the PastMeetingImpl meeting object to the past meeting list.
            this.pastMeetingList.add(tempMeeting);

            // Add cached ID to past ID list.
            this.idPastIntegersList.add(tempID);

            // As this was a converted future meeting object, we've already added the notes during
            // its conversion, so we just return.
            return;
        }

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

        // NOTE: Interface does not specify an exception for a null result, so we just
        // return an empty list in case there are no matches.
        return tempContactSet;
    }

    /**
     * Save all data to disk.
     *
     * This method must be executed when the program is closed and when/if the user requests it.
     */
    public void flush() {
        saveDataAsCSV();
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
     * Saves contacts and meetings to CSV text file.
     *
     * @return returns true if successful save, otherwise false.
     */
    private boolean saveDataAsCSV() {
        File file = new File(DATA_FILE);
        PrintWriter out = null;
        try {
            out = new PrintWriter(file);

            // Save contacts.
            for (Contact contact : contactSet) {
                String contactLine;
                int tempID = contact.getId();
                String tempName = contact.getName();
                String tempNotes = contact.getNotes();

                contactLine = "CONTACT" + DELIMITER + tempID + DELIMITER + tempName + DELIMITER + tempNotes;
                out.println(contactLine);
            }

            // Save past meetings.
            for (PastMeeting meeting : this.pastMeetingList) {
                String meetingLine;

                    int tempID = meeting.getId();
                    Calendar tempDate = meeting.getDate();
                    String tempNotes = meeting.getNotes();
                    String tempContacts = "";

                    // Get string representation of date.
                    String tempStringDate = Utilities.calendarToString(tempDate);

                    // Create delimited list of attendees.
                    for (Contact attendee : meeting.getContacts()) {
                        tempContacts += attendee.getId() + ATTENDEE_DELIMITER;
                    }
                    // Trim last extraneous delimiting character.
                    tempContacts = tempContacts.substring(0, tempContacts.length() - 1);

                    meetingLine = "PASTMEETING" + DELIMITER + tempID + DELIMITER + tempStringDate + DELIMITER + tempNotes + DELIMITER + tempContacts;

                    out.println(meetingLine);
            }

            // Save future meetings.
            for (FutureMeeting meeting : futureMeetingList) {
                String meetingLine;

                    int tempID = meeting.getId();
                    Calendar tempDate = meeting.getDate();
                    String tempContacts = "";

                    // Stringify date.
                    String tempStringDate = Utilities.calendarToString(tempDate);

                    // Create delimited list of attendees.
                    for (Contact attendee : meeting.getContacts()) {
                        tempContacts += attendee.getId() + ATTENDEE_DELIMITER;
                    }
                    // Trim last extraneous delimiting character.
                    tempContacts = tempContacts.substring(0, tempContacts.length() - 1);

                    meetingLine = "FUTUREMEETING" + DELIMITER + tempID + DELIMITER + tempStringDate + DELIMITER + tempContacts;

                out.println(meetingLine);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
                return true;
            }
            return false;
        }
    }

    /**
     * Loads contacts and meetings from CSV text file.
     *
     * @return returns true if successful load, otherwise false.
     */
    private boolean loadDataAsCSV() {
        File file = new File(DATA_FILE);
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String line;

            while ((line = in.readLine()) != null) {
                // Split line using delimiter.
                String[] tokens = line.split(DELIMITER);

                // All data should have at least 4 tokens, otherwise do not process line.
                if (tokens.length >= 4) {
                    if (tokens[0].equals("CONTACT")) {
                        // Get contact attributes.
                        int tempID = Integer.parseInt(tokens[1]);
                        String tempName = tokens[2];
                        String tempNotes = tokens[3];

                        // Create contact object using loaded attributes.
                        Contact recreatedContact = new ContactImpl(tempID, tempName, tempNotes);
                        // Add contact to contact list.
                        this.contactSet.add(recreatedContact);
                    } else if (tokens[0].equals("PASTMEETING")) {
                        // Get past meeting attributes.
                        int meetingID = Integer.parseInt(tokens[1]);
                        String tempStringDate = tokens[2];
                        String tempNotes = tokens[3];
                        String tempContacts = tokens[4];
                        Calendar meetingDate;
                        Set<Contact> tempContactsSet;

                        meetingDate = Utilities.stringToCalendar(tempStringDate);

                        // Create contact set for meeting.
                        tempContactsSet = new HashSet<Contact>();
                        String[] contactTokens = tempContacts.split(ATTENDEE_DELIMITER);
                        for (String contactID : contactTokens) {
                            int tempID = Integer.parseInt(contactID);
                            tempContactsSet.add(getContact(tempID));
                        }

                        // Recreate past meeting.
                        PastMeeting recreatedPastMeeting = new PastMeetingImpl(meetingID, meetingDate, tempContactsSet, tempNotes);
                        // Add meeting to meeting list.
                        this.pastMeetingList.add(recreatedPastMeeting);

                        // Cache meeting ID in ID lists.
                        this.idPastIntegersList.add(meetingID);
                        this.idIntegersList.add(meetingID);

                    } else if (tokens[0].equals("FUTUREMEETING")) {
                        // Get future meeting attributes.
                        int meetingID = Integer.parseInt(tokens[1]);
                        String tempStringDate = tokens[2];
                        String tempContacts = tokens[3];
                        Calendar meetingDate;
                        Set<Contact> tempContactsSet;

                        meetingDate = Utilities.stringToCalendar(tempStringDate);

                        // Create contact set for meeting.
                        tempContactsSet = new HashSet<Contact>();
                        String[] contactTokens = tempContacts.split(ATTENDEE_DELIMITER);
                        for (String contactID : contactTokens) {
                            int tempID = Integer.parseInt(contactID);
                            tempContactsSet.add(getContact(tempID));
                        }

                        // Recreate future meeting.
                        FutureMeeting recreatedFutureMeeting = new FutureMeetingImpl(meetingID, meetingDate, tempContactsSet);
                        // Add meeting to meeting list.
                        this.futureMeetingList.add(recreatedFutureMeeting);

                        // Cache meeting ID in ID lists.
                        this.idFutureIntegersList.add(meetingID);
                        this.idIntegersList.add(meetingID);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("contacts.txt file does not exist. All contacts and meeting data is empty.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }
}
