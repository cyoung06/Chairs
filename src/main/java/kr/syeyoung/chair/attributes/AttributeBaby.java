package kr.syeyoung.chair.attributes;

import kr.syeyoung.chair.data.SerializedEulerAngle;
import kr.syeyoung.chair.gui.propedit.PropEditBoolean;
import kr.syeyoung.chair.gui.propedit.PropEditMenu;
import org.bukkit.entity.*;

public class AttributeBaby extends ChairEntityAttribute<Boolean> {
    public AttributeBaby() {
        this.setAttributeValue(false);
    }
    @Override
    public String getAttributeName() {
        return "baby";
    }

    @Override
    public String getKoreanName() {
        return "작음 상태";
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
    public boolean support(EntityType e) {
        return e == EntityType.ZOMBIE || e == EntityType.ARMOR_STAND || Ageable.class.isAssignableFrom(e.getEntityClass());
    }

    @Override
    public boolean applyAttribute(Entity e) {
        if (e instanceof Ageable) {
            if (getAttributeValue())
                ((Ageable) e).setBaby();
            else
                ((Ageable) e).setAdult();
            return true;
        } else if (e instanceof Zombie) {
            ((Zombie) e).setBaby(getAttributeValue());
            return true;
        } else if (e instanceof ArmorStand) {
            ((ArmorStand) e).setSmall(getAttributeValue());
            return true;
        }
        return false;
    }

    @Override
    public Boolean extractAttribute(Entity e) {
        if (e instanceof Ageable) {
            setAttributeValue(!((Ageable) e).isAdult());
            return !((Ageable) e).isAdult();
        } else if (e instanceof Zombie) {
            setAttributeValue(((Zombie) e).isBaby());
            return ((Zombie) e).isBaby();
        } else if (e instanceof ArmorStand) {
            setAttributeValue(((ArmorStand) e).isSmall());
            return ((ArmorStand) e).isSmall();
        }
        return false;
    }

    @Override
    public ChairEntityAttribute<Boolean> clone() {
        return new AttributeBaby().setAttributeValue(getAttributeValue());
    }
}
