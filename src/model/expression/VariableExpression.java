package model.expression;

import model.exception.MyException;
import model.state.IHeap;
import model.state.SymbolTable;
import model.value.Value;

public record VariableExpression(String variableName) implements Expression {
    @Override
    public Value evaluate(SymbolTable symTable, IHeap heap) throws MyException {
        if (!symTable.isDefined(variableName)) {
            throw new MyException("Variable not defined: " + variableName);
        }
        return symTable.getValue(variableName);
    }

    @Override
    public String toString() {
        return variableName;
    }
}
