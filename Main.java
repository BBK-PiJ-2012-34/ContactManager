import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.IOException;

/**
 * Assignment 3 of Programming in Java - Birkbeck, University of London
 * Main class that manages and runs the contact manager application.
 *
 * Hisham Khalifa (MSc Computer Science 2012 - 2013, Full-Time)
 */
public class Main {

    public static void main(String[] args) {
	    Main contactManagerApp = new Main();

        contactManagerApp.launch();
    }

    private void launch() {
        try {
            // Start our main run loop here.
            mainRunLoop();
        } catch (IOException e) {
            System.out.println(e);
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

            if(choice == 'q') {
                //save data then break
                cleanup();
                break;
            }

            switch (choice) {
                case 'a':
                    System.out.println("Adding!");
                    break;
                default:
                    System.out.println("Error! Unknown selection.");
                    break;
            }
        }

    }

    private void cleanup() {
        //save data
    }
}
