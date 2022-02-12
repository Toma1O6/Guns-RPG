package dev.toma.gunsrpg.resource.progression;

import dev.toma.gunsrpg.resource.util.functions.IFunction;

public class KillRequirementProvider implements IKillRequirementProvider {

    private final ProviderEntry[] entries;

    public KillRequirementProvider(ProviderEntry[] entries) {
        this.entries = entries;
    }

    @Override
    public int getRequiredKillCount(int level) {
        for (ProviderEntry entry : entries) {
            if (entry.function.canApplyFor(level)) {
                return entry.requirement;
            }
        }
        return Integer.MAX_VALUE;
    }

    public static class ProviderEntry {
        private final IFunction function;
        private final int requirement;

        public ProviderEntry(IFunction function, int requirement) {
            this.function = function;
            this.requirement = requirement;
        }
    }
}
