public class QuantityMeasurementApp {

    // ---------------- ENUM ----------------
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

        // Convert to another unit (returns new object → immutability)
        public QuantityLength convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double baseValue = toBaseUnit();
            double converted = baseValue / targetUnit.getFactor();

            return new QuantityLength(converted, targetUnit);
        }

        // Convert to base unit (FEET)
        private double toBaseUnit() {
            return value * unit.getFactor();
        }

        // ----------- OVERRIDDEN METHODS -----------

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof QuantityLength)) return false;

            QuantityLength other = (QuantityLength) obj;

            double thisBase = this.toBaseUnit();
            double otherBase = other.toBaseUnit();

            double epsilon = 1e-6;
            return Math.abs(thisBase - otherBase) < epsilon;
        }

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    // ---------------- STATIC API ----------------

    // Core conversion method (UC5)
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid value");
        }
        if (source == null || target == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }

        double baseValue = value * source.getFactor(); // to FEET
        return baseValue / target.getFactor();         // to target
    }

    // ----------- METHOD OVERLOADING -----------

    // Method 1: raw values
    public static void demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {
        double result = convert(value, from, to);
        System.out.println("convert(" + value + ", " + from + ", " + to + ") = " + result);
    }

    // Method 2: using object
    public static void demonstrateLengthConversion(QuantityLength length, LengthUnit to) {
        QuantityLength converted = length.convertTo(to);
        System.out.println(length + " = " + converted);
    }

    // Equality demo
    public static void demonstrateLengthEquality(QuantityLength l1, QuantityLength l2) {
        System.out.println(l1 + " == " + l2 + " ? " + l1.equals(l2));
    }

    // ---------------- MAIN METHOD ----------------
    public static void main(String[] args) {

        // Basic conversions
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCHES, LengthUnit.YARDS);
        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
        demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCHES);

        System.out.println();

        // Using objects
        QuantityLength length1 = new QuantityLength(3.0, LengthUnit.YARDS);
        demonstrateLengthConversion(length1, LengthUnit.INCHES);

        QuantityLength length2 = new QuantityLength(36.0, LengthUnit.INCHES);
        demonstrateLengthConversion(length2, LengthUnit.YARDS);

        System.out.println();

        // Equality checks
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        demonstrateLengthEquality(a, b);

        QuantityLength c = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        QuantityLength d = new QuantityLength(1.0, LengthUnit.INCHES);
        demonstrateLengthEquality(c, d);
    }
}
