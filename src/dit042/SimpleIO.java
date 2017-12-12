package dit042;

import java.util.Scanner;
import java.util.Locale;

/**
 *  Seventies style standard IO.
 *  @author Cezar Ionescu <cezar@chalmers.se>
 *  @author Johannes Ekberg <uppfinnarn@gmail.com>
 *  @author Musard Balliu
 *  @version 0.4
 *  @since 2015-09-01
 */
// Fixes by Johannes Ekberg: closed scanners, removed useless scanner
// Closing scanners leads to errors for multiple reads.
// Musard: add method to read a String and a line from std input
// Musard: modified readChar and setLocale for double

public class SimpleIO {
    /**
     *  "Global" variable used for error checking.
     *  Always check if the value is false after an input
     *  operation, otherwise the result can be incorrect!
     */
    public static boolean IOError = false;

    /**
     *  Generic print.
     *  @param o object to print
     */
    public static void print(Object o) {
        System.out.print(o);
        IOError = false;
    }

    /**
     *  Generic println.
     *  @param o object to print
     */
    public static void println(Object o) {
        System.out.println(o);
        IOError = false;
    }

    /**
     *  Reads next byte from standard input.
     *  Sets IOResult in case of error.
     *  @return next byte read from standard input.
     */
    public static byte readByte() {
        IOError = false;
        Scanner scan = new Scanner(System.in);
        byte result = 0;

        try {
            result = scan.nextByte();
        } catch (Exception e) {
            IOError = true;
        }
        
        return result;
    }

    /**
     *  Reads next short from standard input.
     *  Sets IOResult in case of error.
     *  @return next short read from standard input.
     */
    public static short readShort() {
        IOError = false;
        Scanner scan = new Scanner(System.in);
        short result = 0;

        try {
            result = scan.nextShort();
        } catch (Exception e) {
            IOError = true;
        }
        
        return result;
    }

    /**
     *  Reads next int from standard input.
     *  Sets IOResult in case of error.
     *  @return next int read from standard input.
     */
    public static int readInt() {
        IOError = false;
        Scanner scan = new Scanner(System.in);
        int result = 0;

        try {
            result = scan.nextInt();
        } catch (Exception e) {
            IOError = true;
        }
        
        return result;
    }

    /**
     *  Reads next long from standard input.
     *  Sets IOResult in case of error.
     *  @return next long read from standard input.
     */
    public static long readLong() {
        IOError = false;
        Scanner scan = new Scanner(System.in);
        long result = 0;

        try {
            result = scan.nextLong();
        } catch (Exception e) {
            IOError = true;
        }
        
        return result;
    }

    /**
     *  Reads next char from standard input.
     *  Should perhaps set IOResult on EOF, but currently doesn't.
     *  @return next char read from standard input.
     */
    public static char readChar() {
        IOError = false;
        char result = 0;
        Scanner scan = new Scanner(System.in);

        try {
            result = scan.nextLine().charAt(0);
        } catch (Exception e) {
            IOError = true;
        }

        return result;
    }

    /**
     *  Reads next float from standard input.
     *  Sets IOResult in case of error.
     *  @return next float read from standard input.
     */
    public static float readFloat() {
        IOError = false;
        Scanner scan = new Scanner(System.in);
        scan.useLocale(Locale.ENGLISH);
        float result = 0;

        try {
            result = scan.nextFloat();
        } catch (Exception e) {
            IOError = true;
        }
        
        return result;
    }

    /**
     *  Reads next double from standard input.
     *  Sets IOResult in case of error.
     *  @return next double read from standard input.
     */
    public static double readDouble() {
        IOError = false;
        Scanner scan = new Scanner(System.in);
        scan.useLocale(Locale.ENGLISH);
        double result = 0;

        try {

            result = scan.nextDouble();
        } catch (Exception e) {
            IOError = true;
        }
        
        return result;
    }

    /**
     *  Reads next boolean from standard input.
     *  Sets IOResult in case of error.
     *  @return next boolean read from standard input.
     */
    public static boolean readBoolean() {
        IOError = false;
        Scanner scan = new Scanner(System.in);
        boolean result = false;

        try {
            result = scan.nextBoolean();
        } catch (Exception e) {
            IOError = true;
        }
        
        return result;
    }

    /**
     *  Reads next string from standard input.
     *  Stops at the first empty space.
     *  Sets IOResult in case of error.
     *  @return next string read from standard input.
     */
    public static String readString() {
        IOError = false;
        Scanner scan = new Scanner(System.in);
        String result = "";

        try {
            result = scan.next();
        } catch (Exception e) {
            IOError = true;
        }
        
        return result;
    }
    
    /**
     *  Reads next line from standard input, including empty spaces.
     *  Sets IOResult in case of error.
     *  @return next line read from standard input.
     */
    public static String readLine() {
        IOError = false;
        Scanner scan = new Scanner(System.in);
        String result = "";

        try {
            result = scan.nextLine();
        } catch (Exception e) {
            IOError = true;
        }
        
        return result;
    }    
            
}
