package kr.syeyoung.chair.attributes;

import kr.syeyoung.chair.gui.propedit.PropEditItemStack;
import kr.syeyoung.chair.gui.propedit.PropEditMenu;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class AttributeLeggings extends ChairEntityAttribute<ItemStack> {
    @Override
    public String getAttributeName() {
        return "leggings";
    }

    @Override
    public boolean support(EntityType e) {
        return LivingEntity.class.isAssignableFrom(e.getEntityClass());
    }
    @Override
    public boolean applyAttribute(Entity e) {
        if (e instanceof LivingEntity) {
            EntityEquipment equipment = ((LivingEntity) e).getEquipment();
            equipment.setLeggings(getAttributeValue());
            return true;
        }
        return false;
    }
    @Override
    public PropEditMenu<ItemStack> getEditMenu(Runnable callbackUpdate) {
        return new PropEditItemStack(this) {
            @Override
            public void onValueUpdate() {
                callbackUpdate.run();
            }
        };
    }

    @Override
    public String getKoreanName() {
        return "바지 아이템";
    }
    @Override
    public ItemStack extractAttribute(Entity e) {
        if (e instanceof LivingEntity) {
            EntityEquipment equipment = ((LivingEntity) e).getEquipment();
            setAttributeValue(equipment.getLeggings());
            return equipment.getLeggings();
        }
        return null;
    }
    @Override
    public ChairEntityAttribute<ItemStack> clone() {
        return new AttributeLeggings().setAttributeValue(getAttributeValue());
    }
}
