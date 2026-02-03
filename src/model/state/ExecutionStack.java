package model.state;

import model.exception.MyException;
import model.statement.Statement;
import java.util.List;

public interface ExecutionStack {
    void push(Statement elem);
    Statement pop() throws MyException;
    boolean isEmpty();

    // Metoda necesara pentru GUI
    List<Statement> getStack();

    String toString();
}
