package model.state;

import model.type.Type;
import model.value.Value;

import java.util.HashMap;
import java.util.Map;

public class MapSymbolTable implements SymbolTable {

    private final Map<String, Value> map = new HashMap<>();

    @Override
    public boolean isDefined(String variableName) {
        return map.containsKey(variableName);
    }

    @Override
    public Type getType(String variableName) {
        return map.get(variableName).getType();
    }

    @Override
    public void declareVariable(String variableName, Type type) {
        map.put(variableName, type.getDefaultValue());
    }

    @Override
    public void update(String variableName, Value value) {
        map.put(variableName, value);
    }

    @Override
    public Value getValue(String variableName) {
        return map.get(variableName);
    }
}
