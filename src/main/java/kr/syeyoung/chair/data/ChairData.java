package kr.syeyoung.chair.data;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SerializableAs("ChairData")
public class ChairData implements ConfigurationSerializable {
    private List<ChairEntity> entityList = new ArrayList<>();

    private ItemStack chairItem;

    private String chairIdentifier;
    private String visibleChairName;

    private File editorFile;

    public void setFile(File f) {
        this.editorFile = f;
    }

    public File getFile() {
        return this.editorFile;
    }

    public String getChairIdentifier() {
        return chairIdentifier;
    }

    public void setChairIdentifier(String chairIdentifier) {
        this.chairIdentifier = chairIdentifier;
    }

    public String getVisibleChairName() {
        return visibleChairName;
    }

    public void setVisibleChairName(String visibleChairName) {
        this.visibleChairName = visibleChairName;
    }

    public List<ChairEntity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<ChairEntity> entityList) {
        this.entityList = entityList;
    }

    public ItemStack getChairItem() {
        return chairItem;
    }

    public void setChairItem(ItemStack chairItem) {
        this.chairItem = chairItem;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", chairIdentifier);
        map.put("name", visibleChairName);
        map.put("parts", entityList);
        map.put("item", chairItem);
        return map;
    }

    public static ChairData deserialize(Map<String,Object> map) {
        ChairData cd = new ChairData();
        cd.setChairIdentifier((String) map.get("id"));
        cd.setVisibleChairName((String) map.get("name"));
        cd.setEntityList((List<ChairEntity>) map.get("parts"));
        cd.setChairItem((ItemStack) map.get("item"));
        return cd;
    }
}
