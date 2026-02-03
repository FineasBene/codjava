package model.statement;

import model.exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.RefType;
import model.type.Type;
import model.value.RefValue;
import model.value.Value;
import java.util.Map;

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
    public Map<String, Type> typecheck(Map<String, Type> typeEnv) throws MyException {
        Type typevar = typeEnv.get(variableName);
        if (typevar == null) throw new MyException("Variable " + variableName + " not defined");

        Type typexp = expression.typecheck(typeEnv);

        if (typevar.equals(new RefType(typexp))) {
            return typeEnv;
        } else {
            throw new MyException("NEW stmt: right hand side and left hand side have different types");
        }
    }

    @Override
    public String toString() {
        return "new(" + variableName + ", " + expression.toString() + ")";
    }
}
