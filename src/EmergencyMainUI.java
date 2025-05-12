import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.border.LineBorder;

import javax.swing.text.StyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.SimpleAttributeSet;


// 메인 화면 UI
public class EmergencyMainUI extends JFrame {
    private JPanel listPanel;
    private JScrollPane scrollPane;
    private String searchType = "address";
    private String sortType = "시간순";
    private boolean isAscending = false;
    private JTextField searchField;
    private ArrayList<String[]> dbReports = new ArrayList<>();

    public EmergencyMainUI(String username) {
        setTitle("119 상황 접수 시스템");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setPreferredSize(new Dimension(250, getHeight()));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        String displayName = getDisplayNameFromDB(username);
        JLabel nameLabel = new JLabel(displayName + "님"); // 여기에 들어갈 사용자 명은 db에 있음
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel titleLabel = new JLabel("119 종합상황실");
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidePanel.add(Box.createVerticalStrut(100));

	     // 이름 + 타이틀을 묶는 패널 생성
	     JPanel nameBox = new JPanel();
	     nameBox.setLayout(new BoxLayout(nameBox, BoxLayout.Y_AXIS));
	     nameBox.setBackground(Color.WHITE); // 사이드 패널 배경색과 동일하게
	     nameBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // 검정 테두리
	     nameBox.setMaximumSize(new Dimension(200, 150)); // 최대 사이즈 조정 (폭, 높이)
	     
	  // 이미지 먼저 추가
	     ImageIcon userIcon = new ImageIcon("./images\\user.png");
	     Image userImage = userIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
	     JLabel userImageLabel = new JLabel(new ImageIcon(userImage));
	     userImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	     nameBox.add(userImageLabel);
	     nameBox.add(Box.createVerticalStrut(10));

	     // 기존 텍스트 추가
	     nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	     titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	     nameBox.add(nameLabel);
	     nameBox.add(Box.createVerticalStrut(5));
	     nameBox.add(titleLabel);
	
	     // 묶은 패널을 사이드에 추가
	     sidePanel.add(nameBox);

        
        ImageIcon callIcon = new ImageIcon("./images\\call.png");
        Image scaledImage = callIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton callButton = new JButton(scaledIcon);
        callButton.setPreferredSize(new Dimension(30, 30));
        callButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        callButton.setBorderPainted(false);
        callButton.setContentAreaFilled(false);
        callButton.setFocusPainted(false);
        callButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        callButton.addActionListener(e -> {
            int result = JOptionPane.showOptionDialog(
                this,
                "전화를 받으시겠습니까?",
                "신고 수신",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"받기", "거절"},
                "받기"
            );
            if (result == JOptionPane.YES_OPTION) {
                new ReportInProgressUI();
            }
        });

        sidePanel.add(Box.createVerticalStrut(30));
        sidePanel.add(callButton);
        
        
        sidePanel.add(Box.createVerticalGlue());
        
     // 하단 빈 공간 박스 추가
        JTextPane bottomBox = new JTextPane();
        bottomBox.setEditable(false);
        bottomBox.setBackground(Color.WHITE);
        bottomBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bottomBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        bottomBox.setPreferredSize(new Dimension(250, 200));


        // 스타일 문서
        StyledDocument doc = bottomBox.getStyledDocument();

        // 기본 스타일
        SimpleAttributeSet normal = new SimpleAttributeSet();
        StyleConstants.setForeground(normal, Color.BLACK);
        StyleConstants.setFontSize(normal, 14);
        StyleConstants.setFontFamily(normal, "SansSerif");

        // 상태별 스타일
        SimpleAttributeSet red = new SimpleAttributeSet();
        StyleConstants.setForeground(red, Color.RED);

        SimpleAttributeSet orange = new SimpleAttributeSet();
        StyleConstants.setForeground(orange, Color.ORANGE);

        SimpleAttributeSet green = new SimpleAttributeSet();
        StyleConstants.setForeground(green, new Color(0, 128, 0));

