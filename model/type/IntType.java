package model.type;

import model.value.IntegerValue;
import model.value.Value;

public class IntType implements Type {
    @Override
    public Value defaultValue() {
        return new IntegerValue(0);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof IntType;
    }

    @Override
    public String toString() {
        return "int";
    }
}
