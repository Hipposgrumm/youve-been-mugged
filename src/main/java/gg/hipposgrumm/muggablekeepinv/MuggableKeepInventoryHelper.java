package gg.hipposgrumm.muggablekeepinv;

import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class MuggableKeepInventoryHelper {
	public static Map<UUID, Boolean> assasinatedByPlayer = new HashMap<>();

	public static boolean wasKilledByPlayer(LivingEntity suspectUUID) {
		return assasinatedByPlayer.getOrDefault(suspectUUID.getUuid(), false);
	}
}