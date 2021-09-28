package dev.toma.gunsrpg.common.attribute;

public interface ICombinedAttribute {

    double value(IAttributeProvider provider);

    float floatValue(IAttributeProvider provider);

    int intValue(IAttributeProvider provider);

    static ICombinedAttribute of(IAttributeId... keys) {
        return new SimpleCombinedAttribute(keys);
    }

    class SimpleCombinedAttribute implements ICombinedAttribute {

        private final IAttributeId[] keys;

        private SimpleCombinedAttribute(IAttributeId[] keys) {
            this.keys = keys;
            if (keys.length < 2)
                throw new IllegalArgumentException("Cannot combine <2 attributes");
        }

        @Override
        public double value(IAttributeProvider provider) {
            double result = 1.0;
            for (IAttributeId id : keys) {
                result *= provider.getAttributeValue(id);
            }
            return result;
        }

        @Override
        public float floatValue(IAttributeProvider provider) {
            return (float) value(provider);
        }

        @Override
        public int intValue(IAttributeProvider provider) {
            double result = value(provider);
            return (int) Math.round(result);
        }
    }
}
