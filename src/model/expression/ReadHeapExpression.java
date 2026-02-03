package model.expression;

import model.exception.MyException;
import model.state.IHeap;
import model.state.SymbolTable;
import model.value.RefValue;
import model.value.Value;
import model.type.Type;
import model.type.RefType;
import java.util.Map;

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
    public Type typecheck(Map<String, Type> typeEnv) throws MyException {
        Type typ = expression.typecheck(typeEnv);
        if (typ instanceof RefType) {
            RefType reft = (RefType) typ;
            return reft.getInner();
        } else {
            throw new MyException("The rH argument is not a Ref Type");
        }
    }

    @Override
    public String toString() {
        return "rH(" + expression.toString() + ")";
    }
}
