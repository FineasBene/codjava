package model.statement;

import model.exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.Type;
import model.value.BooleanValue;
import model.value.Value;
import java.util.Map;
import java.util.HashMap;

public record IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var heap = state.getHeap();
        Value result = condition.evaluate(state.symbolTable(), heap);

        if (!result.getType().equals(new BoolType())) {
             throw new MyException("Condition expression does not evaluate to a boolean.");
        }

        BooleanValue booleanValue = (BooleanValue) result;
        if (booleanValue.value()) {
            state.executionStack().push(thenBranch);
        } else {
            state.executionStack().push(elseBranch);
        }
        return null;
    }

    @Override
    public Map<String, Type> typecheck(Map<String, Type> typeEnv) throws MyException {
        Type typexp = condition.typecheck(typeEnv);
        if (typexp.equals(new BoolType())) {

            thenBranch.typecheck(new HashMap<>(typeEnv));
            elseBranch.typecheck(new HashMap<>(typeEnv));
            return typeEnv;
        } else {
            throw new MyException("The condition of IF has not the type bool");
        }
    }

    @Override
    public String toString() {
        return "(IF " + condition.toString() + " THEN " + thenBranch.toString() + " ELSE " + elseBranch.toString() + ")";
    }
}
