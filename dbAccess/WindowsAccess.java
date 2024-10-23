package dbAccess;

/**
 * Implements management of a Microsoft Access database.
 *
 * @author Mike Smith University of Brighton
 * @version 2.0
 */
class WindowsAccess extends DBAccess {
    public void loadDriver() throws Exception {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
    }

    @Override
    public String getUrlOfDatabase() {
        return "jdbc:odbc:cshop";
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
