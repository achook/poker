package pl.edu.agh.kis.pz1;


/**
 * Shared utilities.
 */
public class Utilities {

    /**
     * Gets the argument from a string.
     * Splits a string by spaces and returns the second element.
     * @param s string to split
     * @return argument
     */
    public static int getArgument(String s) {
        return Integer.parseInt(s.split(" ")[1]);
    }

    public void checkIfStringContains(String s, String substring) throws IllegalArgumentException {
        if (!s.contains(substring)) {
            throw new IllegalArgumentException("String does not contain substring");
        }
    }
}