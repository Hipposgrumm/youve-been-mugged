package gg.hipposgrumm.muggablekeepinv.mixin;

import gg.hipposgrumm.muggablekeepinv.MuggableKeepInventoryHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class EntityMixin {

	@Inject(method = "drop", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;getAttacker()Lnet/minecraft/entity/Entity;", shift = At.Shift.AFTER))
	private void muggablekeepinv_detectplayerdamage(DamageSource source, CallbackInfo ci) {
		LivingEntity victim = (LivingEntity)(Object)this;
		if (meetPlayerkillRequirements(source.getAttacker(), victim)) MuggableKeepInventoryHelper.assasinatedByPlayer.put(victim.getUuid(), true);
	}

	@Inject(method = "drop", at = @At("TAIL"))
	private void muggableKeepInventory_resetPlayerDamageVariable(DamageSource source, CallbackInfo ci) {
		LivingEntity victim = (LivingEntity)(Object)this;
		MuggableKeepInventoryHelper.assasinatedByPlayer.remove(victim.getUuid());
	}

	private boolean meetPlayerkillRequirements(Entity assassin, LivingEntity victim) {
		if (assassin instanceof LivingEntity)
			if (!(assassin == victim) || (!assassin.isTeammate(victim)))
				if (assassin instanceof PlayerEntity && victim instanceof PlayerEntity) {
					return true;
				} else if (assassin instanceof TameableEntity) {
					if (((TameableEntity) assassin).isTamed())
						return !(((TameableEntity) assassin).getOwnerUuid() == victim.getUuid());
				}
		return false;
	}
}