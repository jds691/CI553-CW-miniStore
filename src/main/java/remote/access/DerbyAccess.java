package remote.access;

/**
 * Apache Derby database remote.access
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
class DerbyAccess extends DBAccess {
    private static final String DB_URL = "jdbc:derby:catshop.db";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    /**
     * Load the Apache Derby database driver
     */
    public void loadDriver() throws Exception {
        Class.forName(DRIVER).newInstance();
    }

    /**
     * Return the url to remote.access the database
     *
     * @return url to database
     */
    @Override
    public String getUrlOfDatabase() {
        return DB_URL;
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public String getPassword() {
        return "";
    }
}

