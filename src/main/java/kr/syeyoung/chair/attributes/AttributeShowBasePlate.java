package kr.syeyoung.chair.attributes;

import kr.syeyoung.chair.gui.propedit.PropEditBoolean;
import kr.syeyoung.chair.gui.propedit.PropEditMenu;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class AttributeShowBasePlate extends ChairEntityAttribute<Boolean> {
    public AttributeShowBasePlate() {
        this.setAttributeValue(true);
    }
    @Override
    public String getAttributeName() {
        return "showbaseplate";
    }

    @Override
    public boolean support(EntityType e) {
        return e == EntityType.ARMOR_STAND;
    }
    @Override
    public boolean applyAttribute(Entity e) {
        if (e instanceof ArmorStand) {
            ((ArmorStand) e).setBasePlate(getAttributeValue());
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
        return "바닥 보야짐";
    }
    @Override
    public Boolean extractAttribute(Entity e) {
        if (e instanceof ArmorStand) {
            setAttributeValue(((ArmorStand) e).hasBasePlate());
            return getAttributeValue();
        }
        return false;
    }
    @Override
    public ChairEntityAttribute<Boolean> clone() {
        return new AttributeShowBasePlate().setAttributeValue(getAttributeValue());
    }
}
