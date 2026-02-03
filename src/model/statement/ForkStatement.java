package model.statement;

import model.exception.MyException;
import model.state.ProgramState;
import model.state.StackExecutionStack;
import model.type.Type;
import java.util.Map;
import java.util.HashMap;

public record ForkStatement(Statement statement) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var newStack = new StackExecutionStack();
        return new ProgramState(
                newStack,
                state.symbolTable().copy(),
                state.out(),
                state.getFileTable(),
                state.getHeap(),
                statement
        );
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnv) throws MyException {
        statement.typecheck(new HashMap<>(typeEnv));
        return typeEnv;
    }

    @Override
    public String toString() {
        return "fork(" + statement.toString() + ")";
    }
}
