package com.crystal.mixin;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * <p>无限不死图腾实现类</p>
 * @see <a href="https://www.bilibili.com/video/BV1GK4y1z7zW">无限不死图腾效果视频</a>
 * @see <a href="https://github.com/Mafuyu33/mafishmod/blob/main/src/main/java/net/mafuyu33/mafishmod/mixin/enchantmentitemmixin/InfiniteUndyingMixin.java">参考源代码链接</a>
 */
@Mixin(LivingEntity.class)
public abstract class InfinityTotemUndyingMixin extends Entity implements Attackable {

    public InfinityTotemUndyingMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    /**
     * 设置玩家的生命值
     * @param health 生命值
     */
    @Shadow
    public abstract void setHealth(float health);

    /**
     * @param hand 玩家的手（主手或副手）
     * @return 获取玩家手中的物品
     */
    @Shadow
    public abstract ItemStack getItemInHand(InteractionHand hand);

    /**
     * {@return 清除玩家效果状态}
     */
    @Shadow
    public abstract boolean removeAllEffects();

    /**
     * @see #addEffect(MobEffectInstance effect)
     */
    @Shadow
    public abstract boolean addEffect(MobEffectInstance effect, @Nullable Entity entity);

    /**
     * @param effect 生物效果
     * @return 给玩家添加效果
     */
    @Shadow
    public final boolean addEffect(MobEffectInstance effect) {
        return this.addEffect(effect, null);
    }

    /**
     * @author Mafuyu33
     * @reason Change the totem of undying code
     */
    @Overwrite
    private boolean checkTotemDeathProtection(DamageSource source) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        } else {
            ItemStack itemStack = null;
            // 获取玩家的手（主手和副手）
            InteractionHand[] hands = InteractionHand.values();
            // 遍历数值，如果玩家手中有不死图腾，则获取玩家手中的物品
            for (InteractionHand hand : hands) {
                ItemStack stack = this.getItemInHand(hand);
                if (stack.is(Items.TOTEM_OF_UNDYING)) {
                    // 将物品复制一份，实现无限效果
                    itemStack = stack.copy();
                    // stack.shrink(1);
                    break;
                }
            }

            // 不死图腾效果
            if (itemStack != null) {
                this.setHealth(1.0F);
                this.removeAllEffects();
                this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
                // 同步状态数据
                this.level().broadcastEntityEvent(this, (byte) 15);
            }

            return itemStack != null;
        }
    }
}
