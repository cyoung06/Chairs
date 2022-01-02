package kr.syeyoung.chair.gui.propedit;

import kr.syeyoung.chair.attributes.ChairEntityAttribute;
import kr.syeyoung.chair.gui.MapWidgetSliderInput;

public abstract class PropEditInteger extends PropEditMenu<Integer> {

    private double max;
    private double min;

    public PropEditInteger(ChairEntityAttribute<Integer> entityAttribute, int max, int min) {

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

        int value = getEntityAttribute().getAttributeValue();

        xInput = new MapWidgetSliderInput(value, 1, min, max) {
            @Override
            public void onValueUpdate(double value) {
                getEntityAttribute().setAttributeValue((int) Math.floor(value));
                PropEditInteger.this.onValueUpdate();
            }
        };
        xInput.setBounds(10, 25, getWidth() - 20, 16);


        addWidget(xInput);
    }
}
