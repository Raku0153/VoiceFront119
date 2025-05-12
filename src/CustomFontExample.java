import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

// 실제 프로그램과는 무관한 폰트 적용 확인용 코드
// 이미지 폴더에 폰트 이미지 첨부함.

public class CustomFontExample extends JFrame {

    public CustomFontExample() {
        setTitle("커스텀 글꼴 vs 맑은 고딕");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 패널 설정
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        // 1. NotoSansKR-Regular
        Font notoFont = loadCustomFont("./fonts\\NotoSansKR-Regular.ttf", 24f);
        JLabel notoLabel = new JLabel("이 문장은 나노산스 글꼴입니다.", SwingConstants.CENTER);
        notoLabel.setFont(notoFont);

        // 2. GmarketSansTTFBold
        Font gFont = loadCustomFont("./fonts\\GmarketSansTTFBold.ttf", 24f);
        JLabel gLabel = new JLabel("이 문장은 G산스 글꼴입니다.", SwingConstants.CENTER);
        gLabel.setFont(gFont);

        // 3. 맑은 고딕
        JLabel malgunLabel = new JLabel("이 문장은 나눔고딕 글꼴입니다.", SwingConstants.CENTER);
        malgunLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 24));

        // 패널에 추가
        panel.add(notoLabel);
        panel.add(gLabel);
        panel.add(malgunLabel);
        add(panel);
    }

    private Font loadCustomFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font.deriveFont(size);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomFontExample window = new CustomFontExample();
            window.setVisible(true);
        });
    }
}
