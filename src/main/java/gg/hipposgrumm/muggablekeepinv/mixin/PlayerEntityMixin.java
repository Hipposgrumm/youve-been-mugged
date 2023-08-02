package gg.hipposgrumm.muggablekeepinv.mixin;

import gg.hipposgrumm.muggablekeepinv.MuggableKeepInventoryHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Redirect(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
    private boolean muggablekeepinv_conditiondropinventory(GameRules instance, GameRules.Key<GameRules.BooleanRule> rule) {
        if (!(rule == GameRules.KEEP_INVENTORY)) return instance.getBoolean(rule);
        return instance.getBoolean(rule) && !MuggableKeepInventoryHelper.wasKilledByPlayer((LivingEntity)(Object)this);
    }

    @Redirect(method = "getXpToDrop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
    private boolean muggablekeepinv_conditiondropexperience(GameRules instance, GameRules.Key<GameRules.BooleanRule> rule) {
        return muggablekeepinv_conditiondropinventory(instance, rule);
    }

    @Mixin(ServerPlayerEntity.class)
    public static class ServerPlayerEntityMixin {
        @Redirect(method = "copyFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"))
        private boolean muggablekeepinv_conditionrestoreitems(GameRules instance, GameRules.Key<GameRules.BooleanRule> rule) {
            if (!(rule == GameRules.KEEP_INVENTORY)) return instance.getBoolean(rule);
            return instance.getBoolean(rule) && !MuggableKeepInventoryHelper.wasKilledByPlayer((LivingEntity)(Object)this);
        }
    }
}
