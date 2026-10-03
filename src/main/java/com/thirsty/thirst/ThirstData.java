package com.thirsty.thirst;

import com.thirsty.api.config.CommonConfig;
import com.thirsty.misc.ModDamageSource;
import com.thirsty.misc.ThirstHelper;
import com.thirsty.purity.WaterPurity;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.thirsty.api.cca.PlayerThirst.PLAYER_THIRST;


public class ThirstData implements Component, AutoSyncedComponent, ServerTickingComponent {
    public final Player provider;
    public int thirst = 20;
    public int quenched = 5;
    public float exhaustion = 0;
    public int damageTimer = 0;
    public int syncTimer = 0;
    public float prevTickExhaustion = 0.0F;
    public boolean justHealed = false;
    private boolean shouldTickThirst = true;
    public boolean exhaustionRecalculate = false;
    public boolean init = true;

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
        exhaustion = tag.getFloat("exhaustion");
        damageTimer = tag.getInt("damageTimer");
        syncTimer = tag.getInt("syncTimer");
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
        PLAYER_THIRST.sync(this.provider);
    }

    public int getQuenched() {
        return quenched;
    }

    public void setQuenched(int quenched) {
        this.quenched = quenched;
        PLAYER_THIRST.sync(this.provider);
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

    // Logic

    public static boolean checkTombstoneEffects = false;
    public static boolean checkFDEffects = false;
    public static boolean checkLetsDoBakeryEffects = false;
    public static boolean checkLetsDoBreweryEffects = false;
    public static void drink(ItemStack item, Player player) {
        if(ThirstHelper.itemRestoresThirst(item) && WaterPurity.givePurityEffects(player, item)) {
            drink(player, ThirstHelper.getThirst(item), ThirstHelper.getQuenched(item));
        }
    }

    public static void drink(Player player, int thirst, int quenched)
    {
        ThirstData thirstData = PLAYER_THIRST.get(player);
        int extra_quenched = Math.max(thirstData.getThirst() + thirst - 20, 0);
        if(!AutoConfig.getConfigHolder(CommonConfig.class).getConfig().EXTRA_HYDRATION_CONVERT_TO_QUENCHED)
            extra_quenched = 0;
        thirstData.setThirst(Math.min(thirstData.getThirst() + thirst, 20));
        thirstData.setQuenched(Math.min(thirstData.getQuenched() + quenched + extra_quenched, thirstData.getThirst()));
        PLAYER_THIRST.sync(player);
    }

    @Override
    public void serverTick()
    {
        Player player = this.provider;
        Difficulty difficulty = player.level().getDifficulty();

        if(player.getAbilities().invulnerable)
            return;

        if(!isShouldTickThirst()) {
            if (init) {
                init = false;
                setInit(init);
            }
            return;
        }

        if(checkTombstoneEffects && player.getActiveEffects().stream().anyMatch(e -> e.getDescriptionId().contains("ghostly_shape")))
            return;

        //&& player.hasEffect(ModEffects.NOURISHMENT.get())
        boolean isNourished = checkFDEffects;

        boolean isHunger = player.hasEffect(MobEffects.HUNGER);
        boolean isStuffed = checkLetsDoBakeryEffects &&
                player.getActiveEffects().stream().anyMatch(e -> e.getDescriptionId().contains("stuffed"));
        boolean isSaturated = checkLetsDoBreweryEffects &&
                player.getActiveEffects().stream().anyMatch(e -> e.getDescriptionId().contains("saturated"));
        boolean isSitting = player.isPassenger();

        // CommonConfig.DEPLETES_WHEN_NAUSEA.get() &&
        if(player.getActiveEffects().stream().anyMatch(e->e.getEffect().equals(MobEffects.CONFUSION))){
            addExhaustion(player,0.06F);
        }

        if(isHunger){
            setExhaustion(exhaustion - (0.005F * (float)(player.getEffect(MobEffects.HUNGER).getAmplifier() + 1) *
                    ThirstHelper.getExhaustionBiomeModifier(player) *
                    ThirstHelper.getExhaustionFireProtModifier(player)*
                    ThirstHelper.getExhaustionFireResistanceModifier(player)));
        }

        if (!isSitting && !isNourished && !isStuffed && !isSaturated)
        {
            updateExhaustion(player);
        }

        if (isInit()) {
            setPrevTickExhaustion(player.getFoodData().getExhaustionLevel());
            setInit(false);
        }


        if (exhaustion > 4)
        {
            exhaustion -= 4;
            if (quenched > 0)
            {
                setQuenched(quenched-1);
            }
            // || CommonConfig.THIRST_DEPLETION_IN_PEACEFUL.get()
            else if (difficulty != Difficulty.PEACEFUL)
            {
                setThirst(Math.max(thirst - 1, 0));
            }
        }

        ++syncTimer;
        if(syncTimer > 10)
        {
            //&& !CommonConfig.THIRST_DEPLETION_IN_PEACEFUL.get()
            if(difficulty == Difficulty.PEACEFUL){
                setThirst(Math.min(thirst + 1,20));
            }

            final float angle = Mth.wrapDegrees(player.getXRot());

            //&& CommonConfig.CAN_DRINK_RAIN_WATETR.get()
            if (angle <= -80  && player.level().isRainingAt(player.blockPosition().above()))
            {
                thirst = Math.min(thirst + 1,20);
                quenched = Math.min(quenched +1,20);
                PLAYER_THIRST.sync(player);
            }

            PLAYER_THIRST.sync(player);
            syncTimer = 0;
        }

        if (thirst <= 0)
        {
            ++damageTimer;
            if (damageTimer >= 40)
            {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 0 && difficulty == Difficulty.NORMAL)
                {
                    player.hurt(ModDamageSource.getDamageSource(player.level(),ModDamageSource.DIE_OF_THIRST_KEY), 1.0F);
                    PLAYER_THIRST.sync(player);
                }

                PLAYER_THIRST.sync(player);
                damageTimer = 0;
            }
        }
    }

    public void updateExhaustion(Player player)
    {
        float prev = getPrevTickExhaustion();
        float current = player.getFoodData().getExhaustionLevel();

        float delta = current < prev ? current + 4.0F - prev : current - prev;

        if (delta > 0 && delta < 4.0F) {
            addExhaustion(player, delta);
        }
        setPrevTickExhaustion(current);
    }

    public void addExhaustion(Player player, float amount)
    {
        float exhaustion = getExhaustion();
        boolean justHealed = isJustHealed();
        //!CommonConfig.HEALTH_REGEN_DEPLETES_HYDRATION.get() &&
        if(justHealed)
            amount = 0;


        //!CommonConfig.HEALTH_REGEN_DEHYDRATION_IS_BIOME_DEPENDENT.get() &&
        if(justHealed)
            exhaustion += amount;
        else
            exhaustion += (amount *
                    ThirstHelper.getExhaustionBiomeModifier(player) *
                    ThirstHelper.getExhaustionFireProtModifier(player)*
                    ThirstHelper.getExhaustionFireResistanceModifier(player)
            );

        if(justHealed)
            justHealed = false;
        setExhaustion(exhaustion);
        setJustHealed(justHealed);
    }
}
