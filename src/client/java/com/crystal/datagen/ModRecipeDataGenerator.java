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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

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
                shaped(RecipeCategory.TOOLS, ModBlocks.ADVANCED_FLUID_TANK)
                        .pattern("RRR")
                        .pattern("RBR")
                        .pattern("RRR")
                        .define('R', Items.RED_DYE)
                        .define('B', ModItems.BASIC_FLUID_TANK)
                        .unlockedBy("has_basic_fluid_tank", has(ModItems.BASIC_FLUID_TANK))
                        .save(output);
                shaped(RecipeCategory.TOOLS, ModBlocks.ELITE_FLUID_TANK)
                        .pattern("BBB")
                        .pattern("BAB")
                        .pattern("BBB")
                        .define('B', Items.BLUE_DYE)
                        .define('A', ModItems.ADVANCED_FLUID_TANK)
                        .unlockedBy("has_advanced_fluid_tank", has(ModItems.ADVANCED_FLUID_TANK))
                        .save(output);
                shaped(RecipeCategory.TOOLS, ModBlocks.ULTIMATE_FLUID_TANK)
                        .pattern("PPP")
                        .pattern("PEP")
                        .pattern("PPP")
                        .define('P', Items.PURPLE_DYE)
                        .define('E', ModItems.ELITE_FLUID_TANK)
                        .unlockedBy("has_elite_fluid_tank", has(ModItems.ELITE_FLUID_TANK))
                        .save(output);
            }
        };
    }

    @NotNull
    @Override
    public String getName() {
        return CrystalMod.MOD_ID;
    }
}
