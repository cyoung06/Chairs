package kr.syeyoung.chair.gui;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import kr.syeyoung.chair.attributes.ChairEntityAttribute;
import kr.syeyoung.chair.data.ChairEditEntity;
import kr.syeyoung.chair.util.NanumFont;

public class ChairPropertiesElement extends MapWidget {
    private ChairEditEntity entity;
    private ChairEntityAttribute attribute;

    public ChairPropertiesElement(ChairEditEntity editEntity, ChairEntityAttribute attribute) {
        this.entity = editEntity;
        this.attribute = attribute;
        setFocusable(true);
        setSize(120, 15);
    }

    @Override
    public void onDraw() {
        String name = attribute.getKoreanName();
        this.view.draw(NanumFont.NormalMapNanumFont, 2,2, MapColorPalette.COLOR_BLACK, name);
        if (this.isActivated()) {
            this.view.drawRectangle(0,0, getWidth(), getHeight(), MapColorPalette.COLOR_BLACK);
        }
    }

    @Override
    public void onFocus() {
        super.focus();
        activate();
        invalidate();
    }

    @Override
    public void onKeyPressed(MapKeyEvent event) {
        if (!isActivated()) {
            super.onKeyPressed(event);
            return;
        }
        if (event.getKey() == MapPlayerInput.Key.ENTER) {
            ((ChairEditorGui)this.display).setMenu(attribute.getEditMenu(entity::update));
        } else {
            super.onKeyPressed(event);
        }
    }
}
