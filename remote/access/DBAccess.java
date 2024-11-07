package remote.access;

/**
 * Implements generic management of a database.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
public abstract class DBAccess {
    public abstract void loadDriver() throws Exception;

    public abstract String getUrlOfDatabase();

    public abstract String getUsername();

    //TODO: Storing the password in code is considered insecure
    public abstract String getPassword();
}
