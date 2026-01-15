package model.statement;

import model.state.ProgramState;
import model.exception.MyException;
import model.type.Type;
import java.util.Map;

public record CompoundStatement(Statement first, Statement second) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) {
        var stack = state.executionStack();
        stack.push(second);
        stack.push(first);
        return state;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnv) throws MyException {
        return second.typecheck(first.typecheck(typeEnv));
    }

    @Override
    public String toString() {
        return "(" + first.toString() + "; " + second.toString() + ")";
    }
}
