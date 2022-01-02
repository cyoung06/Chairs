package kr.syeyoung.chair.data;

import kr.syeyoung.chair.Chairs;
import kr.syeyoung.chair.data.ChairData;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Chair {
    private Location center;
    private Vector direction;
    private UUID chairIdentifier;
    private UUID chairOwnerUUID;
    private ChairData chairData;

    private List<Entity> entities = new ArrayList<>();

    public List<Entity> getEntities() {
        return entities;
    }

    public Chair(ChairData chairData, Location center, Player p) {
        this.chairData = chairData;
        this.center = center;
        this.direction = center.getDirection();
        this.chairOwnerUUID = p.getUniqueId();
        this.chairIdentifier = UUID.randomUUID();
    }

    public void summon() {
        Chairs.getInstance().getPlacedChairMap().put(chairIdentifier, this);
        chairData.getEntityList().forEach(c -> {
            entities.add(c.summon(this));
        });

    }

    public void deSummon(boolean remove, boolean dropItem) {
        if (remove)
            Chairs.getInstance().getPlacedChairMap().remove(chairIdentifier);
        entities.forEach(e -> {
            e.eject();
            e.remove();
        });
        if (dropItem)
            center.getWorld().dropItem(center, chairData.getChairItem());
    }

    public UUID getChairIdentifier() {
        return chairIdentifier;
    }

    public UUID getChairOwnerUUID() {
        return chairOwnerUUID;
    }

    public ChairData getChairData() {
        return chairData;
    }

    public Location getCenter() {
        return center;
    }

    public Vector getDirection() {
        return direction;
    }
}
