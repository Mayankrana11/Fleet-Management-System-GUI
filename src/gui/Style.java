package gui;

import javax.swing.*;
import java.awt.*;

public final class Style {

    // Colors
    public static final Color BG = new Color(0x0F1724);
    public static final Color PANEL = new Color(0x11131A);
    public static final Color ACCENT = new Color(0x2ECC71);
    public static final Color WARN = new Color(0xF1C40F);
    public static final Color DANGER = new Color(0xE74C3C);
    public static final Color TEXT = Color.WHITE;

    public static void buttonStyle(AbstractButton b) {
        b.setBackground(new Color(0x2A2F3A));
        b.setForeground(TEXT);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6,10,6,10));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    public static void panelStyle(Component p) {
        // Only Components that support background changes will respond
        p.setBackground(PANEL);

        // Not all Components support foreground, so wrap safely
        try {
            p.setForeground(TEXT);
        } catch (Exception ignored) {}
    }

    public static void labelStyle(JLabel l, int size) {
        l.setForeground(TEXT);
        l.setFont(new Font("Segoe UI", Font.PLAIN, size));
    }

    public static void checkBoxStyle(JCheckBox c) {
        c.setBackground(new Color(0x1F1F2E));
        c.setForeground(TEXT);
    }
}
