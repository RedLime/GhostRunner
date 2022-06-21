package com.redlimerl.ghostrunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.redlimerl.ghostrunner.data.RunnerOptions;
import com.redlimerl.ghostrunner.data.UpdateStatus;
import com.redlimerl.ghostrunner.entity.GhostEntity;
import com.redlimerl.ghostrunner.gui.screen.APIKeyScreen;
import com.redlimerl.ghostrunner.gui.screen.GhostRunnerInfoScreen;
import com.redlimerl.ghostrunner.gui.widget.OpacitySliderWidget;
import com.redlimerl.speedrunigt.option.SpeedRunOption;
import com.redlimerl.speedrunigt.option.SpeedRunOptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Difficulty;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;
import java.util.OptionalLong;

@SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
public class GhostRunner implements ClientModInitializer {

    public static final String MOD_ID = "ghostrunner";

    public static final Identifier BUTTON_ICON_TEXTURE = new Identifier(MOD_ID, "textures/gui/buttons.png");

    public static final Path MAIN_PATH = FabricLoader.getInstance().getGameDir().resolve(MOD_ID);
    public static final Path GHOSTS_PATH = MAIN_PATH.resolve("ghosts");
    public static final Path GHOST_SHARE_PATH = MAIN_PATH.resolve("shares");
    public static final Path GHOST_TEMP_PATH = MAIN_PATH.resolve("temp");
    public static final Path GHOST_OLD_PATH = MAIN_PATH.resolve("old_ghosts");

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static String MOD_VERSION;
    public static final String CLIENT_VERSION = SharedConstants.getGameVersion().getName();
    public static final int GHOST_VERSION = 5;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static OptionalLong OPTIONAL_LONG = OptionalLong.empty();
    public static boolean IS_FSG = false;
    public static boolean IS_HARDCORE = false;
    public static boolean IS_USE_F3 = false;
    public static boolean IS_USE_GHOST = false;
    public static boolean IS_SHOW_USE_GHOST_WARN = false;
    public static Difficulty MINIMUM_DIFFICULTY = Difficulty.HARD;

    public static final UpdateStatus UPDATE_STATUS = new UpdateStatus();

    static {
        if (FabricLoader.getInstance().getModContainer(MOD_ID).isPresent()) {
            MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString().split("\\+")[0];
        }
        MAIN_PATH.toFile().mkdirs();
        GHOSTS_PATH.toFile().mkdirs();
        GHOST_SHARE_PATH.toFile().mkdirs();
        GHOST_TEMP_PATH.toFile().mkdirs();
        GHOST_OLD_PATH.toFile().mkdirs();
    }

    public static final EntityType<GhostEntity> GHOST_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "ghost"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GhostEntity::new)
                    .disableSaving().fireImmune().disableSummon()
                    .dimensions(EntityDimensions.changing(0.6f, 1.8f)).build());

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(GHOST_ENTITY_TYPE, (manager, context) -> new GhostEntity.Renderer(manager));
        FabricDefaultAttributeRegistry.register(GHOST_ENTITY_TYPE,
                GhostEntity.createLivingAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE));

        KeyBinding ghostToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("ghostrunner.title.toggle_ghost", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, "ghostrunner.title"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ghostToggleKey.wasPressed()) {
                boolean option = !SpeedRunOption.getOption(RunnerOptions.TOGGLE_GHOST);
                SpeedRunOption.setOption(RunnerOptions.TOGGLE_GHOST, option);
                if (client.player != null) {
                    client.player.sendMessage(new LiteralText("[Ghost Runner] ").formatted(Formatting.BOLD).formatted(Formatting.AQUA)
                            .append(new TranslatableText("ghostrunner.message.toggle_ghost", new TranslatableText("addServer.resourcePack." + (option ? "enabled" : "disabled"))).formatted(Formatting.WHITE)), true);
                }
            }
        });

        UPDATE_STATUS.check();
    }

    public static void debug(Object obj) {
        System.out.println(obj);
    }
}
