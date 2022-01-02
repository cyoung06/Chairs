package kr.syeyoung.chair.data;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.EulerAngle;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("EulerAngle")
public class SerializedEulerAngle extends EulerAngle implements ConfigurationSerializable {
    public SerializedEulerAngle(EulerAngle angle) {
        super(angle.getX(), angle.getY(), angle.getZ());
    }

    public SerializedEulerAngle(double x, double y, double z) {
        super(x,y,z);
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("x", this.getX());
        map.put("y", this.getY());
        map.put("z", this.getZ());
        return map;
    }

    public static SerializedEulerAngle deserialize(Map<String,Object> objectMap) {
        return new SerializedEulerAngle((double)objectMap.get("x"), (double)objectMap.get("y"), (double)objectMap.get("z"));
    }
}
