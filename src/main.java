public class QuantityMeasurementApp {

    // 🔹 Step 1: Enum with all units (base unit = FEET)
    enum LengthUnit {
        FEET(1.0),                         // base
        INCH(1.0 / 12.0),                  // 1 inch = 1/12 feet
        YARD(3.0),                         // 1 yard = 3 feet
        CENTIMETER(0.0328084);             // 1 cm ≈ 0.0328084 feet

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }
    }

    // 🔹 Step 2: Generic Quantity Class (same as UC3)
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
            if (this == obj) return true;                // Reflexive
            if (obj == null) return false;               // Null-safe
            if (!(obj instanceof QuantityLength)) return false;

            QuantityLength other = (QuantityLength) obj;

            // Compare after converting both to base unit (feet)
            return Double.compare(this.toFeet(), other.toFeet()) == 0;
        }
    }

    // 🔹 Main Method (Demo)
    public static void main(String[] args) {

        // 🔸 Basic equality
        System.out.println("1 ft vs 1 ft: " +
                new QuantityLength(1.0, LengthUnit.FEET)
                        .equals(new QuantityLength(1.0, LengthUnit.FEET)));

        System.out.println("1 inch vs 1 inch: " +
                new QuantityLength(1.0, LengthUnit.INCH)
                        .equals(new QuantityLength(1.0, LengthUnit.INCH)));

        // 🔸 Yard conversions
        System.out.println("1 yard vs 3 feet: " +
                new QuantityLength(1.0, LengthUnit.YARD)
                        .equals(new QuantityLength(3.0, LengthUnit.FEET)));

        System.out.println("1 yard vs 36 inches: " +
                new QuantityLength(1.0, LengthUnit.YARD)
                        .equals(new QuantityLength(36.0, LengthUnit.INCH)));

        // 🔸 Centimeter conversions
        System.out.println("1 cm vs 0.393701 inch: " +
                new QuantityLength(1.0, LengthUnit.CENTIMETER)
                        .equals(new QuantityLength(0.393701, LengthUnit.INCH)));

        // 🔸 Different values
        System.out.println("1 yard vs 2 feet: " +
                new QuantityLength(1.0, LengthUnit.YARD)
                        .equals(new QuantityLength(2.0, LengthUnit.FEET)));

        // 🔸 Transitive property
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARD);
        QuantityLength b = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength c = new QuantityLength(36.0, LengthUnit.INCH);

        System.out.println("Transitive (yard=feet & feet=inch): " +
                (a.equals(b) && b.equals(c) && a.equals(c)));

        // 🔸 Same reference
        System.out.println("Same reference: " + a.equals(a));

        // 🔸 Null comparison
        System.out.println("Null comparison: " + a.equals(null));
    }
}