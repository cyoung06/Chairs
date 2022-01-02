package kr.syeyoung.chair.attributes;

import kr.syeyoung.chair.data.ChairEditEntity;
import kr.syeyoung.chair.gui.propedit.PropEditDouble;
import kr.syeyoung.chair.gui.propedit.PropEditMenu;
import kr.syeyoung.chair.gui.propedit.PropEditVector;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

public class AttributeRelativePitch extends ChairEntityAttribute<Double> {

    private ChairEditEntity entity;

    public AttributeRelativePitch(ChairEditEntity entity) {
        this.entity = entity;
    }


    @Override
    public String getAttributeName() {
        return "relativePitch";
    }

    @Override
    public String getKoreanName() {
        return "시야 세로";
    }

    @Override
    public PropEditMenu<Double> getEditMenu(Runnable callbackUpdate) {
        return new PropEditDouble(this, 90.0, -90.0) {
            @Override
            public void onValueUpdate() {
                callbackUpdate.run();
            }
        };
    }


    public Double getAttributeValue() {
        return entity.getRelativePitch();
    }
    public ChairEntityAttribute<Double> setAttributeValue(Double value) {
        entity.setRelativePitch(value);
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
    public Double extractAttribute(Entity e) {
        return 0.0;
    }

    @Override
    public ChairEntityAttribute clone() {
        return new AttributeRelativePitch(entity);
    }
}
