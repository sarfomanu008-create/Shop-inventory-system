import java.util.Scanner;

/**
 * Shop Inventory System
 * Uses parallel arrays: names[], prices[] and stock[] share the same index,
 * so names[2], prices[2] and stock[2] all describe the same product.
 */
public class InventorySystem {

    static final int CAPACITY = 20;
    static final int LOW_STOCK = 5;

    static String[] names = new String[CAPACITY];
    static double[] prices = new double[CAPACITY];
    static int[] stock = new int[CAPACITY];
    static int count = 0;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Starting stock
        addItem("Rice (5kg)", 85.00, 20);
        addItem("Cooking Oil (1L)", 32.50, 12);
        addItem("Sugar (1kg)", 18.00, 4);
        addItem("Milk Tin", 6.50, 30);

        int choice;
        do {
            System.out.println("\n===== SHOP INVENTORY SYSTEM =====");
            System.out.println("1. View all items");
            System.out.println("2. Add a new item");
            System.out.println("3. Sell an item");
            System.out.println("4. Restock an item");
            System.out.println("5. Search for an item");
            System.out.println("6. Low-stock report");
            System.out.println("7. Total stock value");
            System.out.println("8. Exit");
            System.out.print("Choose: ");

            try {
                choice = Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                choice = 0;
            }

            switch (choice) {
                case 1: viewItems(); break;
                case 2: addNew(in); break;
                case 3: sellItem(in); break;
                case 4: restock(in); break;
                case 5: search(in); break;
                case 6: lowStock(); break;
                case 7: totalValue(); break;
                case 8: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice. Enter 1-8.");
            }
        } while (choice != 8);
    }

    static void addItem(String name, double price, int qty) {
        names[count] = name;
        prices[count] = price;
        stock[count] = qty;
        count++;
    }

    static void viewItems() {
        if (count == 0) {
            System.out.println("No items in inventory.");
            return;
        }
        System.out.printf("%n%-4s %-20s %10s %8s%n", "No.", "Item", "Price", "Stock");
        System.out.println("------------------------------------------------");
        for (int i = 0; i < count; i++) {
            System.out.printf("%-4d %-20s %10.2f %8d%n", i + 1, names[i], prices[i], stock[i]);
        }
    }

    static void addNew(Scanner in) {
        if (count == CAPACITY) {
            System.out.println("Inventory is full (" + CAPACITY + " items).");
            return;
        }
        System.out.print("Item name: ");
        String name = in.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        if (indexOf(name) != -1) {
            System.out.println("That item already exists. Use Restock instead.");
            return;
        }
        try {
            System.out.print("Price (GHS): ");
            double price = Double.parseDouble(in.nextLine().trim());
            System.out.print("Quantity: ");
            int qty = Integer.parseInt(in.nextLine().trim());
            if (price < 0 || qty < 0) {
                System.out.println("Price and quantity cannot be negative.");
                return;
            }
            addItem(name, price, qty);
            System.out.println(name + " added.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Item not added.");
        }
    }

    static void sellItem(Scanner in) {
        viewItems();
        System.out.print("Enter item number to sell: ");
        int i = readIndex(in.nextLine());
        if (i == -1) {
            System.out.println("Invalid item number.");
            return;
        }
        System.out.print("Quantity to sell: ");
        int qty;
        try {
            qty = Integer.parseInt(in.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }
        if (qty <= 0) {
            System.out.println("Quantity must be at least 1.");
        } else if (qty > stock[i]) {
            System.out.println("Not enough stock. Only " + stock[i] + " left.");
        } else {
            stock[i] -= qty;
            System.out.printf("Sold %d x %s. Total: GHS %.2f%n", qty, names[i], qty * prices[i]);
            if (stock[i] <= LOW_STOCK) {
                System.out.println("Warning: " + names[i] + " is running low (" + stock[i] + " left).");
            }
        }
    }

    static void restock(Scanner in) {
        viewItems();
        System.out.print("Enter item number to restock: ");
        int i = readIndex(in.nextLine());
        if (i == -1) {
            System.out.println("Invalid item number.");
            return;
        }
        System.out.print("Quantity to add: ");
        try {
            int qty = Integer.parseInt(in.nextLine().trim());
            if (qty <= 0) {
                System.out.println("Quantity must be at least 1.");
                return;
            }
            stock[i] += qty;
            System.out.println(names[i] + " now has " + stock[i] + " in stock.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
        }
    }

    static void search(Scanner in) {
        System.out.print("Search for: ");
        String term = in.nextLine().trim().toLowerCase();
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (names[i].toLowerCase().contains(term)) {
                System.out.printf("%s | GHS %.2f | %d in stock%n", names[i], prices[i], stock[i]);
                found = true;
            }
        }
        if (!found) System.out.println("No matching items.");
    }

    static void lowStock() {
        boolean any = false;
        System.out.println("\nItems with " + LOW_STOCK + " or fewer in stock:");
        for (int i = 0; i < count; i++) {
            if (stock[i] <= LOW_STOCK) {
                System.out.println("- " + names[i] + " (" + stock[i] + " left)");
                any = true;
            }
        }
        if (!any) System.out.println("None. All items are well stocked.");
    }

    static void totalValue() {
        double total = 0;
        int units = 0;
        for (int i = 0; i < count; i++) {
            total += prices[i] * stock[i];
            units += stock[i];
        }
        System.out.println("\nDifferent items : " + count);
        System.out.println("Units in stock  : " + units);
        System.out.printf("Total value     : GHS %.2f%n", total);
    }

    // Returns the position of an item by name (ignoring case), or -1
    static int indexOf(String name) {
        for (int i = 0; i < count; i++) {
            if (names[i].equalsIgnoreCase(name)) return i;
        }
        return -1;
    }

    // Converts the typed item number (1-based) into an array index, or -1
    static int readIndex(String input) {
        try {
            int n = Integer.parseInt(input.trim());
            if (n >= 1 && n <= count) return n - 1;
        } catch (NumberFormatException e) {
            // fall through
        }
        return -1;
    }
}
