package model.value;

import model.type.BoolType;
import model.type.Type;
import java.util.Objects;

public record BooleanValue(boolean value) implements Value {

    @Override
    public Type getType() {
        return new BoolType(); // Modificat
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) { // Adăugat
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        BooleanValue that = (BooleanValue) other;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
