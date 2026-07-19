package net.tripleteamsstudio.unknownversionpt1.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "unknownversionpt1", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PauseScreenHandler {

    @SubscribeEvent
    public static void onPauseScreenInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof PauseScreen)) return;

        event.getListenersList().forEach(widget -> {
            if (widget instanceof Button button
                    && button.getMessage().getString().equals(
                        Component.translatable("menu.returnToMenu").getString())) {

                button.setMessage(Component.literal("S̸̢a̷v̶e̴ ̵a̴n̵d̴ ̶Q̴u̷i̶t̸?̷"));
                
            }
        });
    }
}