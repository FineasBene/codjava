package model.statement;

import model.exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.StringType;
import model.value.StringValue;
import model.value.Value;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public record OpenRFileStatement(Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value value = expression.evaluate(state.symbolTable(), state.getHeap());

        if (!value.getType().equals(new StringType())) {
            throw new MyException("Expression must evaluate to a StringType.");
        }

        StringValue fileName = (StringValue) value;
        if (state.getFileTable().isDefined(fileName)) {
            throw new MyException("File is already open: " + fileName.getVal());
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName.getVal()));
            state.getFileTable().add(fileName, reader);
        } catch (IOException e) {
            throw new MyException("Could not open file: " + fileName.getVal());
        }
        return null;
    }

    @Override
    public String toString() {
        return "openRFile(" + expression.toString() + ")";
    }
}
