package com.thirsty.thirst;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;


public class ThirstData implements Component, AutoSyncedComponent {
    private final Player provider;
    private int thirst = 20;
    private int quenched = 5;
    private float exhaustion = 0;
    private int damageTimer = 0;
    private int syncTimer = 0;
    private float prevTickExhaustion = 0.0F;
    private boolean justHealed = false;
    private boolean shouldTickThirst = true;
    private boolean exhaustionRecalculate = false;
    private boolean init = true;

    public ThirstData(Player player) {
        this.provider = player;

    }
    /**
     * Reads this component's properties from a {@link CompoundTag}.
     *
     * @param tag a {@code NbtCompound} on which this component's serializable data has been written
     * @implNote implementations should not assert that the data written on the tag corresponds to any
     * specific scheme, as saved data is susceptible to external tempering, and may come from an earlier
     * version.
     */
    @Override
    public void readFromNbt(CompoundTag tag) {
        thirst = tag.getInt("thirst");
        quenched = tag.getInt("quenched");
        exhaustion = tag.getInt("exhaustion");
        damageTimer = tag.getInt("damage_timer");
        syncTimer = tag.getInt("sync_timer");
        prevTickExhaustion = tag.getFloat("prevTickExhaustion");
        justHealed = tag.getBoolean("justHealed");
        shouldTickThirst = tag.getBoolean("shouldTickThirst");
        exhaustionRecalculate = tag.getBoolean("exhaustionRecalculate");
        init = tag.getBoolean("init");
    }

    /**
     * Writes this component's properties to a {@link CompoundTag}.
     *
     * @param tag a {@code NbtCompound} on which to write this component's serializable data
     */
    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putInt("thirst", thirst);
        tag.putInt("quenched", quenched);
        tag.putFloat("exhaustion", exhaustion);
        tag.putInt("damageTimer", damageTimer);
        tag.putInt("syncTimer", syncTimer);
        tag.putFloat("prevTickExhaustion", prevTickExhaustion);
        tag.putBoolean("justHealed", justHealed);
        tag.putBoolean("shouldTickThirst", shouldTickThirst);
        tag.putBoolean("exhaustionRecalculate", exhaustionRecalculate);
        tag.putBoolean("init", init);
    }

    public float getPrevTickExhaustion() {
        return prevTickExhaustion;
    }

    public void setPrevTickExhaustion(float prevTickExhaustion) {
        this.prevTickExhaustion = prevTickExhaustion;
    }

    public boolean isJustHealed() {
        return justHealed;
    }

    public void setJustHealed(boolean justHealed) {
        this.justHealed = justHealed;
    }

    public boolean isShouldTickThirst() {
        return shouldTickThirst;
    }

    public void setShouldTickThirst(boolean shouldTickThirst) {
        this.shouldTickThirst = shouldTickThirst;
    }

    public boolean isExhaustionRecalculate() {
        return exhaustionRecalculate;
    }

    public void setExhaustionRecalculate(boolean exhaustionRecalculate) {
        this.exhaustionRecalculate = exhaustionRecalculate;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
    public int getThirst() {
        return thirst;
    }

    public void setThirst(int thirst) {
        this.thirst = thirst;
    }

    public int getQuenched() {
        return quenched;
    }

    public void setQuenched(int quenched) {
        this.quenched = quenched;
    }

    public float getExhaustion() {
        return exhaustion;
    }

    public void setExhaustion(float exhaustion) {
        this.exhaustion = exhaustion;
    }

    public int getDamageTimer() {
        return damageTimer;
    }

    public void setDamageTimer(int damageTimer) {
        this.damageTimer = damageTimer;
    }

    public int getSyncTimer() {
        return syncTimer;
    }

    public void setSyncTimer(int syncTimer) {
        this.syncTimer = syncTimer;
    }
}
