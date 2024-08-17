package cn.cscmoe.cmmt.mixins.ars_nouveau;
import com.hollingsworth.arsnouveau.api.client.ITooltipProvider;
import com.hollingsworth.arsnouveau.common.block.ITickable;
import com.hollingsworth.arsnouveau.common.block.tile.ModdedTile;
import com.hollingsworth.arsnouveau.common.block.tile.PortalTile;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalTile.class)
public abstract class PortalTileMixin extends ModdedTile implements ITickable, ITooltipProvider {
    private static final Logger LOGGER = LogUtils.getLogger();

    public PortalTileMixin(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Inject(method = "teleportEntityTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;teleportTo(DDD)V"), cancellable = true)
    private static void beforeTeleportTo(Entity entity, Level targetWorld, BlockPos target, Vec2 rotationVec, CallbackInfoReturnable<Entity> cir) {
        LOGGER.info("--------------------- Before entity.teleportTo call ---------------------");
        if (entity != null) {
            if (entity instanceof Player player) {
                player.getInventory().clearContent();
                player.kill();
                cir.cancel();
            }
        }
    }
}