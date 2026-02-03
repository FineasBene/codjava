package model.state;

import model.exception.MyException;
import model.statement.Statement;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class StackExecutionStack implements ExecutionStack {
    private final Stack<Statement> stack;

    public StackExecutionStack() {
        this.stack = new Stack<>();
    }

    @Override
    public void push(Statement elem) {
        stack.push(elem);
    }

    @Override
    public Statement pop() throws MyException {
        if (stack.isEmpty()) {
            throw new MyException("Execution stack is empty!");
        }
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public List<Statement> getStack() {
        // Returnam o lista (copie) pentru a fi afisata in GUI
        // Collections.reverse nu e necesar aici daca facem reverse in GUI,
        // dar returnam lista in ordinea interna a stivei.
        return new ArrayList<>(stack);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Iteram invers pentru a afisa varful stivei sus in consola
        List<Statement> list = new ArrayList<>(stack);
        Collections.reverse(list);

        for (Statement s : list) {
            sb.append(s.toString()).append("\n");
        }
        return sb.toString();
    }
}
