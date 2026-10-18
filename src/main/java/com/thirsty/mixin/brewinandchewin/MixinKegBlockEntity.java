package com.thirsty.mixin.brewinandchewin;

import com.llamalad7.mixinextras.sugar.Local;
import com.thirsty.api.config.CommonConfig;
import com.thirsty.misc.NbtHelper;
import com.thirsty.purity.WaterPurity;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.component.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.crafting.KegPouringRecipe;
import umpaz.brewinandchewin.common.registry.BnCRecipeTypes;
import umpaz.brewinandchewin.common.utility.AbstractedFluidStack;
import umpaz.brewinandchewin.common.utility.KegRecipeWrapper;

import java.util.List;
import java.util.Optional;

@Mixin(value = KegBlockEntity.class,remap = false)
public class MixinKegBlockEntity {
    @Unique
    CommonConfig config = AutoConfig.getConfigHolder(CommonConfig.class).getConfig();

    @Unique
    private int thirst$purityOut = 0;

    @Unique
    private static DataComponentPatch thirst$toPatch(DataComponentMap map) {
        DataComponentPatch.Builder builder = DataComponentPatch.builder();
        for (TypedDataComponent<?> c : map) thirst$put(builder, c);
        return builder.build();
    }

    @Unique
    private static <T> void thirst$put(DataComponentPatch.Builder builder, TypedDataComponent<T> c) {
        builder.set(c.type(), c.value());
    }

    @Unique
    private static DataComponentMap thirst$toMap(DataComponentPatch patch) {
        PatchedDataComponentMap map = new PatchedDataComponentMap(DataComponentMap.EMPTY);
        map.applyPatch(patch);
        return map;
    }

    @Unique
    private static FluidVariant thirst$toVariant(AbstractedFluidStack stack) {
        return FluidVariant.of(stack.fluid(), thirst$toPatch(stack.components()));
    }

    @Inject(method = "processFermenting",
            at = @At(value = "INVOKE",
                    target = "Lumpaz/brewinandchewin/common/container/AbstractedFluidTank;drain(JLumpaz/brewinandchewin/common/utility/FluidUnit;Z)Lumpaz/brewinandchewin/common/utility/AbstractedFluidStack;"))

    private void thirst$capturePurity(KegFermentingRecipe recipe, KegBlockEntity keg,
                                      CallbackInfoReturnable<Boolean> cir) {
        this.thirst$purityOut = WaterPurity.getPurity(thirst$toVariant(keg.getFluidTank().getAbstractedFluid()));
    }

    @ModifyArg(method = "processFermenting",
            at = @At(value = "INVOKE",
                    target = "Lumpaz/brewinandchewin/common/container/AbstractedFluidTank;setAbstractedFluid(Lumpaz/brewinandchewin/common/utility/AbstractedFluidStack;)V"),
            index = 0)

    private AbstractedFluidStack thirst$purityFluidResult(AbstractedFluidStack result,
                                                          @Local(argsOnly = true, name = "keg") KegBlockEntity keg,
                                                          @Local(argsOnly = true, name = "recipe") KegFermentingRecipe recipe) {
        int purity = WaterPurity.getPurity(thirst$toVariant(keg.getFluidTank().getAbstractedFluid()));

        purity = purity < config.FERMENTATION_MOLDING_THRESHOLD ?
                Math.max(purity - config.FERMENTATION_MOLDING_HARSHNESS, WaterPurity.MIN_PURITY) : purity;

        FluidVariant withPurity = WaterPurity.addPurity(thirst$toVariant(result), purity);
        return new AbstractedFluidStack(result.fluid(), result.amount(),
                thirst$toMap(withPurity.getComponents()), recipe.getUnit(), null);
    }

    @ModifyArg(method = "processFermenting",
            at = @At(value = "INVOKE",
                    target = "Lumpaz/brewinandchewin/common/container/AbstractedItemHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;"),
            index = 1)
    private ItemStack thirst$purityItemResult(ItemStack output) {
        if (WaterPurity.isWaterFilledContainer(output)) {
            WaterPurity.addPurity(output, this.thirst$purityOut);
        }
        this.thirst$purityOut = 0;
        return output;
    }

    @Shadow private ResourceLocation lastRecipeID;
    @Shadow
    private boolean checkNewRecipe;
    @Shadow private int fermentTime;

