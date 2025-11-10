
package controller;

import model.exception.MyException;
import model.state.ProgramState;
import model.statement.Statement;
import repository.IRepository;

public class Controller {
    private final IRepository repo;
    private final boolean displayFlag;

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public ProgramState oneStep(ProgramState state) throws MyException {
        var stack = state.executionStack();
        if (stack.isEmpty()) throw new MyException("Execution stack empty");
        Statement crt = stack.pop();
        return crt.execute(state);
    }

    public void allStep() {
        ProgramState prg = repo.getCrtPrg();
        try {
            repo.logPrgStateExec();
            while (!prg.executionStack().isEmpty()) {
                try { oneStep(prg); }
                catch (MyException e) { System.err.println("Error: " + e.getMessage()); break; }
                try { repo.logPrgStateExec(); } catch (MyException e) { System.err.println(e.getMessage()); }
                if (displayFlag) System.out.println(prg);
            }
        } catch (MyException e) {
            System.err.println("Logging failed: " + e.getMessage());
        }
    }
}
