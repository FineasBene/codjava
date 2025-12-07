package model.value;

import model.type.RefType;
import model.type.Type;
import java.util.Objects;

public class RefValue implements Value {
    private final int address;
    private final Type locationType;

    public RefValue(int address, Type locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddr() {
        return address;
    }

    public Type getLocationType() {
        return locationType;
    }

    @Override
    public Type getType() {
        return new RefType(locationType);
    }

    @Override
    public String toString() {
        return "(" + address + ", " + locationType.toString() + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof RefValue)) return false;
        RefValue that = (RefValue) other;
        return address == that.address && locationType.equals(that.locationType);
    }
}
