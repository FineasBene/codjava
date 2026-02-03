package model.state;

import model.exception.MyException;
import model.statement.Statement;
import java.util.concurrent.atomic.AtomicInteger;

public class ProgramState {
    private final ExecutionStack executionStack;
    private final SymbolTable symbolTable;
    private final Out out;
    private final IFileTable fileTable;
    private final IHeap heap;
    private final Statement originalProgram;


    private final int id;
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    public ProgramState(ExecutionStack executionStack, SymbolTable symbolTable, Out out, IFileTable fileTable, IHeap heap, Statement originalProgram) {
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.originalProgram = originalProgram;
        this.id = getNewId();

        if (originalProgram != null) {
            this.executionStack.push(originalProgram);
        }
    }


    private static synchronized int getNewId() {
        return idCounter.incrementAndGet();
    }

    public int getId() { return id; }
    public ExecutionStack executionStack() { return executionStack; }
    public SymbolTable symbolTable() { return symbolTable; }
    public Out out() { return out; }
    public IFileTable getFileTable() { return fileTable; }
    public IHeap getHeap() { return heap; }

    public boolean isNotCompleted() {
        return !executionStack.isEmpty();
    }

    public ProgramState oneStep() throws MyException {
        if (executionStack.isEmpty()) {
            throw new MyException("Program stack is empty");
        }
        Statement crtStmt = executionStack.pop();
        return crtStmt.execute(this);
    }

    @Override
    public String toString() {
        return "Id: " + id + "\n" +
                "ExeStack:\n" + executionStack.toString() +
                "SymTable:\n" + symbolTable.toString() +
                "Out:\n" + out.toString() +
                "FileTable:\n" + fileTable.toString() +
                "Heap:\n" + heap.toString() +
                "----------\n";
    }
}
