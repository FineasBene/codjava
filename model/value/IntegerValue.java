package model.value;

import model.type.Type;

public record IntegerValue(int value) implements Value {

    @Override
    public Type getType() {
        return Type.INTEGER;
    }
}
