package com.crystal.datagen;

import com.crystal.block.ModBlocks;
import com.crystal.item.ModItems;
import com.crystal.item.juice.Juice;
import com.crystal.item.juice.Juices;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup.Provider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModChineseDataGenerator extends FabricLanguageProvider {

    public ModChineseDataGenerator(FabricPackOutput output, CompletableFuture<Provider> registryLookup) {
        super(output, "zh_cn", registryLookup);
    }

    @Override
    public void generateTranslations(@NotNull Provider registryLookup, TranslationBuilder builder) {
        builder.add("itemGroup.crystalmod.crystal", "水晶模组");
        builder.add("tooltip.crystalmod.fluid_empty", "空");
        // 方块
        translateBlock(builder);
        // 物品
        translateItem(builder);
        // 饮品
        createJuiceTranslate(builder, "水", Juices.WATER.value());
        createJuiceTranslate(builder, "胡萝卜汁", Juices.CARROT.value());
        createJuiceTranslate(builder, "茶", Juices.TEA.value());
        createJuiceTranslate(builder, "葡萄汁", Juices.GRAPE.value());
        createJuiceTranslate(builder, "苹果汁", Juices.APPLE.value());
        createJuiceTranslate(builder, "蔬菜汁", Juices.VEGETABLE.value());
        createJuiceTranslate(builder, "西瓜汁", Juices.MELON.value());
        createJuiceTranslate(builder, "金葡萄汁", Juices.GOLDEN_GRAPE.value());
        createJuiceTranslate(builder, "金苹果汁", Juices.GOLDEN_APPLE.value());
        createJuiceTranslate(builder, "可乐", Juices.COKE.value());
        createJuiceTranslate(builder, "雪碧", Juices.SPRITE.value());
        createJuiceTranslate(builder, "奶茶", Juices.MILK_TEA.value());
        createJuiceTranslate(builder, "咖啡", Juices.COFFEE.value());
        createJuiceTranslate(builder, "巧克力奶", Juices.CHOCOLATES_MILK.value());
        createJuiceTranslate(builder, "巧克力水", Juices.CHOCOLATES_WATER.value());
        createJuiceTranslate(builder, "豆浆", Juices.SOY_MILK.value());
        createJuiceTranslate(builder, "白萝卜汁", Juices.WHITE_RADISH.value());
        createJuiceTranslate(builder, "番茄汁", Juices.TOMATO.value());
        createJuiceTranslate(builder, "玉米汁", Juices.CORN.value());
        createJuiceTranslate(builder, "黄瓜汁", Juices.CUCUMBER.value());
        createJuiceTranslate(builder, "梨子汁", Juices.PEAR.value());
        createJuiceTranslate(builder, "荔枝汁", Juices.LYCHEE.value());
        createJuiceTranslate(builder, "桃子汁", Juices.PEACH.value());
        createJuiceTranslate(builder, "橙子汁", Juices.ORANGE.value());
        createJuiceTranslate(builder, "枇杷汁", Juices.LOQUAT.value());
        createJuiceTranslate(builder, "芒果汁", Juices.MANGO.value());
        createJuiceTranslate(builder, "柠檬汁", Juices.LEMON.value());
        createJuiceTranslate(builder, "柚子汁", Juices.GRAPEFRUIT.value());
        createJuiceTranslate(builder, "柿子汁", Juices.PERSIMMON.value());
        createJuiceTranslate(builder, "木瓜汁", Juices.PAPAYA.value());
        createJuiceTranslate(builder, "山楂汁", Juices.HAWTHORN.value());
        createJuiceTranslate(builder, "石榴汁", Juices.POMEGRANATE.value());
        createJuiceTranslate(builder, "红枣汁", Juices.CHINESE_DATE.value());
        createJuiceTranslate(builder, "草莓汁", Juices.STRAWBERRY.value());
        createJuiceTranslate(builder, "椰子汁", Juices.COCONUT.value());
        createJuiceTranslate(builder, "樱桃汁", Juices.CHERRY.value());
        createJuiceTranslate(builder, "香蕉汁", Juices.BANANA.value());
        createJuiceTranslate(builder, "椰奶", Juices.COCONUT_MILK.value());
    }

    private void createJuiceTranslate(TranslationBuilder builder, String name, Juice juice) {
        builder.add(ModItems.JUICE.asItem().getDescriptionId() + "." + juice.getName(), name);
    }

    private void translateBlock(TranslationBuilder builder) {
        builder.add(ModBlocks.BASIC_FLUID_TANK, "§a基础 液体储罐");
        builder.add(ModBlocks.ADVANCED_FLUID_TANK, "§c高级 液体储罐");
        builder.add(ModBlocks.ELITE_FLUID_TANK, "§b精英 液体储罐");
        builder.add(ModBlocks.ULTIMATE_FLUID_TANK, "§d终极 液体储罐");
        builder.add(ModBlocks.CREATIVE_FLUID_TANK, "创造 液体储罐");
        builder.add(ModBlocks.BASIC_MECHANICAL_PIPE, "§a基础 机械管道");
        builder.add(ModBlocks.ADVANCED_MECHANICAL_PIPE, "§c高级 机械管道");
    }

    private void translateItem(TranslationBuilder builder) {
        builder.add(ModItems.JUICE_BOTTLE, "果汁玻璃杯");
    }
}
