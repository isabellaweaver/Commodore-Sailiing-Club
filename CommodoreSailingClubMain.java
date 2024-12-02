import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class for the Fleet Management System.
 * Handles loading, saving, and managing a list of boats.
 * Provides a menu for user interaction to add, remove, and manage boat expenses.
 * @author Isabella Weaver
 * @version 1.1
 *
 */

public class CommodoreSailingClubMain {
    private static final String DB_FILE = "FleetData.db";

    /**
     * Main method to execute the Fleet Management System.
     *
     * @param args command-line arguments, where args[0] can be a CSV file for initial data.
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<Boat> boatList;

        // Load data from CSV or serialized file (.db)
        if (args.length > 0) {
            try {
                boatList = readCSVFileToList(args[0]);
                Boat.setBoatList(boatList);
                writeSerializedFleetData(boatList);
            } catch (IOException e) {
                System.out.println("Error reading CSV file: " + e.getMessage());
                return;
            }
        } else {
            try {
                boatList = readFleetData(DB_FILE);
                Boat.setBoatList(boatList);
            } catch (IOException | ClassNotFoundException e) {
                boatList = new ArrayList<>();
                Boat.setBoatList(boatList);
            }
        }

        System.out.println("Welcome to the Fleet Management System");
        System.out.println("--------------------------------------");
        System.out.println();

        // Main menu loop
        char choice;
        do {
            System.out.print("(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");
            choice = input.nextLine().trim().toUpperCase().charAt(0);

            switch (choice) {
                case 'P':
                    System.out.println("\nFleet report:");
                    for (Boat boat : boatList) {
                        System.out.println("    " + boat);
                    }
                    System.out.printf("    Total                                                 : Paid $%10.2f : Spent $%10.2f%n",
                            Boat.getPaidTotal(), Boat.getSpentTotal());
                    System.out.println();
                    break;

                case 'A':
                    System.out.print("Please enter the new boat CSV data          : ");
                    String[] data = input.nextLine().split(",");
                    System.out.println();
                    try {
                        BoatType boatType = BoatType.valueOf(data[0].toUpperCase());
                        Boat newBoat = new Boat(boatType, data[1],
                                Integer.parseInt(data[2]), data[3],
                                Double.parseDouble(data[4]), Double.parseDouble(data[5]), 0);
                        boatList.add(newBoat);
                    } catch (Exception e) {
                        System.out.println("Invalid data format.");
                    }
                    break;

                case 'R':
                    System.out.print("Which boat do you want to remove?           : ");
                    String removeName = input.nextLine();
                    Boat toRemove = boatList.stream()
                            .filter(boat -> boat.getName().equalsIgnoreCase(removeName))
                            .findFirst()
                            .orElse(null);
                    if (toRemove != null) {
                        boatList.remove(toRemove);
                        System.out.println();
                    } else {
                        System.out.println("Cannot find boat " + removeName);
                        System.out.println();
                    }
                    break;

                case 'E':
                    System.out.print("Which boat do you want to spend on?         : ");
                    String spendName = input.nextLine();
                    Boat toSpend = boatList.stream()
                            .filter(boat -> boat.getName().equalsIgnoreCase(spendName))
                            .findFirst()
                            .orElse(null);
                    if (toSpend != null) {
                        System.out.print("How much do you want to spend?              : ");
                        double amount = Double.parseDouble(input.nextLine());
                        if (toSpend.checkExpense(amount)) {
                            toSpend.setExpense(toSpend.getExpense() + amount);
                            System.out.printf("Expense authorized, $%.2f spent.%n", toSpend.getExpense());
                            System.out.println();
                        }
                    } else {
                        System.out.println("Cannot find boat " + spendName);
                        System.out.println();
                    }
                    break;

                case 'X':
                    writeSerializedFleetData(boatList);
                    System.out.println("\nExiting the Fleet Management System");
                    break;

                default:
                    System.out.println("Invalid menu option, try again");
            }
        } while (choice != 'X');


    }

    /**
     * Reads boat data from a CSV file and creates a list of boats.
     *
     * @param fileName the name of the CSV file.
     * @return a list of boats.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    private static ArrayList<Boat> readCSVFileToList(String fileName) throws IOException {
        ArrayList<Boat> boatList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                BoatType boatType = BoatType.valueOf(data[0].toUpperCase());
                Boat boat = new Boat(boatType, data[1], Integer.parseInt(data[2]),
                        data[3], Double.parseDouble(data[4]), Double.parseDouble(data[5]), 0);
                boatList.add(boat);
            }
        }
        return boatList;
    }

    /**
     * Reads serialized fleet data from a file.
     *
     * @param fileName the name of the file containing serialized data.
     * @return a list of boats.
     * @throws IOException if an I/O error occurs.
     * @throws ClassNotFoundException if the class for the object is not found.
     */
    private static ArrayList<Boat> readFleetData(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (ArrayList<Boat>) ois.readObject();
        }
    }

    /**
     * Saves the fleet data to a serialized file.
     *
     * @param boatList the list of boats to serialize.
     */
    private static void writeSerializedFleetData(ArrayList<Boat> boatList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DB_FILE))) {
            oos.writeObject(boatList);
        } catch (IOException e) {
            System.out.println("Error saving fleet data: " + e.getMessage());
        }
    }
}
