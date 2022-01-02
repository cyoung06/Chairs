package kr.syeyoung.chair.attributes;

import kr.syeyoung.chair.data.ChairEditEntity;
import kr.syeyoung.chair.gui.propedit.PropEditBoolean;
import kr.syeyoung.chair.gui.propedit.PropEditMenu;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class AttributeIssit extends ChairEntityAttribute<Boolean> {

    private ChairEditEntity entity;

    public AttributeIssit(ChairEditEntity entity) {
        this.entity = entity;
    }


    @Override
    public String getAttributeName() {
        return "sit";
    }

    @Override
    public String getKoreanName() {
        return "앉을 수 있음";
    }

    @Override
    public PropEditMenu<Boolean> getEditMenu(Runnable callbackUpdate) {
        return new PropEditBoolean(this) {
            @Override
            public void onValueUpdate() {
                callbackUpdate.run();
            }
        };
    }


    public Boolean getAttributeValue() {
        return entity.isChair();
    }
    public ChairEntityAttribute<Boolean> setAttributeValue(Boolean value) {
        entity.setSit(value);
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
    public Boolean extractAttribute(Entity e) {
        return false;
    }

    @Override
    public ChairEntityAttribute clone() {
        return new AttributeIssit(entity);
    }
}
