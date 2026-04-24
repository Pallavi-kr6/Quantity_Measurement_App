public class QuantityMeasurementApp {

    // ---------------- ENUM ----------------
    enum LengthUnit {
        FEET(1.0),
        INCHES(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.0328084);

        private final double factor;

        LengthUnit(double factor) {
            this.factor = factor;
        }

        public double getFactor() {
            return factor;
        }
    }

    // ---------------- VALUE OBJECT ----------------
    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Invalid value");
            }
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        public double getValue() {
            return value;
        }

        public LengthUnit getUnit() {
            return unit;
        }

        // Convert to another unit
        public QuantityLength convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double baseValue = toBaseUnit();
            double converted = baseValue / targetUnit.getFactor();

            return new QuantityLength(converted, targetUnit);
        }

        // 🔹 UC6: ADD METHOD (instance)
        public QuantityLength add(QuantityLength other) {
            if (other == null) {
                throw new IllegalArgumentException("Other length cannot be null");
            }

            double sumBase = this.toBaseUnit() + other.toBaseUnit();
            double result = sumBase / this.unit.getFactor();

            return new QuantityLength(result, this.unit);
        }

        private double toBaseUnit() {
            return value * unit.getFactor();
        }

        // ----------- OVERRIDDEN METHODS -----------

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof QuantityLength)) return false;

            QuantityLength other = (QuantityLength) obj;

            double epsilon = 1e-6;
            return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < epsilon;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // ---------------- STATIC API ----------------

    // UC5 conversion
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid value");
        }
        if (source == null || target == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        double baseValue = value * source.getFactor();
        return baseValue / target.getFactor();
    }

    // 🔹 UC6: ADD (static version)
    public static QuantityLength add(QuantityLength l1, QuantityLength l2) {
        if (l1 == null || l2 == null) {
            throw new IllegalArgumentException("Lengths cannot be null");
        }

        double sumBase = l1.getValue() * l1.getUnit().getFactor()
                       + l2.getValue() * l2.getUnit().getFactor();

        double result = sumBase / l1.getUnit().getFactor();

        return new QuantityLength(result, l1.getUnit());
    }

    // 🔹 OVERLOADED ADD (raw values)
    public static QuantityLength add(double v1, LengthUnit u1,
                                     double v2, LengthUnit u2,
                                     LengthUnit targetUnit) {

        double sumBase = (v1 * u1.getFactor()) + (v2 * u2.getFactor());
        double result = sumBase / targetUnit.getFactor();

        return new QuantityLength(result, targetUnit);
    }

    // ---------------- DEMO METHODS ----------------

    public static void demonstrateAddition(QuantityLength l1, QuantityLength l2) {
        QuantityLength result = l1.add(l2);
        System.out.println(l1 + " + " + l2 + " = " + result);
    }

    public static void demonstrateAddition(double v1, LengthUnit u1,
                                           double v2, LengthUnit u2,
                                           LengthUnit target) {
        QuantityLength result = add(v1, u1, v2, u2, target);
        System.out.println("add(" + v1 + " " + u1 + ", " +
                           v2 + " " + u2 + ") = " + result);
    }

    // ---------------- MAIN METHOD ----------------
    public static void main(String[] args) {

        // Same unit
        demonstrateAddition(new QuantityLength(1.0, LengthUnit.FEET),
                            new QuantityLength(2.0, LengthUnit.FEET));

        // Cross unit
        demonstrateAddition(new QuantityLength(1.0, LengthUnit.FEET),
                            new QuantityLength(12.0, LengthUnit.INCHES));

        demonstrateAddition(new QuantityLength(12.0, LengthUnit.INCHES),
                            new QuantityLength(1.0, LengthUnit.FEET));

        // Yards + Feet
        demonstrateAddition(new QuantityLength(1.0, LengthUnit.YARDS),
                            new QuantityLength(3.0, LengthUnit.FEET));

        // Centimeter + Inch
        demonstrateAddition(new QuantityLength(2.54, LengthUnit.CENTIMETERS),
                            new QuantityLength(1.0, LengthUnit.INCHES));

        // Zero & negative
        demonstrateAddition(new QuantityLength(5.0, LengthUnit.FEET),
                            new QuantityLength(0.0, LengthUnit.INCHES));

        demonstrateAddition(new QuantityLength(5.0, LengthUnit.FEET),
                            new QuantityLength(-2.0, LengthUnit.FEET));
    }
}
