import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Main class for Coursework Assignment 3 of Programming in Java - Birkbeck, University of London
 *
 * Manages and runs the contact manager application.
 *
 * Hisham Khalifa (MSc Computer Science 2012 - 2013, Full-Time).
 */
public class Main {

    private ContactManager contactManagerImpl;

    /**
     * Calls method launch.
     *
     * @param args not used / ignored.
     */
    public static void main(String[] args) {
        Main mainRunLoopManager = new Main();

        mainRunLoopManager.launch();
    }

    /**
     * Instantiates an object of this class and calls its main run loop method.
     */
    private void launch() {
        // Instantiate the ContactManager controller object.
        contactManagerImpl = new ContactManagerImpl();

        try {
            // Start our main run loop here.
            mainRunLoop();
        } catch (IOException e) {
            System.out.println("Input error. Will exit gracefully, saving data...");
            e.printStackTrace();
            cleanup();
        }
    }

    /**
     * Main run loop with textual menu display and selection prompt.
     *
     * @throws IOException if selection input is malformed.
     */
    private void mainRunLoop() throws IOException {
        char choice, ignore;

        for (;;) {
            do {
                System.out.println("*************************");
                System.out.println("*****CONTACT MANAGER*****");
                System.out.println("*************************");
                System.out.println();

                System.out.println("TEST CONTACT OPTIONS");
                System.out.println("--------------------");
                System.out.println("J. *Add a new contact");
                System.out.println("K. *List contacts for provided IDs");
                System.out.println("L. *Search for contact names");
                System.out.println();

                System.out.println("TEST MEETING OPTIONS");
                System.out.println("--------------------");
                System.out.println("A. *Add a new meeting to be held in the future");
                System.out.println("B. *Search for a past meeting using a meeting ID");
                System.out.println("C. *Search for a future meeting using a meeting ID");
                System.out.println("D. *Search for a meeting using a meeting ID");
                System.out.println();
                System.out.println("E. *List future meetings for a given contact");
                System.out.println("F. *List past and future meetings for a given date");
                System.out.println("G. *List past meetings for a given contact");
                System.out.println();
                System.out.println("H. *Create a record for a meeting that took place in the past");
                System.out.println("I. *Add notes to a meeting");
                System.out.println();

                System.out.println("TEST GENERAL OPTIONS");
                System.out.println("--------------------");
                System.out.println("M. *Save all data to disk");
                System.out.println("Q. *Quit program");

                choice = (char) System.in.read();
                choice = Character.toLowerCase(choice);

                // Eat up extraneous new line characters to avoid messing up input on some consoles.
                do {
                    ignore = (char) System.in.read();
                } while (ignore != '\n');

            } while (choice < 'a' | choice > 'm' & choice != 'q');

            // Run selected choice.
            doSelectedChoice(choice);
        }
    }

    /**
     * Calls selected option method based on provided choice char.
     *
     * @param choice selection char.
     */
    private void doSelectedChoice(char choice) {
        switch (choice) {
            case 'a':
                addNewFutureMeeting();
                break;
            case 'b':
                searchPastMeetingUsingID();
                break;
            case 'c':
                searchFutureMeetingUsingID();
                break;
            case 'd':
                searchMeetingUsingID();
                break;
            case 'e':
                listFutureMeetingsWithContact();
                break;
            case 'f':
                listMeetingsOnDate();
                break;
            case 'g':
                listPastMeetingsWithContact();
                break;
            case 'h':
                createRecordForPastMeeting();
                break;
            case 'i':
                addNotesToAMeeting();
                break;
            case 'j':
                addNewContact();
                break;
            case 'k':
                listContactsForProvidedIDs();
                break;
            case 'l':
                listContactsHavingStringInName();
                break;
            case 'm':
                contactManagerImpl.flush();
                break;
            case 'q':
                cleanup(); // No need for break. System will exit in cleanup().
            default:
                System.out.println("Error! Unknown selection.");
                break;
        }
    }

