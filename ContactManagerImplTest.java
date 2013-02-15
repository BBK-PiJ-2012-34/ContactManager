import org.junit.*;

import java.util.Set;

/**
 * JUnit test class for ContactManageImpl
 */
public class ContactManagerImplTest {
    private ContactManager contactManager;

    @Before
    public void setUp() throws Exception {
        contactManager = new ContactManagerImpl();
    }

    @After
    public void tearDown() throws Exception {
        contactManager = null;
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

    }

    @Test
    public void testGetPastMeeting() throws Exception {

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
