package model.type;

import model.value.BooleanValue;
import model.value.Value;

public class BoolType implements Type {
    @Override
    public Value defaultValue() {
        return new BooleanValue(false);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof BoolType;
    }

    @Override
    public String toString() {
        return "bool";
    }
}
