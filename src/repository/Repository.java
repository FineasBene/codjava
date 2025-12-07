package repository;

import model.exception.MyException;
import model.state.ProgramState;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private final List<ProgramState> programs = new ArrayList<>();
    private final String logFilePath;

    public Repository(ProgramState initialState, String logFilePath) throws IOException {
        this.logFilePath = logFilePath;
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
    public ProgramState getCrtPrg() {
        if (programs.isEmpty()) throw new RuntimeException("No programs");
        return programs.get(0);
    }

    @Override
    public List<ProgramState> getAll() { return programs; }

    @Override
    public void logPrgStateExec() throws MyException {
        ProgramState prg = getCrtPrg();
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
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