        // 내용 추가
        try {
            doc.insertString(doc.getLength(), "처리 중인 사건\n\n", normal);

            doc.insertString(doc.getLength(), "사고1\t", normal);
            doc.insertString(doc.getLength(), "미처리\t", red);
            doc.insertString(doc.getLength(), "우선순위5\n\n", normal);
            
            
            doc.insertString(doc.getLength(), "사고2\t", normal);
            doc.insertString(doc.getLength(), "처리중\t", orange);
            doc.insertString(doc.getLength(), "우선순위4\n\n", normal);

            
            doc.insertString(doc.getLength(), "사고3\t", normal);
            doc.insertString(doc.getLength(), "완료\t", green);
            doc.insertString(doc.getLength(), "우선순위2\n\n", normal);

            
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        bottomBox.setMaximumSize(new Dimension(250, 200));
        bottomBox.setBackground(Color.WHITE);
        bottomBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        bottomBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidePanel.add(Box.createVerticalStrut(30));
        sidePanel.add(bottomBox);
        

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(235, 140, 30));


        JRadioButton addressRadio = new JRadioButton("주소");
        JRadioButton typeRadio = new JRadioButton("사고 유형");
        addressRadio.setSelected(true);
        ButtonGroup searchGroup = new ButtonGroup();
        searchGroup.add(addressRadio);
        searchGroup.add(typeRadio);
        addressRadio.addActionListener(e -> searchType = "address");
        typeRadio.addActionListener(e -> searchType = "type");

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.setOpaque(false);
        radioPanel.add(addressRadio);
        radioPanel.add(typeRadio);

