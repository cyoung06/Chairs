package kr.syeyoung.chair.util;

import com.bergerkiller.bukkit.common.map.MapFont;
import kr.syeyoung.chair.Chairs;

import java.awt.*;

public class NanumFont {
    public static final Font NanumFont;
    public static final MapFont<Character> BigMapNanumFont;
    public static final MapFont<Character> NormalMapNanumFont;

    static {
        Font f = null;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, Chairs.getInstance().getResource("kr/syeyoung/chair/res/NanumGothic.ttf"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        NanumFont = f;

        BigMapNanumFont = MapFont.fromJavaFont(NanumFont.deriveFont(12.0f));
        NormalMapNanumFont = MapFont.fromJavaFont(NanumFont.deriveFont(10.0f));
    }
}
