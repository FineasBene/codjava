package model.state;

import model.value.Value;
import java.util.List;

public interface Out {
    void add(Value value);

    // Metoda necesara pentru GUI
    List<Value> getList();
}
