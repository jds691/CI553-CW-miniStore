/**
 * @author Mike Smith University of Brighton
 * @version 3.0
 */

package remote.access;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Manages the starting up of the database.
 * The database may be Access, mySQL etc.
 */
public class DBAccessFactory {
    private static String databaseAction = "";
    private static String databaseType = "";

    public static void setAction(String value) {
        databaseAction = value;
    }

    //REVIEW: This can probably be heavily slimmed down, or removed
    private static void setEnvironment() {
        databaseType = fileToString("DataBase.txt") + databaseAction;
        String os = System.getProperties().getProperty("os.name");
        String arch = System.getProperties().getProperty("os.arch");
        String osVer = System.getProperties().getProperty("os.version");
        String osEnvironment = String.format("%s %s %s", os, osVer, arch);
        System.out.println(osEnvironment);
    }

    /**
     * Return an object to implement system level remote.access to the database.
     * @return An object to provide system level remote.access to the database
     */
    public DBAccess getNewDBAccess() {
        setEnvironment();
        System.out.printf("Using [%s] as database type\n", databaseType);
        switch (databaseType) {
            case "Derby":
                return new DerbyAccess();

            case "DerbyCreate":
                return new DerbyCreateAccess();

            case "Access":
            case "AccessCreate":
                return new WindowsAccess();

            case "mySQL":
            case "mySQLCreate":
                return new LinuxAccess();

            default:
                System.err.printf("DataBase [%s] not known\n", databaseType);
                System.exit(-1);
        }

        throw new RuntimeException("No database driver");
    }

    /**
     * return as a string the contents of a file
     * stripping out newline and carriage returns from contents
     * @param file File name
     * @return contents of a file as a string
     */
    private static String fileToString(String file) {
        byte[] vec = fileToBytes(file);
        return new String(vec).replaceAll("\n", "").replaceAll("\r", "");
    }

    /**
     * Return contents of file as a byte vector
     * @param file File name
     * @return contents as byte array
     */
    private static byte[] fileToBytes(String file) {
        byte[] vec = new byte[0];
        try {
            final int len = (int) length(file);
            if (len < 1000) {
                vec = new byte[len];
                FileInputStream istream = new FileInputStream(file);
                final int read = istream.read(vec, 0, len);
                istream.close();
                return vec;
            } else {
                System.err.printf("File %s length %d bytes too long\n", file, len);
            }
        } catch (FileNotFoundException err) {
            System.err.printf("File does not exist: fileToBytes [%s]\n", file);
            System.exit(-1);
        } catch (IOException err) {
            System.err.printf("IO error: fileToBytes [%s]\n", file);
            System.exit(-1);
        }

        return vec;
    }

    /**
     * return number of characters in file
     * @param path File name
     * @return Number of characters in file
     */
    private static long length(String path) {
        try {
            File in = new File(path);
            return in.length();
        } catch (SecurityException err) {
            System.err.printf("Security error: length of file [%s]\n", path);
            System.exit(-1);
        }

        return -1;
    }
}

