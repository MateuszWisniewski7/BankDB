package com.company;

import com.company.customersdata.Datasource;

import java.util.Scanner;

public class Main {

    private static Scanner s = new Scanner(System.in);
    private static Datasource ds = new Datasource();

    public static void main(String[] args) {

        if (ds.open()) {
            boolean loop = false;
            showMenu();
            while (!loop) {
                System.out.println("Choose 0-7 option (0-print menu).");
                try {
                    int action = s.nextInt();
                    s.nextLine();
                    switch (action) {
                        case 0:
                            showMenu();
                            break;
                        case 1:
                            addBranch();
                            break;
                        case 2:
                            addCustomer();
                            break;
                        case 3:
                            addTransaction();
                            break;
                        case 4:
                            showBranches();
                            break;
                        case 5:
                            showCustomers();
                            break;
                        case 6:
                            showTransactions();
                            break;
                        case 7:
                            loop = true;
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Syntax error.");
                    s.nextLine();
                }
            }
        }
        ds.close();
    }
    // Printing in console available options

    public static void showMenu() {
        System.out.println("\nPress ");
        System.out.println("\t 0 - To print menu.");
        System.out.println("\t 1 - To add a branch.");
        System.out.println("\t 2 - To add a customer.");
        System.out.println("\t 3 - To add a transaction.");
        System.out.println("\t 4 - To show list of branches.");
        System.out.println("\t 5 - To show customers' list of chosen branch.");
        System.out.println("\t 6 - To show list of chosen customer transactions.");
        System.out.println("\t 7 - To quit the application.");
    }

    // Adding new branch to database

    public static void addBranch() {
        System.out.println("Enter branch name:");
        String name = s.nextLine();
        ds.addBranch(name);
    }
    // Adding new customer to concrete branch

    public static void addCustomer() {
        System.out.println("Enter branch name:");
        String branchName = s.nextLine();
        int branchId = ds.selectBranchId(branchName);
        if (branchId > 0) {
            System.out.println("Enter customer name:");
            String name = s.nextLine();
            if (ds.selectCustomerId(name, branchId) == 0) {
                System.out.println("Enter initial transaction value:");
                double initial = s.nextDouble();
                ds.addCustomer(branchId, name, initial);
            } else
                System.out.println("Customer already exists in database.");
        } else
            System.out.println("Branch doesn't exists");
    }
    // Adding customer's transaction

    public static void addTransaction() {
        System.out.println("Enter branch name:");
        String branchName = s.nextLine();
        int branchId = ds.selectBranchId(branchName);
        if (branchId > 0) {
            System.out.println("Enter customer name:");
            String name = s.nextLine();
            int customerId = ds.selectCustomerId(name, branchId);
            if (customerId > 0) {
                System.out.println("Enter transaction value:");
                double initial = s.nextDouble();
                ds.addTransaction(customerId, initial);
            } else
                System.out.println("Customer doesn't exists");
        } else
            System.out.println("Branch doesn't exists");
    }

    // Printing list of branches
    public static void showBranches() {
        ds.selectBranches();
    }

    // Printing customers list for given branch

    public static void showCustomers() {
        System.out.println("Enter branch name:");
        String branchName = s.nextLine();
        int branchId = ds.selectBranchId(branchName);
        if (branchId > 0)
            ds.selectCustomers(branchId);
        else
            System.out.println("Branch doesn't exists");
    }

    // Printing concrete customer transactions

    public static void showTransactions() {
        System.out.println("Enter branch name:");
        String branchName = s.nextLine();
        int branchId = ds.selectBranchId(branchName);
        if (branchId > 0) {
            System.out.println("Enter customer name:");
            String name = s.nextLine();
            int customerId = ds.selectCustomerId(name, branchId);
            if (customerId > 0)
                ds.selectTransactions(customerId);
            else
                System.out.println("Customer doesn't exists");
        } else
            System.out.println("Branch doesn't exists");
    }
}