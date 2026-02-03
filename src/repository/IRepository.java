package repository;

import model.exception.MyException;
import model.state.ProgramState;
import java.util.List;

public interface IRepository {
    void addProgram(ProgramState state);

    List<ProgramState> getPrgList();

    void setPrgList(List<ProgramState> list);

    void logPrgStateExec(ProgramState prg) throws MyException;
}
