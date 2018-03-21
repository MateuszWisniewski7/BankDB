package com.company.customersdata;

import java.sql.*;
import java.util.Date;

public class Datasource {

    static final String DB_NAME = "customers";
    static final String SERVER = "localhost:3306";
    static final String CONNECTION_STRING = "jdbc:mysql://" + SERVER + "/?useSSL=false&serverTimezone=UTC";
    static final String USER = "student";
    static final String PASSWORD = "student";

    static final String TABLE_BRANCHES = "branches";
    static final String COLUMN_BRANCH_ID = "_id";
    static final String COLUMN_BRANCH_NAME = "name";

    static final String TABLE_CUSTOMERS = "customers";
    static final String COLUMN_CUSTOMER_ID = "_id";
    static final String COLUMN_CUSTOMER_NAME = "name";
    static final String COLUMN_CUSTOMER_BRANCH_ID = "branch";


    static final String TABLE_TRANSACTIONS = "transactions";
    static final String COLUMN_TRANSACTION_DATE = "_date";
    static final String COLUMN_TRANSACTION_VALUE = "_value";
    static final String COLUMN_TRANSACTION_CUSTOMER_ID = "customer";

    private Connection conn;

    // opening connection with server, creating database if's not created and tables

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);

            String createDB = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            String createBranches = "CREATE TABLE IF NOT EXISTS " + TABLE_BRANCHES + " ("
                    + COLUMN_BRANCH_ID + " int NOT NULL AUTO_INCREMENT, "
                    + COLUMN_BRANCH_NAME + " text, "
                    + "PRIMARY KEY(" + COLUMN_BRANCH_ID + ")"
                    + ")";
            String createCustomers = "CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOMERS + " ("
                    + COLUMN_CUSTOMER_ID + " int NOT NULL AUTO_INCREMENT, "
                    + COLUMN_CUSTOMER_NAME + " text, "
                    + COLUMN_CUSTOMER_BRANCH_ID + " int NOT NULL, "
                    + "PRIMARY KEY(" + COLUMN_CUSTOMER_ID + ")"
                    + ")";
            String createTransactions = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSACTIONS + " ("
                    + COLUMN_TRANSACTION_DATE + " TIMESTAMP, "
                    + COLUMN_TRANSACTION_VALUE + " decimal(10,2), "
                    + COLUMN_TRANSACTION_CUSTOMER_ID + " int NOT NULL, "
                    + "PRIMARY KEY(" + COLUMN_TRANSACTION_DATE + ")"
                    + ")";
            conn.setCatalog(DB_NAME);
            Statement statement = conn.createStatement();
            statement.executeUpdate(createDB);
            statement.executeUpdate(createBranches);
            statement.executeUpdate(createCustomers);
            statement.executeUpdate(createTransactions);
            statement.close();


            System.out.println("Connection successfully opened.");
            return true;
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public void addBranch(String name) {
            if (selectBranchId(name)==0){
        //if (!checkBranch(name)) {
            String insertBranch = "INSERT INTO " + TABLE_BRANCHES + " (" + COLUMN_BRANCH_NAME + ") " + "VALUES ('" + name + "')";
            try {
                Statement statement = conn.createStatement();
                statement.executeUpdate(insertBranch);
                statement.close();
                System.out.println("Branch " + name + " is" + " successfully added.");
            } catch (SQLException e) {
                System.out.println("ERROR in addBranch method: " + e.getMessage());
            }
        } else
            System.out.println("Branch already exists in database.");
    }

//    public boolean checkBranch(String name) {
//        String selectBranch = "SELECT " + COLUMN_BRANCH_NAME + " FROM " + TABLE_BRANCHES + " WHERE " + COLUMN_BRANCH_NAME + "='" + name + "'";
//        boolean checker = true;
//        try {
//            Statement statement = conn.createStatement();
//            ResultSet resultSet = statement.executeQuery(selectBranch);
//            if (!resultSet.next())
//                checker = false;
//            else
//                checker = true;
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e) {
//            System.out.println("ERROR in checkBranch method: " + e.getMessage());
//        }
//        return checker;
//    }

    public int selectBranchId(String name) {
        String selectBranchId = "SELECT " + COLUMN_BRANCH_ID + " FROM " + TABLE_BRANCHES + " WHERE " + COLUMN_BRANCH_NAME + "='" + name + "'";
        int id = 0;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectBranchId);
            while (resultSet.next()) {
                id = resultSet.getInt(COLUMN_BRANCH_ID);
            }
            resultSet.close();
            statement.close();
            return id;
        } catch (SQLException e) {
            System.out.println("ERROR in selectBranchId method: " + e.getMessage());
            return 0;
        }
    }

    public void selectBranches(){
        String selectBranches = "SELECT * FROM " + TABLE_BRANCHES;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectBranches);
            System.out.println("ID\tBRANCH_NAME");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(COLUMN_BRANCH_ID)+"\t"+
                                    resultSet.getString(COLUMN_BRANCH_NAME));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("ERROR in selectBranches method: " + e.getMessage());
        }
    }

    public void addCustomer(int branchId, String name, double initial) {
                    String insertCustomer = "INSERT INTO " + TABLE_CUSTOMERS + " ("
                            + COLUMN_CUSTOMER_NAME + " , "
                            + COLUMN_CUSTOMER_BRANCH_ID + ") "
                            + "VALUES ('" + name + "' , " + branchId + ")";
                    try {
                        Statement statement = conn.createStatement();
                        statement.executeUpdate(insertCustomer);
                        statement.close();
                        System.out.println("Customer " + name + " is" + " successfully added.");
                    } catch (SQLException e) {
                        System.out.println("ERROR in addCustomer method: " + e.getMessage());
                    }
                    addTransaction(1,initial);

    }

