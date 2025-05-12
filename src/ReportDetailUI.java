import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportDetailUI extends JFrame {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

    private JLabel dateLabel;
    private JLabel realtimeLabel;
    private JLabel openTimeLabel;
    private JLabel elapsedLabel;
    private JTextArea memoArea;
    private final LocalDateTime openedTime = LocalDateTime.now();

    public ReportDetailUI(int reportId) {
        setTitle("신고 진행중");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String dialogueText = readTextFromDBPath(reportId, "dialogue_path");
        String summaryText = readTextFromDBPath(reportId, "summary_path");
        String memoText = getMemoFromDB(reportId);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(235, 140, 30));


        JTextArea dialogueArea = new JTextArea(dialogueText);
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setEditable(false);
        dialogueArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        JScrollPane dialogueScroll = new JScrollPane(dialogueArea);
        dialogueScroll.setPreferredSize(new Dimension(500, 300));
        dialogueScroll.setBorder(BorderFactory.createEmptyBorder());
        dialogueScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        dateLabel = new JLabel(LocalDateTime.now().format(dateFormat));
        openTimeLabel = new JLabel("전화 받은 시각 : " + openedTime.format(formatter));
        realtimeLabel = new JLabel();
        elapsedLabel = new JLabel();

        dateLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        openTimeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        realtimeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        elapsedLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

     //   JLabel typeLabel = new JLabel("\uC2E0\uACE0 \uC0C1\uC138 \uBCF4\uAE30");
     //   typeLabel.setFont(new Font("\uB9D1\uC740 \uACE0\uB515", Font.PLAIN, 14));

        infoPanel.add(dateLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(openTimeLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(realtimeLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(elapsedLabel);
        infoPanel.add(Box.createVerticalStrut(5));
     //   infoPanel.add(typeLabel);
        
        
        
        
        
        String urgency = getUrgencyLevelFromDB(reportId);  // "상", "중", "하"

        JLabel urgencyText = new JLabel("긴급도");
        urgencyText.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        ImageIcon urgencyIcon = null;
        switch (urgency) {
            case "상":
                //urgencyIcon = new ImageIcon("./images/H.png");
                urgencyIcon = new ImageIcon("C:/Users/00rak/Desktop/Hansung/HansungJ/HansungJ/images/H.png");
                break;
            case "중":
                //urgencyIcon = new ImageIcon("./images/M.png");
                urgencyIcon = new ImageIcon("C:/Users/00rak/Desktop/Hansung/HansungJ/HansungJ/images/M.png");
                break;
            case "하":
                //urgencyIcon = new ImageIcon("./images/L.png");
                urgencyIcon = new ImageIcon("C:/Users/00rak/Desktop/Hansung/HansungJ/HansungJ/images/L.png");
                break;
        }

        if (urgencyIcon != null) {
            Image scaledImg = urgencyIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            JLabel urgencyLabel = new JLabel(new ImageIcon(scaledImg));
            infoPanel.add(Box.createVerticalStrut(10));
            infoPanel.add(urgencyText);
            infoPanel.add(urgencyLabel);
        }

        
        
        
        
        
        

        memoArea = new JTextArea(memoText);
        memoArea.setPreferredSize(new Dimension(200, 100));
        memoArea.setLineWrap(true);        
        memoArea.setWrapStyleWord(true);
        JScrollPane memoScroll = new JScrollPane(memoArea);
        memoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        

        JButton saveMemoButton = new JButton("저장");
        saveMemoButton.addActionListener(e -> {
            String newMemo = memoArea.getText();
            saveMemoToDB(reportId, newMemo);
            JOptionPane.showMessageDialog(this, "메모 저장 완료.");
        });

        JButton backButton = new JButton("돌아가기");
        backButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(235, 140, 3));
        buttonPanel.add(saveMemoButton);
        buttonPanel.add(backButton);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.add(dialogueScroll, BorderLayout.CENTER);
        
        JLabel urgencyLabel = new JLabel("긴급도");
        urgencyLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
       // urgencyLabel.setForeground(Color.WHITE);
        

        JPanel rightTopPanel = new JPanel();
        rightTopPanel.setLayout(new BoxLayout(rightTopPanel, BoxLayout.Y_AXIS));
        rightTopPanel.setOpaque(false);
        rightTopPanel.add(infoPanel);
        rightTopPanel.add(Box.createVerticalStrut(20));
        rightTopPanel.add(memoScroll);
        rightTopPanel.add(Box.createVerticalStrut(10));
        rightTopPanel.add(buttonPanel);
        rightTopPanel.add(Box.createVerticalStrut(10));
        //rightTopPanel.add(urgencyLabel);  // 긴급도 라벨 추가

        topPanel.add(rightTopPanel, BorderLayout.EAST);

        JTextArea reportSummary = new JTextArea(summaryText);
        reportSummary.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        reportSummary.setLineWrap(true);
        reportSummary.setWrapStyleWord(true);
        
        
        // 공간상의 문제로 7줄 넘어가지 않게 하는 코드
		memoArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
		    private void checkLineLimit() {
		        int lines = memoArea.getLineCount();
		        if (lines > 6) {
		            SwingUtilities.invokeLater(() -> {
		                try {
		                    int end = memoArea.getDocument().getLength();
		                    memoArea.getDocument().remove(end - 1, 1);
		                } catch (Exception ex) {
		                    ex.printStackTrace();
		                }
		            });
		        }
		    }
		
		    public void insertUpdate(javax.swing.event.DocumentEvent e) {
		        checkLineLimit();
		    }
		
		    public void removeUpdate(javax.swing.event.DocumentEvent e) {}
		    public void changedUpdate(javax.swing.event.DocumentEvent e) {}
		});

        
        
        
        reportSummary.setEditable(false);
        reportSummary.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        reportSummary.setBackground(new Color(255, 245, 230));
        JScrollPane summaryScroll = new JScrollPane(reportSummary);
        summaryScroll.setPreferredSize(new Dimension(800, 150));
        summaryScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        

        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(summaryScroll, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
    }
    
    
    
    private String getUrgencyLevelFromDB(int reportId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/emergency_system", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("SELECT urgency_level FROM reports WHERE id = ?")) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("urgency_level");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    
    
    
    

    private String readTextFromDBPath(int reportId, String columnName) {
        String path = "";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/emergency_system", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("SELECT " + columnName + " FROM reports WHERE id = ?")) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                path = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (path == null || path.isBlank()) return "(파일 경로 없음)";

        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (Exception e) {
            content.append("(파일을 읽을 수 없습니다.)");
        }

        return content.toString();
    }

    private String getMemoFromDB(int reportId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/emergency_system", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("SELECT memo FROM reports WHERE id = ?")) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("memo");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void saveMemoToDB(int reportId, String memo) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/emergency_system", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement("UPDATE reports SET memo = ? WHERE id = ?")) {
            stmt.setString(1, memo);
            stmt.setInt(2, reportId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        dateLabel.setText(now.format(dateFormat));
        realtimeLabel.setText("현재 시간 : " + now.format(formatter));

        Duration elapsed = Duration.between(openedTime, now);
        long hours = elapsed.toHours();
        long minutes = elapsed.toMinutes() % 60;
        long seconds = elapsed.getSeconds() % 60;
        elapsedLabel.setText("경과 시간 : " + String.format("%02d시간 %02d분 %02d초", hours, minutes, seconds));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReportDetailUI(1));
    }
}
