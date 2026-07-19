package net.tripleteamsstudio.unknownversionpt3.client;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.tripleteamsstudio.unknownversionpt3.util.VerityConfigWriter;

import java.net.URI;

public class ApiKeyScreen extends Screen {

    private static final String TUTORIAL_URL = "https://m.youtube.com/watch?v=_i4O7pyMlks&feature=youtu.be";
    private static final String NOTE_TEXT =
            "Api key is for Verity MIND, your api key is secure and Doesn't upload to TripleTeams, please only use groq api key.";

    private final Screen parentScreen;
    private final boolean allowCancel;

    private EditBox apiKeyBox;
    private Button continueButton;
    private MultiLineLabel noteLabel;

    // Forced first-run version (no cancel, no parent to return to)
    public ApiKeyScreen() {
        this(null, false);
    }

    // Voluntary edit version (opened from title screen button)
    public ApiKeyScreen(Screen parentScreen, boolean allowCancel) {
        super(Component.literal("Verity Setup"));
        this.parentScreen = parentScreen;
        this.allowCancel = allowCancel;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int boxWidth = 300;

        apiKeyBox = new EditBox(this.font, centerX - boxWidth / 2, 90, boxWidth, 20,
                Component.literal("Groq API Key"));
        apiKeyBox.setMaxLength(256);
        apiKeyBox.setValue(VerityConfigWriter.getApiKey());
        apiKeyBox.setResponder(text -> continueButton.active = !text.trim().isEmpty());
        this.addRenderableWidget(apiKeyBox);
        this.setInitialFocus(apiKeyBox);

        noteLabel = MultiLineLabel.create(this.font, Component.literal(NOTE_TEXT), boxWidth);

        continueButton = Button.builder(Component.literal("Continue"), b -> onContinue())
                .bounds(centerX - 75, 190, 150, 20)
                .build();
        continueButton.active = !apiKeyBox.getValue().trim().isEmpty();
        this.addRenderableWidget(continueButton);

        Button tutorialButton = Button.builder(Component.literal("Open Tutorial (YouTube)"), b -> {
                    try {
                        Util.getPlatform().openUri(new URI(TUTORIAL_URL));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .bounds(centerX - 100, 220, 200, 20)
                .build();
        this.addRenderableWidget(tutorialButton);

        if (allowCancel) {
            Button backButton = Button.builder(Component.literal("Back"), b -> onClose())
                    .bounds(centerX - 75, 250, 150, 20)
                    .build();
            this.addRenderableWidget(backButton);
        }
    }

    private void onContinue() {
        String key = apiKeyBox.getValue().trim();
        if (key.isEmpty()) return;

        boolean success = VerityConfigWriter.writeApiKey(key);
        if (success) {
            Minecraft.getInstance().setScreen(parentScreen != null
                    ? parentScreen
                    : new net.minecraft.client.gui.screens.TitleScreen());
        } else {
            apiKeyBox.setValue("");
            apiKeyBox.setSuggestion("Failed to save - check console");
        }
    }

    @Override
    public void onClose() {
        if (allowCancel && parentScreen != null) {
            Minecraft.getInstance().setScreen(parentScreen);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);

        guiGraphics.drawCenteredString(this.font, "Verity - API Key",
                this.width / 2, 40, 0xFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        noteLabel.renderCentered(guiGraphics, this.width / 2, 120);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return allowCancel;
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}