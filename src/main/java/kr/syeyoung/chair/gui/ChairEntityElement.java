package kr.syeyoung.chair.gui;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import kr.syeyoung.chair.Chairs;
import kr.syeyoung.chair.data.ChairEditEntity;
import kr.syeyoung.chair.data.ChairEditSession;
import org.bukkit.entity.EntityType;

import java.awt.*;

public class ChairEntityElement extends MapWidget {

    private ChairEditEntity entity;
    private ChairEditSession session;

    public ChairEntityElement(ChairEditEntity entity, ChairEditSession session) {
        this.entity = entity;
        this.setFocusable(true);
        this.session = session;
    }

    @Override
    public void onDraw() {
        String name = entity.getEntityType().name();
        this.view.draw(MapFont.MINECRAFT, 2,2, MapColorPalette.COLOR_BLACK, name);
        if (this.isActivated()) {
            this.view.drawRectangle(0,0, getWidth(), getHeight(), MapColorPalette.COLOR_BLACK);
        }
    }

    MapWidgetButton removeButton = new MapWidgetButton() {
        public void onActivate() {
            ((ChairEditorGui)this.display).setMenu(new AreYouSure("정말로 삭제하시겠습니까?") {
                @Override
                public void callback(boolean agreed) {
                    if (agreed) {
                        ChairEntityElement.this.removeWidget();
                        entity.remove();
                        ((ChairEntitySelector)ChairEntityElement.this.getParent()).resetWidgets();
                    }
                }
            });
        }
    };
    MapWidgetButton editButton = new MapWidgetButton() {
        public void onActivate() {
            ((ChairEditorGui)this.display).setMenu(new ChairPropertiesList(entity));
        }
    };

    @Override
    public void onActivate() {
        super.onActivate();
        editButton.setIcon(this.display.loadTexture("kr/syeyoung/chair/res/edit.png"));
        editButton.setShowBorder(true);
        editButton.setBounds(getWidth()-51,0,36,12);
        addWidget(editButton);
        removeButton.setIcon(this.display.loadTexture("kr/syeyoung/chair/res/remove.png"));
        removeButton.setShowBorder(true);
        removeButton.setBounds(getWidth()-13,0,12,12);
        addWidget(removeButton);
        invalidate();
    }

    @Override
    public void onDeactivate() {
        clearWidgets();
        invalidate();
    }

    @Override
    public void onFocus() {
        activate();
        invalidate();
    }
}
