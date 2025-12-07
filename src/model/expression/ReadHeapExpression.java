package model.expression;

import model.exception.MyException;
import model.state.IHeap;
import model.state.SymbolTable;
import model.value.RefValue;
import model.value.Value;

public record ReadHeapExpression(Expression expression) implements Expression {
    @Override
    public Value evaluate(SymbolTable symTable, IHeap heap) throws MyException {
        Value val = expression.evaluate(symTable, heap);

        if (!(val instanceof RefValue)) {
            throw new MyException("Expression inside rH must evaluate to RefValue.");
        }

        RefValue refVal = (RefValue) val;
        int address = refVal.getAddr();

        if (!heap.isDefined(address)) {
            throw new MyException("Address " + address + " is not defined in the Heap.");
        }

        return heap.get(address);
    }

    @Override
    public String toString() {
        return "rH(" + expression.toString() + ")";
    }
}
