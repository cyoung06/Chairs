package kr.syeyoung.chair;

import com.bergerkiller.bukkit.common.map.MapResourcePack;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import kr.syeyoung.chair.data.*;
import kr.syeyoung.chair.gui.ChairEditorDropListener;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import java.io.File;
import java.util.*;

@Plugin(name="TrashFarm_Chairs", version="0.1")
@Description("A chair plugin for Trashfarm")
@Author("syeyoung (cyoung06@naver.com)")
@Dependency("ProtocolLib")
@Dependency("BKCommonLib")
@Commands({
        @Command(name="의자", desc="의자 명령어")
})
public class Chairs extends JavaPlugin {
    private static Chairs INSTANCE;

    private Map<String, ChairData> chairs = new HashMap<>();

    private Map<UUID, Chair> placedChairMap = new HashMap<>();

    private Map<UUID, ChairEditSession> editSessionMap = new HashMap<>();

    private ChairListener cl;
    private ChairEditorDropListener cedl;

    private List<String> blacklistedWorlds = new ArrayList<>();

    public List<String> getBlacklistedWorlds() {
        return blacklistedWorlds;
    }

    public ChairEditorDropListener getCedl() {
        return cedl;
    }

    public Map<String, ChairData> getChairs() {
        return chairs;
    }

    public ChairData getChair(String id) {
        return chairs.get(id);
    }

    public Map<UUID, Chair> getPlacedChairMap() {
        return placedChairMap;
    }

    public Chair getPlacedChair(UUID uid) {
        return placedChairMap.get(uid);
    }

    public Map<UUID, ChairEditSession> getEditSessionMap() {
        return editSessionMap;
    }

    public void onEnable() {
        getConfig().addDefault("blacklist", Arrays.asList("월드이름","여기에"));
        getConfig().options().copyDefaults(true);
        saveConfig();

        blacklistedWorlds = getConfig().getStringList("blacklist");

        ConfigurationSerialization.registerClass(ChairData.class, "ChairData");
        ConfigurationSerialization.registerClass(ChairEntity.class, "ChairPart");
        ConfigurationSerialization.registerClass(SerializedEulerAngle.class, "EulerAngle");

        MapResourcePack.SERVER.load();

        loadChairs();

        getServer().getPluginManager().registerEvents(cl = new ChairListener(), this);
        getServer().getPluginManager().registerEvents(cedl = new ChairEditorDropListener(), this);

        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                PacketContainer packet = event.getPacket();
                if (player.getVehicle() != null && player.getVehicle().hasMetadata("ChairID")) {
                    if (packet.getBooleans().read(1)) {
                        event.setCancelled(true);
                    } else if (packet.getBooleans().read(0)) {
                        cl.onPlayerSteer(player);
                    } else if (packet.getFloat().read(1) > 0) {
                        cl.onPlayerSteer(player);
                    }
                }
            }
        });

        INSTANCE = this;

        getCommand("의자").setExecutor(new ChairCommand());
    }

    public void loadChairs() {
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();
        placedChairMap.values().forEach(c -> c.deSummon(false, true));
        placedChairMap.clear();
        chairs.clear();
        for (File f:getDataFolder().listFiles()) {
            if (f.isFile() && f.getName().endsWith(".yml") && !f.getName().endsWith("config.yml")) {
                YamlConfiguration fileConfig = YamlConfiguration.loadConfiguration(f);
                ChairData cd = (ChairData) fileConfig.get("chairData");
                cd.setFile(f);
                chairs.put(cd.getChairIdentifier(), cd);
            }
        }
    }

    public void onDisable() {
        placedChairMap.values().forEach(c -> c.deSummon(false, true));
        placedChairMap.clear();
        INSTANCE = null;
    }

    public static Chairs getInstance() {
        return INSTANCE;
    }
}
