package com.xulai.betterparagliders.capabilities;

import net.minecraft.core.BlockPos;

public interface MiningDrainTracker {

    void markDrained(BlockPos pos);

    boolean isDrained(BlockPos pos);
}
