package model.value;

import model.type.StringType;
import model.type.Type;
import java.util.Objects;

public class StringValue implements Value {
    private final String value;

    public StringValue(String v) {
        this.value = v;
    }

    public String getVal() {
        return value;
    }

    @Override
    public Type getType() {
        return new StringType();
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        StringValue that = (StringValue) other;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
