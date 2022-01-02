package kr.syeyoung.chair.attributes;

import kr.syeyoung.chair.gui.propedit.PropEditDouble;
import kr.syeyoung.chair.gui.propedit.PropEditInteger;
import kr.syeyoung.chair.gui.propedit.PropEditItemStack;
import kr.syeyoung.chair.gui.propedit.PropEditMenu;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class AttributeSize extends ChairEntityAttribute<Integer> {
    public AttributeSize() {
        this.setAttributeValue(0);
    }
    @Override
    public String getAttributeName() {
        return "size";
    }

    @Override
    public String getKoreanName() {
        return "크기";
    }
    @Override
    public PropEditMenu<Integer> getEditMenu(Runnable callbackUpdate) {
        return new PropEditInteger(this, Integer.MAX_VALUE, 0) {
            @Override
            public void onValueUpdate() {
                callbackUpdate.run();
            }
        };
    }
    @Override
    public boolean support(EntityType e) {
        return e == EntityType.SLIME;
    }

    @Override
    public boolean applyAttribute(Entity e) {
        if (e instanceof Slime) {
            ((Slime) e).setSize(getAttributeValue());
            return true;
        }
        return false;
    }

    @Override
    public Integer extractAttribute(Entity e) {
        if (e instanceof Slime) {
            setAttributeValue(((Slime) e).getSize());
            return ((Slime) e).getSize();
        }
        return null;
    }

    @Override
    public ChairEntityAttribute<Integer> clone() {
        return new AttributeSize().setAttributeValue(getAttributeValue());
    }
}
