package com.thirsty.purity;

import com.thirsty.config.CommonConfig;
import com.thirsty.item.ItemInit;
import com.thirsty.misc.MathHelper;
import com.thirsty.misc.ReflectionUtil;
import com.thirsty.misc.TickHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import com.thirsty.misc.ThirstHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class WaterPurity {
    private static final List<ContainerWithPurity> waterContainers = new ArrayList<>();
    private static final List<Block> fillablesWithPurity = new ArrayList<>();
    public static final int MIN_PURITY = 0;
    public static final int MAX_PURITY = 3;

    /**
     * Specifies the purity of a block filled with water. Has to be incremented by one
     * number because while using Mixins, generally every block that
     * implements water purity has a mixin-able "createBlockStateDefinition" function,
     * but doesn't have an as-accessible "setDefaultState" function. Thus, I am forced to
     * use 0 as the "null" value for the block purity.
     * <br><br>
     * On the bright side, there is a function in this class which takes in a BlockState and
     * returns the already-modified purity
     * */
    public static final IntegerProperty BLOCK_PURITY = IntegerProperty.create("purity", 0, 4);

    public static boolean tanLoaded = false;

    public static void init() throws NoSuchMethodException {
        registerDispenserBehaviours();
        registerContainers();
        registerFillables();

        if(FabricLoader.getInstance().isModLoaded("toughasnails"))
        {
            // registerToughAsNailsContainers();
            tanLoaded = true;
        }
    }

    private static void registerContainers()
    {
        waterContainers.add(new ContainerWithPurity(new ItemStack(Items.GLASS_BOTTLE),
                PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)).setEqualsFilled(itemStack ->
                itemStack.is(Items.POTION) && PotionUtils.getPotion(itemStack) == Potions.WATER));
        waterContainers.add(new ContainerWithPurity(new ItemStack(ItemInit.TERRACOTTA_BOWL),
                new ItemStack(ItemInit.TERRACOTTA_WATER_BOWL)));
        waterContainers.add(new ContainerWithPurity(new ItemStack(Items.BUCKET),
                new ItemStack(Items.WATER_BUCKET), false).canHarvestRunningWater(false));
    }

    private static void registerFillables()
    {
        fillablesWithPurity.add(Blocks.CAULDRON);
        fillablesWithPurity.add(Blocks.WATER_CAULDRON);
    }



    static void fillablesHandler()
    {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ItemStack itemUse = player.getItemInHand(hand);
            if (player instanceof ServerPlayer && isWaterFilledContainer(itemUse)) {
                BlockPos pos = hitResult.getBlockPos();
                BlockState blockState = world.getBlockState(pos);

                if (isFillableBlock(blockState)) {
                    int purity = getPurity(itemUse);

                    int blockPurity = !blockState.hasProperty(BLOCK_PURITY) ?
                            3 : (blockState.getValue(BLOCK_PURITY) - 1 < 0 ?
                                 3 : blockState.getValue(BLOCK_PURITY) - 1);

                    TickHelper.nextTick(world, () -> {
                        BlockState blockState1 = world.getBlockState(pos);

                        if (!blockState1.hasProperty(BLOCK_PURITY))
                            return;

                        world.setBlock(
                                pos,
                                blockState1.setValue(BLOCK_PURITY, Math.min(purity, blockPurity) + 1),
                                0
                        );
                    });
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.PASS;
        });
    }

    @Deprecated
    @SuppressWarnings("unused")
    public static void addContainer(ContainerWithPurity container)
    {
        waterContainers.add(container);
    }

    /**
     * Returns the filled equivalent of the water container given in input.
     * The second parameter specifies if the container inputted is the empty or
     * filled version
     */
    @SuppressWarnings("unused")
    public static ItemStack getFilledContainer(ItemStack container, boolean fromFilled)
    {
        for (ContainerWithPurity waterContainer : waterContainers)
            if ((!fromFilled && waterContainer.equalsEmpty(container)) || (fromFilled && waterContainer.equalsFilled(container)))
                return waterContainer.getFilledItem().copy();

        return ItemStack.EMPTY.copy();
    }

    /**
     * Gives the ability to certain water containers to pick up water from
     * non-source blocks
     */

    static void harvestRunningWater()
    {
        UseItemCallback.EVENT.register((Player player, Level world, InteractionHand hand)->{
            ItemStack item = player.getItemInHand(hand);
            if (player == null)
                return InteractionResultHolder.success(item);

            if (!canHarvestRunningWater(item))
                return InteractionResultHolder.success(item);

            BlockPos blockPos = MathHelper.getPlayerPOVHitResult(world, player, ClipContext.Fluid.ANY).getBlockPos();

            if (world.getFluidState(blockPos).is(FluidTags.WATER))
                return InteractionResultHolder.success(item);

            SoundEvent sound;
            ItemStack filledItem;

            if(item.getItem() == Items.GLASS_BOTTLE && world.getFluidState(blockPos).isSource())
            {
                sound = SoundEvents.BOTTLE_FILL;
                filledItem = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
            }
            else if(item.getItem() == ItemInit.TERRACOTTA_BOWL)
            {
                sound = SoundEvents.BUCKET_FILL;
                filledItem = new ItemStack(ItemInit.TERRACOTTA_WATER_BOWL);
            }
            else
                return InteractionResultHolder.success(item);

            world.playSound(player, player.getX(), player.getY(), player.getZ(), sound, SoundSource.NEUTRAL, 1.0F, 1.0F);
            world.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);

            CompoundTag tag = filledItem.getOrCreateTag();
            tag.putInt("Purity", getBlockPurity(world, blockPos));

            ItemStack result = ItemUtils.createFilledResult(item, player, filledItem);

            player.setItemInHand(hand, result);
            return InteractionResultHolder.success(result);
        });

    }

    /**
     * Renders the client-side tooltip for items that have a water
     * purity tag
     */

    public static boolean isWaterFilledContainer(ItemStack item)
    {
        for (ContainerWithPurity waterContainer : waterContainers)
            if (waterContainer.equalsFilled(item))
                return true;

        return false;
    }

    public static boolean isEmptyWaterContainer(ItemStack item)
    {
        for (ContainerWithPurity waterContainer : waterContainers)
            if (waterContainer.equalsEmpty(item))
                return true;

        return false;
    }

    static boolean isFillableBlock(Block block)
    {
        for (Block fillable : fillablesWithPurity)
        {
            if (fillable == block)
                return  true;
        }

        return false;
    }

    static boolean isFillableBlock(BlockState blockState)
    {
        return isFillableBlock(blockState.getBlock());
    }

    static boolean canHarvestRunningWater(ItemStack item)
    {
        for (ContainerWithPurity waterContainer : waterContainers)
            if (waterContainer.equalsEmpty(item) && waterContainer.canHarvestRunningWater())
                return true;

        return false;
    }

    /**
     * Reads the purity from an item
     */
    public static int getPurity(ItemStack item)
    {
        if(!item.getOrCreateTag().contains("Purity"))
        {

            return AutoConfig.getConfigHolder(CommonConfig.class).getConfig().DEFAULT_PURITY;
        }

        return Objects.requireNonNull(item.getTag()).getInt("Purity");
    }

    /**
     * Reads the purity from a fluid
     */
    public static int getPurity(FluidVariant fluid)
    {
        if(!fluid.copyOrCreateNbt().contains("Purity"))
            return AutoConfig.getConfigHolder(CommonConfig.class).getConfig().DEFAULT_PURITY;

        return fluid.getNbt().getInt("Purity");
    }

    /**
     * Returns the purity string in the language selected by the player
     */
    public static String getPurityText(int purity)
    {
        if(purity==-1) return null;
        String purityText = purity == 0 ? "dirty" :
                purity == 1 ? "slightly_dirty" :
                purity == 2 ? "acceptable" : "purified";

        return MutableComponent.create(new TranslatableContents("thirst.purity." + purityText,purityText,TranslatableContents.NO_ARGS)).getString();
    }

    /**
     * Returns the purity color in decimal format
     */
    public static int getPurityColor(int purity)
    {
        return purity == 0 ? 11028517 :
                purity == 1 ? 7957617 :
                purity == 2 ? 6128285 : 2208255;
    }

    /**
     * Returns the already-adjusted water purity level of a
     * block with the BLOCK_PURITY tag
     */
    public static int getBlockPurity(BlockState blockState)
    {
        return blockState.hasProperty(BLOCK_PURITY) ? blockState.getValue(BLOCK_PURITY) - 1 : -1;
    }

    public static boolean hasPurity(ItemStack item)
    {
        if(!item.hasTag())
            return false;
        else {
            assert item.getTag() != null;
            return item.getTag().contains("Purity");
        }
    }

    public static boolean hasPurity(FluidVariant fluid)
    {
        if(!fluid.hasNbt())
            return false;
        else
            return fluid.getNbt().contains("Purity");
    }

    /**
     * Shorthand for adding purity to an item if in a context where the block
     * the player is pointing at is accessible
     */
    public static ItemStack addPurity(ItemStack item, BlockPos pos, Level level)
    {
        CompoundTag tag = item.getOrCreateTag();
        tag.putInt("Purity", getBlockPurity(level, pos));

        return  item;
    }


    /**
     * Adds the "Purity" tag to an item
     */
    public static ItemStack addPurity(ItemStack item, int purity)
    {
        CompoundTag tag = item.getOrCreateTag();
        if(purity== AutoConfig.getConfigHolder(CommonConfig.class).getConfig().DEFAULT_PURITY)
            tag.remove("Purity");
        else
            tag.putInt("Purity", purity);

        return item;
    }

    /**
     * Adds the "Purity" tag to a fluid
     */
    public static FluidVariant addPurity(FluidVariant fluid, int purity)
    {
        CompoundTag tag = fluid.copyOrCreateNbt();

        if(purity==AutoConfig.getConfigHolder(CommonConfig.class).getConfig().DEFAULT_PURITY)
            tag.remove("Purity");
        else
            tag.putInt("Purity", purity);

        return fluid;
    }


    /**
     * Calculates the water purity of a specific block in the level
     */
    public static int getBlockPurity(Level level, BlockPos pos)
    {
        CommonConfig config = AutoConfig.getConfigHolder(CommonConfig.class).getConfig();
        int purity = (pos.getY() > config.MOUNTAINS_Y || pos.getY() < config.CAVES_Y)
                && pos.getY() < config.MOUNTAINS_Y - 32 ? 1 : 0;

        if(level.getFluidState(pos).is(FluidTags.WATER))
        {
            if(!level.getFluidState(pos).isSource())
                purity = Math.min(purity + config.RUNNING_WATER_PURIFICATION_AMOUNT, MAX_PURITY);

            return purity;
        }
        else if(level.getBlockState(pos).is(Blocks.WATER_CAULDRON))
        {
            return level.getBlockState(pos).getValue(BLOCK_PURITY) - 1;
        }
        else
            return AutoConfig.getConfigHolder(CommonConfig.class).getConfig().DEFAULT_PURITY;
    }

    /**
     * Gives the player effects based on the purity of the water just drunk
     * and returns whether thirst and quenched should be added or not
     */
    public static boolean givePurityEffects(Player player, ItemStack item)
    {
        if(!isWaterFilledContainer(item)) return true;
        if(!hasPurity(item)) return true;
        return givePurityEffects(player, ThirstHelper.getPurity(item));
    }

    /**
     * Calculates purity-derived effects
     */
    public static boolean givePurityEffects(Player player, int purity)
    {
        boolean shouldRegenerate = true;
        Random random = new Random();
        float chance = random.nextFloat();

        CommonConfig config = AutoConfig.getConfigHolder(CommonConfig.class).getConfig();

        switch (purity) {
            case 0 -> {
                if (chance < config.DIRTY_NAUSEA_PERCENTAGE / 100.0f) {
                    if(player instanceof ServerPlayer)
                    {
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 5, 0));
                        player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * 30, 0));
                    }

                }

                if (chance <= config.DIRTY_POISON_PERCENTAGE / 100.0f) {
                    if(player instanceof ServerPlayer)
                    {
                        player.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 10, 0));
                    }
                    shouldRegenerate = false;
                }

            }
            case 1 -> {
                if (chance < config.SLIGHTLY_DIRTY_NAUSEA_PERCENTAGE / 100.0f) {
                    if(player instanceof ServerPlayer)
                    {
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 5, 0));
                        player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * 30, 0));
                    }

                }

                if (chance <= config.SLIGHTLY_DIRTY_POISON_PERCENTAGE / 100.0f) {
                    if(player instanceof ServerPlayer)
                    {
                        player.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 10, 0));
                    }
                    shouldRegenerate = false;
                }

            }
            case 2 -> {
                if (chance < config.ACCEPTABLE_NAUSEA_PERCENTAGE / 100.0f) {
                    if(player instanceof ServerPlayer)
                    {
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 5, 0));
                        player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * 30, 0));
                    }

                }

                if (chance <= config.ACCEPTABLE_POISON_PERCENTAGE / 100.0f) {
                    if(player instanceof ServerPlayer)
                    {
                        player.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 10, 0));
                    }
                    shouldRegenerate = false;
                }

            }
            case 3 -> {
                if (chance < config.PURIFIED_NAUSEA_PERCENTAGE / 100.0f) {
                    if(player instanceof ServerPlayer)
                    {
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 20 * 5, 0));
                        player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 20 * 30, 0));
                    }

                }

                if (chance <= config.PURIFIED_POISON_PERCENTAGE / 100.0f) {
                    if(player instanceof ServerPlayer)
                    {
                        player.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 10, 0));
                    }
                    shouldRegenerate = false;
                }

            }
        }

        return shouldRegenerate || config.QUENCH_THIRST_WHEN_DEBUFFED;
    }

    static void registerDispenserBehaviours() throws NoSuchMethodException {
        //mappings (the default is getDispenseMethod)
        Method getDispenseMethod_ = DispenserBlock.class.getDeclaredMethod("getDispenseMethod", ItemStack.class);
        getDispenseMethod_.setAccessible(true);

        DispenseItemBehavior bucketDefaultBehaviour = (DispenseItemBehavior) ReflectionUtil.fuckYouReflections(getDispenseMethod_, Blocks.DISPENSER, new ItemStack(Items.BUCKET));
        DispenseItemBehavior bottleDefaultBehaviour = (DispenseItemBehavior) ReflectionUtil.fuckYouReflections(getDispenseMethod_, Blocks.DISPENSER, new ItemStack(Items.GLASS_BOTTLE));

        //mappings
        Method execute = DefaultDispenseItemBehavior.class.getDeclaredMethod("execute", BlockSource.class, ItemStack.class);
        execute.setAccessible(true);

        DispenserBlock.registerBehavior(Items.BUCKET, (block, item) ->
        {
            Level level = block.getLevel();
            BlockPos blockpos = block.getPos().relative(block.getBlockState().getValue(DispenserBlock.FACING));
            if(level.getFluidState(blockpos).is(FluidTags.WATER) && level.getBlockState(blockpos).getFluidState().isSource())
            {
                ItemStack result = new ItemStack(Items.WATER_BUCKET);
                return getStack(block, item, level, blockpos, result,true);
            }
            else
                return (ItemStack) ReflectionUtil.fuckYouReflections(execute, bucketDefaultBehaviour, block, item);

        });

        DispenserBlock.registerBehavior(Items.GLASS_BOTTLE, (block, item) ->
        {
            Level level = block.getLevel();
            BlockPos blockpos = block.getPos().relative(block.getBlockState().getValue(DispenserBlock.FACING));

            if(level.getFluidState(blockpos).is(FluidTags.WATER))
            {
                ItemStack result = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                return getStack(block, item, level, blockpos, result,false);
            }
            else
                return (ItemStack) ReflectionUtil.fuckYouReflections(execute, bottleDefaultBehaviour, block, item);
        });
    }

    @NotNull
    private static ItemStack getStack(BlockSource block, ItemStack item, Level level, BlockPos blockpos, ItemStack result, boolean pickupBlock) {
        level.gameEvent(null, GameEvent.FLUID_PICKUP, blockpos);
        addPurity(result, blockpos, level);

        if(pickupBlock)
            ((BucketPickup)level.getBlockState(blockpos).getBlock()).pickupBlock(level, blockpos, level.getBlockState(blockpos));

        item.shrink(1);
        if (item.isEmpty()) {
            return result;
        } else
        {
            if (block.<DispenserBlockEntity>getEntity().addItem(result) < 0)
            {
                new DefaultDispenseItemBehavior().dispense(block, result);
            }

            return item;
        }
    }

    public static boolean matchRecipe(FluidVariant stack, FluidVariant other) {
        return (stack.getNbt() == null || stack.getNbt().isEmpty()) ?
                (other.getNbt() == null || other.getNbt().isEmpty()) : other.getNbt() != null && stack.getNbt().equals(other.getNbt());
    }

    public static void eventInit(){
        fillablesHandler();
        harvestRunningWater();
    }
}
