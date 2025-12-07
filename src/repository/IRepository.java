package repository;

import model.exception.MyException;
import model.state.ProgramState;
import java.util.List;

public interface IRepository {
    ProgramState getCrtPrg();
    void addProgram(ProgramState state);
    List<ProgramState> getAll();
    void logPrgStateExec() throws MyException;
}