        searchField = new JTextField("주소 또는 사고 유형 검색...");
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("주소 또는 사고 유형 검색...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("주소 또는 사고 유형 검색...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        JButton searchButton = new JButton("검색");
        searchButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchButton.setPreferredSize(new Dimension(80, 35));
        searchButton.setFocusPainted(false);
        searchButton.setBackground(Color.DARK_GRAY);
        searchButton.setForeground(Color.WHITE);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        JComboBox<String> sortCombo = new JComboBox<>(new String[]{"시간순", "긴급도순"});
        sortCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton toggleOrderButton = new JButton("▲");
        toggleOrderButton.setPreferredSize(new Dimension(50, 25));
        toggleOrderButton.setFocusPainted(false);
        toggleOrderButton.addActionListener(e -> {
            isAscending = !isAscending;
            toggleOrderButton.setText(isAscending ? "▼" : "▲");
            sortType = (String) sortCombo.getSelectedItem();
            updateFilteredList();
        });

        sortCombo.addActionListener(e -> {
            updateFilteredList();
        });

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortPanel.setOpaque(false);
        sortPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        sortPanel.add(new JLabel("정렬:"));
        sortPanel.add(sortCombo);
        sortPanel.add(toggleOrderButton);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setOpaque(false);
        topPanel.add(radioPanel);
        topPanel.add(searchPanel);
        topPanel.add(sortPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);
        scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> updateFilteredList());
        loadReportsFromDB();
        updateFilteredList();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidePanel, BorderLayout.WEST);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadReportsFromDB() {
        dbReports.clear();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/emergency_system", "root", "1234");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, report_time, address, accident_type, urgency_level, location_detail, problem_description, witness_location, false_report, phone_number FROM reports");
            while (rs.next()) {
                dbReports.add(new String[]{
                    rs.getString("report_time") + " " + rs.getString("address"),
                    rs.getString("accident_type"),
                    "긴급도(" + rs.getString("urgency_level") + ")",
                    rs.getString("location_detail"),
                    rs.getString("problem_description"),
                    rs.getString("witness_location"),
                    rs.getString("false_report"),
                    rs.getString("phone_number"),
                    rs.getString("id")
                });
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateReportList(ArrayList<String[]> reports) {
        listPanel.removeAll();
        for (String[] report : reports) {
            JPanel wrapperPanel = new JPanel();
            wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
            wrapperPanel.setOpaque(false);

            JPanel itemPanel = new JPanel(new GridBagLayout());
            itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 5, 0, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridy = 0;

            JButton toggleButton = new JButton("▶");
            toggleButton.setPreferredSize(new Dimension(40, 25));
            toggleButton.setFont(new Font("SansSerif", Font.BOLD, 12));
            toggleButton.setFocusPainted(false);
            toggleButton.setContentAreaFilled(false);
            itemPanel.add(toggleButton, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.6;
            JLabel locationLabel = new JLabel(report[0]);
            locationLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            itemPanel.add(locationLabel, gbc);

            gbc.gridx = 2;
            gbc.weightx = 0.25;
            JLabel typeLabel = new JLabel(report[1]);
            typeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            itemPanel.add(typeLabel, gbc);

            gbc.gridx = 3;
            gbc.weightx = 0.15;
            JLabel urgencyLabel = new JLabel(report[2]);
            urgencyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            urgencyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            itemPanel.add(urgencyLabel, gbc);

            

            JPanel detailPanel = new JPanel();
            detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
            detailPanel.setBorder(BorderFactory.createEmptyBorder(5, 70, 5, 10));
            detailPanel.setBackground(new Color(255, 255, 255, 200));
            detailPanel.setVisible(false);

            String detailHtml = String.format(
                "<html>ID: %s<br><html>위치: %s<br>문제: %s<br>처리 결과: %s<br>wav: %s<br>전화번호: %s</html>",
                report[8], report[3], report[4], report[5], report[6], report[7]
            );

            JLabel detailLabel = new JLabel(detailHtml);
            detailLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            detailPanel.add(detailLabel);

            JButton viewFullDetailButton = new JButton("상세보기");
            viewFullDetailButton.setAlignmentX(Component.LEFT_ALIGNMENT);

            
            
            
            viewFullDetailButton.addActionListener(e -> {
                System.out.println("디버그: 선택된 신고 ID = " + report[8]);
                new ReportDetailUI(Integer.parseInt(report[8]));
            });

            detailPanel.add(Box.createVerticalStrut(5));
            detailPanel.add(viewFullDetailButton);


            
            toggleButton.addActionListener(e -> {
                boolean isVisible = detailPanel.isVisible();
                detailPanel.setVisible(!isVisible);
                toggleButton.setText(isVisible ? "▶" : "▼");
                wrapperPanel.revalidate();
            });

            wrapperPanel.add(itemPanel);
            wrapperPanel.add(detailPanel);
            listPanel.add(wrapperPanel);
            listPanel.add(Box.createVerticalStrut(10));
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private void updateFilteredList() {
        String keyword = searchField.getText().trim();
        ArrayList<String[]> filtered = new ArrayList<>();
        for (String[] report : dbReports) {
            if (keyword.isEmpty() || keyword.equals("주소 또는 사고 유형 검색...")) {
                filtered.add(report);
            } else {
                if (searchType.equals("address") && report[0].contains(keyword)) {
                    filtered.add(report);
                } else if (searchType.equals("type") && report[1].contains(keyword)) {
                    filtered.add(report);
                }
            }
        }
        filtered.sort(getComparator(sortType, isAscending));
        updateReportList(filtered);
    }

    private Comparator<String[]> getComparator(String sortType, boolean ascending) {
        Comparator<String[]> comparator;
        if (sortType.equals("긴급도순")) {
            Map<String, Integer> urgencyMap = Map.of("하", 1, "중", 2, "상", 3);
            comparator = Comparator.comparing(
                (String[] a) -> urgencyMap.getOrDefault(a[2].replaceAll(".*\\((.*)\\)", "$1"), 0)
            );
        } else {
            comparator = Comparator.comparing((String[] arr) -> arr[0]);
        }
        return ascending ? comparator : comparator.reversed();
    }

    private String getDisplayNameFromDB(String username) {
        String displayName = "사용자";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/emergency_system", "root", "1234");
            PreparedStatement stmt = conn.prepareStatement("SELECT name FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                displayName = rs.getString("name");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return displayName;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmergencyMainUI("test1")); // 아이디 db에 없는걸로 하면 사용자님이라고 나옴
        																// jason0153 이게 이지성 test2 는 김민수
    }
}
