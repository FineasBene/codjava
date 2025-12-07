package model.statement;

import model.exception.MyException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.StringType;
import model.value.StringValue;
import model.value.Value;
import java.io.BufferedReader;
import java.io.IOException;

public record CloseRFileStatement(Expression expression) implements Statement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value value = expression.evaluate(state.symbolTable(), state.getHeap());

        if (!value.getType().equals(new StringType())) {
            throw new MyException("Expression must evaluate to a StringType.");
        }

        StringValue fileName = (StringValue) value;
        BufferedReader reader = state.getFileTable().get(fileName);

        try {
            reader.close();
            state.getFileTable().remove(fileName);
        } catch (IOException e) {
            throw new MyException("Error closing file.");
        }
        return null;
    }

    @Override
    public String toString() {
        return "closeRFile(" + expression.toString() + ")";
    }
}
