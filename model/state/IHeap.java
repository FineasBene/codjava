package model.state;

import model.exception.MyException;
import model.value.Value;
import java.util.Map;

public interface IHeap {
    int allocate(Value value);
    Value get(int address) throws MyException;
    void update(int address, Value value) throws MyException;
    boolean isDefined(int address);
    Map<Integer, Value> getContent();
    void setContent(Map<Integer, Value> newContent);
}
