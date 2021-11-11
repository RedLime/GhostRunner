package com.redlimerl.ghostrunner.record.data;

import com.redlimerl.ghostrunner.util.Utils;
import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.RunCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.Difficulty;
import net.minecraft.world.gen.GeneratorOptions;
import org.apache.commons.compress.utils.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.redlimerl.ghostrunner.GhostRunner.*;

public class GhostData {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private final UUID uuid;
    private final String modVersion;
    private final String clientVersion;
    private final int ghostVersion;
    private String ghostName;
    private final UUID ghostUserUuid;
    private final String ghostUserName;
    private final int ghostType;
    private final long seed;
    private long realTimeAttack;
    private long inGameTime;
    private String createdDate;
    private final String key;
    private boolean isSubmitted;
    private boolean isUseF3;
    private final boolean shouldGenerateStructures;
    private String difficulty;
    private String ghostCategory;
    private String recordURL;

    public static GhostData loadData(Path path) {
        File infoFile = new File(path.toFile(), ".gri");

        if (!infoFile.exists()) throw new IllegalArgumentException("Not found a Record file");

        GhostData result;
        try {
            String infoData = FileUtils.readFileToString(infoFile, Charsets.UTF_8);
            result = GSON.fromJson(infoData, GhostData.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Old or corrupted ghost files");
        }

        //For old ghost checking
        if (result.ghostVersion < GHOST_VERSION) throw new IllegalArgumentException("Old ghost file, you cannot use this ghost.");

        return result;
    }

    public static GhostData create(GeneratorOptions generatorOptions, GhostType ghostType, boolean isHardcore) {
        return new GhostData(
                UUID.randomUUID(),
                MOD_VERSION,
                CLIENT_VERSION,
                GHOST_VERSION,
                "temp",
                Utils.UUIDFromString(MinecraftClient.getInstance().getSession().getUuid()),
                MinecraftClient.getInstance().getSession().getUsername(),
                ghostType.getId() + (isHardcore ? 1 : 0),
                generatorOptions.getSeed(),
                0,
                0,
                Instant.now().toString(),
                RandomStringUtils.randomAlphanumeric(32),
                false,
                false,
                Difficulty.EASY.getName(),
                "",
                RunCategory.ANY.name(),
                generatorOptions.shouldGenerateStructures()
        );
    }

    public GhostData(UUID uuid, String modVersion, String clientVersion, int ghostVersion, String ghostName, UUID ghostUserUuid, String ghostUserName,
                     int ghostType, long seed, long realTimeAttack, long inGameTime, String createdDate, String key, boolean isSubmitted,
                     boolean isUseF3, String difficulty, String recordURL, String ghostCategory, boolean shouldGenerateStructures) {
        this.uuid = uuid;
        this.modVersion = modVersion;
        this.clientVersion = clientVersion;
        this.ghostVersion = ghostVersion;
        this.ghostName = ghostName;
        this.ghostUserUuid = ghostUserUuid;
        this.ghostUserName = ghostUserName;
        this.ghostType = ghostType;
        this.seed = seed;
        this.realTimeAttack = realTimeAttack;
        this.inGameTime = inGameTime;
        this.createdDate = createdDate;
        this.key = key;
        this.isSubmitted = isSubmitted;
        this.isUseF3 = isUseF3;
        this.difficulty = difficulty;
        this.recordURL = recordURL;
        this.ghostCategory = ghostCategory;
        this.shouldGenerateStructures = shouldGenerateStructures;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getModVersion() {
        return modVersion;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public int getGhostVersion() {
        return ghostVersion;
    }

    public String getGhostName() {
        return ghostName;
    }

    public void setGhostName(String ghostName) {
        this.ghostName = ghostName;
    }

    public UUID getGhostUserUuid() {
        return ghostUserUuid;
    }

    public String getGhostUserName() {
        return ghostUserName;
    }

    public GhostType getType() {
        for (GhostType value : GhostType.values()) {
            if (value.getId() == ghostType - (ghostType % 2)) return value;
        }
        return GhostType.RSG;
    }
    
    public boolean isHardcore() {
        return ghostType % 2 == 1;
    }

    public boolean isGenerateStructures() {
        return shouldGenerateStructures;
    }

    public long getSeed() {
        return seed;
    }

    public void setRealTimeAttack(long realTimeAttack) {
        this.realTimeAttack = realTimeAttack;
    }

    public long getRealTimeAttack() {
        return realTimeAttack;
    }

    public void setInGameTime(long inGameTime) {
        this.inGameTime = inGameTime;
    }

    public long getInGameTime() {
        return inGameTime;
    }

    public Date getCreatedDate() {
        return Date.from(Instant.parse(createdDate));
    }

    public void updateCreatedDate() {
        this.createdDate = Instant.now().toString();
    }

    public String getKey() {
        return key;
    }

    public String getDefaultName() {
        return seed + "_" + InGameTimer.timeToStringFormat(inGameTime).replace(':', '.') + "_" + DATE_FORMAT.format(getCreatedDate());
    }

    public Path getPath() {
        return GHOSTS_PATH.resolve(uuid.toString());
    }

    public Difficulty getDifficulty() {
        return Difficulty.byName(difficulty);
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty.getName();
    }

    public void setGhostCategory(RunCategory ghostCategory) {
        this.ghostCategory = ghostCategory.name();
    }

    public RunCategory getGhostCategory() {
        return RunCategory.valueOf(ghostCategory);
    }

    public boolean isUseF3() {
        return isUseF3;
    }

    public void setUseF3(boolean useF3) {
        isUseF3 = useF3;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(boolean submitted) {
        isSubmitted = submitted;
    }

    public String getRecordURL() {
        return recordURL != null && recordURL.startsWith("https://www.speedrun.com/mc") ? recordURL : "";
    }

    public void setRecordURL(String recordURL) {
        this.recordURL = recordURL;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void update() {
        new Thread(() -> {
            File ghostFile = this.getPath().toFile();
            ghostFile.mkdirs();

            try {
                FileUtils.writeStringToFile(new File(ghostFile, ".gri"), this.toString(), Charsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
