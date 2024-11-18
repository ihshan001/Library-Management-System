package library.management.system.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

public class GlowingEffect {

    // Method to add a glowing effect with a gradient background to buttons
    public static void gradientEffect(JButton button, Color startColor, Color endColor) {
        Color originalForeground = button.getForeground(); // Save original text color
        Font originalFont = button.getFont(); // Save original font
        Border originalBorder = button.getBorder(); // Save original border

        // Set initial gradient color background
        button.setContentAreaFilled(false); // Disable default button background

        // Override button's paintComponent to draw gradient
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, javax.swing.JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                int width = button.getWidth();
                int height = button.getHeight();
                GradientPaint gradient = new GradientPaint(0, 0, startColor, width, height, endColor);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
                super.paint(g2d, c);
            }
        });

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.WHITE); // Change text color to white
                button.setFont(new Font("Arial", Font.BOLD, originalFont.getSize() + 1)); // Slightly bigger, bold text
                button.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 204), 2)); // Glowing border
                button.repaint(); // Refresh button to apply gradient
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(originalForeground); // Reset text color
                button.setFont(originalFont); // Reset font size
                button.setBorder(originalBorder); // Reset border
                button.repaint(); // Refresh button to restore gradient
            }
        });
    }
}

// call mthod  | GlowingEffect.gradientEffect(loginBtn, new Color(0, 153, 255), new Color(0, 255, 204));
    