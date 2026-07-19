package net.tripleteamsstudio.unknownversionpt1.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.world.Difficulty;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "unknownversionpt1", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CreateWorldLockHandler {

    private static final int TAB_BAR_HEIGHT = 24;

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void onInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof CreateWorldScreen)) return;

        for (var widget : event.getListenersList()) {
            if (!(widget instanceof CycleButton<?> cycleButton)) continue;

            Object value = cycleButton.getValue();

            if (value instanceof Difficulty) {
                CycleButton<Difficulty> diffButton = (CycleButton<Difficulty>) cycleButton;
                int guard = 0;
                while (diffButton.getValue() != Difficulty.HARD && guard++ < 10) {
                    diffButton.onPress();
                }
                diffButton.active = false;
            } else if (value instanceof WorldCreationUiState.SelectedGameMode) {
                CycleButton<WorldCreationUiState.SelectedGameMode> modeButton =
                        (CycleButton<WorldCreationUiState.SelectedGameMode>) cycleButton;
                int guard = 0;
                while (modeButton.getValue() != WorldCreationUiState.SelectedGameMode.SURVIVAL && guard++ < 10) {
                    modeButton.onPress();
                }
                modeButton.active = false;
            }
        }
    }

    @SubscribeEvent
    public static void onRender(ScreenEvent.Render.Post event) {
        if (!(event.getScreen() instanceof CreateWorldScreen screen)) return;

        GuiGraphics gg = event.getGuiGraphics();
        gg.fill(0, 0, screen.width, TAB_BAR_HEIGHT, 0xFF2B2B2B);
    }

    @SubscribeEvent
    public static void onMouseClick(ScreenEvent.MouseButtonPressed.Pre event) {
        if (!(event.getScreen() instanceof CreateWorldScreen)) return;

        if (event.getMouseY() <= TAB_BAR_HEIGHT) {
            event.setCanceled(true);
        }
    }
}