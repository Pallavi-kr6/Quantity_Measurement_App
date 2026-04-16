public class QuantityMeasurementApp {

    // 🔹 Feet Class
    static class Feet {
        private double value;

        public Feet(double value) {
            if (Double.isNaN(value)) {
                throw new IllegalArgumentException("Invalid input");
            }
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public double toInches() {
            return value * 12;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;

            if (obj instanceof Feet) {
                Feet other = (Feet) obj;
                return Double.compare(this.value, other.value) == 0;
            }

            if (obj instanceof Inches) {
                Inches other = (Inches) obj;
                return Double.compare(this.toInches(), other.getValue()) == 0;
            }

            return false;
        }
    }

    // 🔹 Inches Class
    static class Inches {
        private double value;

        public Inches(double value) {
            if (Double.isNaN(value)) {
                throw new IllegalArgumentException("Invalid input");
            }
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public double toFeet() {
            return value / 12;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;

            if (obj instanceof Inches) {
                Inches other = (Inches) obj;
                return Double.compare(this.value, other.value) == 0;
            }

            if (obj instanceof Feet) {
                Feet other = (Feet) obj;
                return Double.compare(this.value, other.toInches()) == 0;
            }

            return false;
        }
    }

    // 🔹 Static methods (as required in UC2)
    public static boolean checkFeetEquality(double a, double b) {
        Feet f1 = new Feet(a);
        Feet f2 = new Feet(b);
        return f1.equals(f2);
    }

    public static boolean checkInchesEquality(double a, double b) {
        Inches i1 = new Inches(a);
        Inches i2 = new Inches(b);
        return i1.equals(i2);
    }

    // 🔹 Main Method
    public static void main(String[] args) {

        // Same unit comparisons
        System.out.println("1.0 inch vs 1.0 inch: " + checkInchesEquality(1.0, 1.0));
        System.out.println("1.0 ft vs 1.0 ft: " + checkFeetEquality(1.0, 1.0));

        // Different values
        System.out.println("1.0 ft vs 2.0 ft: " + checkFeetEquality(1.0, 2.0));

        // Cross-unit comparison
        Feet f = new Feet(1.0);
        Inches i = new Inches(12.0);
        System.out.println("1 ft vs 12 inches: " + f.equals(i));

        // Edge cases
        Feet fRef = new Feet(1.0);
        System.out.println("Same reference: " + fRef.equals(fRef));
        System.out.println("Null comparison: " + fRef.equals(null));
    }
}