//    private boolean checkCustomer(String name, int branchId) {
//        String selectCustomer = "SELECT " + COLUMN_CUSTOMER_NAME + " FROM " + TABLE_CUSTOMERS + " WHERE " + COLUMN_CUSTOMER_NAME + "='" + name + "'"
//                + " and " + COLUMN_CUSTOMER_BRANCH_ID + "=" + branchId;
//        boolean checker = true;
//        try {
//            Statement statement = conn.createStatement();
//            ResultSet resultSet = statement.executeQuery(selectCustomer);
//            if (!resultSet.next())
//                checker = false;
//            else
//                checker = true;
//            resultSet.close();
//            statement.close();
//        } catch (SQLException e) {
//            System.out.println("ERROR in checkCustomer method: " + e.getMessage());
//        }
//        return checker;
//    }

    public int selectCustomerId(String name, int branchId) {
        String selectCustomerId = "SELECT " + COLUMN_CUSTOMER_ID + " FROM "
                + TABLE_CUSTOMERS
                + " WHERE "
                + COLUMN_CUSTOMER_NAME + "='" + name + "'"
                + " and " + COLUMN_CUSTOMER_BRANCH_ID + "=" + branchId;
        int id = 0;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectCustomerId);
            while (resultSet.next()) {
                id = resultSet.getInt(COLUMN_CUSTOMER_ID);
            }
            resultSet.close();
            statement.close();
            return id;
        } catch (SQLException e) {
            System.out.println("ERROR in selectCustomer method: " + e.getMessage());
            return 0;
        }
    }

    public void selectCustomers(int branchId){
        String selectCustomers = "SELECT * FROM " + TABLE_CUSTOMERS + " WHERE " + COLUMN_CUSTOMER_BRANCH_ID + "=" + branchId;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectCustomers);
            System.out.println("ID\tCUSTOMER_NAME\tBRANCH_ID");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(COLUMN_CUSTOMER_ID)+"\t"+
                        resultSet.getString(COLUMN_CUSTOMER_NAME)+"\t"+
                        resultSet.getInt(COLUMN_CUSTOMER_BRANCH_ID));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("ERROR in selectCustomers method: " + e.getMessage());
        }
    }

    public void addTransaction(int customerId, double value) {
        java.util.Date now = new Date();
        java.sql.Timestamp date = new java.sql.Timestamp(now.getTime());
        String insertTransaction = "INSERT INTO " + TABLE_TRANSACTIONS + " ("
                + COLUMN_TRANSACTION_DATE + " , "
                + COLUMN_TRANSACTION_VALUE + " , "
                + COLUMN_TRANSACTION_CUSTOMER_ID + ") "
                + "VALUES ('"
                + date + "' , '"
                + value + "' , "
                + customerId + ")";
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(insertTransaction);
            statement.close();
            System.out.println("Transaction successfully added.");
        } catch (SQLException e) {
            System.out.println("ERROR in addCustomer method: " + e.getMessage());
        }
    }

    public void selectTransactions(int customerId){
        String selectTransactions = "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TRANSACTION_CUSTOMER_ID + "=" + customerId;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectTransactions);
            System.out.println("DATE\tVALUE\tCUSTOMER_ID");
            while (resultSet.next()) {
                System.out.println(resultSet.getTimestamp(COLUMN_TRANSACTION_DATE)+"\t"+
                        resultSet.getString(COLUMN_TRANSACTION_VALUE)+"\t"+
                        resultSet.getInt(COLUMN_TRANSACTION_CUSTOMER_ID));
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("ERROR in selectCustomers method: " + e.getMessage());
        }
    }

}













