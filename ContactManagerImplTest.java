import org.junit.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * JUnit test class for ContactManageImpl
 */
public class ContactManagerImplTest {
    private ContactManager contactManager;
    private Calendar someFutureDate;
    private Calendar somePastDate;

    @Before
    public void setUp() throws Exception {
        contactManager = new ContactManagerImpl();

        // Five months in the future.
        someFutureDate = Calendar.getInstance();
        someFutureDate.add(Calendar.MONTH, 5);

        // Five months in the past.
        somePastDate = Calendar.getInstance();
        somePastDate.add(Calendar.MONTH, -5);
    }

    @After
    public void tearDown() throws Exception {
        contactManager = null;
        someFutureDate = null;
        somePastDate = null;
    }

    @Test
    public void testAddNewContact() throws Exception {
        String name = "John Maloney";
        String note = "Super good guy";

        contactManager.addNewContact(name, note);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNewContactWithNullName() throws Exception {
        String name = null;
        String note = "Super good guy";

        contactManager.addNewContact(name, note);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNewContactWithNullNote() throws Exception {
        String name = "John Maloney";
        String note = null;

        contactManager.addNewContact(name, note);
    }

    @Test
         public void testGetContactsUsingIDThatExist() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = null;
        int [] ids = {1,2};

        contactSet = contactManager.getContacts(ids);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetContactsUsingIDThatDontExit() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = null;
        int [] ids = {1,212,3,4,101};

        contactSet = contactManager.getContacts(ids);
    }

    @Test
    public void testGetContactsUsingNameThatExit() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = null;

        contactSet = contactManager.getContacts("John");

        Contact john = contactSet.iterator().next();

        org.junit.Assert.assertEquals(name1, john.getName());

        contactSet = contactManager.getContacts("Hugo Smith");

        Contact smith = contactSet.iterator().next();

        org.junit.Assert.assertEquals(name2, smith.getName());
    }

    @Test
    public void testGetContactsUsingNameThatDontExist() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = null;

        contactSet = contactManager.getContacts("Zarathustra");

        // Should return an empty set.
        org.junit.Assert.assertTrue(contactSet.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testGetContactsUsingNameWithNullForNameParameter() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = null;

        String nameToFind = null;
        contactSet = contactManager.getContacts(nameToFind);
    }

    @Test
    public void testAddFutureMeetingThatIsInFuture() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = null;
        int [] ids = {1,2};

        contactSet = contactManager.getContacts(ids);

        contactManager.addFutureMeeting(contactSet, someFutureDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFutureMeetingThatIsNotInFuture() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = null;
        int [] ids = {1,2};

        contactSet = contactManager.getContacts(ids);

        contactManager.addFutureMeeting(contactSet, somePastDate);
    }

    @Test
    public void testAddPastMeetingThatIsInThePast() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = null;
        int [] ids = {1,2};

        contactSet = contactManager.getContacts(ids);

        String note = "Everyone was civil.";

        contactManager.addNewPastMeeting(contactSet, somePastDate, note);
    }

    @Test
    public void testAddPastMeetingThatIsNotInThePast() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = null;
        int [] ids = {1,2};

        contactSet = contactManager.getContacts(ids);

        String note = "Everyone was civil.";

        // NOTE: This method as defined by the interface does NOT check for a date being in the FUTURE.
        // Therefore we can create with it a PAST MEETING THAT HAS A FUTURE DATE and NOT throw an exception.
        contactManager.addNewPastMeeting(contactSet, someFutureDate, note);
    }

    @Test(expected = NullPointerException.class)
    public void testAddPastMeetingThatIsInThePastWithNullParameters() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = null;
        int [] ids = {1,2};

        contactSet = contactManager.getContacts(ids);

        String note = null;

        contactManager.addNewPastMeeting(contactSet, somePastDate, note);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPastMeetingThatIsInThePastWithEmptyContactsList() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = new HashSet<Contact>();

        String note = "Everyone was civil.";

        contactManager.addNewPastMeeting(contactSet, somePastDate, note);
    }

    @Test
    public void testGetPastMeetingForMeetingIDThatDoesExist() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = null;
        int [] ids = {1,2};

        contactSet = contactManager.getContacts(ids);

        String note = "Everyone was civil.";

        contactManager.addNewPastMeeting(contactSet, somePastDate, note);

        PastMeeting pastMeeting = null;
        pastMeeting = contactManager.getPastMeeting(2444);

        org.junit.Assert.assertEquals(2444, pastMeeting.getId());
    }

    @Test
    public void testGetPastMeetingForMeetingIDThatDoesNotExist() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = null;
        int [] ids = {1,2};

        contactSet = contactManager.getContacts(ids);

        String note = "Everyone was civil.";

        contactManager.addNewPastMeeting(contactSet, somePastDate, note);

        PastMeeting pastMeeting = null;
        pastMeeting = contactManager.getPastMeeting(3265);

        org.junit.Assert.assertNull(pastMeeting);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPastMeetingForMeetingIDThatIsInTheFuture() throws Exception {
        String name1 = "John Maloney";
        String note1 = "Super good guy";

        contactManager.addNewContact(name1, note1);

        String name2 = "Hugo Smith";
        String note2 = "Another super cool dude";

        contactManager.addNewContact(name2, note2);

        Set<Contact> contactSet = null;
        int [] ids = {1,2};

        contactSet = contactManager.getContacts(ids);

        String note = "Everyone was civil.";

        contactManager.addNewPastMeeting(contactSet, somePastDate, note);

        PastMeeting pastMeeting = null;
        pastMeeting = contactManager.getPastMeeting(422657491);
    }

    @Test
    public void testGetFutureMeeting() throws Exception {

    }

    @Test
    public void testGetMeeting() throws Exception {

    }

    @Test
    public void testGetFutureMeetingList() throws Exception {

    }

    @Test
    public void testGetPastMeetingList() throws Exception {

    }

    @Test
    public void testAddNewPastMeeting() throws Exception {

    }

    @Test
    public void testAddMeetingNotes() throws Exception {

    }

    @Test
    public void testFlush() throws Exception {

    }
}
