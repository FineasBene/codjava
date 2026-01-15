package model.expression;

import model.exception.MyException;
import model.state.IHeap;
import model.state.SymbolTable;
import model.value.Value;
import model.type.Type;
import java.util.Map;

public record VariableExpression(String variableName) implements Expression {
    @Override
    public Value evaluate(SymbolTable symTable, IHeap heap) throws MyException {
        if (!symTable.isDefined(variableName)) {
            throw new MyException("Variable not defined: " + variableName);
        }
        return symTable.getValue(variableName);
    }

    @Override
    public Type typecheck(Map<String, Type> typeEnv) throws MyException {
        return typeEnv.get(variableName);
    }

    @Override
    public String toString() {
        return variableName;
    }
}
