package dev.toma.gunsrpg.common.attribute;

public interface ICombinedAttribute {

    double getValue(IAttributeProvider provider);

    int getIntValue(IAttributeProvider provider);

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
        public double getValue(IAttributeProvider provider) {
            double result = 1.0;
            for (IAttributeId id : keys) {
                result *= provider.getAttributeValue(id);
            }
            return result;
        }

        @Override
        public int getIntValue(IAttributeProvider provider) {
            double result = getValue(provider);
            return (int) Math.round(result);
        }
    }
}
