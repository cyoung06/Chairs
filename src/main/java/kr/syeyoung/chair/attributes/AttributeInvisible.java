package kr.syeyoung.chair.attributes;

import kr.syeyoung.chair.gui.propedit.PropEditBoolean;
import kr.syeyoung.chair.gui.propedit.PropEditMenu;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AttributeInvisible extends ChairEntityAttribute<Boolean> {
    public AttributeInvisible() {
        this.setAttributeValue(false);
    }
    @Override
    public String getAttributeName() {
        return "invisible";
    }

    @Override
    public boolean support(EntityType e) {
        return e == EntityType.ARMOR_STAND || LivingEntity.class.isAssignableFrom(e.getEntityClass());
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
    public boolean applyAttribute(Entity e) {
        if (e instanceof ArmorStand) {
            ((ArmorStand) e).setVisible(!getAttributeValue());
            return true;
        } else if (e instanceof LivingEntity) {
            if (getAttributeValue()) {
                ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false), true);
            } else {
                ((LivingEntity) e).removePotionEffect(PotionEffectType.INVISIBILITY);
            }

        }
        return false;
    }

    @Override
    public String getKoreanName() {
        return "투명 상태";
    }
    @Override
    public Boolean extractAttribute(Entity e) {
        if (e instanceof ArmorStand) {
            setAttributeValue(!((ArmorStand) e).isVisible());
            return getAttributeValue();
        } else if (e instanceof LivingEntity) {
            setAttributeValue(((LivingEntity) e).hasPotionEffect(PotionEffectType.INVISIBILITY));
            return getAttributeValue();
        }
        return null;
    }
    @Override
    public ChairEntityAttribute<Boolean> clone() {
        return new AttributeInvisible().setAttributeValue(getAttributeValue());
    }
}
