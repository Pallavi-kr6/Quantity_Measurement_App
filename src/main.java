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

        // ---------- UC5: CONVERSION ----------
        public QuantityLength convertTo(LengthUnit targetUnit) {
            if (targetUnit == null) {
                throw new IllegalArgumentException("Target unit cannot be null");
            }

            double base = value * unit.getFactor();
            double result = base / targetUnit.getFactor();

            return new QuantityLength(result, targetUnit);
        }

        private double toBaseUnit() {
            return value * unit.getFactor();
        }

        // ---------- UC6: ADD (default → first unit) ----------
        public QuantityLength add(QuantityLength other) {
            if (other == null) {
                throw new IllegalArgumentException("Other cannot be null");
            }

            double sumBase = this.toBaseUnit() + other.toBaseUnit();
            double result = sumBase / this.unit.getFactor();

            return new QuantityLength(result, this.unit);
        }

        // ---------- UC7: ADD WITH TARGET UNIT ----------
        public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
            if (other == null || targetUnit == null) {
                throw new IllegalArgumentException("Invalid input");
            }

            double sumBase = this.toBaseUnit() + other.toBaseUnit();
            double result = sumBase / targetUnit.getFactor();

            return new QuantityLength(result, targetUnit);
        }

        // ---------- OVERRIDDEN METHODS ----------
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

    // UC5
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (!Double.isFinite(value) || source == null || target == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        double base = value * source.getFactor();
        return base / target.getFactor();
    }

    // UC6
    public static QuantityLength add(QuantityLength l1, QuantityLength l2) {
        return l1.add(l2);
    }

    // UC7
    public static QuantityLength add(QuantityLength l1, QuantityLength l2, LengthUnit target) {
        return l1.add(l2, target);
    }

    // OVERLOADED (raw values)
    public static QuantityLength add(double v1, LengthUnit u1,
                                     double v2, LengthUnit u2,
                                     LengthUnit target) {

        if (!Double.isFinite(v1) || !Double.isFinite(v2) ||
            u1 == null || u2 == null || target == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        double sumBase = (v1 * u1.getFactor()) + (v2 * u2.getFactor());
        double result = sumBase / target.getFactor();

        return new QuantityLength(result, target);
    }

    // ---------------- DEMO ----------------
    public static void main(String[] args) {

        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        // UC7 Examples
        System.out.println(add(a, b, LengthUnit.FEET));     // 2 FEET
        System.out.println(add(a, b, LengthUnit.INCHES));   // 24 INCHES
        System.out.println(add(a, b, LengthUnit.YARDS));    // ~0.667 YARDS

        // More cases
        System.out.println(add(
            new QuantityLength(36.0, LengthUnit.INCHES),
            new QuantityLength(1.0, LengthUnit.YARDS),
            LengthUnit.FEET)); // 6 FEET

        System.out.println(add(
            new QuantityLength(2.54, LengthUnit.CENTIMETERS),
            new QuantityLength(1.0, LengthUnit.INCHES),
            LengthUnit.CENTIMETERS)); // ~5.08 CM

        // Edge cases
        System.out.println(add(
            new QuantityLength(5.0, LengthUnit.FEET),
            new QuantityLength(0.0, LengthUnit.INCHES),
            LengthUnit.YARDS)); // ~1.667 YARDS
    }
}
