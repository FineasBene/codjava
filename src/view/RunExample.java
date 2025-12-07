package view;

import controller.Controller;
import model.exception.MyException;

public class RunExample extends Command {
    private final Controller controller;

    public RunExample(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            controller.allStep();
        } catch (MyException e) { //
            System.err.println(" --- RUNTIME ERROR ---");
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println(" --- UNEXPECTED RUNTIME ERROR ---");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
