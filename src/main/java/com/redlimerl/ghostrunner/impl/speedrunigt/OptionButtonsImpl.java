package com.redlimerl.ghostrunner.impl.speedrunigt;

import com.redlimerl.ghostrunner.data.RunnerOptions;
import com.redlimerl.ghostrunner.gui.screen.APIKeyScreen;
import com.redlimerl.ghostrunner.gui.screen.GhostRunnerInfoScreen;
import com.redlimerl.ghostrunner.gui.widget.OpacitySliderWidget;
import com.redlimerl.speedrunigt.api.OptionButtonFactory;
import com.redlimerl.speedrunigt.api.SpeedRunIGTApi;
import com.redlimerl.speedrunigt.option.SpeedRunOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableText;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

public class OptionButtonsImpl implements SpeedRunIGTApi {

    @Override
    public Collection<OptionButtonFactory> createOptionButtons() {
        List<OptionButtonFactory> factoryList = Lists.newArrayList();

        factoryList.add(screen ->
                new OptionButtonFactory.Builder()
                        .setButtonWidget(new OpacitySliderWidget())
                        .setCategory("ghostrunner.title")
        );

        factoryList.add(screen ->
                new OptionButtonFactory.Builder()
                        .setButtonWidget(new ButtonWidget(0, 0, 150, 20, new TranslatableText("ghostrunner.option.toggle_point_notification").append(": ").append(SpeedRunOption.getOption(RunnerOptions.TOGGLE_CHECKPOINT_MESSAGE) ? ScreenTexts.ON : ScreenTexts.OFF), button -> {
                            SpeedRunOption.setOption(RunnerOptions.TOGGLE_CHECKPOINT_MESSAGE, !SpeedRunOption.getOption(RunnerOptions.TOGGLE_CHECKPOINT_MESSAGE));
                            button.setMessage(new TranslatableText("ghostrunner.option.toggle_point_notification").append(": ").append(SpeedRunOption.getOption(RunnerOptions.TOGGLE_CHECKPOINT_MESSAGE) ? ScreenTexts.ON : ScreenTexts.OFF));
                        }))
                        .setCategory("ghostrunner.title")
        );

        factoryList.add(screen ->
                new OptionButtonFactory.Builder()
                        .setButtonWidget(new ButtonWidget(0, 0, 150, 20, new TranslatableText("ghostrunner.option.toggle_fsg_macro_mode").append(": ").append(SpeedRunOption.getOption(RunnerOptions.TOGGLE_MACRO_FOR_FSG) ? ScreenTexts.ON : ScreenTexts.OFF), button -> {
                            SpeedRunOption.setOption(RunnerOptions.TOGGLE_MACRO_FOR_FSG, !SpeedRunOption.getOption(RunnerOptions.TOGGLE_MACRO_FOR_FSG));
                            button.setMessage(new TranslatableText("ghostrunner.option.toggle_fsg_macro_mode").append(": ").append(SpeedRunOption.getOption(RunnerOptions.TOGGLE_MACRO_FOR_FSG) ? ScreenTexts.ON : ScreenTexts.OFF));
                        }))
                        .setCategory("ghostrunner.title")
        );

        factoryList.add(screen ->
                new OptionButtonFactory.Builder()
                        .setButtonWidget(new ButtonWidget(0, 0, 150, 20, new TranslatableText("ghostrunner.menu.register_api_key"), button -> {
                            MinecraftClient client = MinecraftClient.getInstance();
                            if (client != null) client.openScreen(new APIKeyScreen(bool -> client.openScreen(screen)));
                        }))
                        .setCategory("ghostrunner.title")
        );

        factoryList.add(screen ->
                new OptionButtonFactory.Builder()
                        .setButtonWidget(new ButtonWidget(0, 0, 150, 20, new TranslatableText("ghostrunner.menu.info"), button -> {
                            MinecraftClient client = MinecraftClient.getInstance();
                            if (client != null) client.openScreen(new GhostRunnerInfoScreen(screen));
                        }))
                        .setCategory("ghostrunner.title")
        );

        factoryList.add(screen ->
                new OptionButtonFactory.Builder()
                        .setButtonWidget(new ButtonWidget(0, 0, 150, 20, new TranslatableText("options.controls"), button -> {
                            MinecraftClient client = MinecraftClient.getInstance();
                            if (client != null) client.openScreen(new ControlsOptionsScreen(screen, client.options));
                        }))
                        .setCategory("ghostrunner.title")
        );

        return factoryList;
    }
}
