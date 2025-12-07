package view;

import java.util.*;

public class TextMenu {
    private final Map<String, Command> commands = new HashMap<>();

    public TextMenu() { }

    public void addCommand(Command c) { commands.put(c.getKey(), c); }

    private void printMenu() {
        System.out.println(" === MENU ===");
        for (Command c : commands.values()) {
            String line = String.format("%4s : %s", c.getKey(), c.getDescription());
            System.out.println(line);
        }
    }

    public void show() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.print("Choice an option: ");
            String key = sc.nextLine();
            Command c = commands.get(key);
            if (c == null) {
                System.out.println("Invalid option");
                continue;
            }
            c.execute();
        }
    }
}
