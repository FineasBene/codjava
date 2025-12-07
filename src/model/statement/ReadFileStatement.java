package model.statement;

import model.exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.IntType;
import model.type.StringType;
import model.value.IntegerValue;
import model.value.StringValue;
import model.value.Value;
import java.io.BufferedReader;
import java.io.IOException;

public record ReadFileStatement(Expression expression, String variableName) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var symTable = state.symbolTable();
        if (!symTable.isDefined(variableName) || !symTable.getType(variableName).equals(new IntType())) {
            throw new MyException("Variable " + variableName + " invalid.");
        }

        Value value = expression.evaluate(symTable, state.getHeap());
        if (!value.getType().equals(new StringType())) {
            throw new MyException("Expression must evaluate to a StringType.");
        }

        StringValue fileName = (StringValue) value;
        BufferedReader reader = state.getFileTable().get(fileName);

        try {
            String line = reader.readLine();
            int intValue = (line == null || line.trim().isEmpty()) ? 0 : Integer.parseInt(line);
            symTable.update(variableName, new IntegerValue(intValue));
        } catch (IOException e) {
            throw new MyException("Error reading from file: " + fileName.getVal(), e);
        }
        return null;
    }

    @Override
    public String toString() {
        return "readFile(" + expression.toString() + ", " + variableName + ")";
    }
}
