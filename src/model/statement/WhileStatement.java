package model.statement;

import model.exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.BoolType;
import model.value.BooleanValue;
import model.value.Value;

public record WhileStatement(Expression expression, Statement statement) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var stack = state.executionStack();
        var symTable = state.symbolTable();
        var heap = state.getHeap();

        Value result = expression.evaluate(symTable, heap);

        if (!result.getType().equals(new BoolType())) {
            throw new MyException("Condition expression in While is not boolean.");
        }

        BooleanValue boolRes = (BooleanValue) result;
        if (boolRes.value()) {
            stack.push(this);
            stack.push(statement);
        }

        return null;
    }

    @Override
    public String toString() {
        return "(while (" + expression.toString() + ") " + statement.toString() + ")";
    }
}
