package model.statement;

import model.state.ProgramState;
import model.exception.MyException;
public interface Statement {
    ProgramState execute(ProgramState state) throws MyException;
}
