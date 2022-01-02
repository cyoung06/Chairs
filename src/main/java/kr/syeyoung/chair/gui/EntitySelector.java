package kr.syeyoung.chair.gui;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import org.bukkit.entity.EntityType;

public abstract class EntitySelector extends MapWidget {
    private int selectedIndex = 0;
    private int length = 6;
    private int startIndex = 0;

    private static final EntityType[] types = EntityType.values();

    public EntitySelector() {
        setFocusable(true);
        setDepthOffset(4);
        setSize(100, length * 12);
    }

    @Override
    public void onDraw() {
        this.view.fillRectangle(0,0, this.getWidth(), this.getHeight(), MapColorPalette.COLOR_WHITE);
        this.view.drawRectangle(0,0 , this.getWidth(), this.getHeight(), MapColorPalette.COLOR_BLUE);
        for (int i = startIndex, j = 0; i < Math.min(selectedIndex + length, types.length); i++, j++) {
            this.view.draw(MapFont.MINECRAFT, 2, j * 12+2, MapColorPalette.COLOR_BLACK, types[i].name());
            if (i == selectedIndex) {
                this.view.drawRectangle(0,j*12, getWidth(), 12, MapColorPalette.COLOR_BLACK);
            }
        }
    }

    public abstract void onSelect(EntityType eType);


    @Override
    public void onAttached() {
        super.onAttached();
        activate();
    }

    @Override
    public void onKeyPressed(MapKeyEvent event) {
        if (!isActivated()) {
            return;
        }
        if (event.getKey() == MapPlayerInput.Key.BACK && isActivated()) {
            removeWidget();
            return;
        }

        boolean invalidate = false;
        if (event.getKey() == MapPlayerInput.Key.DOWN) {
            if (selectedIndex + 1 < types.length) {
                selectedIndex += 1;
                invalidate = true;
            }
        } else if (event.getKey() == MapPlayerInput.Key.UP) {
            if (selectedIndex > 0) {
                selectedIndex -= 1;
                invalidate = true;
            }
        } else if (event.getKey() == MapPlayerInput.Key.ENTER) {
            onSelect(types[selectedIndex]);
        } else {
            super.onKeyPressed(event);
        }
        if (invalidate) {
            if (selectedIndex < startIndex) {
                startIndex = selectedIndex;
            } else if (selectedIndex >= startIndex+length) {
                startIndex = selectedIndex - length + 1;
            }
            invalidate();
        }
    }
}
