package kr.syeyoung.chair.attributes;

import kr.syeyoung.chair.data.SerializedEulerAngle;
import kr.syeyoung.chair.gui.propedit.PropEditEulerAngle;
import kr.syeyoung.chair.gui.propedit.PropEditMenu;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class AttributePosLeftArm extends ChairEntityAttribute<SerializedEulerAngle> {
    public AttributePosLeftArm() {
        this.setAttributeValue(new SerializedEulerAngle(0,0,0));
    }
    @Override
    public String getAttributeName() {
        return "pos_left_arm";
    }

    @Override
    public PropEditMenu<SerializedEulerAngle> getEditMenu(Runnable callbackUpdate) {
        return new PropEditEulerAngle(this) {
            @Override
            public void onValueUpdate() {
                callbackUpdate.run();
            }
        };
    }
    @Override
    public boolean support(EntityType e) {
        return e == EntityType.ARMOR_STAND;
    }
    @Override
    public boolean applyAttribute(Entity e) {
        if (e instanceof ArmorStand) {
            ((ArmorStand) e).setLeftArmPose(getAttributeValue());
            return true;
        }
        return false;
    }
    @Override
    public String getKoreanName() {
        return "왼팔 위치";
    }
    @Override
    public SerializedEulerAngle extractAttribute(Entity e) {
        if (e instanceof ArmorStand) {
            setAttributeValue(new SerializedEulerAngle(((ArmorStand) e).getLeftArmPose()));
            return getAttributeValue();
        }
        return null;
    }
    @Override
    public ChairEntityAttribute<SerializedEulerAngle> clone() {
        return new AttributePosLeftArm().setAttributeValue(getAttributeValue());
    }
}
