package model.type;

import model.value.IntegerValue;
import model.value.BooleanValue;
import model.value.Value;

public enum Type {
    INTEGER,
    BOOLEAN;

    public Value getDefaultValue() {
        switch (this) {
            case INTEGER:
                return new IntegerValue(0);
            case BOOLEAN:
                return new BooleanValue(false);
            default:
                throw new IllegalStateException("Unexpected type: " + this);
        }
    }
}
