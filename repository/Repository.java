package repository;

import model.exception.MyException;
import model.state.ProgramState;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<ProgramState> programs;
    private final String logFilePath;

    public Repository(ProgramState initialState, String logFilePath) throws IOException {
        this.logFilePath = logFilePath;
        this.programs = new ArrayList<>();
        this.programs.add(initialState);

        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, false)))) {
            logFile.println("--- LOG START ---");
        }
    }

    @Override
    public void addProgram(ProgramState state) {
        programs.add(state);
    }

    @Override
    public List<ProgramState> getPrgList() {
        return programs;
    }

    @Override
    public void setPrgList(List<ProgramState> list) {
        this.programs = list;
    }

    @Override
    public void logPrgStateExec(ProgramState prg) throws MyException {
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            logFile.println("Id: " + prg.getId());
            logFile.println("ExeStack:");
            logFile.println(prg.executionStack().toString());
            logFile.println("SymTable:");
            logFile.println(prg.symbolTable().toString());
            logFile.println("Out:");
            logFile.println(prg.out().toString());
            logFile.println("FileTable:");
            logFile.println(prg.getFileTable().toString());
            logFile.println("Heap:");
            logFile.println(prg.getHeap().toString());
            logFile.println("----------");
        } catch (IOException e) {
            throw new MyException("Cannot write log: " + logFilePath, e);
        }
    }
}
