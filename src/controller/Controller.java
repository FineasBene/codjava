package controller;

import model.exception.MyException;
import model.state.ProgramState;
import model.statement.Statement;
import model.value.RefValue;
import model.value.Value;
import repository.IRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    public void allStep() throws MyException {
        ProgramState prg = repo.getCrtPrg();
        try {
            repo.logPrgStateExec();
            if (displayFlag) System.out.println(prg.toString());

            while (!prg.executionStack().isEmpty()) {
                oneStep(prg);
                prg.getHeap().setContent(
                    safeGarbageCollector(
                        getAddrFromSymTable(prg.symbolTable().getContent().values()),
                        getAddrFromHeap(prg.getHeap().getContent().values()),
                        prg.getHeap().getContent()
                    )
                );

                repo.logPrgStateExec();
                if (displayFlag) System.out.println(prg.toString());
            }
        } catch (MyException e) {
            System.err.println("Error: " + e.getMessage());
            throw e; 
        }
    }

    List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> { RefValue v1 = (RefValue)v; return v1.getAddr(); })
                .collect(Collectors.toList());
    }


    List<Integer> getAddrFromHeap(Collection<Value> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> { RefValue v1 = (RefValue)v; return v1.getAddr(); })
                .collect(Collectors.toList());
    }

    Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, List<Integer> heapAddr, Map<Integer, Value> heap) {

        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey())) 

                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
