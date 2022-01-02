package kr.syeyoung.chair.attributes;

import kr.syeyoung.chair.gui.propedit.PropEditMenu;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public abstract class ChairEntityAttribute<T> implements Cloneable {
    private T value;

    public abstract String getAttributeName();
    public abstract String getKoreanName();

    public abstract PropEditMenu<T> getEditMenu(Runnable callbackUpdate);

    public T getAttributeValue() {return value;}
    public ChairEntityAttribute<T> setAttributeValue(T value) {this.value = value; return this;}

    public abstract boolean support(EntityType e);

    public abstract boolean applyAttribute(Entity e);
    public abstract T extractAttribute(Entity e);

    public abstract ChairEntityAttribute<T> clone();
}
