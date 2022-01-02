package kr.syeyoung.chair.gui.propedit;

import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import com.bergerkiller.bukkit.common.map.MapPlayerInput;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import kr.syeyoung.chair.attributes.ChairEntityAttribute;
import kr.syeyoung.chair.data.SerializedEulerAngle;
import kr.syeyoung.chair.gui.MapWidgetSliderInput;
import org.bukkit.util.EulerAngle;

public abstract class PropEditEulerAngle extends PropEditMenu<SerializedEulerAngle> {

    public PropEditEulerAngle(ChairEntityAttribute<SerializedEulerAngle> entityAttribute) {
        super(entityAttribute);
    }

    MapWidgetSliderInput xInput, yInput, zInput;

    @Override
    public void onDraw() {
        super.onDraw();
        this.view.draw(MapFont.MINECRAFT, 10, 28, MapColorPalette.COLOR_BLACK, "X:");
        this.view.draw(MapFont.MINECRAFT, 10, 48, MapColorPalette.COLOR_BLACK, "Y:");
        this.view.draw(MapFont.MINECRAFT, 10, 68, MapColorPalette.COLOR_BLACK, "Z:");
    }

    @Override
    public void onAttached() {
        super.onAttached();

        EulerAngle angle = getEntityAttribute().getAttributeValue();

        xInput = new MapWidgetSliderInput(Math.round(Math.toDegrees(angle.getX())), 1, 0, 360) {
            @Override
            public void onValueUpdate(double value) {
                ChairEntityAttribute<SerializedEulerAngle> angle = getEntityAttribute();
                SerializedEulerAngle sea = angle.getAttributeValue();
                sea = new SerializedEulerAngle(sea.setX(Math.toRadians(value)));
                getEntityAttribute().setAttributeValue(sea);
                PropEditEulerAngle.this.onValueUpdate();
            }
        };
        xInput.setBounds(20, 25, getWidth() - 30, 16);

        yInput = new MapWidgetSliderInput(Math.round(Math.toDegrees(angle.getY())), 1, 0, 360) {
            @Override
            public void onValueUpdate(double value) {
                ChairEntityAttribute<SerializedEulerAngle> angle = getEntityAttribute();
                SerializedEulerAngle sea = angle.getAttributeValue();
                sea = new SerializedEulerAngle(sea.setY(Math.toRadians(value)));
                getEntityAttribute().setAttributeValue(sea);
                PropEditEulerAngle.this.onValueUpdate();
            }
        };
        yInput.setBounds(20, 45, getWidth() - 30, 16);
        zInput = new MapWidgetSliderInput(Math.round(Math.toDegrees(angle.getZ())), 1, 0, 360) {
            @Override
            public void onValueUpdate(double value) {
                ChairEntityAttribute<SerializedEulerAngle> angle = getEntityAttribute();
                SerializedEulerAngle sea = angle.getAttributeValue();
                sea = new SerializedEulerAngle(sea.setZ(Math.toRadians(value)));
                getEntityAttribute().setAttributeValue(sea);
                PropEditEulerAngle.this.onValueUpdate();
            }
        };
        zInput.setBounds(20, 65, getWidth() - 30, 16);

        addWidget(xInput);
        addWidget(yInput);
        addWidget(zInput);
    }
}
