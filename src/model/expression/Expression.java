package model.expression;

import model.exception.MyException;
import model.state.IHeap;
import model.state.SymbolTable;
import model.value.Value;

public interface Expression {
    // Semnătura modificată conform cerinței 
    Value evaluate(SymbolTable symTable, IHeap heap) throws MyException;
    String toString();
}
