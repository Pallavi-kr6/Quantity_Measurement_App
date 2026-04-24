public class QuantityMeasurementApp {

    // ================== UC8: Standalone Enum ==================
    enum LengthUnit {

        FEET(1.0),
        INCHES(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.0328084);

        private final double factor; // relative to FEET (base unit)

        LengthUnit(double factor) {
            this.factor = factor;
        }

        public double getFactor() {
            return factor;
        }

        // Convert TO base unit (FEET)
        public double convertToBaseUnit(double value) {
            return value * factor;
        }

        // Convert FROM base unit (FEET)
        public double convertFromBaseUnit(double baseValue) {
            return baseValue / factor;
        }
    }

    // ================== QuantityLength ==================
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

        // -------- UC5: Conversion --------
        public QuantityLength convertTo(LengthUnit target) {
            if (target == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double base = unit.convertToBaseUnit(value);
            double result = target.convertFromBaseUnit(base);

            return new QuantityLength(result, target);
        }

        // -------- UC6: Add (default unit) --------
        public QuantityLength add(QuantityLength other) {
            if (other == null) {
                throw new IllegalArgumentException("Other cannot be null");
            }

            double sumBase = unit.convertToBaseUnit(value)
                             + other.unit.convertToBaseUnit(other.value);

            double result = unit.convertFromBaseUnit(sumBase);

            return new QuantityLength(result, unit);
        }

        // -------- UC7: Add with target unit --------
        public QuantityLength add(QuantityLength other, LengthUnit target) {
            if (other == null || target == null) {
                throw new IllegalArgumentException("Invalid input");
            }

            double sumBase = unit.convertToBaseUnit(value)
                             + other.unit.convertToBaseUnit(other.value);

            double result = target.convertFromBaseUnit(sumBase);

            return new QuantityLength(result, target);
        }

        // -------- Equality --------
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof QuantityLength)) return false;

            QuantityLength other = (QuantityLength) obj;

            double epsilon = 1e-6;

            double thisBase = unit.convertToBaseUnit(value);
            double otherBase = other.unit.convertToBaseUnit(other.value);

            return Math.abs(thisBase - otherBase) < epsilon;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // ================== MAIN (Testing All UC) ==================
    public static void main(String[] args) {

        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        // -------- UC5: Conversion --------
        System.out.println("=== Conversion ===");
        System.out.println(a + " -> " + a.convertTo(LengthUnit.INCHES));
        System.out.println(a + " -> " + a.convertTo(LengthUnit.YARDS));

        // -------- UC6: Addition (default unit) --------
        System.out.println("\n=== Addition (Default Unit) ===");
        System.out.println(a + " + " + b + " = " + a.add(b));

        // -------- UC7: Addition (Target Unit) --------
        System.out.println("\n=== Addition (Target Unit) ===");
        System.out.println(a + " + " + b + " (FEET) = " + a.add(b, LengthUnit.FEET));
        System.out.println(a + " + " + b + " (INCHES) = " + a.add(b, LengthUnit.INCHES));
        System.out.println(a + " + " + b + " (YARDS) = " + a.add(b, LengthUnit.YARDS));

        // -------- More Cases --------
        QuantityLength c = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength d = new QuantityLength(1.0, LengthUnit.YARDS);

        System.out.println("\n=== More Examples ===");
        System.out.println(c + " + " + d + " (FEET) = " + c.add(d, LengthUnit.FEET));

        QuantityLength e = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        QuantityLength f = new QuantityLength(1.0, LengthUnit.INCHES);

        System.out.println(e + " + " + f + " (CM) = " + e.add(f, LengthUnit.CENTIMETERS));

        // -------- Equality --------
        System.out.println("\n=== Equality ===");
        System.out.println(new QuantityLength(36, LengthUnit.INCHES)
                .equals(new QuantityLength(1, LengthUnit.YARDS)));

        // -------- Edge Cases --------
        System.out.println("\n=== Edge Cases ===");
        System.out.println(new QuantityLength(5.0, LengthUnit.FEET)
                .add(new QuantityLength(0.0, LengthUnit.INCHES), LengthUnit.YARDS));

        System.out.println(new QuantityLength(5.0, LengthUnit.FEET)
                .add(new QuantityLength(-2.0, LengthUnit.FEET), LengthUnit.INCHES));
    }
}
