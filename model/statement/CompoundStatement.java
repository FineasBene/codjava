package model.statement;

import model.state.ProgramState;

public record CompoundStatement(Statement first, Statement second) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        var stack = state.executionStack();
        stack.push(second);
        stack.push(first);
        return state;
    }
}
