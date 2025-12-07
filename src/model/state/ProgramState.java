package model.state;

import model.statement.Statement;

public class ProgramState {
    private final ExecutionStack executionStack;
    private final SymbolTable symbolTable;
    private final Out out;
    private final IFileTable fileTable;
    private final IHeap heap; // Câmp nou [cite: 43]
    private final Statement originalProgram;

    public ProgramState(ExecutionStack executionStack, SymbolTable symbolTable, Out out, IFileTable fileTable, IHeap heap, Statement originalProgram) {
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap; // Inițializare
        this.originalProgram = originalProgram;
        if (originalProgram != null) this.executionStack.push(originalProgram);
    }

    public ExecutionStack executionStack() { return executionStack; }
    public SymbolTable symbolTable() { return symbolTable; }
    public Out out() { return out; }
    public IFileTable getFileTable() { return fileTable; }
    public IHeap getHeap() { return heap; } // Getter nou
    public Statement originalProgram() { return originalProgram; }

    @Override
    public String toString() {
        return "--- Program State ---\n" +
                "ExeStack:\n" + executionStack.toString() +
                "SymTable:\n" + symbolTable.toString() +
                "Out:\n" + out.toString() +
                "FileTable:\n" + fileTable.toString() +
                "Heap:\n" + heap.toString() + // Afișare heap
                "----------\n";
    }
}
