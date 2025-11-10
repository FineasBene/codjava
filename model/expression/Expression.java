package model.expression;

import model.exception.MyException;
import model.state.SymbolTable;
import model.value.Value;

public interface Expression {
    Value evaluate(SymbolTable symTable) throws MyException;
}
