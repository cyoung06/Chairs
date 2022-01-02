package kr.syeyoung.chair.attributes;

import kr.syeyoung.chair.gui.propedit.PropEditBoolean;
import kr.syeyoung.chair.gui.propedit.PropEditMenu;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class AttributeShowArm extends ChairEntityAttribute<Boolean> {
    public AttributeShowArm() {
        this.setAttributeValue(false);
    }
    @Override
    public String getAttributeName() {
        return "showarm";
    }

    @Override
    public boolean support(EntityType e) {
        return e == EntityType.ARMOR_STAND;
    }
    @Override
    public boolean applyAttribute(Entity e) {
        if (e instanceof ArmorStand) {
            ((ArmorStand) e).setArms(getAttributeValue());
            return true;
        }
        return false;
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
    @Override
    public String getKoreanName() {
        return "팔 보여짐";
    }
    @Override
    public Boolean extractAttribute(Entity e) {
        if (e instanceof ArmorStand) {
            setAttributeValue(((ArmorStand) e).hasArms());
            return getAttributeValue();
        }
        return false;
    }
    @Override
    public ChairEntityAttribute<Boolean> clone() {
        return new AttributeShowArm().setAttributeValue(getAttributeValue());
    }
}
