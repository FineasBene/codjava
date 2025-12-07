package model.state;

import model.exception.MyException;
import model.value.Value;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MyHeap implements IHeap {
    private Map<Integer, Value> map;
    private final AtomicInteger freeLocation;

    public MyHeap() {
        this.map = new HashMap<>();
        this.freeLocation = new AtomicInteger(1);
    }

    @Override
    public int allocate(Value value) {
        int newAddress = freeLocation.getAndIncrement();
        map.put(newAddress, value);
        return newAddress;
    }

    @Override
    public Value get(int address) throws MyException {
        if (!map.containsKey(address)) {
            throw new MyException("Heap address " + address + " is not defined.");
        }
        return map.get(address);
    }

    @Override
    public void update(int address, Value value) throws MyException {
        if (!map.containsKey(address)) {
            throw new MyException("Heap address " + address + " is not defined.");
        }
        map.put(address, value);
    }

    @Override
    public boolean isDefined(int address) {
        return map.containsKey(address);
    }

    @Override
    public Map<Integer, Value> getContent() {
        return map;
    }

    @Override
    public void setContent(Map<Integer, Value> newContent) {
        this.map = newContent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Value> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
