package model.state;

import model.statement.Statement;

import java.util.LinkedList;
import java.util.List;

public class StackExecutionStack implements ExecutionStack {
    private final LinkedList<Statement> stack = new LinkedList<>();

    @Override
    public void push(Statement statement) {
        stack.addFirst(statement);
    }

    @Override
    public Statement pop() {
        return stack.removeFirst();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public Statement peek() {
        return stack.peekFirst();
    }
}
