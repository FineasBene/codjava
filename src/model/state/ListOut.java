package model.state;

import model.value.Value;
import java.util.ArrayList;
import java.util.List;

public class ListOut implements Out {
    private final List<Value> list;

    public ListOut() {
        this.list = new ArrayList<>();
    }

    @Override
    public void add(Value value) {
        list.add(value);
    }

    // Implementarea metodei pentru GUI
    @Override
    public List<Value> getList() {
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Value v : list) {
            sb.append(v.toString()).append("\n");
        }
        return sb.toString();
    }
}
