package kr.syeyoung.chair.attributes;

import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AttributeFactory {
    private static Map<String, ChairEntityAttribute> examples = new HashMap<>();

    static {
        examples.put("baby", new AttributeBaby());
        examples.put("boots", new AttributeBoots());
        examples.put("chestplate", new AttributeChestplate());
        examples.put("helmet", new AttributeHelmet());
        examples.put("invisible", new AttributeInvisible());
        examples.put("lefthand", new AttributeLeftHand());
        examples.put("leggings", new AttributeLeggings());
        examples.put("pos_body", new AttributePosBody());
        examples.put("pos_head", new AttributePosHead());
        examples.put("pos_left_arm", new AttributePosLeftArm());
        examples.put("pos_left_leg", new AttributePosLeftLeg());
        examples.put("pos_right_arm", new AttributePosRightArm());
        examples.put("pos_right_leg", new AttributePosRightLeg());
        examples.put("righthand", new AttributeRightHand());
        examples.put("showbaseplate", new AttributeShowBasePlate());
        examples.put("showarm", new AttributeShowArm());
        examples.put("size", new AttributeSize());
    }

    public static <T> ChairEntityAttribute<T> getAttribute(String name, T value){
        ChairEntityAttribute<T> attribute = examples.get(name).clone();
        attribute.setAttributeValue(value);
        return attribute;
    }

    public static <T> ChairEntityAttribute<T> getAttribute(String name) {
        ChairEntityAttribute<T> attribute =  examples.get(name).clone();
        return attribute;
    }

    public static List<ChairEntityAttribute> getAttributes(EntityType eType) {
        return examples.values().stream().filter(e -> e.support(eType)).map(e -> e.clone()).collect(Collectors.toList());
    }
}
