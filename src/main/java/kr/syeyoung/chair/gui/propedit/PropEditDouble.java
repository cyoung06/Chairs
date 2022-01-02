package kr.syeyoung.chair.gui.propedit;

import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapFont;
import kr.syeyoung.chair.attributes.ChairEntityAttribute;
import kr.syeyoung.chair.gui.MapWidgetSliderInput;
import org.bukkit.util.Vector;

public abstract class PropEditDouble extends PropEditMenu<Double> {

    private double max;
    private double min;

    public PropEditDouble(ChairEntityAttribute<Double> entityAttribute, double max, double min) {

        super(entityAttribute);
        this.max = max;
        this.min = min;
    }

    MapWidgetSliderInput xInput;

    @Override
    public void onDraw() {
        super.onDraw();
    }

    @Override
    public void onAttached() {
        super.onAttached();

        Double value = getEntityAttribute().getAttributeValue();

        xInput = new MapWidgetSliderInput(value, 0.5, min, max) {
            @Override
            public void onValueUpdate(double value) {
                getEntityAttribute().setAttributeValue(value);
                PropEditDouble.this.onValueUpdate();
            }
        };
        xInput.setBounds(10, 25, getWidth() - 20, 16);


        addWidget(xInput);
    }
}
