package model.expression;

import model.exception.MyException;
import model.state.IHeap;
import model.state.SymbolTable;
import model.type.BoolType;
import model.type.IntType;
import model.type.Type;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.Value;
import java.util.Map;

public record BinaryOperatorExpression(String operator, Expression left, Expression right) implements Expression {

    @Override
    public Value evaluate(SymbolTable symTable, IHeap heap) throws MyException {
        var leftTerm = left.evaluate(symTable, heap);
        var rightTerm = right.evaluate(symTable, heap);

        switch (operator) {
            case "+", "-", "*", "/":
                checkTypes(leftTerm, rightTerm, new IntType());
                var leftValue = (IntegerValue) leftTerm;
                var rightValue = (IntegerValue) rightTerm;
                return evaluateArithmeticExpression(leftValue, rightValue);
            case "&&", "||":
                checkTypes(leftTerm, rightTerm, new BoolType());
                var leftValueB = (BooleanValue) leftTerm;
                var rightValueB = (BooleanValue) rightTerm;
                return evaluateBooleanExpression(leftValueB, rightValueB);
            case ">", "<", "==", "!=", ">=", "<=":
                checkTypes(leftTerm, rightTerm, new IntType());
                var leftValR = (IntegerValue) leftTerm;
                var rightValR = (IntegerValue) rightTerm;
                return evaluateRelationalExpression(leftValR, rightValR);
        }
        throw new IllegalArgumentException("Unknown operator: " + operator);
    }

    @Override
    public Type typecheck(Map<String, Type> typeEnv) throws MyException {
        Type typ1 = left.typecheck(typeEnv);
        Type typ2 = right.typecheck(typeEnv);

        if (operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("/")) {
            if (typ1.equals(new IntType())) {
                if (typ2.equals(new IntType())) {
                    return new IntType();
                } else throw new MyException("Second operand is not an integer");
            } else throw new MyException("First operand is not an integer");
        } else if (operator.equals("<") || operator.equals("<=") || operator.equals(">") || operator.equals(">=") || operator.equals("==") || operator.equals("!=")) {
            if (typ1.equals(new IntType())) {
                if (typ2.equals(new IntType())) {
                    return new BoolType();
                } else throw new MyException("Second operand is not an integer");
            } else throw new MyException("First operand is not an integer");
        } else if (operator.equals("&&") || operator.equals("||")) {
            if (typ1.equals(new BoolType())) {
                if (typ2.equals(new BoolType())) {
                    return new BoolType();
                } else throw new MyException("Second operand is not a boolean");
            } else throw new MyException("First operand is not a boolean");
        }

        throw new MyException("Invalid operator: " + operator);
    }

    private void checkTypes(Value leftTerm, Value rightTerm, Type type) throws MyException {
        if (!leftTerm.getType().equals(type) || !rightTerm.getType().equals(type)) {
            throw new MyException("Wrong types for operator " + operator);
        }
    }

    private IntegerValue evaluateArithmeticExpression(IntegerValue leftValue, IntegerValue rightValue) throws MyException {
        return switch (operator) {
            case "+" -> new IntegerValue(leftValue.value() + rightValue.value());
            case "-" -> new IntegerValue(leftValue.value() - rightValue.value());
            case "*" -> new IntegerValue(leftValue.value() * rightValue.value());
            case "/" -> {
                if (rightValue.value() == 0) throw new MyException("Division by zero");
                yield new IntegerValue(leftValue.value() / rightValue.value());
            }
            default -> throw new IllegalStateException("Unreachable code");
        };
    }

    private BooleanValue evaluateBooleanExpression(BooleanValue leftValue, BooleanValue rightValue) {
        return switch (operator) {
            case "&&" -> new BooleanValue(leftValue.value() && rightValue.value());
            case "||" -> new BooleanValue(leftValue.value() || rightValue.value());
            default -> throw new IllegalStateException("Unreachable code");
        };
    }

    private BooleanValue evaluateRelationalExpression(IntegerValue leftValue, IntegerValue rightValue) {
        int left = leftValue.value();
        int right = rightValue.value();
        return switch (operator) {
            case ">" -> new BooleanValue(left > right);
            case "<" -> new BooleanValue(left < right);
            case "==" -> new BooleanValue(left == right);
            case "!=" -> new BooleanValue(left != right);
            case ">=" -> new BooleanValue(left >= right);
            case "<=" -> new BooleanValue(left <= right);
            default -> throw new IllegalStateException("Unreachable code");
        };
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + operator + " " + right.toString() + ")";
    }
}
