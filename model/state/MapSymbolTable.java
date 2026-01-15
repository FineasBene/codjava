package model.state;

import model.type.Type;
import model.value.Value;
import java.util.HashMap;
import java.util.Map;

public class MapSymbolTable implements SymbolTable {
    private final Map<String, Value> map;

    public MapSymbolTable() {
        this.map = new HashMap<>();
    }

    private MapSymbolTable(Map<String, Value> newMap) {
        this.map = newMap;
    }

    @Override
    public boolean isDefined(String variableName) {
        return map.containsKey(variableName);
    }

    @Override
    public Type getType(String variableName) {
        if (!isDefined(variableName)) return null;
        return map.get(variableName).getType();
    }

    @Override
    public void declareVariable(String variableName, Type type) {
        map.put(variableName, type.defaultValue());
    }

    @Override
    public void update(String variableName, Value value) {
        map.put(variableName, value);
    }

    @Override
    public Value getValue(String variableName) {
        return map.get(variableName);
    }

    @Override
    public Map<String, Value> getContent() {
        return map;
    }

    @Override
    public SymbolTable copy() {
        Map<String, Value> newMap = new HashMap<>(this.map);
        return new MapSymbolTable(newMap);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Value> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(" --> ").append(entry.getValue().toString()).append("\n");
        }
        return sb.toString();
    }
}
