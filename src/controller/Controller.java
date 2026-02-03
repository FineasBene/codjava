package controller;

import model.exception.MyException;
import model.state.ProgramState;
import model.value.RefValue;
import model.value.Value;
import repository.IRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    private final IRepository repo;
    private final boolean displayFlag;
    private ExecutorService executor;

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public IRepository getRepository() {
        return repo;
    }

    // Facem metoda publică pentru a o folosi în GUI
    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    // Metoda pentru executia unui singur pas (folosita de butonul Run One Step)
    public void executeOneStep() throws InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        List<ProgramState> prgList = removeCompletedPrg(repo.getPrgList());

        if (prgList.size() > 0) {
            // --- GARBAGE COLLECTOR ---
            // 1. Get all addresses from all symTables
            List<Integer> allSymTableAddrs = prgList.stream()
                    .flatMap(p -> getAddrFromSymTable(p.symbolTable().getContent().values()).stream())
                    .collect(Collectors.toList());

            // 2. Get heap (shared)
            var heap = prgList.get(0).getHeap();

            // 3. Call Safe GC
            heap.setContent(
                safeGarbageCollector(
                    allSymTableAddrs,
                    getAddrFromHeap(heap.getContent().values()),
                    heap.getContent()
                )
            );
            // -------------------------

            // One Step for all
            oneStepForAllPrg(prgList);

            // Remove completed
            prgList = removeCompletedPrg(repo.getPrgList());
        }

        executor.shutdownNow();
        repo.setPrgList(prgList);
    }

    public void oneStepForAllPrg(List<ProgramState> prgList) throws InterruptedException {
        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
                if(displayFlag) System.out.println(prg);
            } catch (MyException e) {
                System.err.println("Log error: " + e.getMessage());
            }
        });

        List<Callable<ProgramState>> callList = prgList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) (() -> {
                    return p.oneStep();
                }))
                .collect(Collectors.toList());

        List<ProgramState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        System.err.println("Execution error: " + e.getMessage());
                        // e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        prgList.addAll(newPrgList);

        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
                if(displayFlag) System.out.println(prg);
            } catch (MyException e) {
                System.err.println("Log error: " + e.getMessage());
            }
        });

        repo.setPrgList(prgList);
    }

    public void allStep() throws InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        List<ProgramState> prgList = removeCompletedPrg(repo.getPrgList());

        while(prgList.size() > 0) {
            // GC Logic duplicated here for full run compatibility
             List<Integer> allSymTableAddrs = prgList.stream()
                    .flatMap(p -> getAddrFromSymTable(p.symbolTable().getContent().values()).stream())
                    .collect(Collectors.toList());
            var heap = prgList.get(0).getHeap();
            heap.setContent(safeGarbageCollector(allSymTableAddrs, getAddrFromHeap(heap.getContent().values()), heap.getContent()));

            oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(repo.getPrgList());
        }

        executor.shutdownNow();
        repo.setPrgList(prgList);
    }

    public List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> { RefValue v1 = (RefValue)v; return v1.getAddr(); })
                .collect(Collectors.toList());
    }

    public List<Integer> getAddrFromHeap(Collection<Value> heapValues) {
        return heapValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> { RefValue v1 = (RefValue)v; return v1.getAddr(); })
                .collect(Collectors.toList());
    }

    public Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, List<Integer> heapAddr, Map<Integer, Value> heap) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
