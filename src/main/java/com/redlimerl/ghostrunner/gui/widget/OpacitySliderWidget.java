package com.redlimerl.ghostrunner.gui.widget;

import com.redlimerl.ghostrunner.data.RunnerOptions;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class OpacitySliderWidget extends SliderWidget {

    public OpacitySliderWidget() {
        super(0, 0, 150, 20, LiteralText.EMPTY, SpeedRunOptions.getOption(RunnerOptions.GHOST_OPACITY));
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        int amount = (int) (((float) this.value) * 100);
        setMessage(new TranslatableText("ghostrunner.option.ghost_opacity").append(": ").append(amount+"%"));
    }

    @Override
    protected void applyValue() {
        SpeedRunOptions.setOption(RunnerOptions.GHOST_OPACITY, (float) this.value);
    }
}
