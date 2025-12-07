package model.state;

import model.exception.MyException;
import model.value.StringValue;
import java.io.BufferedReader;
import java.util.Map;

public interface IFileTable {
    void add(StringValue fileName, BufferedReader reader) throws MyException;
    void remove(StringValue fileName) throws MyException;
    BufferedReader get(StringValue fileName) throws MyException;
    boolean isDefined(StringValue fileName);
    Map<StringValue, BufferedReader> getContent();
}
