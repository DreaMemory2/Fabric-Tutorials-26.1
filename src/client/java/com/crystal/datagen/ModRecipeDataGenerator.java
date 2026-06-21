package com.crystal.datagen;

import com.crystal.CrystalMod;
import com.crystal.block.ModBlocks;
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
                shaped(RecipeCategory.TOOLS, ModBlocks.FLUID_TANK)
                        .pattern("RIR")
                        .pattern("I I")
                        .pattern("RIR")
                        .define('R', Items.REDSTONE)
                        .define('I', Items.IRON_INGOT)
                        .unlockedBy("has_iron", has(Items.IRON_INGOT))
                        .unlockedBy("has_redstone", has(Items.REDSTONE))
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
