package gui;

import java.awt.*;
//flow layout

public class WrapLayout extends FlowLayout {
    public WrapLayout() { super(); }
    public WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }
    @Override
    public Dimension minimumLayoutSize(Container target) {
        return layoutSize(target, false);
    }

    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getWidth();
            if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;

            Insets insets = target.getInsets();
            int maxWidth = targetWidth - (insets.left + insets.right + getHgap()*2);
            int x = 0, y = insets.top + getVgap();
            int rowHeight = 0;

            for (Component comp : target.getComponents()) {
                if (!comp.isVisible()) continue;
                Dimension d = preferred ? comp.getPreferredSize() : comp.getMinimumSize();
                if (x + d.width > maxWidth) {
                    x = 0;
                    y += rowHeight + getVgap();
                    rowHeight = 0;
                }
                x += d.width + getHgap();
                rowHeight = Math.max(rowHeight, d.height);
            }
            y += rowHeight + getVgap();
            y += insets.bottom;
            return new Dimension(targetWidth, y);
        }
    }
}
