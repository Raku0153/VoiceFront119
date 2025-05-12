import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// 로그인 화면 UI
public class EmergencyLoginUI extends JFrame {

    public EmergencyLoginUI() {
        setTitle("119긴급센터 로그인");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 배경색.
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(235, 140, 30));


        Box contentBox = Box.createVerticalBox();

        JLabel titleLabel;
        
        titleLabel = new JLabel("VoiceFront 119 ");
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT,
                new java.io.File("./fonts\\GmarketSansTTFBold.ttf")     // GSans 폰트
            ).deriveFont(Font.PLAIN, 48f);

            titleLabel.setFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 48));  // 글꼴 없으면 기본 글꼴로
        }
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.DARK_GRAY);
        
        
        
        
        

        ImageIcon icon = new ImageIcon("./images\\119.png"); // src 폴더의 상위에 있는 곳의 images 폴더의 안에 있는 이미지 가져옴
        Image scaledImage = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  
        
        
        // 아이디 입력칸
        JTextField emailField = new JTextField("아이디");
        emailField.setForeground(Color.GRAY);
        emailField.setMaximumSize(new Dimension(200, 50));
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        emailField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals("아이디")) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setText("아이디");
                    emailField.setForeground(Color.GRAY);
                }
            }
        });
        
        // 비번 입력칸 
        JPasswordField passwordField = new JPasswordField("비밀번호");
        passwordField.setForeground(Color.GRAY);
        passwordField.setMaximumSize(new Dimension(200, 50));
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        passwordField.setEchoChar((char) 0);
        
        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).equals("비밀번호")) {
                    passwordField.setText("");
                    passwordField.setEchoChar('●');
                    passwordField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("비밀번호");
                    passwordField.setEchoChar((char) 0);
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });
        
        // 솔직히 위에 아이디나 비번이나 좀 맘에 안들긴 하는데
        // 처음에 접속할때 입력이 안가있고 다시 지우면 아이디 회색 글씨 입력할라고 하면 사라지는 그거 나올라고 햇더니 이렇게 됨
        // 나중에 수정하게되면 수정하기
        // 문제점 : 아이디에 '아이디' 라고 입력하고 비밀번호로 바꾸면 저장안되고 회색글씨로 사라짐 
        

        // 👁 비밀번호 보기 버튼
        // 그 흔히 있는 비밀번호 안보이게 된거 보이게 해주는 그런 버튼
        JButton togglePasswordButton = new JButton("👁");
        togglePasswordButton.setFocusable(false);
        togglePasswordButton.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));
        passwordPanel.setMaximumSize(new Dimension(200, 30));
        passwordPanel.add(passwordField);
        passwordPanel.add(togglePasswordButton);

        togglePasswordButton.addActionListener(e ->
        // 그냥 0 상태랑 ● 상태 오가는거 누를때마다
        passwordField.setEchoChar(passwordField.getEchoChar() == 0 ? '●' : 0)
        );
        
        
        

        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(120, 50));
        loginButton.setMaximumSize(new Dimension(120, 50));
        loginButton.setBackground(Color.DARK_GRAY);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));

        loginButton.addActionListener(e -> {
            String inputEmail = emailField.getText().trim();
            String inputPassword = new String(passwordField.getPassword());

            if (inputEmail.equals("아이디") || inputPassword.equals("비밀번호")) { // 둘중 하나라도 빈칸이면
                JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (DBUtil.login(inputEmail, inputPassword)) { // 둘 조합이 안맞으면
                dispose();
                new EmergencyMainUI(inputEmail);
            } else {
                JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 올바르지 않습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        
        // 로그인 버튼 누르는거 말고 입력하고 Enter 누르면 로그인 되는 로직
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });

        contentBox.add(titleLabel);
        contentBox.add(Box.createVerticalStrut(20));
        contentBox.add(imageLabel);
        contentBox.add(Box.createVerticalStrut(20));
        contentBox.add(emailField);
        contentBox.add(Box.createVerticalStrut(20));
        contentBox.add(passwordPanel);
        contentBox.add(Box.createVerticalStrut(30));
        contentBox.add(loginButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(contentBox, gbc);

        add(panel);
        setVisible(true);

        // 자동 포커스 방지 : 없으면 창 키자마자 아이디에 입력 가버림
        // 기껏 회색 글씨 만들어둿는데 포커스 가버려서 안보이는거때매 넣음
        SwingUtilities.invokeLater(() -> panel.requestFocusInWindow());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmergencyLoginUI::new);
    }
}
