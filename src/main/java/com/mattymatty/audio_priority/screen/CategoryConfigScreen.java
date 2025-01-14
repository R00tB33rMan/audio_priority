package com.mattymatty.audio_priority.screen;

import com.mattymatty.audio_priority.Configs;
import com.mattymatty.audio_priority.client.AudioPriority;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.TranslatableTextContent;

import java.io.IOException;
import java.util.stream.IntStream;

public class CategoryConfigScreen extends Screen {

    protected final Screen origin;
    protected final Screen parent;

    public CategoryConfigScreen(Screen parent, Screen origin) {
        super(new LiteralText("Sound Category Priorities"));
        this.origin = origin;
        this.parent = parent;
    }

    @Override
    protected void init() {
        assert this.client != null;

        SoundCategory[] categories = SoundCategory.values();
        int count = categories.length;

        this.addDrawableChild(CyclingButtonWidget.builder(LiteralText::new)
                .values(IntStream.range(0, count - 1).mapToObj(Integer::toString).toList())
                .initially(Configs.getInstance().categoryClasses.getOrDefault(SoundCategory.MASTER.getName(), 0).toString())
                .build(this.width / 2 - 155, this.height / 6 - 12, 310, 20, new TranslatableText("soundCategory." + SoundCategory.MASTER.getName())
                        , (button, value) -> {
                            Configs.getInstance().categoryClasses.put(SoundCategory.MASTER.getName(), Integer.parseInt(value));
                        }));

        int i = 2;
        for (SoundCategory category : SoundCategory.values()) {
            if (category == SoundCategory.MASTER) continue;
            int j = this.width / 2 - 155 + i % 2 * 160;
            int k = this.height / 6 - 12 + 24 * (i >> 1);
            BaseText label = new TranslatableText("soundCategory." + category.getName());
            this.addDrawableChild(CyclingButtonWidget.builder(LiteralText::new)
                    .values(IntStream.range(0, count - 1).mapToObj(Integer::toString).toList())
                    .initially(Configs.getInstance()
                            .categoryClasses.getOrDefault(category.getName(),SoundCategory.values().length).toString())
                    .build(j, k, 150, 20, label
                            , (button, value) -> {
                                Configs.getInstance().categoryClasses.put(category.getName(), Integer.parseInt(value));
                            }));
            ++i;
        }

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 105, (int) (this.height * 0.9), 100, 20, ScreenTexts.BACK, button -> this.client.setScreen(this.parent)));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 5, (int) (this.height * 0.9), 100, 20, ScreenTexts.DONE, button -> this.client.setScreen(this.origin)));

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
