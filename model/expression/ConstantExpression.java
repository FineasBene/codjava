package model.expression;

import model.exception.MyException;
import model.state.IHeap;
import model.state.SymbolTable;
import model.value.Value;
import model.type.Type;
import java.util.Map;

public record ConstantExpression(Value value) implements Expression {
    @Override
    public Value evaluate(SymbolTable symTable, IHeap heap) {
        return value;
    }

    @Override
    public Type typecheck(Map<String, Type> typeEnv) throws MyException {
        return value.getType();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
