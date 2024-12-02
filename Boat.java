import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a boat with its details, such as type, name, year, make, size,
 * purchase price, and expenses. Provides methods to manage and analyze a fleet of boats.
 */
public class Boat implements Serializable {
    private BoatType boatType;
    private String name;
    private int year;
    private String make;
    private double feet;
    private double purchasePrice;
    private double expense;

    private static ArrayList<Boat> boatList = new ArrayList<>();

    /**
     * Constructor for creating a new boat instance.
     *
     * @param boatType      the type of the boat (e.g., POWER, SAILING)
     * @param name          the name of the boat
     * @param year          the year the boat was manufactured
     * @param make          the make of the boat
     * @param feet          the size of the boat in feet
     * @param purchasePrice the purchase price of the boat
     * @param expense       the current expense associated with the boat
     */
    public Boat(BoatType boatType, String name, int year, String make, double feet, double purchasePrice, double expense) {
        this.boatType = boatType;
        this.name = name;
        this.year = year;
        this.make = make;
        this.feet = feet;
        this.purchasePrice = purchasePrice;
        this.expense = expense;
    }


    /**
     * Gets the name of the boat.
     *
     * @return the name of the boat
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the purchase price of the boat.
     *
     * @return the purchase price of the boat
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Gets the current expenses associated with the boat.
     *
     * @return the total expenses
     */
    public double getExpense() {
        return expense;
    }

    /**
     * Sets the expense for the boat.
     *
     * @param expense the new expense value
     */
    public void setExpense(double expense) {
        this.expense = expense;
    }

    /**
     * Sets the list of boats in the fleet.
     *
     * @param list the list of boats to set
     */
    public static void setBoatList(ArrayList<Boat> list) {
        boatList = list;
    }

    /**
     * Calculates the total purchase price of all boats in the fleet.
     *
     * @return the total purchase price of the fleet
     */
    public static double getPaidTotal() {
        return boatList.stream().mapToDouble(Boat::getPurchasePrice).sum();
    }

    /**
     * Calculates the total expenses for all boats in the fleet.
     *
     * @return the total expenses of the fleet
     */
    public static double getSpentTotal() {
        return boatList.stream().mapToDouble(Boat::getExpense).sum();
    }

    /**
     * Checks if an expense can be added without exceeding the boat's budget.
     *
     * @param amount the expense amount to be checked
     * @return true if the expense is within budget, false otherwise
     */
    public boolean checkExpense(double amount) {
        double remainingBudget = purchasePrice - expense;
        if (amount > remainingBudget) {
            System.out.printf("Expense not permitted, only $%.2f left to spend.%n", remainingBudget);
            System.out.println();
            return false;
        }
        return true;
    }

    /**
     * Provides a formatted string representation of the boat.
     *
     * @return a string with boat details in a readable format
     */
    @Override
    public String toString() {
        return String.format("%-8s %-20s %4d %-12s %4d' : Paid $%10.2f : Spent $%10.2f",
                boatType, name, year, make, (int) feet, purchasePrice, expense);
    }

}

    /**
    * Enum representing the type of boat.
    */
    enum BoatType {
    POWER, SAILING
    }
