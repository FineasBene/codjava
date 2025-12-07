package model.statement;

import model.exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.RefType;
import model.value.RefValue;
import model.value.Value;

public record NewStatement(String variableName, Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var symTable = state.symbolTable();
        var heap = state.getHeap();


        if (!symTable.isDefined(variableName)) {
            throw new MyException("Variable " + variableName + " not defined.");
        }
        Value varValue = symTable.getValue(variableName);
        if (!(varValue.getType() instanceof RefType)) {
            throw new MyException("Variable " + variableName + " must be of type RefType.");
        }


        Value evaluated = expression.evaluate(symTable, heap);
        RefType refType = (RefType) varValue.getType();

        if (!evaluated.getType().equals(refType.getInner())) {
            throw new MyException("Type mismatch: cannot store " + evaluated.getType() + " in " + refType);
        }

        int newAddress = heap.allocate(evaluated);

        symTable.update(variableName, new RefValue(newAddress, refType.getInner()));

        return null;
    }

    @Override
    public String toString() {
        return "new(" + variableName + ", " + expression.toString() + ")";
    }
}
