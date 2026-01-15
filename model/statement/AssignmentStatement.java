package model.statement;

import model.exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.state.SymbolTable;
import model.value.Value;
import model.type.Type;
import java.util.Map;

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
    public Map<String, Type> typecheck(Map<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.get(variableName);
        if (typevar == null) throw new MyException("Variable " + variableName + " not defined");

        Type typexp = expression.typecheck(typeEnv);
        if (typevar.equals(typexp))
            return typeEnv;
        else
            throw new MyException("Assignment: right hand side and left hand side have different types for variable " + variableName);
    }

    @Override
    public String toString() {
        return variableName + " = " + expression.toString();
    }
}
