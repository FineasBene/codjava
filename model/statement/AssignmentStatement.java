package model.statement;

import model.exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.value.Value;

public record AssignmentStatement(Expression expression, String variableName)
        implements Statement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        SymbolTable symbolTable = state.symbolTable();
        if (!symbolTable.isDefined(variableName)) {
            throw new model.exception.MyException("Variable not defined");
        }
        Value value = expression.evaluate(symbolTable);
        if (!value.getType().equals(symbolTable.getType(variableName))) {
            throw new model.exception.MyException("Type mismatch");
        }
        symbolTable.update(variableName, value);
        return null;
    }
}
