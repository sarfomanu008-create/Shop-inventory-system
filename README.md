# Arrays in Real-World Applications: Shop Inventory System

## Assignment Statement

> Describe real-world applications that require arrays as a data structure, explain why arrays are necessary, and choose one application to develop.

---

## 1. Real-World Applications That Require Arrays

An array is a fixed-size, ordered collection of items of the same type, stored side by side in memory and accessed by an index. Many everyday systems depend on this structure.

| Application | How arrays are used |
|---|---|
| **Inventory and point of sale** | Product names, prices and stock levels held in parallel arrays |
| **Student records** | Scores or names of many students stored in order, e.g. `scores[0]` to `scores[n-1]` |
| **Banking** | A customer's list of transactions or monthly balances |
| **Image processing** | A photo is a 2D array of pixels (rows x columns), each holding a colour value |
| **Seat booking** | Cinema, bus, train or flight seats modelled as a grid of rows and columns |
| **Sensor and weather data** | Hourly temperature readings stored in sequence for analysis |
| **Games** | Chess, draughts, tic-tac-toe and Sudoku boards are 2D arrays |
| **Spreadsheets and tables** | Rows and columns of data are 2D arrays |

---

## 2. Why Arrays Are Necessary

1. **Fast direct access.** Any element is reached immediately by its index (`stock[5]`) without searching through the others.
2. **Grouping related data.** One array replaces dozens of separate variables such as `stock1`, `stock2`, `stock3`, and so on.
3. **Easy processing with loops.** A single `for` loop can read, update or total thousands of items in a few lines of code.
4. **Efficient memory use.** Elements are stored next to each other in memory, which makes reading and processing fast.
5. **Natural fit for tables and grids.** Parallel and two-dimensional arrays represent records, rows and columns directly.
6. **Foundation for other structures.** Lists, stacks, queues and matrices are built on arrays.

---

## 3. Chosen Application: Shop Inventory System

### Problem
A small shop needs a simple system to list its products, record sales, restock items, search for a product, warn when stock is low, and report the total value of goods in stock.

### Why arrays suit this problem
- Each product has the same kinds of data (name, price, quantity), which fits **parallel arrays**.
- One loop can print the whole inventory, search it, find low stock and total its value.
- Any product is reached instantly by its position, e.g. `stock[2]` is the quantity of item 3.
- The shop has a known maximum number of products, which fits the fixed size of an array.

### Data design
| Array | Type | Purpose |
|---|---|---|
| `names[CAPACITY]` | `String` | Product name |
| `prices[CAPACITY]` | `double` | Price of one unit in GHS |
| `stock[CAPACITY]` | `int` | Units currently in stock |

All three arrays share the same index, so `names[2]`, `prices[2]` and `stock[2]` describe the same product. A counter `count` tracks how many positions are in use.

### Features
1. View all items in a table
2. Add a new item (rejects empty names, duplicates and negative values)
3. Sell an item (rejects overselling and warns when stock is low)
4. Restock an item
5. Search for an item by part of its name
6. Low-stock report (5 units or fewer)
7. Total stock value
8. Exit

### How the program works
1. The program starts with four sample products, then shows a menu in a `do-while` loop until the user chooses Exit.
2. `addItem()` stores a product at position `count` in all three arrays, then increases `count`.
3. `sellItem()` checks the quantity against `stock[i]`, reduces the stock and prints the sale total (`qty * prices[i]`).
4. `viewItems()`, `search()`, `lowStock()` and `totalValue()` use `for` loops to go through the arrays.
5. `readIndex()` converts the item number typed by the user into an array index and rejects invalid input.

---

## 4. The Code (`InventorySystem.java`)

```java
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
```

### How to run
Requires Java (JDK 8 or later).

```
javac InventorySystem.java
java InventorySystem
```

---

## 5. Sample Output

Viewing all items:

```
No.  Item                      Price    Stock
------------------------------------------------
1    Rice (5kg)                85.00       20
2    Cooking Oil (1L)          32.50       12
3    Sugar (1kg)               18.00        4
4    Milk Tin                   6.50       30
```

Selling 2 units of Sugar:

```
Enter item number to sell: 3
Quantity to sell: 2
Sold 2 x Sugar (1kg). Total: GHS 36.00
Warning: Sugar (1kg) is running low (2 left).
```

Trying to sell more than is in stock:

```
Enter item number to sell: 3
Quantity to sell: 99
Not enough stock. Only 2 left.
```

Total stock value after restocking Sugar by 10:

```
Different items : 4
Units in stock  : 74
Total value     : GHS 2501.00
```

---

## 6. Conclusion

Arrays are essential wherever data comes in an ordered, fixed-size collection. In the inventory system, parallel arrays keep each product's name, price and quantity linked by one index, give instant access to any product, and let simple loops list, search and total the whole stock. The same idea powers student record systems, image processing, game boards and many other real-world applications.
