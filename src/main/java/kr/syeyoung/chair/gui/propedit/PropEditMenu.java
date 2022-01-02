package kr.syeyoung.chair.gui.propedit;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import kr.syeyoung.chair.attributes.ChairEntityAttribute;
import kr.syeyoung.chair.gui.ChairPropertiesList;
import kr.syeyoung.chair.util.NanumFont;

public abstract class PropEditMenu<T> extends MapWidget {
    private ChairEntityAttribute<T> entityAttribute;

    public PropEditMenu(ChairEntityAttribute<T> entityAttribute) {
        this.entityAttribute = entityAttribute;
        setFocusable(true);
        setDepthOffset(8);
        setSize(100,100);
    }

    public ChairEntityAttribute<T> getEntityAttribute() {
        return entityAttribute;
    }


    @Override
    public void onDraw() {
        this.view.fillRectangle(0,0, this.getWidth(), this.getHeight(), MapColorPalette.getColor(198, 198, 198));
        this.view.drawRectangle(0,0 , this.getWidth(), this.getHeight(), MapColorPalette.COLOR_BLACK);
        this.view.draw(NanumFont.NormalMapNanumFont, 3,3, MapColorPalette.COLOR_BLACK, entityAttribute.getKoreanName());
        this.view.drawLine(0,15, getWidth(), 15, MapColorPalette.COLOR_BLACK);
    }

    public abstract void onValueUpdate();

    @Override
    public void onAttached() {
        super.onAttached();
        focus();
    }

    @Override
    public void onFocus() {
        super.onFocus();
        activate();
    }

    @Override
    public void onKeyPressed(MapKeyEvent event) {
        if (!isActivated()) {
            super.onKeyPressed(event);
            return;
        }
        if (event.getKey() == MapPlayerInput.Key.BACK && isActivated()) {
            deactivate();
            removeWidget();
            invalidate();
            this.parent.focus();
            this.parent.invalidate();
            this.parent.activate();
            this.parent.invalidate();
            return;
        }
        super.onKeyPressed(event);
    }
}
