public class QuantityMeasurementApp {

    // 🔹 Step 1: Enum for Units (base unit = feet)
    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }
    }

    // 🔹 Step 2: Generic Quantity Class
    static class QuantityLength {
        private double value;
        private LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (Double.isNaN(value)) {
                throw new IllegalArgumentException("Invalid numeric value");
            }
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        public double toFeet() {
            return unit.toFeet(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;              // Same reference
            if (obj == null) return false;             // Null check
            if (!(obj instanceof QuantityLength)) return false; // Type check

            QuantityLength other = (QuantityLength) obj;

            // Convert both to same base unit (feet)
            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }
    }

    // 🔹 Main Method (Demo)
    public static void main(String[] args) {

        // Same unit equality
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(1.0, LengthUnit.FEET);
        System.out.println("1 ft vs 1 ft: " + q1.equals(q2));

        // Inch equality
        QuantityLength q3 = new QuantityLength(1.0, LengthUnit.INCH);
        QuantityLength q4 = new QuantityLength(1.0, LengthUnit.INCH);
        System.out.println("1 inch vs 1 inch: " + q3.equals(q4));

        // Cross-unit equality
        QuantityLength q5 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q6 = new QuantityLength(12.0, LengthUnit.INCH);
        System.out.println("1 ft vs 12 inch: " + q5.equals(q6));

        // Different values
        QuantityLength q7 = new QuantityLength(2.0, LengthUnit.FEET);
        System.out.println("1 ft vs 2 ft: " + q5.equals(q7));

        // Same reference
        System.out.println("Same reference: " + q5.equals(q5));

        // Null comparison
        System.out.println("Null comparison: " + q5.equals(null));
    }
}