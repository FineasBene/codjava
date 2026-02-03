package model.expression;

import model.exception.MyException;
import model.state.IHeap;
import model.state.SymbolTable;
import model.value.Value;
import model.type.Type;
import java.util.Map;

public interface Expression {
    Value evaluate(SymbolTable symTable, IHeap heap) throws MyException;

    Type typecheck(Map<String, Type> typeEnv) throws MyException;

    String toString();
}
