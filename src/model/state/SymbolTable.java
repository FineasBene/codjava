package model.state;

import model.type.Type;
import model.value.Value;
import java.util.Map;

public interface SymbolTable {
    boolean isDefined(String variableName);
    Type getType(String variableName);
    void declareVariable(String variableName, Type type);
    void update(String variableName, Value value);
    Value getValue(String variableName);
    Map<String, Value> getContent();
    SymbolTable copy(); 

    String toString();
}
