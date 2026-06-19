package com.crystal.api;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;

public interface TickableBlockEntity {
    void tick();

    /**
     * <p>重写Tick方法，静态扩展方块实体</p>
     * <p>使得方法重写Tick方法变得简单，必须实现BlockEntityTicker中参数</p>
     * <p>由于是服务端，所以需要传入世界数据，判断是否为客户端</p>
     * @param world 世界
     * @return Tick时间
     * @param <T> 使静态方法更够通用到任何方块实体上
     */
    static <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world) {
        return world.isClientSide() ? null : (pworld, pos, state, blockEntity) -> {
            // 检查是否为具有Tick的方块实体
            if (blockEntity instanceof TickableBlockEntity tickableBlockEntity){
                tickableBlockEntity.tick();
            }
        };
    }
}
