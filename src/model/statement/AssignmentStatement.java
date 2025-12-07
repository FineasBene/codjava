package model.statement;

import model.exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.value.Value;

public record AssignmentStatement(Expression expression, String variableName) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        SymbolTable symbolTable = state.symbolTable();
        var heap = state.getHeap();

        if (!symbolTable.isDefined(variableName)) {
            throw new MyException("Variable not defined: " + variableName);
        }


        Value value = expression.evaluate(symbolTable, heap);

        if (!value.getType().equals(symbolTable.getType(variableName))) {
            throw new MyException("Type mismatch: Cannot assign " + value.getType() + " to " + symbolTable.getType(variableName));
        }
        symbolTable.update(variableName, value);
        return null;
    }

    @Override
    public String toString() {
        return variableName + " = " + expression.toString();
    }
}
