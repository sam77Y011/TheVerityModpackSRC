package net.tripleteamsstudio.unknownversionpt3.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tripleteamsstudio.unknownversionpt3.util.VerityConfigWriter;

@Mod.EventBusSubscriber(modid = "unknownversionpt3", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TitleScreenHandler {

    @SubscribeEvent
    public static void onTitleScreenInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof TitleScreen titleScreen)) return;

        // Force setup screen on first run (no key set yet)
        if (VerityConfigWriter.isApiKeyMissing()) {
            Minecraft.getInstance().setScreen(new ApiKeyScreen());
            return;
        }

        // Add "Change API Key" button, top-left corner
        Button changeKeyButton = Button.builder(Component.literal("Change API Key"), b ->
                        Minecraft.getInstance().setScreen(new ApiKeyScreen(titleScreen, true)))
                .bounds(5, 5, 130, 20)
                .build();

        event.addListener(changeKeyButton);
    }
}