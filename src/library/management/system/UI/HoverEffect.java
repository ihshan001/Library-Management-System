package library.management.system.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
public class HoverEffect {

    // Method to add a glowing hover effect to buttons
    public static void glowEffect(JButton button) {
        Color originalBackground = button.getBackground(); // Save original background color
        Color originalForeground = button.getForeground(); // Save original text color
        Font originalFont = button.getFont(); // Save original font
        Border originalBorder = button.getBorder(); // Save original border

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(51,51,51)); // Set background to a glowing blue
                button.setForeground(Color.WHITE); // Change text color to white
                button.setFont(new Font("Arial", Font.BOLD, originalFont.getSize() + 1)); // Slightly bigger, bold text
                button.setBorder(BorderFactory.createLineBorder(new Color(0,0,0), 4)); // Glowing border
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBackground); // Reset background color
                button.setForeground(originalForeground); // Reset text color
                button.setFont(originalFont); // Reset font size
                button.setBorder(originalBorder); // Reset border
            }
        });
    }
}
