package com.mattymatty.audio_priority.screen;

import com.mattymatty.audio_priority.Configs;
import com.mattymatty.audio_priority.client.AudioPriority;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;

import java.io.IOException;

public class ConfigScreen extends Screen {

    protected final Screen parent;

    public ConfigScreen(Screen parent) {
        this(parent, new LiteralText("Audio Engine Tweaks Configs"));
    }

    public ConfigScreen(Screen parent, Text title) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        assert this.client != null;
        super.init();
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height / 6 + 48 - 6, 150, 20,
                new LiteralText("Sound Category Priorities"), button -> this.client.setScreen(new CategoryConfigScreen(this, this.parent))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height / 6 + 72 - 6, 150, 20,
                new LiteralText("Thresholds"), button -> this.client.setScreen(new ThresholdConfigScreen(this, this.parent))));
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height / 6 + 96 - 6, 150, 20,
                new LiteralText("Instant Categories"), button -> this.client.setScreen(new InstantConfigScreen(this, this.parent))));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 168, 200, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.parent)));

    }


    @Override
    public void removed() {
        try {
            Configs.saveConfig();
        } catch (IOException e) {
            AudioPriority.LOGGER.error("Exception Saving Config file");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        DrawableHelper.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
