package kr.syeyoung.chair.data;

import kr.syeyoung.chair.Chairs;
import kr.syeyoung.chair.attributes.AttributeFactory;
import kr.syeyoung.chair.attributes.ChairEntityAttribute;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.*;

@SerializableAs("ChairPart")
public class ChairEntity implements ConfigurationSerializable {

    Vector relativeLocation;
    double relativeYaw;
    double relativePitch;
    EntityType entityType;
    List<ChairEntityAttribute> attributeList = new ArrayList<>();
    boolean isSit;

    private ChairEntity() {

    }

    public ChairEntity(EntityType eType, boolean isSit) {
        this.entityType = eType;
        this.isSit = isSit;
        AttributeFactory.getAttributes(eType).forEach(attributeList::add);
    }

    public ChairEntity(Entity entity, boolean isSit) {
        this.entityType = entity.getType();
        this.isSit = isSit;
        AttributeFactory.getAttributes(entity.getType())
                .forEach(attr -> {attr.extractAttribute(entity); attributeList.add(attr);});
    }

    public ChairEntity(ChairEditEntity editEntity) {
        this.entityType = editEntity.getEntityType();
        this.isSit = editEntity.isChair();
        this.attributeList = editEntity.attributeList;
        this.relativeLocation = editEntity.getRelativeLocation();
        this.relativePitch = editEntity.getRelativePitch();
        this.relativeYaw = editEntity.getRelativeYaw();
    }


    public Vector getRelativeLocation() {
        return relativeLocation;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public boolean isChair() {
        return isSit;
    }

    public double getRelativeYaw() {
        return relativeYaw;
    }

    public double getRelativePitch() {
        return relativePitch;
    }

    public List<ChairEntityAttribute> getAttributes() {
        return attributeList;
    }

    public void setRelativeLocation(Vector relativeLocation) {
        this.relativeLocation = relativeLocation;
    }

    public void setRelativeYaw(double relativeYaw) {
        this.relativeYaw = relativeYaw;
    }

    public void setRelativePitch(double relativePitch) {
        this.relativePitch = relativePitch;
    }

    public void setSit(boolean sit) {
        isSit = sit;
    }

    public <T> ChairEntityAttribute<T> getAttribute(Class<T> attributeValueClass, String attributeName) {
        return (ChairEntityAttribute<T>) attributeList.stream().filter(ce -> ce.getAttributeName().equalsIgnoreCase(attributeName)).findFirst().orElseGet(null);
    }

    public Entity summon(Chair c) {
        Location loc = c.getCenter();
        Vector relative = getRelativeLocation();
        Vector direction = c.getDirection();

        Location result = loc.clone();

        result.add(direction.clone().multiply(relative.getX()));
        result.add(direction.clone().crossProduct(new Vector(0,1,0)).multiply(relative.getZ()));
        result.add(0,relative.getY(), 0);
        result.setYaw((float) (loc.getYaw() + getRelativeYaw()));
        result.setPitch((float) (loc.getPitch() + getRelativePitch()));

        Entity e = loc.getWorld().spawnEntity(result, this.getEntityType());
        e.setGravity(false);
        e.setInvulnerable(true);
        e.setSilent(true);
        if (e instanceof LivingEntity)
            ((LivingEntity) e).setAI(false);

        e.setMetadata("ChairID", new FixedMetadataValue(Chairs.getInstance(), c.getChairIdentifier().toString()));
        e.setMetadata("ChairSittable", new FixedMetadataValue(Chairs.getInstance(), isChair()));

        this.getAttributes().forEach(a -> a.applyAttribute(e));

        return e;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("relativeLoc", relativeLocation);
        map.put("relativeYaw", relativeYaw);
        map.put("relativePitch", relativePitch);
        map.put("entityType", entityType.name());
        map.put("sittable", isChair());
        List<Map<String,Object>> attributes = new ArrayList<>();
        for (ChairEntityAttribute attr : getAttributes()) {
            Map<String, Object> attribute = new HashMap<>();
            attribute.put("name", attr.getAttributeName());
            attribute.put("value", attr.getAttributeValue());
            attributes.add(attribute);
        }

        map.put("attributes", attributes);
        return map;
    }

    public static ChairEntity deserialize(Map<String, Object> map) {
        ChairEntity ce = new ChairEntity();
        ce.relativeLocation = (Vector) map.get("relativeLoc");
        ce.relativeYaw = (double) map.get("relativeYaw");
        ce.relativePitch = (double) map.get("relativePitch");
        ce.entityType = EntityType.valueOf((String) map.get("entityType"));
        ce.isSit = (boolean) map.get("sittable");
        List<Map<String,Object>> attributes = (List<Map<String, Object>>) map.get("attributes");
        for (Map<String,Object> attr : attributes) {
            ChairEntityAttribute cea = AttributeFactory.getAttribute((String) attr.get("name"));
            cea.setAttributeValue(attr.get("value"));
            ce.attributeList.add(cea);
        }

        return ce;
    }


}
