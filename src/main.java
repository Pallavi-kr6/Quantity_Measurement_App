// =========================
// WeightUnit Enum (Standalone)
// =========================
enum WeightUnit {
    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double factor; // relative to kilogram

    WeightUnit(double factor) {
        this.factor = factor;
    }

    public double toBase(double value) {
        return value * factor; // convert to kg
    }

    public double fromBase(double baseValue) {
        return baseValue / factor; // convert from kg
    }
}

// =========================
// QuantityWeight Class
// =========================
class QuantityWeight {

    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {
        if (unit == null || !Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid input");
        }
        this.value = value;
        this.unit = unit;
    }

    // Convert to another unit
    public QuantityWeight convertTo(WeightUnit target) {
        if (target == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }

        double base = unit.toBase(value);
        double converted = target.fromBase(base);
        return new QuantityWeight(converted, target);
    }

    // Add (default → result in this.unit)
    public QuantityWeight add(QuantityWeight other) {
        return add(other, this.unit);
    }

    // Add with target unit
    public QuantityWeight add(QuantityWeight other, WeightUnit target) {
        if (other == null || target == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        double sumBase = this.unit.toBase(this.value)
                + other.unit.toBase(other.value);

        double result = target.fromBase(sumBase);
        return new QuantityWeight(result, target);
    }

    // Equality check (cross-unit)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityWeight)) return false;

        QuantityWeight other = (QuantityWeight) obj;

        double base1 = this.unit.toBase(this.value);
        double base2 = other.unit.toBase(other.value);

        return Math.abs(base1 - base2) < 1e-6;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(unit.toBase(value));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}

// =========================
// Main App (Testing UC9)
// =========================
public class main {
    public static void Main(String[] args) {

        // ===== Equality =====
        System.out.println("Equality Tests:");
        System.out.println(new QuantityWeight(1, WeightUnit.KILOGRAM)
                .equals(new QuantityWeight(1000, WeightUnit.GRAM))); // true

        System.out.println(new QuantityWeight(1, WeightUnit.KILOGRAM)
                .equals(new QuantityWeight(2.20462, WeightUnit.POUND))); // true approx

        // ===== Conversion =====
        System.out.println("\nConversion Tests:");
        System.out.println(new QuantityWeight(1, WeightUnit.KILOGRAM)
                .convertTo(WeightUnit.GRAM)); // 1000 g

        System.out.println(new QuantityWeight(500, WeightUnit.GRAM)
                .convertTo(WeightUnit.POUND)); // ~1.10231 lb

        // ===== Addition (default unit) =====
        System.out.println("\nAddition (default unit):");
        System.out.println(new QuantityWeight(1, WeightUnit.KILOGRAM)
                .add(new QuantityWeight(1000, WeightUnit.GRAM))); // 2 kg

        // ===== Addition (explicit target unit) =====
        System.out.println("\nAddition (target unit):");
        System.out.println(new QuantityWeight(1, WeightUnit.KILOGRAM)
                .add(new QuantityWeight(1000, WeightUnit.GRAM), WeightUnit.GRAM)); // 2000 g

        System.out.println(new QuantityWeight(2, WeightUnit.KILOGRAM)
                .add(new QuantityWeight(4, WeightUnit.POUND), WeightUnit.KILOGRAM)); // ~3.82 kg

        // ===== Edge Cases =====
        System.out.println("\nEdge Cases:");
        System.out.println(new QuantityWeight(0, WeightUnit.KILOGRAM)
                .convertTo(WeightUnit.GRAM)); // 0

        System.out.println(new QuantityWeight(-1, WeightUnit.KILOGRAM)
                .convertTo(WeightUnit.GRAM)); // -1000
    }
}
