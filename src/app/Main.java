package app;

import controller.Controller;
import model.expression.*;
import model.exception.MyException;
import model.state.*;
import model.statement.*;
import model.type.*;
import model.value.*;
import repository.IRepository;
import repository.Repository;
import view.Command;
import view.ExitCommand;
import view.RunExample;
import view.TextMenu;

import java.io.IOException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "Exit"));

        try {
            // --- EXEMPLE INITIALE (FARA HEAP) ---

            // Exemplul 1: int v; v=2; Print(v)
            Statement ex1 = new CompoundStatement(
                    new VariableDeclarationStatement(new IntType(), "v"),
                    new CompoundStatement(
                            new AssignmentStatement(new ConstantExpression(new IntegerValue(1)), "v"),
                            new PrintStatement(new VariableExpression("v"))
                    )
            );
            menu.addCommand(createCommand("1", "Basic: " + ex1.toString(), ex1, "log1.txt"));

            // Exemplul 2: Aritmetic
            Statement ex2 = new CompoundStatement(
                    new VariableDeclarationStatement(new IntType(), "a"),
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
                                    ), "a"
                            ),
                            new CompoundStatement(
                                    new VariableDeclarationStatement(new IntType(), "b"),
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
                                                    ), "b"
                                            ),
                                            new PrintStatement(new VariableExpression("b"))
                                    )
                            )
                    )
            );
            menu.addCommand(createCommand("2", "Arithmetic: " + ex2.toString(), ex2, "log2.txt"));

            // Exemplul 3: Conditional (IF)
            Statement ex3 = new CompoundStatement(
                    new VariableDeclarationStatement(new BoolType(), "a"),
                    new CompoundStatement(
                            new AssignmentStatement(new ConstantExpression(new BooleanValue(false)), "a"),
                            new CompoundStatement(
                                    new VariableDeclarationStatement(new IntType(), "v"),
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
            menu.addCommand(createCommand("3", "If: " + ex3.toString(), ex3, "log3.txt"));

            // Exemplul 4: Fisiere
            Statement ex4 = new CompoundStatement(
                    new VariableDeclarationStatement(new StringType(), "varf"),
                    new CompoundStatement(
                            new AssignmentStatement(new ConstantExpression(new StringValue("test.in")), "varf"),
                            new CompoundStatement(
                                    new OpenRFileStatement(new VariableExpression("varf")),
                                    new CompoundStatement(
                                            new VariableDeclarationStatement(new IntType(), "varc"),
                                            new CompoundStatement(
                                                    new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                    new CompoundStatement(
                                                            new PrintStatement(new VariableExpression("varc")),
                                                            new CompoundStatement(
                                                                    new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                    new CompoundStatement(
                                                                            new PrintStatement(new VariableExpression("varc")),
                                                                            new CloseRFileStatement(new VariableExpression("varf"))
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            );
            menu.addCommand(createCommand("4", "Files: " + ex4.toString(), ex4, "log4.txt"));

            // --- EXEMPLE HEAP & WHILE (LAB 7) ---

            // Exemplu 5: Heap Allocation
            Statement ex5 = new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(
                    new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                    new CompoundStatement(
                        new VariableDeclarationStatement(new RefType(new RefType(new IntType())), "a"),
                        new CompoundStatement(
                            new NewStatement("a", new VariableExpression("v")),
                            new CompoundStatement(
                                new PrintStatement(new VariableExpression("v")),
                                new PrintStatement(new VariableExpression("a"))
                            )
                        )
                    )
                )
            );
            menu.addCommand(createCommand("5", "Heap Alloc: " + ex5.toString(), ex5, "log5.txt"));

            // Exemplu 6: Read Heap
            Statement ex6 = new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(
                    new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                    new CompoundStatement(
                        new VariableDeclarationStatement(new RefType(new RefType(new IntType())), "a"),
                        new CompoundStatement(
                            new NewStatement("a", new VariableExpression("v")),
                            new CompoundStatement(
                                new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new PrintStatement(new BinaryOperatorExpression("+",
                                    new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))),
                                    new ConstantExpression(new IntegerValue(5))
                                ))
                            )
                        )
                    )
                )
            );
            menu.addCommand(createCommand("6", "Read Heap: " + ex6.toString(), ex6, "log6.txt"));

            // Exemplu 7: Write Heap
            Statement ex7 = new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(
                    new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                    new CompoundStatement(
                        new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                        new CompoundStatement(
                            new WriteHeapStatement("v", new ConstantExpression(new IntegerValue(30))),
                            new PrintStatement(new BinaryOperatorExpression(
                                "+",
                                new ReadHeapExpression(new VariableExpression("v")),
                                new ConstantExpression(new IntegerValue(5))
                            ))
                        )
                    )
                )
            );
            menu.addCommand(createCommand("7", "Write Heap: " + ex7.toString(), ex7, "log7.txt"));

            // Exemplu 8: Garbage Collector
            Statement ex8 = new CompoundStatement(
                new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(
                    new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                    new CompoundStatement(
                        new VariableDeclarationStatement(new RefType(new RefType(new IntType())), "a"),
                        new CompoundStatement(
                            new NewStatement("a", new VariableExpression("v")),
                            new CompoundStatement(
                                new NewStatement("v", new ConstantExpression(new IntegerValue(30))),
                                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))))
                            )
                        )
                    )
                )
            );
            menu.addCommand(createCommand("8", "Garbage Collector: " + ex8.toString(), ex8, "log8.txt"));

            // Exemplu 9: While Statement
            Statement ex9 = new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(
                    new AssignmentStatement(new ConstantExpression(new IntegerValue(4)), "v"),
                    new CompoundStatement(
                        new WhileStatement(
                            new BinaryOperatorExpression(">", new VariableExpression("v"), new ConstantExpression(new IntegerValue(0))),
                            new CompoundStatement(
                                new PrintStatement(new VariableExpression("v")),
                                new AssignmentStatement(
                                    new BinaryOperatorExpression("-", new VariableExpression("v"), new ConstantExpression(new IntegerValue(1))),
                                    "v"
                                )
                            )
                        ),
                        new PrintStatement(new VariableExpression("v"))
                    )
                )
            );
            menu.addCommand(createCommand("9", "While Statement: " + ex9.toString(), ex9, "log9.txt"));

            // --- EXEMPLE CONCURENTA (LAB 8) ---

            // Exemplul 10: Fork
            Statement ex10 = new CompoundStatement(
                new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(
                    new VariableDeclarationStatement(new RefType(new IntType()), "a"),
                    new CompoundStatement(
                        new AssignmentStatement(new ConstantExpression(new IntegerValue(10)), "v"),
                        new CompoundStatement(
                            new NewStatement("a", new ConstantExpression(new IntegerValue(22))),
                            new CompoundStatement(
                                new ForkStatement(
                                    new CompoundStatement(
                                        new WriteHeapStatement("a", new ConstantExpression(new IntegerValue(30))),
                                        new CompoundStatement(
                                            new AssignmentStatement(new ConstantExpression(new IntegerValue(32)), "v"),
                                            new CompoundStatement(
                                                new PrintStatement(new VariableExpression("v")),
                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                            )
                                        )
                                    )
                                ),
                                new CompoundStatement(
                                    new PrintStatement(new VariableExpression("v")),
                                    new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))
                                )
                            )
                        )
                    )
                )
            );
            menu.addCommand(createCommand("10", "Fork Example: " + ex10.toString(), ex10, "log10.txt"));

        } catch (Exception e) {
            System.err.println("Eroare la construirea comenzilor: " + e.getMessage());
            e.printStackTrace();
        }
        menu.show();
    }

    private static Command createCommand(String key, String desc, Statement stmt, String log) throws IOException {
        // --- TYPE CHECKING STEP (Lab 10) ---
        try {
            stmt.typecheck(new HashMap<>());
        } catch (MyException e) {
            System.out.println("COMMAND " + key + " TYPE CHECK ERROR: " + e.getMessage());
            // Putem returna o comanda care doar afiseaza eroarea, sau sa nu adaugam comanda deloc.
            // Pentru simplitate, vom afisa eroarea la initializare dar permitem crearea (care va crapa la runtime oricum daca e logic gresit, 
            // dar typechecker-ul ar trebui sa previna asta).
            // Corect ar fi sa nu permitem rularea.
            return new ExitCommand(key, "TYPE CHECK FAILED: " + desc);
        }
        // -----------------------------------

        var exeStack = new StackExecutionStack();
        var symTable = new MapSymbolTable();
        var out = new ListOut();
        var fileTable = new MapFileTable();
        var heap = new MyHeap();

        ProgramState prg = new ProgramState(exeStack, symTable, out, fileTable, heap, stmt);
        IRepository repo = new Repository(prg, log);
        Controller ctrl = new Controller(repo, true);
        return new RunExample(key, desc, ctrl);
    }
}
