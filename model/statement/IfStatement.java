package model.statement;

import model.exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.value.BooleanValue;
import model.value.Value;

public record IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value result = condition.evaluate(state.symbolTable());
        if (result instanceof BooleanValue booleanValue) {
            if (booleanValue.value()) {
                state.executionStack().push(thenBranch);
            } else {
                state.executionStack().push(elseBranch);
            }
        } else {
            throw new model.exception.MyException("Condition expression does not evaluate to a boolean.");
        }
        return state;
    }


}
