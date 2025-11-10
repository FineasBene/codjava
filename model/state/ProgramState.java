package model.state;

import model.statement.Statement;

public class ProgramState {
    private final ExecutionStack executionStack;
    private final SymbolTable symbolTable;
    private final Out out;
    private final Statement originalProgram;

    public ProgramState(ExecutionStack executionStack, SymbolTable symbolTable, Out out, Statement originalProgram) {
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.out = out;
        this.originalProgram = originalProgram;
        if (originalProgram != null) this.executionStack.push(originalProgram);
    }

    public ExecutionStack executionStack() { return executionStack; }
    public SymbolTable symbolTable() { return symbolTable; }
    public Out out() { return out; }
    public Statement originalProgram() { return originalProgram; }

    @Override
    public String toString() {
        return "ProgramState{" +
                "executionStack=" + executionStack +
                ", symbolTable=" + symbolTable +
                ", out=" + out +
                ", originalProgram=" + originalProgram +
                '}';
    }
}
