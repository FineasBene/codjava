package model.expression;

import model.exception.MyException;
import model.state.SymbolTable;
import model.value.Value;

public record VariableExpression (String variableName)implements Expression{

    @Override
    public Value evaluate(SymbolTable symTable) throws MyException {
        if(!symTable.isDefined(variableName)){
            throw new model.exception.MyException("Variable not defined");
        }
        return symTable.getValue(variableName);
    }
}
