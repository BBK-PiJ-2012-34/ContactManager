import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.IOException;

/**
 * Assignment 3 of Programming in Java - Birkbeck, University of London
 * Main class that manages and runs the contact manager application.
 *
 * Hisham Khalifa (MSc Computer Science 2012 - 2013, Full-Time)
 */
public class Main {
    private ContactManager contactManagerImpl;

    public static void main(String[] args) {
	    Main mainRunLoopManager = new Main();

        mainRunLoopManager.launch();
    }

    private void launch() {
        // Instantiate the ContactManager controller object.
        contactManagerImpl = new ContactManagerImpl();

        try {
            // Start our main run loop here.
            mainRunLoop();
        } catch (IOException e) {
            System.out.println("Input error. Will exit gracefully, saving data...");
            System.out.println(e);
            cleanup();
        }
    }

    private void mainRunLoop() throws IOException{

        char choice, ignore;

        for(;;) {
            do {
                System.out.println("*************************");
                System.out.println("*****CONTACT MANAGER*****");
                System.out.println("*************************");
                System.out.println();

                System.out.println("-------------------------");
                System.out.println("|      Select Option    |");
                System.out.println("-------------------------");
                System.out.println();

                System.out.println("MEETING OPTIONS");
                System.out.println("---------------");
                System.out.println("A. Add a new meeting to be held in the future");
                System.out.println("B. Search for a past meeting using a meeting ID");
                System.out.println("C. Search for a future meeting using a meeting ID");
                System.out.println("D. Search for a meeting using a meeting ID");
                System.out.println("E. List future meetings for a given contact");
                System.out.println("F. List future meetings for a given date");
                System.out.println("G. List past meetings for a given contact");
                System.out.println("H. Create a record for a meeting that took place in the past");
                System.out.println("I. Add notes to a meeting");
                System.out.println();

                System.out.println("CONTACT OPTIONS");
                System.out.println("---------------");
                System.out.println("J. Add a new contact");
                System.out.println("K. List contacts for provided IDs");
                System.out.println("L. Search for contact names");
                System.out.println();

                System.out.println("GENERAL OPTIONS");
                System.out.println("---------------");
                System.out.println("M. Save all data to disk");
                System.out.println("Q. Quit program");

                choice = (char) System.in.read();
                choice = Character.toLowerCase(choice);


                // Eat up extraneous new line characters to avoid messing up input on some consoles.
                do {
                    ignore = (char) System.in.read();
                } while(ignore != '\n');

            } while ( choice < 'a' | choice > 'm' & choice != 'q');

            // Run selected choice.
            doSelectedChoice(choice);
        }

    }

    private void doSelectedChoice(char choice) {
        switch (choice) {
            case 'a':
                System.out.println("Adding!");
                break;
            case 'b':
                break;
            case 'c':
                break;
            case 'd':
                break;
            case 'e':
                break;
            case 'f':
                break;
            case 'g':
                break;
            case 'h':
                break;
            case 'i':
                break;

            case 'j':
                addNewContact();
                break;
            case 'k':
                break;
            case 'l':
                break;

            case 'm':
                break;
            case 'q':
                cleanup(); // No need for break. System will exit in cleanup().


            default:
                System.out.println("Error! Unknown selection.");
                break;
        }
    }

    /**
     * Gets a name and note for a new contact and creates one using a call to contactManagerImpl.
     */
    private void addNewContact(){
        String name = null;
        String note = null;

        System.out.print("Enter name for new contact: ");
        name = System.console().readLine();
        System.out.print("Enter note for new contact: ");
        note = System.console().readLine();

        try {
            contactManagerImpl.addNewContact(name, note);
        } catch (NullPointerException e){
            System.out.println("Name or note where left null!");
            System.out.println(e);
        }
    }

    private void cleanup() {
        //save data
        System.out.println("Saving data to disk...");
        System.exit(0);
    }
}
