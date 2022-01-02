package kr.syeyoung.chair.gui;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapPlayerInput;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import kr.syeyoung.chair.util.NanumFont;

import java.awt.*;

public abstract class AreYouSure extends MapWidget {


    MapWidgetButton yes = new MapWidgetButton() {
        public void onActivate() {
            AreYouSure.this.removeWidget();
            callback(true);
        }
    };
    MapWidgetButton no = new MapWidgetButton() {
        public void onActivate() {
            AreYouSure.this.removeWidget();
            callback(false);
        }
    };

    private String question;

    public AreYouSure(String question) {
        this.setFocusable(true);
        this.setDepthOffset(4);
        setSize(100, 100);
        this.question = question;
    }

    public void onDraw() {
        this.view.fillRectangle(0,0, getWidth(), getHeight(), MapColorPalette.COLOR_WHITE);
        this.view.drawRectangle(0,0, getWidth(), getHeight(), MapColorPalette.COLOR_BLUE);
        Dimension dimension = this.view.calcFontSize(NanumFont.BigMapNanumFont, question);
        int x = (getWidth()-dimension.width) / 2;
        this.view.draw(NanumFont.BigMapNanumFont, x,10, MapColorPalette.COLOR_BLACK, question);
    }

    public abstract void callback(boolean agreed);

    @Override
    public void onAttached() {
        super.onAttached();
        yes.setIcon(this.display.loadTexture("kr/syeyoung/chair/res/yes.png"));
        no.setIcon(this.display.loadTexture("kr/syeyoung/chair/res/no.png"));
        yes.setBounds(13,75, 24, 12);
        no.setBounds(57,75, 30, 12);
        addWidget(no);
        addWidget(yes);
        activate();
    }

    @Override
    public void onKeyPressed(MapKeyEvent event) {
        if (!isActivated()) {
            return;
        }
        if (event.getKey() == MapPlayerInput.Key.BACK && isActivated()) {
            removeWidget();
            callback(false);
            return;
        }
        super.onKeyPressed(event);
    }
}
