package net.tripleteamsstudio.unknownversionpt4.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tripleteamsstudio.unknownversionpt4.util.ChatLinesLoader;

@Mod.EventBusSubscriber(modid = "unknownversionpt4", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GhostChatHandler {

    private static final int INTERVAL_TICKS = 20 * 60 * 8;

    private static int timer = INTERVAL_TICKS;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        timer--;
        if (timer <= 0) {
            String line = ChatLinesLoader.getRandomLine();
            mc.player.displayClientMessage(Component.literal(line), false);
            timer = INTERVAL_TICKS;
        }
    }
}