package app;

import controller.Controller;
import model.expression.*;
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

public class Main {
    public static void main(String[] args) {
        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "Exit"));

        try {
            // Exemplul 1: int v; v=2; Print(v)
            Statement ex1 = new CompoundStatement(
                    new VariableDeclarationStatement(new IntType(), "v"),
                    new CompoundStatement(
                            new AssignmentStatement(new ConstantExpression(new IntegerValue(2)), "v"),
                            new PrintStatement(new VariableExpression("v"))
                    )
            );
            menu.addCommand(createCommand("1", "Basic: " + ex1.toString(), ex1, "log1.txt"));

            // Exemplul 2: Aritmetic
            // int a; a=2+3*5; int b; b=a-4/2+7; Print(b)
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
            // bool a; a=false; int v; If a Then v=2 Else v=3; Print(v)
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

            // Exemplul 4: Fisiere (File Operations)
            // string varf; varf="test.in"; openRFile(varf); int varc; readFile(varf,varc); print(varc); ...
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

            // --- EXEMPLE NOI (LAB 7 - HEAP & WHILE) ---

            // Exemplu 5: Heap Allocation
            // Ref int v; new(v,20); Ref Ref int a; new(a,v); print(v); print(a)
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
            // Ref int v; new(v,20); Ref Ref int a; new(a,v); print(rH(v)); print(rH(rH(a))+5)
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
            // Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5);
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
            // Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); print(rH(rH(a)))
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
            // int v; v=4; (while (v>0) print(v);v=v-1); print(v)
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

        } catch (Exception e) {
            System.err.println("Eroare la construirea comenzilor: " + e.getMessage());
            e.printStackTrace();
        }
        menu.show();
    }

    private static Command createCommand(String key, String desc, Statement stmt, String log) throws IOException {
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