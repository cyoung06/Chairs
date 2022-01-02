package kr.syeyoung.chair.gui;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import kr.syeyoung.chair.data.ChairEditEntity;
import kr.syeyoung.chair.data.ChairEditSession;
import org.bukkit.entity.EntityType;

import javax.swing.text.html.parser.Entity;
import java.awt.*;

public class ChairEntitySelector extends MapWidget {

    private ChairEditSession editSession;

    private int selectedIndex;

    private int startIndex = 0;

    private int length = 7;

    public ChairEntitySelector(ChairEditSession editSession, int width, int maxHeight) {
        this.editSession = editSession;
        length = maxHeight / 12;
        setSize(width, length * 12);
        setFocusable(true);
        resetWidgets();
    }

    @Override
    public void onKeyPressed(MapKeyEvent event) {
        boolean invalidate = false;
        if (event.getKey() == MapPlayerInput.Key.DOWN) {
            if (selectedIndex < editSession.getRepresentation().size()) {
                editSession.getRepresentation().get(selectedIndex).highlight(false);
                selectedIndex += 1;
                if (selectedIndex != editSession.getRepresentation().size())
                    editSession.getRepresentation().get(selectedIndex).highlight(true);
                invalidate = true;
            }
        } else if (event.getKey() == MapPlayerInput.Key.UP) {
            if (selectedIndex > 0) {
                if (selectedIndex != editSession.getRepresentation().size())
                    editSession.getRepresentation().get(selectedIndex).highlight(false);
                selectedIndex -= 1;
                editSession.getRepresentation().get(selectedIndex).highlight(true);
                invalidate = true;
            }
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

    MapWidgetButton prewidget = new MapWidgetButton() {
        @Override
        public void onActivate() {
            ((ChairEditorGui)this.display).setMenu(new EntitySelector() {
                @Override
                public void onSelect(EntityType eType) {
                    editSession.getRepresentation().add(new ChairEditEntity(editSession, eType, false));
                    removeWidget();
                    resetWidgets();
                }
            });
        }
    };

    public void resetWidgets() {
        clearWidgets();
        for (int i = startIndex, j = 0; i < Math.min(editSession.getRepresentation().size()+1, startIndex + length); i++, j++) {
            MapWidget widget;
            if (i == editSession.getRepresentation().size()) {
                prewidget.setText("+");
                widget = prewidget;
            } else {
                widget = new ChairEntityElement(editSession.getRepresentation().get(i), editSession);
            }
            widget.setBounds(0, j*12, getWidth(), 12);
            addWidget(widget);
            if (i == selectedIndex) {
                widget.focus();
            }
        }
        invalidate();
    }
}
