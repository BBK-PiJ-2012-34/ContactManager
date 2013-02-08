import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: hisham
 * Date: 2/8/13
 * Time: 1:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utilities {
    public static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FORMAT_NO_TIME = "yyyy/MM/dd";
    /**
     * Checks if provided date is in the future.
     *
     * @param date date to check.
     * @return true if the date is in the future, otherwise false.
     */
    public static boolean timeInFuture(Calendar date) {
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
    public static boolean calendarsEqual(Calendar calendarFirst, Calendar calendarSecond) {
        if (calendarFirst.get(Calendar.YEAR) != calendarSecond.get(Calendar.YEAR)) {
            return false;
        } else if (calendarFirst.get(Calendar.MONTH) != calendarSecond.get(Calendar.MONTH)) {
            return false;
        } else if (calendarFirst.get(Calendar.DAY_OF_MONTH) != calendarSecond.get(Calendar.DAY_OF_MONTH)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a unique integer that is not found in the provided int array.
     *
     * @param existingIntegers list of Integers.
     * @return a unique int that is not found in the provided integers list.
     */
    public static int createUniqueInteger(List<Integer> existingIntegers) {
        Random randomNumberGenerator = new Random();

        Integer newInt = Math.abs(randomNumberGenerator.nextInt());

        // Make sure number is unique. If not, pseudo-randomize until we get a unique int.
        while ( existingIntegers.contains(newInt) ) {
            newInt = randomNumberGenerator.nextInt();
        }

        return newInt;
    }

    /**
     * Removes duplicates from meeting list.
     *
     * @param meetings the meeting list to prune.
     * @return meeting list with no duplicates.
     */
     public static <T> List<T> removeDuplicateMeetings(List<T> meetings) {
        // Convert to set and back to conveniently get rid off duplicates.
        Set<T> meetingSet = new HashSet<T>(meetings);
        List<T> prunedMeetingList = new ArrayList<T>(meetingSet);

        return prunedMeetingList;
    }

    /**
     * Converts a Calendar as a string formatted as yyyy/MM/dd HH:mm:ss.
     *
     * @param calendar Calendar to convert.
     * @return string object with date.
     */
    public static String calendarToString(Calendar calendar) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        String tempStringDate = dateFormatter.format(calendar.getTime());

        return tempStringDate;
    }

    /**
     * Converts a string in format yyyy/MM/dd HH:mm:ss to a Calendar.
     *
     * @param string Date as yyyy/MM/dd HH:mm:ss string.
     * @return Calendar object.
     */
    public static Calendar stringToCalendar(String string) {
        Calendar calendar = null;
        try {
            // Reformat date into Calendar.
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
            Date date = dateFormatter.parse(string);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            System.out.println("Date format mangled.");
        }

        return calendar;
    }
}
