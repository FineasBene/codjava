package model.statement;

import model.state.ProgramState;
import model.exception.MyException;
import model.type.Type;
import java.util.Map;

public interface Statement {
    ProgramState execute(ProgramState state) throws MyException;


    Map<String, Type> typecheck(Map<String, Type> typeEnv) throws MyException;
}
