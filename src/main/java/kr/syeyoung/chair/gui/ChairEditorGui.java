package kr.syeyoung.chair.gui;


import com.bergerkiller.bukkit.common.events.map.MapKeyEvent;
import com.bergerkiller.bukkit.common.map.MapColorPalette;
import com.bergerkiller.bukkit.common.map.MapDisplay;
import com.bergerkiller.bukkit.common.map.MapSessionMode;
import com.bergerkiller.bukkit.common.map.widgets.MapWidget;
import com.bergerkiller.bukkit.common.map.widgets.MapWidgetWindow;
import com.bergerkiller.bukkit.common.utils.ItemUtil;
import kr.syeyoung.chair.Chairs;
import kr.syeyoung.chair.data.ChairData;
import kr.syeyoung.chair.data.ChairEditEntity;
import kr.syeyoung.chair.data.ChairEditSession;
import kr.syeyoung.chair.util.NanumFont;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.stream.Collectors;

public class ChairEditorGui extends MapDisplay {

    private String chairID;

    private ChairData chairData;
    private ChairEditSession editSession;


    private MapWidgetWindow window = new MapWidgetWindow();

    @Override
    public void onAttached() {
        setGlobal(true);
        setSessionMode(MapSessionMode.VIEWING);
        setReceiveInputWhenHolding(true);
        clearWidgets();

        chairID = ItemUtil.getMetaTag(getMapItem()).getValue("ChairID", String.class);

        chairData = Chairs.getInstance().getChair(chairID);


        this.window = new MapWidgetWindow();
        this.window.setBounds(0, 0, getWidth(), getHeight());
        this.window.getTitle().setFont(NanumFont.BigMapNanumFont);
        this.window.getTitle().setText("짱짱 멋진 의자 에디터");
        this.window.getTitle().setColor(MapColorPalette.COLOR_BLUE);
        addWidget(this.window);

        if (chairData == null) {
            getLayer(1).draw(NanumFont.BigMapNanumFont, 3, 64, MapColorPalette.COLOR_RED, "존재하지 않는 의자입니다");
            return;
        }

        Location eye = getOwners().get(0).getEyeLocation().clone();
        Vector direction = eye.getDirection().multiply(5);
        eye.add(direction);
        editSession = new ChairEditSession(eye, getOwners().get(0), chairData);
        editSession.getRepresentation().addAll(chairData.getEntityList().stream().map(ce -> new ChairEditEntity(editSession, ce)).collect(Collectors.toList()));

        Chairs.getInstance().getServer().getScheduler().runTaskLater(Chairs.getInstance(), () -> editSession.getRepresentation().forEach(ChairEditEntity::update), 1L);


        ChairEntitySelector ces = new ChairEntitySelector(editSession, getWidth(), getHeight() - 23);
        ces.setPosition(0,23);
        window.addWidget(ces);
        this.getRootWidget().setBounds(0,0,getWidth(),getHeight());
        ces.focus();
        ces.activate();

        update();
    }

    @Override
    public void onTick() {
        this.getLayer().getView(0,0,getWidth(),getHeight()).fillRectangle(0,0,getWidth(),getHeight(), MapColorPalette.COLOR_CYAN);
    }

    @Override
    public void onDetached() {
        clearWidgets();
        editSession.remove(true);
        System.out.println(getWidth() + " - " + getHeight() + " - "+this);
    }

    public void setMenu(MapWidget widget) {
        widget.setBounds((getWidth() - widget.getWidth()) / 2, (getHeight() - widget.getHeight()) / 2, widget.getWidth(), widget.getHeight());
        widget.focus();
        addWidget(widget);
    }
}
