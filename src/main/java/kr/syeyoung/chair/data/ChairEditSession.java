package kr.syeyoung.chair.data;

import kr.syeyoung.chair.Chairs;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChairEditSession {
    private Player player;
    private List<ChairEditEntity> representation;
    private ChairData chairData;
    private Location center;
    private UUID editSessionId;

    public UUID getEditSessionId() {
        return editSessionId;
    }

    public ChairEditSession(Location center, Player p, ChairData cd) {
        this.player = p;
        this.center = center;
        center.setDirection(new Vector(1,0,0));
        representation = new ArrayList<>();
        editSessionId = UUID.randomUUID();
        this.chairData = cd;
        Chairs.getInstance().getEditSessionMap().put(editSessionId, this);
    }

    public void remove(boolean removeFromMap) {
        representation.forEach(ChairEditEntity::kill);

        this.chairData.getEntityList().clear();
        for (ChairEditEntity chairEditEntity : representation) {
            this.chairData.getEntityList().add(new ChairEntity(chairEditEntity));
        }

        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("chairData", this.chairData);
        try {
            yaml.save(this.chairData.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (removeFromMap) {
            Chairs.getInstance().getEditSessionMap().remove(getEditSessionId());
        }
    }

    public List<ChairEditEntity> getRepresentation() {
        return representation;
    }

    public void addNewEntity(EntityType eType) {
        representation.add(new ChairEditEntity(this, eType, false));
    }

    public Location getCenter() {
        return center;
    }

    public Player getPlayer() {
        return player;
    }
}


