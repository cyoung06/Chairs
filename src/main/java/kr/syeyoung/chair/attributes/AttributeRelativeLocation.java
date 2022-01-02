package kr.syeyoung.chair.attributes;

import kr.syeyoung.chair.data.ChairEditEntity;
import kr.syeyoung.chair.data.SerializedEulerAngle;
import kr.syeyoung.chair.gui.propedit.PropEditEulerAngle;
import kr.syeyoung.chair.gui.propedit.PropEditMenu;
import kr.syeyoung.chair.gui.propedit.PropEditVector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

public class AttributeRelativeLocation extends ChairEntityAttribute<Vector> {

    private ChairEditEntity entity;

    public AttributeRelativeLocation(ChairEditEntity entity) {
        this.entity = entity;
    }


    @Override
    public String getAttributeName() {
        return "relativelocation";
    }

    @Override
    public String getKoreanName() {
        return "위치";
    }

    @Override
    public PropEditMenu<Vector> getEditMenu(Runnable callbackUpdate) {
        return new PropEditVector(this) {
            @Override
            public void onValueUpdate() {
                callbackUpdate.run();
            }
        };
    }

    public Vector getAttributeValue() {
        return entity.getRelativeLocation();
    }
    public ChairEntityAttribute<Vector> setAttributeValue(Vector value) {
        entity.setRelativeLocation(value);
        return this;
    }

    @Override
    public boolean support(EntityType e) {
        return true;
    }

    @Override
    public boolean applyAttribute(Entity e) {
        return false;
    }

    @Override
    public Vector extractAttribute(Entity e) {
        return new Vector(0,0,0);
    }

    @Override
    public ChairEntityAttribute clone() {
        return new AttributeRelativeLocation(entity);
    }
}
