import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportInProgressUI extends JFrame {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

    private JLabel dateLabel;
    private JLabel realtimeLabel;
    private JLabel openTimeLabel;
    private JLabel elapsedLabel;

    private final LocalDateTime openedTime = LocalDateTime.now();

    public ReportInProgressUI() {
        setTitle("신고 진행 중");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(235, 140, 3));

        // 왼쪽 - 대화 내용
        JTextArea dialogueArea = new JTextArea(
                "여보세요\n" +
                "예\n" +
                "이 계속 전화 하고 전화 하는데도 안 열려갖고요.\n" +
                "예 예 지금 개.\n" +
                "예 예, 불은 켜져 있는데.\n" +
                "일단 계속 두들기는데도 열 생각을 안 해요.\n" +
                "예 예, 문자도 보내고 지금 카톡도 하고\n" +
                "예, 알겠습니다."
        );
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setEditable(false);
        dialogueArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        JScrollPane dialogueScroll = new JScrollPane(dialogueArea);
        dialogueScroll.setPreferredSize(new Dimension(500, 300));
        dialogueScroll.setBorder(BorderFactory.createEmptyBorder());

        // 오른쪽 - 시간 및 사고 정보
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        dateLabel = new JLabel(LocalDateTime.now().format(dateFormat));
        openTimeLabel = new JLabel("전화 받은 시각: " + openedTime.format(formatter));
        realtimeLabel = new JLabel();
        elapsedLabel = new JLabel();

        dateLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        openTimeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        realtimeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        elapsedLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

        JLabel typeLabel = new JLabel("안전사고(구조-중)");
        typeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));

        infoPanel.add(dateLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(openTimeLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(realtimeLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(elapsedLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(typeLabel);

        // 메모 필드
        JTextArea memoArea = new JTextArea("메모");
        memoArea.setPreferredSize(new Dimension(200, 100));
        memoArea.setLineWrap(true);
        memoArea.setWrapStyleWord(true);
        JScrollPane memoScroll = new JScrollPane(memoArea);

        // 위쪽 레이아웃 구성
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.add(dialogueScroll, BorderLayout.CENTER);

        JPanel rightTopPanel = new JPanel();
        rightTopPanel.setLayout(new BoxLayout(rightTopPanel, BoxLayout.Y_AXIS));
        rightTopPanel.setOpaque(false);
        rightTopPanel.add(infoPanel);
        rightTopPanel.add(Box.createVerticalStrut(20));
        rightTopPanel.add(memoScroll);

        topPanel.add(rightTopPanel, BorderLayout.EAST);

        // 아래쪽 - 신고 요약 정보
        JTextArea reportSummary = new JTextArea(
                "신고 내용 : 딸이 집 안에 있음에도 불구하고 문을 열지 않으며, 전화 및 문자에도 응답하지 않음\n" +
                "주소 : 서울특별시 마포구 성산동\n" +
                "비고 : 이후 상황에 따라 경찰 및 추가 의료 지원이 필요할 수 있음."
        );
        reportSummary.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        reportSummary.setLineWrap(true);
        reportSummary.setWrapStyleWord(true);
        reportSummary.setEditable(false);
        reportSummary.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        reportSummary.setBackground(new Color(255, 245, 230));
        JScrollPane summaryScroll = new JScrollPane(reportSummary);
        summaryScroll.setPreferredSize(new Dimension(800, 150));

        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(summaryScroll, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);

        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
    }

    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        dateLabel.setText(now.format(dateFormat));
        realtimeLabel.setText("현재 시각: " + now.format(formatter));

        Duration elapsed = Duration.between(openedTime, now);
        long hours = elapsed.toHours();
        long minutes = elapsed.toMinutes() % 60;
        long seconds = elapsed.getSeconds() % 60;
        elapsedLabel.setText("경과 시간: " + String.format("%02d시간 %02d분 %02d초", hours, minutes, seconds));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReportInProgressUI::new);
    }
}
