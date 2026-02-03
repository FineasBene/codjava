package model.state;

import model.exception.MyException;
import model.value.StringValue;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class MapFileTable implements IFileTable {
    private final Map<StringValue, BufferedReader> map = new HashMap<>();

    @Override
    public void add(StringValue fileName, BufferedReader reader) throws MyException {
        if (map.containsKey(fileName)) {
            throw new MyException("File already opened: " + fileName.getVal());
        }
        map.put(fileName, reader);
    }

    @Override
    public void remove(StringValue fileName) throws MyException {
        if (!map.containsKey(fileName)) {
            throw new MyException("File not found: " + fileName.getVal());
        }
        map.remove(fileName);
    }

    @Override
    public BufferedReader get(StringValue fileName) throws MyException {
        if (!map.containsKey(fileName)) {
            throw new MyException("File not found: " + fileName.getVal());
        }
        return map.get(fileName);
    }

    @Override
    public boolean isDefined(StringValue fileName) {
        return map.containsKey(fileName);
    }

    @Override
    public Map<StringValue, BufferedReader> getContent() {
        return map;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (StringValue fileName : map.keySet()) {
            sb.append(fileName.toString()).append(" ");
        }
        return sb.toString();
    }
}
