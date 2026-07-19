package net.tripleteamsstudio.unknownversionpt4.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = "unknownversionpt4", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ParanoidTntHandler {

    private static final Random RANDOM = new Random();

    // Tune these to change how rare the scare is
    private static final int MIN_COOLDOWN_TICKS = 20 * 60 * 3;  // 3 min
    private static final int MAX_COOLDOWN_TICKS = 20 * 60 * 6;  // 6 min

    private static int cooldown = randomCooldown();

    private static int randomCooldown() {
        return MIN_COOLDOWN_TICKS + RANDOM.nextInt(MAX_COOLDOWN_TICKS - MIN_COOLDOWN_TICKS);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        cooldown--;
        if (cooldown <= 0) {
            mc.getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.TNT_PRIMED, 1.0F, 1.0F)
            );
            cooldown = randomCooldown();
        }
    }
}