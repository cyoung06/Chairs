package kr.syeyoung.chair.data;

import kr.syeyoung.chair.Chairs;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class ChairEditEntity extends ChairEntity {
    private Entity entity;
    private ChairEditSession ces;

    public ChairEditEntity(ChairEditSession ces, EntityType eType, boolean isSit) {
        super(eType, isSit);
        this.ces = ces;
        this.relativeLocation = new Vector(0,0,0);
        update();
    }

    public ChairEditEntity(ChairEditSession ces, Entity entity, boolean isSit) {
        super(entity, isSit);
        this.entity = entity;
        this.ces = ces;
        this.relativeLocation = new Vector(0,0,0);
        setting();
        update();
    }

    public void setting() {
        entity.setGravity(false);
        entity.setInvulnerable(true);
        entity.setSilent(true);
        if (entity instanceof LivingEntity)
            ((LivingEntity) entity).setAI(false);
    }

    public ChairEditEntity(ChairEditSession ces, ChairEntity entity) {
        super(entity.getEntityType(), entity.isSit);
        this.entityType = entity.getEntityType();
        this.isSit = entity.isSit;
        this.attributeList = entity.attributeList;
        this.relativeLocation = entity.getRelativeLocation();
        this.relativePitch = entity.getRelativePitch();
        this.relativeYaw = entity.getRelativeYaw();
        this.ces = ces;
        update();
    }



    public void update() {
        Location loc = ces.getCenter();
        Vector relative = getRelativeLocation();
        Vector direction = loc.getDirection();

        Location result = loc.clone();

        result.add(direction.clone().multiply(relative.getX()));
        result.add(direction.clone().crossProduct(new Vector(0,1,0)).multiply(relative.getZ()));
        result.add(0,relative.getY(), 0);
        result.setYaw((float) (loc.getYaw() + getRelativeYaw()));
        result.setPitch((float) (loc.getPitch() + getRelativePitch()));

        if (entity == null || entity.isDead()) {
            this.entity = ces.getCenter().getWorld().spawnEntity(result, getEntityType());
            this.entity.setMetadata("Editor", new FixedMetadataValue(Chairs.getInstance(), ces.getEditSessionId().toString()));
            setting();
        } else {
            entity.teleport(result);
        }


        this.getAttributes().forEach(a -> a.applyAttribute(entity));
    }

    public void kill() {
        this.entity.remove();
    }

    public void remove() {
        kill();
        this.ces.getRepresentation().remove(this);
    }

    public void highlight(boolean highlight) {
        this.entity.setGlowing(highlight);
    }
}
