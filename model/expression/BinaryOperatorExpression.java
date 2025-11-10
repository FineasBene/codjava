package model.expression;

import model.exception.MyException;
import model.state.SymbolTable;
import model.type.Type;
import model.value.BooleanValue;
import model.value.IntegerValue;
import model.value.Value;

public record BinaryOperatorExpression
        (String operator, Expression left, Expression right)
        implements Expression {

    @Override
    public Value evaluate(SymbolTable symTable) throws MyException {
        var leftTerm = left.evaluate(symTable);
        var rightTerm = right.evaluate(symTable);

        switch (operator) {
            case "+", "-", "*", "/":
                checkTypes(leftTerm, rightTerm, Type.INTEGER);
                var leftValue = (IntegerValue) leftTerm;
                var rightValue = (IntegerValue) rightTerm;
                return evaluateArithmeticExpression(leftValue, rightValue);
            case "&&", "||":
                checkTypes(leftTerm, rightTerm, Type.BOOLEAN);
                var leftValueB = (BooleanValue) leftTerm;
                var rightValueB = (BooleanValue) rightTerm;
                return evaluateBooleanExpression(leftValueB, rightValueB);

            case ">", "<", "==", "!=", ">=", "<=":
                checkTypes(leftTerm, rightTerm, Type.INTEGER);
                var leftValR = (IntegerValue) leftTerm;
                var rightValR = (IntegerValue) rightTerm;
                return evaluateRelationalExpression(leftValR, rightValR);

        }

        throw new IllegalArgumentException("Unknown operator" + operator);
    }

    private void checkTypes(Value leftTerm, Value rightTerm, Type type) throws MyException {
        if (leftTerm.getType() != type ||
                rightTerm.getType() != type) {
            throw new model.exception.MyException("Wrong types for operator " + operator);
        }
    }

    private IntegerValue evaluateArithmeticExpression(IntegerValue leftValue, IntegerValue rightValue) {
        return switch (operator) {
            case "+" -> new IntegerValue(leftValue.value() + rightValue.value());
            case "-" -> new IntegerValue(leftValue.value() - rightValue.value());
            case "*" -> new IntegerValue(leftValue.value() * rightValue.value());
            case "/" -> new IntegerValue(leftValue.value() / rightValue.value());
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
}