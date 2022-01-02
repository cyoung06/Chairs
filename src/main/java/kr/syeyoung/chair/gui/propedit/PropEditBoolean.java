package kr.syeyoung.chair.gui.propedit;

import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import com.sun.org.apache.xpath.internal.operations.Bool;
import kr.syeyoung.chair.attributes.ChairEntityAttribute;

public abstract class PropEditBoolean extends PropEditMenu<Boolean> {
    public PropEditBoolean(ChairEntityAttribute<Boolean> prop) {
        super(prop);
    }

    public MapWidgetButton widgetButton = new MapWidgetButton() {
        @Override
        public void onActivate() {
            ChairEntityAttribute<Boolean> bool = PropEditBoolean.this.getEntityAttribute();
            bool.setAttributeValue(!bool.getAttributeValue());

            setText(bool.getAttributeValue() ? "ON" : "OFF");

            onValueUpdate();
        }
    };

    @Override
    public void onAttached() {
        super.onAttached();
        widgetButton.setText(getEntityAttribute().getAttributeValue() ? "ON" : "OFF");
        widgetButton.setBounds(10, 25, 80, 25);
        addWidget(widgetButton);
    }
}
