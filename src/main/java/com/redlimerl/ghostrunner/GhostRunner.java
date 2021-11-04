package com.redlimerl.ghostrunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.redlimerl.ghostrunner.data.RunnerOptions;
import com.redlimerl.ghostrunner.entity.GhostEntity;
import com.redlimerl.ghostrunner.gui.screen.APIKeyScreen;
import com.redlimerl.ghostrunner.gui.widget.OpacitySliderWidget;
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
import net.minecraft.client.gui.screen.options.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.KeyBinding;
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

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static String MOD_VERSION;
    public static final String CLIENT_VERSION = SharedConstants.getGameVersion().getName();
    public static final int GHOST_VERSION = 2;

    public static boolean isComplete = false;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static OptionalLong optionalLong = OptionalLong.empty();
    public static boolean isFsg = false;
    public static boolean isHardcore = false;
    public static boolean isUseF3 = false;
    public static Difficulty minimumDifficulty = Difficulty.HARD;

    static {
        if (FabricLoader.getInstance().getModContainer(MOD_ID).isPresent()) {
            MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString().split("\\+")[0];
        }
        MAIN_PATH.toFile().mkdirs();
        GHOSTS_PATH.toFile().mkdirs();
        GHOST_SHARE_PATH.toFile().mkdirs();
        GHOST_TEMP_PATH.toFile().mkdirs();
    }

    public static final EntityType<GhostEntity> GHOST_ENTITY_TYPE = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "ghost"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, GhostEntity::new)
                    .disableSaving().fireImmune()//.disableSummon()
                    .dimensions(EntityDimensions.changing(0.6f, 1.8f)).build());

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(GHOST_ENTITY_TYPE, (manager, context) -> new GhostEntity.Renderer(manager));
        FabricDefaultAttributeRegistry.register(GHOST_ENTITY_TYPE,
                GhostEntity.createLivingAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE));

        KeyBinding ghostToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("ghostrunner.title.toggle_ghost", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, "ghostrunner.title"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (ghostToggleKey.wasPressed()) {
                boolean option = !SpeedRunOptions.getOption(RunnerOptions.TOGGLE_GHOST);
                SpeedRunOptions.setOption(RunnerOptions.TOGGLE_GHOST, option);
                if (client.player != null) {
                    client.player.sendMessage(new LiteralText("[Ghost Runner] ").formatted(Formatting.BOLD).formatted(Formatting.AQUA)
                            .append(new TranslatableText("ghostrunner.message.toggle_ghost", new TranslatableText("addServer.resourcePack." + (option ? "enabled" : "disabled"))).formatted(Formatting.WHITE)), true);
                }
            }
        });

        SpeedRunOptions.addOptionButton(screen ->
                new ButtonWidget(0, 0, 150, 20, new TranslatableText("options.controls"), button -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client != null) client.openScreen(new ControlsOptionsScreen(screen, client.options));
                })
        );
        SpeedRunOptions.addOptionButton(screen -> new OpacitySliderWidget());
        SpeedRunOptions.addOptionButton(screen ->
                new ButtonWidget(0, 0, 150, 20, new TranslatableText("ghostrunner.menu.register_api_key"), button -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client != null) client.openScreen(new APIKeyScreen(bool -> client.openScreen(screen)));
                })
        );

    }

    public static void debug(Object obj) {
        System.out.println(obj);
    }
}
