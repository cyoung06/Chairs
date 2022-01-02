package kr.syeyoung.chair.gui;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput;
import com.bergerkiller.bukkit.common.map.MapTexture;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;

import java.awt.*;
import java.text.DecimalFormat;

public abstract class MapWidgetSliderInput extends MapWidget {
    double startValue; double step; double min; double max;

    private MapTexture left;
    private MapTexture right;

    public MapWidgetSliderInput(double startValue, double step, double min, double max) {
        setFocusable(true);
        this.startValue = startValue;
        this.step = step;
        this.min = min;
        this.max = max;
    }

    private static final DecimalFormat format = new DecimalFormat("#######0.00");

    public void onDraw() {
        byte color;
        if (isActivated()) {
            color = MapColorPalette.COLOR_BLACK;
        } else if (isFocused()) {
            color = MapColorPalette.getColor(100,100,100);
        } else {
            color = MapColorPalette.getColor(200,200,200);
        }

        this.view.fillRectangle(0,0,getWidth(),getHeight(), MapColorPalette.COLOR_WHITE);
        this.view.drawRectangle(0,0,getWidth(),getHeight(), color);
        this.view.draw(left, 0,getHeight()/2 - 5);
        this.view.draw(right, getWidth() - 10,getHeight()/2 - 5);
        this.view.drawLine(10,0,10,getHeight(),color);
        this.view.drawLine(getWidth() - 10,0,getWidth() - 10,getHeight(),color);
        String number = format.format(startValue);

        Dimension dimension = this.view.calcFontSize(MapFont.MINECRAFT, number);

        this.view.draw(MapFont.MINECRAFT,  (getWidth() - dimension.width) / 2, ( getHeight() - dimension.height) / 2, MapColorPalette.COLOR_BLACK, number);
    }

    public abstract void  onValueUpdate(double value);

    @Override
    public void onAttached() {
        super.onAttached();
        left = this.display.loadTexture("kr/syeyoung/chair/res/left.png");
        right = this.display.loadTexture("kr/syeyoung/chair/res/right.png");
    }

    @Override
    public void onKeyPressed(MapKeyEvent event) {
        if (!isActivated()) {
            super.onKeyPressed(event);
            return;
        }

        if (event.getKey() == MapPlayerInput.Key.LEFT) {
            double NewstartValue = Math.max(min, startValue - (event.getRepeat() > 10 ? step * 5: step));
            if (NewstartValue != startValue)
                onValueUpdate(NewstartValue);
            startValue = NewstartValue;
            invalidate();
        } else if (event.getKey() == MapPlayerInput.Key.RIGHT) {
            double NewstartValue = Math.min(startValue + (event.getRepeat() > 10 ? step * 5: step), max);
            if (NewstartValue != startValue)
                onValueUpdate(NewstartValue);
            startValue = NewstartValue;
            invalidate();
        } else if (event.getKey() == MapPlayerInput.Key.ENTER) {
            focus();
            activate();
            invalidate();
        } else {
            super.onKeyPressed(event);
        }
    }
}
