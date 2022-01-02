package kr.syeyoung.chair.gui;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import kr.syeyoung.chair.attributes.*;
import kr.syeyoung.chair.data.ChairEditEntity;
import kr.syeyoung.chair.data.ChairEntity;

public class ChairPropertiesList extends MapWidget {
    private ChairEditEntity entity;

    private int selectedIndex;

    private int startIndex = 0;

    private int length = 8;

    private ChairEntityAttribute[] attributes = new ChairEntityAttribute[4];

    public ChairPropertiesList(ChairEditEntity entity) {
        this.entity = entity;
        setFocusable(true);
        setDepthOffset(4);
        setSize(120, 120);

        attributes[0] = new AttributeRelativeLocation(entity);
        attributes[1] = new AttributeRelativeYaw(entity);
        attributes[2] = new AttributeRelativePitch(entity);
        attributes[3] = new AttributeIssit(entity);
    }


    @Override
    public void onAttached() {
        super.onAttached();
        focus();
    }

    @Override
    public void onFocus() {
        super.onFocus();
        activate();
        resetWidgets();
    }

    @Override
    public void onDraw() {
        this.view.fillRectangle(0,0, this.getWidth(), this.getHeight(), MapColorPalette.COLOR_WHITE);
        this.view.drawRectangle(0,0 , this.getWidth(), this.getHeight(), MapColorPalette.COLOR_BLUE);
    }

    @Override
    public void onKeyPressed(MapKeyEvent event) {
        boolean invalidate = false;
        if (event.getKey() == MapPlayerInput.Key.DOWN) {
            if (selectedIndex < (entity.getAttributes().size()+3)) {
                selectedIndex += 1;
                invalidate = true;
            }
        } else if (event.getKey() == MapPlayerInput.Key.UP) {
            if (selectedIndex > 0) {
                selectedIndex -= 1;
                invalidate = true;
            }
        } else if (event.getKey() == MapPlayerInput.Key.BACK) {
            removeWidget();
            return;
        } else {
            super.onKeyPressed(event);
        }
        if (invalidate) {
            if (selectedIndex < startIndex) {
                startIndex = selectedIndex;
            } else if (selectedIndex >= startIndex+length) {
                startIndex = selectedIndex - length + 1;
            }
            resetWidgets();
        }
    }

    public void resetWidgets() {
        clearWidgets();
        for (int i = startIndex, j = 0; i < Math.min(entity.getAttributes().size() + 4, startIndex + length); i++, j++) {
            MapWidget widget;
            if (i > 3) {
                widget =new ChairPropertiesElement(entity, entity.getAttributes().get(i - 4));
            } else {
                widget = new ChairPropertiesElement(entity, attributes[i]);
            }
            widget.setBounds(0, j*widget.getHeight(), 120, 15);
            addWidget(widget);
            if (i == selectedIndex) {
                widget.focus();
            }
        }
        invalidate();
    }
}
