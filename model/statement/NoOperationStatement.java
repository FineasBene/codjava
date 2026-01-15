package model.statement;

import model.state.ProgramState;
import model.type.Type;
import java.util.Map;

public class NoOperationStatement implements Statement {
    @Override
    public ProgramState execute(ProgramState state) {
        return null;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnv) {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "nop";
    }
}
