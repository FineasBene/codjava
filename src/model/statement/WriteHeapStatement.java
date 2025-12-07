package model.statement;

import model.exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.RefType;
import model.value.RefValue;
import model.value.Value;

public record WriteHeapStatement(String variableName, Expression expression) implements Statement {
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

        RefValue refValue = (RefValue) varValue;
        if (!heap.isDefined(refValue.getAddr())) {
            throw new MyException("Address " + refValue.getAddr() + " no longer exists in Heap.");
        }


        Value evaluated = expression.evaluate(symTable, heap);


        if (!evaluated.getType().equals(refValue.getLocationType())) {
            throw new MyException("Type mismatch for Heap write.");
        }


        heap.update(refValue.getAddr(), evaluated);

        return null;
    }

    @Override
    public String toString() {
        return "wH(" + variableName + ", " + expression.toString() + ")";
    }
}
