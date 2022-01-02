package kr.syeyoung.chair.gui.propedit;

import com.bergerkiller.bukkit.common.map.MapResourcePack;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetButton;
import kr.syeyoung.chair.Chairs;
import kr.syeyoung.chair.attributes.ChairEntityAttribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class PropEditItemStack extends PropEditMenu<ItemStack> {
    private Player viewer;
    public PropEditItemStack(ChairEntityAttribute<ItemStack> prop) {
        super(prop);
    }

    public MapWidgetButton widgetButton = new MapWidgetButton() {
        @Override
        public void onActivate() {
            Chairs.getInstance().getCedl().addConsumer(viewer, (stack) -> {
                PropEditItemStack.this.getEntityAttribute().setAttributeValue(stack);
                PropEditItemStack.this.invalidate();
                PropEditItemStack.this.onValueUpdate();

            });
        }
    };

    @Override
    public void onDraw() {
        super.onDraw();
        if (getEntityAttribute().getAttributeValue() != null)
            this.view.drawItem(MapResourcePack.SERVER, getEntityAttribute().getAttributeValue(), 27,20,46,46);
    }

    @Override
    public void onDetached() {
        Chairs.getInstance().getCedl().removeConsumer(viewer);
    }

    @Override
    public void onAttached() {
        super.onAttached();
        viewer = this.display.getViewers().get(0);
        widgetButton.setIcon(this.display.loadTexture("kr/syeyoung/chair/res/replace.png"));
        widgetButton.setBounds(10, 70, 80, 20);
        addWidget(widgetButton);
    }
}