    /**
     * @author mlus
     * @reason match recipe
     */
    @Overwrite
    private Optional<RecipeHolder<KegFermentingRecipe>> getMatchingRecipe(KegRecipeWrapper inventoryWrapper) {
        KegBlockEntity self = (KegBlockEntity) (Object) this;
        Level level = self.getLevel();
        if (level == null) return Optional.empty();

        if (this.lastRecipeID != null) {
            Optional<RecipeHolder<?>> holder = level.getRecipeManager().byKey(this.lastRecipeID);
            if (holder.isPresent()
                    && holder.get().value() instanceof KegFermentingRecipe last
                    && last.matches(inventoryWrapper, level)) {
                @SuppressWarnings("unchecked")
                RecipeHolder<KegFermentingRecipe> result = (RecipeHolder<KegFermentingRecipe>) holder.get();
                return Optional.of(result);
            }
        }

        if (this.checkNewRecipe) {
            AbstractedFluidStack tankFluid = self.getFluidTank().getAbstractedFluid();
            Optional<RecipeHolder<KegFermentingRecipe>> recipe = level.getRecipeManager()
                    .getAllRecipesFor(BnCRecipeTypes.FERMENTING).stream()
                    .filter(a -> a.value().matches(inventoryWrapper, level)
                            && thirst$fluidMatches(a.value(), tankFluid))
                    .findFirst();
            if (recipe.isPresent()) {
                ResourceLocation newRecipeID = recipe.get().id();
                if (this.lastRecipeID != null && !this.lastRecipeID.equals(newRecipeID)) {
                    this.fermentTime = 0;
                }
                this.lastRecipeID = newRecipeID;
                return recipe;
            }
        }

        this.checkNewRecipe = false;
        return Optional.empty();
    }

    @Unique
    private static boolean thirst$fluidMatches(KegFermentingRecipe recipe, AbstractedFluidStack tankFluid) {
        if (recipe.getFluidIngredient().isEmpty()) return true;

        FluidVariant stripped = NbtHelper.edit(thirst$toVariant(tankFluid), tag -> tag.remove("Purity"));
        AbstractedFluidStack cleaned = new AbstractedFluidStack(
                tankFluid.fluid(), tankFluid.amount(),
                thirst$toMap(stripped.getComponents()), recipe.getUnit(), null);

        return recipe.getFluidIngredient().get().ingredient().matches(cleaned);
    }
    @Shadow @Final
    private KegRecipeWrapper recipeWrapper;

    @ModifyArg(method = "fluidExtract",
            at = @At(value = "INVOKE",
                    target = "Lumpaz/brewinandchewin/common/container/AbstractedFluidTank;fill(Lumpaz/brewinandchewin/common/utility/AbstractedFluidStack;Z)Lumpaz/brewinandchewin/common/utility/AbstractedFluidStack;",
                    ordinal = 0),
            index = 0)
    private AbstractedFluidStack thirst$purityOnFill(AbstractedFluidStack stack,
                                                     @Local(argsOnly = true, name = "slotIn") ItemStack slotIn,
                                                     @Local(name = "recipe") Optional<KegPouringRecipe> recipe) {
        if (!WaterPurity.isWaterFilledContainer(slotIn)) return stack;

        FluidVariant withPurity = WaterPurity.addPurity(thirst$toVariant(stack), WaterPurity.getPurity(slotIn));
        return new AbstractedFluidStack(stack.fluid(), stack.amount(),
                thirst$toMap(withPurity.getComponents()), recipe.get().getUnit(), null);
    }

    @Inject(method = "fluidExtract",
            at = @At(value = "INVOKE",
                    target = "Lumpaz/brewinandchewin/common/container/AbstractedFluidTank;drain(JLumpaz/brewinandchewin/common/utility/FluidUnit;Z)Lumpaz/brewinandchewin/common/utility/AbstractedFluidStack;",
                    ordinal = 0))
    private void thirst$purityOnPour(ItemStack slotIn, int maxTakeAmount, boolean inGui, boolean isCreative,
                                     CallbackInfoReturnable<List<ItemStack>> cir,
                                     @Local(name = "resultItem") ItemStack resultItem) {
        if (!WaterPurity.isWaterFilledContainer(resultItem)) return;

        KegBlockEntity keg = (KegBlockEntity) (Object) this;
        int purity = keg.getFluidTank().isEmpty()
                ? config.DEFAULT_PURITY
                : WaterPurity.getPurity(thirst$toVariant(keg.getFluidTank().getAbstractedFluid()));

        WaterPurity.addPurity(resultItem, purity);
    }
}
