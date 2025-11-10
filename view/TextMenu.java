
package view;

import java.util.*;

public class TextMenu {
    private final Map<String, Command> commands = new HashMap<>();
    public void addCommand(Command c) { commands.put(c.getKey(), c); }
    private void printMenu() {
        System.out.println("=== MENU ===");
        for (Command c : commands.values())
            System.out.printf("%s : %s%n", c.getKey(), c.getDescription());
    }
    public void show() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.print("Alege: ");
            String key = sc.nextLine();
            Command c = commands.get(key);
            if (c == null) System.out.println("Invalid");
            else c.execute();
        }
    }
}