    /**
     * Prompts user for a name and note for a new contact and creates one.
     * Calls ContactManagerImpl addNewContact(String name, String notes).
     */
    private void addNewContact() {
        String name = null;
        String note = null;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter name for new contact: ");
            name = br.readLine();

            // Idiom to make it null if nothing is entered.
            if (name.length() == 0) {
                name = null;
            }

            System.out.print("Enter note for new contact: ");
            note = br.readLine();

            // Idiom to make it null if nothing is entered.
            if (note.length() == 0) {
                note = null;
            }

            contactManagerImpl.addNewContact(name, note);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Buffered input error!");
            e.printStackTrace();
        }
    }

    /**
     * Prompts user for contact IDs and prints out those that are found.
     * Calls ContactManagerImpl getContacts(int... ids).
     */
    private void listContactsForProvidedIDs() {
        String idList;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter contact ID ");
            idList = br.readLine();

            Set<Contact> contactSet = contactManagerImpl.getContacts(Utilities.delimitedNumbersStringToIntArray(idList));

            // Print contacts list.
            printContacts(contactSet);

        } catch (IllegalArgumentException e) {
            System.out.println("ID supplied is invalid!");
            e.printStackTrace();
        }  catch (IOException e) {
            System.out.println("Buffered input error!");
            e.printStackTrace();
        }
    }

    /**
     * Prompts user for a search string and prints out those contacts whose name contains the string.
     * Calls ContactManagerImpl getContacts(String name).
     */
    private void listContactsHavingStringInName() {
        String searchString = null;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter search string for contacts:  ");
            searchString = br.readLine();

            // Idiom to set empty entry to null.
            if (searchString.length() == 0) {
                searchString = null;
            }

            Set<Contact> contactSet = contactManagerImpl.getContacts(searchString);

            // Print contacts list.
            printContacts(contactSet);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Buffered input error!");
            e.printStackTrace();
        }
    }

    /**
     * Prompts user for details of a future meeting and adds it.
     * Calls ContactManagerImpl addFutureMeeting(Set<Contact> contacts, Calendar date).
     */
    private void addNewFutureMeeting() {
        Calendar meetingDate = null;
        Set<Contact> attendees = null;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter date for future meeting (yyyy/MM/dd HH:mm:ss): ");
            String userDateInput = br.readLine();

            SimpleDateFormat dateFormatter = new SimpleDateFormat(Utilities.DATE_FORMAT);
            Date date = null;
            if (userDateInput != null) {
                date = dateFormatter.parse(userDateInput);
            }
            meetingDate = Calendar.getInstance();
            meetingDate.setTime(date);

            System.out.print("Enter ID of attendees: ");
            String contactIDs = br.readLine();

            attendees = contactManagerImpl.getContacts(Utilities.delimitedNumbersStringToIntArray(contactIDs));

            contactManagerImpl.addFutureMeeting(attendees, meetingDate);

        } catch (ParseException e) {
            System.out.println("Date format incorrect. New meeting action terminated.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Buffered input error!");
            e.printStackTrace();
        }
    }

    /**
     * Prompts user for past meeting ID and prints its details.
     * Calls ContactManagerImpl getPastMeeting(int id).
     */
    private void searchPastMeetingUsingID() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter past meeting ID to display details: ");
            int meetingID = Integer.parseInt(br.readLine());

            PastMeeting meeting = contactManagerImpl.getPastMeeting(meetingID);
            if (meeting != null) {
                printPastMeetings(new ArrayList<PastMeeting>(Arrays.asList(meeting)));
            } else {
                System.out.println("Meeting not found.");
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Buffered input error!");
            e.printStackTrace();
        }
    }

    /**
     * Prompts user for future meeting ID and prints its details.
     * Calls ContactManagerImpl getFutureMeeting(int id).
     */
    private void searchFutureMeetingUsingID() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter future meeting ID to display details: ");
            int meetingID = Integer.parseInt(br.readLine());

            Meeting meeting = contactManagerImpl.getFutureMeeting(meetingID);
            if (meeting != null) {
                printMeetings(new ArrayList<Meeting>(Arrays.asList(meeting)));
            } else {
                System.out.println("Meeting not found.");
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Buffered input error!");
            e.printStackTrace();
        }
    }

    /**
     * Prompts user for meeting ID and prints its details.
     * Calls ContactManagerImpl getMeeting(int id).
     */
    private void searchMeetingUsingID() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter meeting ID to display details: ");
            int meetingID = Integer.parseInt(br.readLine());

            Meeting meeting = contactManagerImpl.getMeeting(meetingID);
            if (meeting != null) {
                printMeetings(new ArrayList<Meeting>(Arrays.asList(meeting)));
            }

        } catch (IOException e) {
            System.out.println("Buffered input error!");
            e.printStackTrace();
        }
    }

    /**
     * Prompts user for contact ID and prints all future meetings with contact attending.
     * Calls ContactManagerImpl getFutureMeetingList(Contact contact).
     */
    private void listFutureMeetingsWithContact() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter contact ID to display future meetings with contact attending: ");
            int contactID = Integer.parseInt(br.readLine());

            // Use the ContactManager interface method of getContacts to get our one contact in a set.
            Set<Contact> attendee = null;
            // Will have one contact only.
            attendee = contactManagerImpl.getContacts(contactID);
            // Get the one and only contact from the set.
            Contact contact = attendee.iterator().next();
            List<Meeting> meetings = contactManagerImpl.getFutureMeetingList(contact);

            if (meetings != null) {
                printMeetings(meetings);
            } else {
                System.out.println("No future meetings scheduled with contact.");
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Buffered input error!");
            e.printStackTrace();
        }
    }

    /**
     * Prompts user for a date and prints all meetings on that day (whether past or future).
     * Calls ContactManagerImpl getFutureMeetingList(Calendar date).
     */
    private void listMeetingsOnDate() {
        Calendar searchDate = null;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter date to list meetings on (yyyy/MM/dd): ");
            String userDateInput = br.readLine();

            SimpleDateFormat dateFormatter = new SimpleDateFormat(Utilities.DATE_FORMAT_NO_TIME);
            Date date = null;
            if (userDateInput != null) {
                date = dateFormatter.parse(userDateInput);
            }
            searchDate = Calendar.getInstance();
            searchDate.setTime(date);

            List<Meeting> meetings = contactManagerImpl.getFutureMeetingList(searchDate);

            if (meetings != null) {
                printMeetings(meetings);
            } else {
                System.out.println("No meetings found for provided date!");
            }

        } catch (ParseException e) {
            System.out.println("Date format incorrect! New meeting action terminated.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Buffered input error!");
            e.printStackTrace();
        }
    }

    /**
     * Prompts user for contact ID and prints all past meetings with contact having attended.
     * Calls ContactManagerImpl getPastMeetingList(Contact contact).
     */
    private void listPastMeetingsWithContact() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter contact ID to display past meetings with contact: ");
            int contactID = Integer.parseInt(br.readLine());

            // Use the ContactManager interface method of getContacts to get our one contact in a set.
            Set<Contact> attendee = null;
            attendee = contactManagerImpl.getContacts(contactID); // Will have one contact only.
            List<PastMeeting> meetings = contactManagerImpl.getPastMeetingList(attendee.iterator().next());

            if (meetings != null) {
                printPastMeetings(meetings);
            } else {
                System.out.println("No past meetings occurred with contact.");
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Buffered input error!");
            e.printStackTrace();
        }
    }

    /**
     * Prompts user for details of a past meeting and adds it.
     * Calls ContactManagerImpl addNewPastMeeting(Set<Contact> contacts, Calendar date, String text).
     */
    private void createRecordForPastMeeting() {
        Calendar meetingDate = null;
        Set<Contact> attendees = null;
        String notes = null;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter date of past meeting (yyyy/MM/dd HH:mm:ss): ");
            String userDateInput = br.readLine();

            SimpleDateFormat dateFormatter = new SimpleDateFormat(Utilities.DATE_FORMAT);
            Date date = null;
            if (userDateInput != null) {
                date = dateFormatter.parse(userDateInput);
            }
            meetingDate = Calendar.getInstance();
            meetingDate.setTime(date);

            System.out.print("Enter ID of attendees for past meeting: ");
            String contactIDs = br.readLine();

            attendees = contactManagerImpl.getContacts(Utilities.delimitedNumbersStringToIntArray(contactIDs));

            System.out.print("Enter notes for past meeting: ");
            notes = br.readLine();

            contactManagerImpl.addNewPastMeeting(attendees, meetingDate, notes);

        } catch (ParseException e) {
            System.out.println("Date format incorrect! New meeting action terminated.");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Buffered input error!");
            e.printStackTrace();
        }
    }

    /**
     * Prompts user for a meeting ID and the note to be added to the meeting.
     * Calls ContactManagerImpl addMeetingNotes(int id, String text).
     */
    private void addNotesToAMeeting() {
        int meetingID = 0;
        String notes = null;

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter meeting ID to set note for: ");
            meetingID = Integer.parseInt(br.readLine());

            System.out.print("Enter notes for meeting: ");
            notes = br.readLine();

            contactManagerImpl.addMeetingNotes(meetingID, notes);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Buffered input error!");
            e.printStackTrace();
        }
    }

    /**
     * Convenience method that prints given contact set and pauses.
     *
     * @param contactSet set of contacts to print.
     */
    private void printContacts(Set<Contact> contactSet) {
        if (contactSet.isEmpty()) {
            System.out.println("No contacts found.");
            pause();
            return;
        }

        for (Contact contact : contactSet) {
            System.out.print("ID: " + contact.getId() + "   Name: " + contact.getName());
            System.out.print("      Notes: " + contact.getNotes());
            System.out.println();
        }

        pause();
    }

    /**
     * Convenience method that prints given meeting list and pauses.
     *
     * @param meetingList set of meetings to print.
     */
    private void printMeetings(List<Meeting> meetingList) {
        if (meetingList.isEmpty()) {
            System.out.println("No meetings found.");
            pause();
            return;
        }

        System.out.println("Future Meetings");
        System.out.println("---------------");
        for (Meeting meeting : meetingList) {
            System.out.print("ID: " + meeting.getId() + "   Date: " + Utilities.calendarToString(meeting.getDate()));
            // Print notes in case it's a past meeting
            System.out.println();
        }

        pause();
    }

    /**
     * Convenience method that prints given past meeting list and pauses.
     *
     * @param meetingList set of past meetings to print.
     */
    private void printPastMeetings(List<PastMeeting> meetingList) {
        if (meetingList.isEmpty()) {
            System.out.println("No meetings found.");
            pause();
            return;
        }

        System.out.println("Past Meetings");
        System.out.println("---------------");
        for (Meeting meeting : meetingList) {
            System.out.print("ID: " + meeting.getId() + "   Date: " + Utilities.calendarToString(meeting.getDate()));
            // Print notes since it's a past meeting.
            System.out.println("    Notes: " + ((PastMeetingImpl) meeting).getNotes());
            System.out.println();
        }

        pause();
    }

    /**
     * Utility method that pauses display until return is pressed.
     */
    private void pause() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Press RETURN to continue...");
        try {
            br.readLine();
        } catch (IOException e) {
            System.out.println("Buffered input error!");
            e.printStackTrace();
        }
    }

    /**
     * Method to flush and exit.
     */
    private void cleanup() {
        contactManagerImpl.flush();
        System.out.println("Saving data to disk...");
        System.exit(0);
    }
}
