package app;

import controller.Controller;
import model.expression.*;
import model.state.ListOut;
import model.state.MapSymbolTable;
import model.state.ProgramState;
import model.state.StackExecutionStack;
import model.statement.*;
import model.type.Type;
import model.value.BooleanValue;
import model.value.IntegerValue;
import repository.IRepository;
import repository.Repository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== MENU =====");
            System.out.println("Choose a program to run:");
            System.out.println("a) int v; v = 2; Print(v)");
            System.out.println("b) int a; a = 2 + 3*5; int b; b = a - 4/2 + 7; Print(b)");
            System.out.println("c) bool a; a = false; int v; If a Then v = 2 Else v = 3; Print(v)");
            System.out.println("x) Exit");
            System.out.print("Enter your choice (a/b/c) or x to exit: ");
            String choice = scanner.nextLine().toLowerCase();

            if (choice.equals("x")) {
                System.out.println("Exiting the program.");
                break;
            }

            System.out.print("Do you want detailed logging? (true/false): ");
            boolean debug = Boolean.parseBoolean(scanner.nextLine());

            ProgramState prg = null;
            IRepository repo = new Repository();

            switch (choice) {
                case "a" -> {
                    var stk = new StackExecutionStack();
                    var sym = new MapSymbolTable();
                    var out = new ListOut();
                    var ex = new CompoundStatement(
                            new VariableDeclarationStatement(Type.INTEGER, "v"),
                            new CompoundStatement(
                                    new AssignmentStatement(new ConstantExpression(new IntegerValue(2)), "v"),
                                    new PrintStatement(new VariableExpression("v"))
                            )
                    );
                    prg = new ProgramState(stk, sym, out, ex);
                }
                case "b" -> {
                    var stk = new StackExecutionStack();
                    var sym = new MapSymbolTable();
                    var out = new ListOut();
                    var ex = new CompoundStatement(
                            new VariableDeclarationStatement(Type.INTEGER, "a"),
                            new CompoundStatement(
                                    new AssignmentStatement(
                                            new BinaryOperatorExpression(
                                                    "+",
                                                    new ConstantExpression(new IntegerValue(2)),
                                                    new BinaryOperatorExpression(
                                                            "*",
                                                            new ConstantExpression(new IntegerValue(3)),
                                                            new ConstantExpression(new IntegerValue(5))
                                                    )
                                            ),
                                            "a"
                                    ),
                                    new CompoundStatement(
                                            new VariableDeclarationStatement(Type.INTEGER, "b"),
                                            new CompoundStatement(
                                                    new AssignmentStatement(
                                                            new BinaryOperatorExpression(
                                                                    "+",
                                                                    new BinaryOperatorExpression(
                                                                            "-",
                                                                            new VariableExpression("a"),
                                                                            new BinaryOperatorExpression(
                                                                                    "/",
                                                                                    new ConstantExpression(new IntegerValue(4)),
                                                                                    new ConstantExpression(new IntegerValue(2))
                                                                            )
                                                                    ),
                                                                    new ConstantExpression(new IntegerValue(7))
                                                            ),
                                                            "b"
                                                    ),
                                                    new PrintStatement(new VariableExpression("b"))
                                            )
                                    )
                            )
                    );
                    prg = new ProgramState(stk, sym, out, ex);
                }
                case "c" -> {
                    var stk = new StackExecutionStack();
                    var sym = new MapSymbolTable();
                    var out = new ListOut();
                    var ex = new CompoundStatement(
                            new VariableDeclarationStatement(Type.BOOLEAN, "a"),
                            new CompoundStatement(
                                    new AssignmentStatement(new ConstantExpression(new BooleanValue(false)), "a"),
                                    new CompoundStatement(
                                            new VariableDeclarationStatement(Type.INTEGER, "v"),
                                            new CompoundStatement(
                                                    new IfStatement(
                                                            new VariableExpression("a"),
                                                            new AssignmentStatement(new ConstantExpression(new IntegerValue(2)), "v"),
                                                            new AssignmentStatement(new ConstantExpression(new IntegerValue(3)), "v")
                                                    ),
                                                    new PrintStatement(new VariableExpression("v"))
                                            )
                                    )
                            )
                    );
                    prg = new ProgramState(stk, sym, out, ex);
                }
                default -> {
                    System.out.println("Invalid choice!");
                    continue;
                }
            }

            repo.addProgram(prg);
            Controller ctrl = new Controller(repo, debug);

            System.out.println("\nRunning example " + choice + "...");
            ctrl.allStep();
            System.out.println("Output: " + prg.out().toString());
        }
    }
}
