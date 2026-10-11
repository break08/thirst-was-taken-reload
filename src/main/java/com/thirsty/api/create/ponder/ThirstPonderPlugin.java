package com.thirsty.api.create.ponder;

import com.simibubi.create.foundation.ponder.PonderWorldBlockEntityFix;
import com.thirsty.ThirstWasTaken;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ThirstPonderPlugin implements PonderPlugin {
    @Override
    public @NotNull String getModId() {
        return ThirstWasTaken.MOD_ID;
    }

    @Override
    public void registerScenes(@NotNull PonderSceneRegistrationHelper<ResourceLocation> helper) {
        ThirstPonders.registerScenes(helper);
    }

    @Override
    public void registerTags(@NotNull PonderTagRegistrationHelper<ResourceLocation> helper) {
        ThirstPonders.registerTags(helper);
    }

    @Override
    public void onPonderLevelRestore(@NotNull PonderLevel ponderLevel) {
        PonderWorldBlockEntityFix.fixControllerBlockEntities(ponderLevel);
    }
}
