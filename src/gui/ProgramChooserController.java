package gui;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.exception.MyException;
import model.expression.*;
import model.state.*;
import model.statement.*;
import model.type.*;
import model.value.*;
import repository.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramChooserController {
    @FXML
    private ListView<Statement> programsListView;

    @FXML
    public void initialize() {
        programsListView.setItems(FXCollections.observableArrayList(getAllExamples()));
    }

    @FXML
    public void displayProgram(ActionEvent actionEvent) {
        Statement selectedStatement = programsListView.getSelectionModel().getSelectedItem();
        if (selectedStatement == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No program selected!");
            alert.showAndWait();
            return;
        }

        int id = programsListView.getSelectionModel().getSelectedIndex() + 1;

        try {
            // Type Checking
            selectedStatement.typecheck(new java.util.HashMap<>());

            // Build Controller
            ProgramState prg = new ProgramState(new StackExecutionStack(), new MapSymbolTable(), new ListOut(), new MapFileTable(), new MyHeap(), selectedStatement);
            Repository repo = new Repository(prg, "log" + id + ".txt");
            Controller controller = new Controller(repo, true);

            // Open Executor Window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProgramExecutor.fxml"));
            Parent root = loader.load();

            ProgramExecutorController executorController = loader.getController();
            executorController.setController(controller);

            Stage stage = new Stage();
            stage.setTitle("Program Executor");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

        } catch (IOException | MyException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private List<Statement> getAllExamples() {
        List<Statement> examples = new ArrayList<>();

        // Exemplul 1: int v; v=2; Print(v)
        Statement ex1 = new CompoundStatement(new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(new AssignmentStatement(new ConstantExpression(new IntegerValue(2)), "v"),
                        new PrintStatement(new VariableExpression("v"))));
        examples.add(ex1);

        // Exemplul 2: Aritmetic
        // int a; a=2+3*5; int b; b=a-4/2+7; Print(b)
        Statement ex2 = new CompoundStatement(new VariableDeclarationStatement(new IntType(), "a"),
                new CompoundStatement(new AssignmentStatement(new BinaryOperatorExpression("+", new ConstantExpression(new IntegerValue(2)),
                        new BinaryOperatorExpression("*", new ConstantExpression(new IntegerValue(3)), new ConstantExpression(new IntegerValue(5)))), "a"),
                        new CompoundStatement(new VariableDeclarationStatement(new IntType(), "b"),
                                new CompoundStatement(new AssignmentStatement(new BinaryOperatorExpression("+", new BinaryOperatorExpression("-", new VariableExpression("a"),
                                        new BinaryOperatorExpression("/", new ConstantExpression(new IntegerValue(4)), new ConstantExpression(new IntegerValue(2)))), new ConstantExpression(new IntegerValue(7))), "b"),
                                        new PrintStatement(new VariableExpression("b"))))));
        examples.add(ex2);

        // Exemplul 3: Conditional (IF)
        // bool a; a=false; int v; If a Then v=2 Else v=3; Print(v)
        Statement ex3 = new CompoundStatement(new VariableDeclarationStatement(new BoolType(), "a"),
                new CompoundStatement(new AssignmentStatement(new ConstantExpression(new BooleanValue(false)), "a"),
                        new CompoundStatement(new VariableDeclarationStatement(new IntType(), "v"),
                                new CompoundStatement(new IfStatement(new VariableExpression("a"),
                                        new AssignmentStatement(new ConstantExpression(new IntegerValue(2)), "v"),
                                        new AssignmentStatement(new ConstantExpression(new IntegerValue(3)), "v")),
                                        new PrintStatement(new VariableExpression("v"))))));
        examples.add(ex3);

        // Exemplul 4: Fisiere
        // string varf; varf="test.in"; openRFile(varf); int varc; readFile(varf,varc); print(varc); ...
        Statement ex4 = new CompoundStatement(new VariableDeclarationStatement(new StringType(), "varf"),
                new CompoundStatement(new AssignmentStatement(new ConstantExpression(new StringValue("test.in")), "varf"),
                        new CompoundStatement(new OpenRFileStatement(new VariableExpression("varf")),
                                new CompoundStatement(new VariableDeclarationStatement(new IntType(), "varc"),
                                        new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                        new CloseRFileStatement(new VariableExpression("varf"))))))))));
        examples.add(ex4);

        // Exemplul 5: Heap Allocation
        // Ref int v; new(v,20); Ref Ref int a; new(a,v); print(v); print(a)
        Statement ex5 = new CompoundStatement(new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement(new RefType(new RefType(new IntType())), "a"),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                new PrintStatement(new VariableExpression("a")))))));
        examples.add(ex5);

        // Exemplul 6: Read Heap
        // Ref int v; new(v,20); Ref Ref int a; new(a,v); print(rH(v)); print(rH(rH(a))+5)
        Statement ex6 = new CompoundStatement(new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement(new RefType(new RefType(new IntType())), "a"),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                                new PrintStatement(new BinaryOperatorExpression("+",
                                                        new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))),
                                                        new ConstantExpression(new IntegerValue(5)))))))));
        examples.add(ex6);

        // Exemplul 7: Write Heap
        // Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5);
        Statement ex7 = new CompoundStatement(new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                new CompoundStatement(new WriteHeapStatement("v", new ConstantExpression(new IntegerValue(30))),
                                        new PrintStatement(new BinaryOperatorExpression("+",
                                                new ReadHeapExpression(new VariableExpression("v")),
                                                new ConstantExpression(new IntegerValue(5))))))));
        examples.add(ex7);

        // Exemplul 8: Garbage Collector
        // Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); print(rH(rH(a)))
        Statement ex8 = new CompoundStatement(new VariableDeclarationStatement(new RefType(new IntType()), "v"),
                new CompoundStatement(new NewStatement("v", new ConstantExpression(new IntegerValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement(new RefType(new RefType(new IntType())), "a"),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new NewStatement("v", new ConstantExpression(new IntegerValue(30))),
                                                new PrintStatement(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a")))))))));
        examples.add(ex8);

        // Exemplul 9: While Statement
        // int v; v=4; (while (v>0) print(v);v=v-1); print(v)
        Statement ex9 = new CompoundStatement(new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(new AssignmentStatement(new ConstantExpression(new IntegerValue(4)), "v"),
                        new CompoundStatement(new WhileStatement(new BinaryOperatorExpression(">", new VariableExpression("v"), new ConstantExpression(new IntegerValue(0))),
                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                        new AssignmentStatement(new BinaryOperatorExpression("-", new VariableExpression("v"), new ConstantExpression(new IntegerValue(1))), "v"))),
                                new PrintStatement(new VariableExpression("v")))));
        examples.add(ex9);

        // Exemplul 10: Fork Statement
        // int v; Ref int a; v=10; new(a,22); fork(wH(a,30); v=32; print(v); print(rH(a))); print(v); print(rH(a))
        Statement ex10 = new CompoundStatement(new VariableDeclarationStatement(new IntType(), "v"),
                new CompoundStatement(new VariableDeclarationStatement(new RefType(new IntType()), "a"),
                        new CompoundStatement(new AssignmentStatement(new ConstantExpression(new IntegerValue(10)), "v"),
                                new CompoundStatement(new NewStatement("a", new ConstantExpression(new IntegerValue(22))),
                                        new CompoundStatement(new ForkStatement(new CompoundStatement(new WriteHeapStatement("a", new ConstantExpression(new IntegerValue(30))),
                                                new CompoundStatement(new AssignmentStatement(new ConstantExpression(new IntegerValue(32)), "v"),
                                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                                new PrintStatement(new ReadHeapExpression(new VariableExpression("a"))))))),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new ReadHeapExpression(new VariableExpression("a")))))))));
        examples.add(ex10);

        return examples;
    }
}
