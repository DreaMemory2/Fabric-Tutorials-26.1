package com.crystal.datagen;

import com.crystal.CrystalMod;
import com.crystal.block.ModBlocks;
import com.crystal.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.TransmuteRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.TransmuteRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * @see net.minecraft.data.recipes.packs.VanillaRecipeProvider VanillaRecipeProvider
 */
public class ModRecipeDataGenerator extends FabricRecipeProvider {

    public ModRecipeDataGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @NotNull
    @Override
    protected RecipeProvider createRecipeProvider(@NotNull HolderLookup.Provider provider, @NotNull RecipeOutput output) {
        return new RecipeProvider(provider, output) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
                shaped(RecipeCategory.TOOLS, ModBlocks.BASIC_FLUID_TANK)
                        .pattern("RIR")
                        .pattern("I I")
                        .pattern("RIR")
                        .define('R', Items.REDSTONE)
                        .define('I', Items.IRON_INGOT)
                        .unlockedBy("has_iron", has(Items.IRON_INGOT))
                        .unlockedBy("has_redstone", has(Items.REDSTONE))
                        .save(output);
                TransmuteRecipeBuilder.transmute(RecipeCategory.MISC, Ingredient.of(ModBlocks.BASIC_FLUID_TANK), Ingredient.of(Items.RED_DYE), ModBlocks.ADVANCED_FLUID_TANK.asItem())
                        .group("fluid_tank")
                        .unlockedBy("has_basic_fluid_tank", has(ModBlocks.BASIC_FLUID_TANK))
                        .save(output, "advanced_fluid_tank");
                TransmuteRecipeBuilder.transmute(RecipeCategory.MISC, Ingredient.of(ModBlocks.ADVANCED_FLUID_TANK), Ingredient.of(Items.BLUE_DYE), ModBlocks.ELITE_FLUID_TANK.asItem())
                        .group("fluid_tank")
                        .unlockedBy("has_advanced_fluid_tank", has(ModBlocks.ADVANCED_FLUID_TANK))
                        .save(output, "elite_fluid_tank");
                TransmuteRecipeBuilder.transmute(RecipeCategory.MISC, Ingredient.of(ModBlocks.ELITE_FLUID_TANK), Ingredient.of(Items.PURPLE_DYE), ModBlocks.ULTIMATE_FLUID_TANK.asItem())
                        .group("fluid_tank")
                        .unlockedBy("has_elite_fluid_tank", has(ModBlocks.ELITE_FLUID_TANK))
                        .save(output, "ultimate_fluid_tank");
            }
        };
    }

    @NotNull
    @Override
    public String getName() {
        return CrystalMod.MOD_ID;
    }
}
