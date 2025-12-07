package model.statement;

import model.exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;

public record PrintStatement(Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        state.out().add(expression.evaluate(state.symbolTable(), state.getHeap()));
        return null;
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }
}
