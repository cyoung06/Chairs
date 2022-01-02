package kr.syeyoung.chair.attributes;

import kr.syeyoung.chair.gui.propedit.PropEditItemStack;
import kr.syeyoung.chair.gui.propedit.PropEditMenu;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class AttributeChestplate extends ChairEntityAttribute<ItemStack> {
    @Override
    public String getAttributeName() {
        return "chestplate";
    }

    @Override
    public String getKoreanName() {
        return "갑옷 아이템";
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
    public boolean support(EntityType e) {
        return LivingEntity.class.isAssignableFrom(e.getEntityClass());
    }
    @Override
    public boolean applyAttribute(Entity e) {
        if (e instanceof LivingEntity) {
            EntityEquipment equipment = ((LivingEntity) e).getEquipment();
            equipment.setChestplate(getAttributeValue());
            return true;
        }
        return false;
    }
    @Override
    public ItemStack extractAttribute(Entity e) {
        if (e instanceof LivingEntity) {
            EntityEquipment equipment = ((LivingEntity) e).getEquipment();
            setAttributeValue(equipment.getChestplate());
            return equipment.getChestplate();
        }
        return null;
    }

    @Override
    public ChairEntityAttribute<ItemStack> clone() {
        return new AttributeChestplate().setAttributeValue(getAttributeValue());
    }
}
