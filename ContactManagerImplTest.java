import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * JUnit test class for ContactManagerImpl.
 *
 */
public class ContactManagerImplTest {
    ContactManagerImpl contactManagerImpl = null;


    @Before
    public void setUp() throws Exception {
        contactManagerImpl = new ContactManagerImpl();
    }

    @After
    public void tearDown() throws Exception {
        contactManagerImpl = null;
    }

    @Test
    public void testAddFutureMeeting() throws Exception {

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

    /**
     * addNewContact() tests
     *
     */

    @Test
    public void testAddNewContact() {
        contactManagerImpl.addNewContact("John Crifton", "Super nice guy");
        contactManagerImpl.addNewContact("Jack Smith", "A superhero");

        Set<Contact> contactSet = contactManagerImpl.getContacts(1, 2, 3);

        assertTrue(contactSet.size() == 2);
    }

    @Test( expected = NullPointerException.class )
    public void testAddNewContactNullName() {
        contactManagerImpl.addNewContact(null, "Super nice guy");
    }

    @Test( expected = NullPointerException.class )
    public void testAddNewContactNullNote() {
        contactManagerImpl.addNewContact("John Crifton", null);
    }

    @Test
    public void testGetContacts() throws Exception {

    }

    @Test
    public void testFlush() throws Exception {

    }
}
