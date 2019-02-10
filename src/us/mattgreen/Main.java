package us.mattgreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//import java.util.Collections;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;

public class Main {

    private Scanner keyboard;
    private Cookbook cookbook;
    //private Object Collections;

    public Main() {
        keyboard = new Scanner(System.in);
        cookbook = new Cookbook();

        FileInput indata = new FileInput("meals_data.csv");

        String line;

        System.out.println("Reading in meals information from file...");
        while ((line = indata.fileReadLine()) != null) {
            String[] fields = line.split(",");
            cookbook.addElementWithStrings(fields[0], fields[1], fields[2]);
        }

        runMenu();
    }

    public static void main(String[] args) {
        new Main();
    }

    private void printMenu() {
        System.out.println("");
        System.out.println("Select Action");
        System.out.println("1. List All Items");
        System.out.println("2. List All Items by Meal");
        System.out.println("3. Search by Meal Name");
        System.out.println("4. Do Control Break");
        System.out.println("5. Exit");
        System.out.print("Please Enter your Choice: ");
    }

    private void runMenu() {
        boolean userContinue = true;

        while (userContinue) {
            printMenu();

            String ans = keyboard.nextLine();
            switch (ans) {
                case "1":
                    cookbook.printAllMeals();
                    break;
                case "2":
                    listByMealType();
                    break;
                case "3":
                    searchByName();
                    break;
                case "4":
                    doControlBreak();
                    break;
                case "5":
                    userContinue = false;
                    break;
            }
        }

        System.out.println("Goodbye");
        System.exit(0);
    }

    private void listByMealType() {
        // Default value pre-selected in case
        // something goes wrong w/user choice
        MealType mealType = MealType.DINNER;

        System.out.println("Which Meal Type");

        // Generate the menu using the ordinal value of the enum
        for (MealType m : MealType.values()) {
            System.out.println((m.ordinal() + 1) + ". " + m.getMeal());
        }

        System.out.print("Please Enter your Choice: ");
        String ans = keyboard.nextLine();

        try {
            int ansNum = Integer.parseInt(ans);
            if (ansNum < MealType.values().length) {
                mealType = MealType.values()[ansNum - 1];
            }
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid Meal Type " + ans + ", defaulted to " + mealType.getMeal() + ".");
        }

        cookbook.printMealsByType(mealType);
    }

    private void searchByName() {
        keyboard.nextLine();
        System.out.print("Please Enter Value: ");
        String ans = keyboard.nextLine();
        cookbook.printByNameSearch(ans);
    }

    private void doControlBreak() {

        DecimalFormat df2 = new DecimalFormat(".0000");
        List<Meal> mealArrayList = new ArrayList<Meal>();
        MealType mealType;
        FileInput indata = new FileInput("meals_data.csv");
        String line;
        int i = 0;
        int mealCount = 0;
        int count = 0;
        int nbr = 0;
        double sum = 0;
        Integer max = Integer.MIN_VALUE;
        double mean = 0;
        double median = 0;
        double pos1 = 0;
        double pos2 = 0;

        // Print the heading line
        System.out.printf("%-15s %10s %12s %8s %10s %10s %n", "MealType", "Total", "Mean", "Min", "Max", "Median");

        String currentMealType = "dummy";

        // Read the file and load to mealArrayList
        while ((line = indata.fileReadLine()) != null) {
            String[] fields = line.split(",");
            //System.out.println("Meal" + fields[0]);
            switch (fields[0]) {
                case "Breakfast":
                    mealType = MealType.BREAKFAST;
                    break;
                case "Lunch":
                    mealType = MealType.LUNCH;
                    break;
                case "Dinner":
                    mealType = MealType.DINNER;
                    break;
                case "Dessert":
                    mealType = MealType.DESSERT;
                    break;
                default:
                    mealType = MealType.DINNER;

            }

            // This adds the Meal object to the mealArrayList
            mealArrayList.add(new Meal(mealType, fields[1], Integer.parseInt(fields[2])));
            nbr = Integer.parseInt(fields[2]);
        }

        // This instantiates the arraylist which grabs calories for each mealtype
        ArrayList<Integer> mealGroup = new ArrayList<>();

        for (Meal str : mealArrayList) {

            // If currentMealType value is same as next record, add the calories to arraylist
            if (currentMealType.toUpperCase().equals(str.getMealType().toString())) {

                mealGroup.add(str.getCalories());

            } else {
                // If the currentMealType value is different than next record, do the calcs, display output and reset the arraylist

                // Find max calorie value, count and total in arraylist
                for (Integer number : mealGroup) {
                    sum = sum + number;
                    count++;
                    if (number > max) {
                        max = number;
                    }
                }

                // Find min calorie value in arraylist
                Integer min = max;
                for (Integer number : mealGroup) {
                    if (number < min) {
                        min = number;
                    }
                }

                mean = sum / count;
                Collections.sort(mealGroup);
                // Calculate median (middle number)
                median = 0;
                pos1 = Math.floor((mealGroup.size() - 1.0) / 2.0);
                pos2 = Math.ceil((mealGroup.size() - 1.0) / 2.0);
                if (mealGroup.size() != 0) {
                    if (pos1 == pos2) {
                        median = mealGroup.get((int) pos1);
                    } else {
                        median = (mealGroup.get((int) pos1) + mealGroup.get((int) pos2)) / 2.0;
                    }
                }

                // Print the output but skip initial dummy value
                if (!currentMealType.equals("dummy")) {

                    System.out.printf("%-15s %10s %13s %7s %10s %10s %n", currentMealType, (int) sum, df2.format(mean), min, max, median);

                }

                //Clearing array and variables / Updating Meal type
                mealGroup.clear();
                currentMealType = str.getMealType().toString();
                sum = 0;
                count = 0;
                mealGroup.add(str.getCalories());
            }
        }

        // Process the last mealtype
        Collections.sort(mealGroup);
        //ArrayList.sort(mealGroup;
        count = 0;
        sum = 0;
        max = 0;

        // Find max calorie value, count and total in arraylist
        for (Integer number : mealGroup) {
            sum = sum + number;
            count++;
            if (number > max) {
                max = number;
            }
        }

        // Find min calorie value in arraylist
        Integer min = max;
        for (Integer number : mealGroup) {
            if (number < min) {
                min = number;
            }
        }

        mean = sum / count;

        // Calculate median (middle number)
        median = 0;
        pos1 = Math.floor((mealGroup.size() - 1.0) / 2.0);
        pos2 = Math.ceil((mealGroup.size() - 1.0) / 2.0);
        if (mealGroup.size() != 0) {
            if (pos1 == pos2) {
                median = mealGroup.get((int) pos1);
            } else {
                median = (mealGroup.get((int) pos1) + mealGroup.get((int) pos2)) / 2.0;
            }
        }

        System.out.printf("%-15s %10s %13s %7s %10s %10s %n", currentMealType, (int) sum, df2.format(mean), min, max, median);

    }
}
