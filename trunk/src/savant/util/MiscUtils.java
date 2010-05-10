/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package savant.util;

import savant.view.swing.Savant;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 *
 * @author mfiume
 */
public class MiscUtils {

    /** [[ Miscellaneous Functions ]] */
    /**
     * Format an integer to a string (adding commas)
     * @param num The number to format
     * @return A formatted string
     */
    public static String intToString(int num) {
        //TODO: implement formatter
        DecimalFormat df = new DecimalFormat("###,###");
        return df.format(num);
    }

    /**
     * Get an integer from a string
     * @param str The string respresenting an integer
     * (possibly with commas)
     * @return An integer
     */
    public static int stringToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            Savant.log(e.getLocalizedMessage());
            return -1;
        }
    }

    /**
     * Get a string representation of the the current time
     * @return A string representing the current time
     */
    public static String now() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime().toGMTString();
    }


    /**
     * Remove the specified character from the given string.
     * @param s The string from which to remove the character
     * @param c The character to remove from the string
     * @return The string with the character removed
     */
    public static String removeChar(String s, char c) {
        String r = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) {
                r += s.charAt(i);
            }
        }
        return r;
    }

    public static String getFilenameFromPath(String path) {
        int lastSlashIndex = path.lastIndexOf(System.getProperty("file.separator"));
        return path.substring(lastSlashIndex+1, path.length());
    }

    public static String getTemporaryDirectory() {
        String os = System.getProperty("os.name");

        String tmpDir;
        if (os.toLowerCase().contains("mac") || os.toLowerCase().contains("linux")) {
            tmpDir = System.getenv("TMPDIR");
            if (tmpDir != null) {
                return tmpDir;
            }
            else {
                return "/tmp/savant";
            }
        }
        else {
            if ((tmpDir = System.getenv("TEMP")) != null) {
                return tmpDir;
            }
            else if ((tmpDir = System.getenv("TMP")) != null) {
                return tmpDir;
            }
            else {
                return System.getProperty("user.dir");
            }
        }
    }
}
