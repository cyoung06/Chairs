package kr.syeyoung.chair.gui.propedit;

import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import kr.syeyoung.chair.attributes.ChairEntityAttribute;
import kr.syeyoung.chair.data.SerializedEulerAngle;
import kr.syeyoung.chair.gui.MapWidgetSliderInput;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public abstract class PropEditVector extends PropEditMenu<Vector> {

    public PropEditVector(ChairEntityAttribute<Vector> entityAttribute) {
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

        Vector vector = getEntityAttribute().getAttributeValue();

        xInput = new MapWidgetSliderInput(vector.getX(), 0.01, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY) {
            @Override
            public void onValueUpdate(double value) {
                ChairEntityAttribute<Vector> angle = getEntityAttribute();
                Vector sea = angle.getAttributeValue();
                sea = sea.setX(value);
                getEntityAttribute().setAttributeValue(sea);
                PropEditVector.this.onValueUpdate();
            }
        };
        xInput.setBounds(20, 25, getWidth() - 30, 16);

        yInput = new MapWidgetSliderInput(vector.getY(), 0.01, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY) {
            @Override
            public void onValueUpdate(double value) {
                ChairEntityAttribute<Vector> angle = getEntityAttribute();
                Vector sea = angle.getAttributeValue();
                sea = sea.setY(value);
                getEntityAttribute().setAttributeValue(sea);
                PropEditVector.this.onValueUpdate();
            }
        };
        yInput.setBounds(20, 45, getWidth() - 30, 16);
        zInput = new MapWidgetSliderInput(vector.getZ(), 0.01, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY) {
            @Override
            public void onValueUpdate(double value) {
                ChairEntityAttribute<Vector> angle = getEntityAttribute();
                Vector sea = angle.getAttributeValue();
                sea = sea.setZ(value);
                getEntityAttribute().setAttributeValue(sea);
                PropEditVector.this.onValueUpdate();
            }
        };
        zInput.setBounds(20, 65, getWidth() - 30, 16);

        addWidget(xInput);
        addWidget(yInput);
        addWidget(zInput);
    }
}
