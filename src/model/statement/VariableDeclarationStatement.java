package model.statement;

import model.exception.MyException;
import model.state.ProgramState;
import model.type.Type;

public record VariableDeclarationStatement(Type type, String variableName) implements Statement {

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var symbolTable = state.symbolTable();
        if (symbolTable.isDefined(variableName)) {
            throw new model.exception.MyException("Variable already defined: " + variableName);
        }
        symbolTable.declareVariable(variableName, type);
        return null;
    }

    @Override
    public String toString() {
        return type.toString() + " " + variableName;
    }
}
