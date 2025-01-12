package clients;

import remote.access.DBAccess;
import remote.access.DBAccessFactory;

import java.sql.*;
import java.util.ArrayList;

/**
 * Repopulate the database with test data
 *
 * @author Mike Smith University of Brighton
 * @version 3.0 Derby
 */

class Setup {
    /*
    SQL Code to setup database tables
    */
    /*
    TODO: These statements function fine under Apache Derby, they will not in MySQL because SQL dialects differ a lot
    Potentially use a token system to define the differences but oml a pain and a half that would be
     */
    private static final String[] sqlStatements = {
            "create table ProductTable (" +
                    "productNo      Char(4)," +
                    "name    Varchar(40)," +
                    // Based on Figma testing
                    "description    Varchar(120)," +
                    "picture        Varchar(80)," +
                    "price          Float,"+
                    "CONSTRAINT Product_PK PRIMARY KEY (productNo)"+
            ")",

            "insert into ProductTable values " +
                    "('0001', '40 inch LED HD TV', 'Discover over 500,000 movies and shows from the likes of Netflix, Prime Video, and Disney+ with the best TV around!', 'images/pic0001.jpg', 269.00)",
            "insert into ProductTable values " +
                    "('0002', 'DAB Radio', 'Enjoy your favourite radio stations on the go, at home, and in style with the Play 11 from loved radio experts Roberts.', 'images/pic0002.jpg', 29.99)",
            "insert into ProductTable values " +
                    "('0003', 'Toaster', '5 different settings for all of your toasting needs.','images/pic0003.jpg', 19.99)",
            "insert into ProductTable values " +
                    "('0004', 'Watch', 'A fashionable timepiece that is always for you.', 'images/pic0004.jpg', 29.99)",
            "insert into ProductTable values " +
                    "('0005', 'Digital Camera', 'Take crystal clear images images with the latest and greatest 24MP camera.', 'images/pic0005.jpg', 89.99)",
            "insert into ProductTable values " +
                    "('0006', 'MP3 player', 'Listen to your whole music collection in stunning 128kp/s quality.', 'images/pic0006.jpg', 7.99)",
            "insert into ProductTable values " +
                    "('0007', '32Gb USB2 drive', 'High read-write speeds for all your work needs wth reliable USB 3.1.', 'images/pic0007.jpg', 6.99)",

            "create table StockTable (" +
                    "productNo      Char(4)," +
                    "stockLevel     Integer,"+
                    "CONSTRAINT Stock_Product_FK FOREIGN KEY (productNo) REFERENCES ProductTable (productNo)"+
            ")",

            "insert into StockTable values ( '0001',  90 )",
            "insert into StockTable values ( '0002',  20 )",
            "insert into StockTable values ( '0003',  33 )",
            "insert into StockTable values ( '0004',  10 )",
            "insert into StockTable values ( '0005',  17 )",
            "insert into StockTable values ( '0006',  15 )",
            "insert into StockTable values ( '0007',  01 )",

            "select * from StockTable, ProductTable " +
                    " where StockTable.productNo = ProductTable.productNo",

            // Order persistence
            "create table OrderTable (" +
                "orderId INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                "state INTEGER,"+
                "CONSTRAINT Order_PK PRIMARY KEY (orderId)"+
            ")",

            "create table OrderProductTable (" +
                    "orderId INTEGER," +
                    "productNo Char(4)," +
                    "quantity INTEGER NOT NULL,"+
                    "CONSTRAINT OrderProduct_Order_FK FOREIGN KEY (orderId) REFERENCES OrderTable (orderId),"+
                    "CONSTRAINT OrderProduct_Product_FK FOREIGN KEY (productNo) REFERENCES ProductTable (productNo)," +
                    "CONSTRAINT OrderProduct_PK PRIMARY KEY (orderId, productNo)"+
            ")"
    };

    public static void main(String[] args) {
        Connection theCon = null;
        DBAccess dbDriver = null;
        DBAccessFactory.setAction("Create");
        System.out.println("Setup CatShop database of stock items");
        try {
            dbDriver = (new DBAccessFactory()).getNewDBAccess();
            dbDriver.loadDriver();
            theCon = DriverManager.getConnection
                    (dbDriver.getUrlOfDatabase(),
                            dbDriver.getUsername(),
                            dbDriver.getPassword());
        } catch (SQLException e) {
            System.err.println("Problem with connection to " +
                    dbDriver.getUrlOfDatabase());
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState:     " + e.getSQLState());
            System.out.println("VendorError:  " + e.getErrorCode());
            System.exit(-1);
        } catch (Exception e) {
            System.err.println("Can not load JDBC/ODBC driver.");
            System.exit(-1);
        }

        Statement stmt = null;
        try {
            stmt = theCon.createStatement();
        } catch (Exception e) {
            System.err.println("problems creating statement object");
        }

        // execute SQL commands to create table, insert data
        for (String sqlStatement : sqlStatements) {
            try {
                System.out.println(sqlStatement);
                switch (sqlStatement.charAt(0)) {
                    case '/':
                        System.out.println("------------------------------");
                        break;
                    case 's':
                    case 'f':
                        query(stmt, dbDriver.getUrlOfDatabase(), sqlStatement);
                        break;
                    case '*':
                        if (sqlStatement.length() >= 2)
                            switch (sqlStatement.charAt(1)) {
                                case 'c':
                                    theCon.commit();
                                    break;
                                case 'r':
                                    theCon.rollback();
                                    break;
                                case '+':
                                    theCon.setAutoCommit(true);
                                    break;
                                case '-':
                                    theCon.setAutoCommit(false);
                                    break;
                            }
                        break;
                    default:
                        stmt.execute(sqlStatement);
                }
            } catch (Exception e) {
                System.out.println("problems with SQL sent to " +
                        dbDriver.getUrlOfDatabase() +
                        "\n" + sqlStatement + "\n" + e.getMessage());
            }
        }

        try {
            theCon.close();
        } catch (Exception e) {
            System.err.println("problems with close " +
                    ": " + e.getMessage());
        }

    }


    private static void query(Statement stmt, String url, String stm) {
        try {
            ResultSet res = stmt.executeQuery(stm);

            ArrayList<String> names = new ArrayList<>(10);

            ResultSetMetaData md = res.getMetaData();
            int cols = md.getColumnCount();

            for (int j = 1; j <= cols; j++) {
                String name = md.getColumnName(j);
                System.out.printf("%-14.14s ", name);
                names.add(name);
            }
            System.out.println();

            for (int j = 1; j <= cols; j++) {
                System.out.printf("%-14.14s ", md.getColumnTypeName(j));
            }
            System.out.println();

            while (res.next()) {
                for (int j = 0; j < cols; j++) {
                    String name = names.get(j);
                    System.out.printf("%-14.14s ", res.getString(name));
                }
                System.out.println();
            }


        } catch (Exception e) {
            System.err.println("problems with SQL sent to " + url +
                    "\n" + e.getMessage());
        }
    }

    private static String m(int len, String s) {
        if (s.length() >= len) {
            return s.substring(0, len - 1) + " ";
        } else {
            StringBuilder res = new StringBuilder(len);
            res.append(s);
            for (int i = s.length(); i < len; i++)
                res.append(' ');
            return res.toString();
        }
    }
}
