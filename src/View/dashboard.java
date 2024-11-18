package View;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class dashboard extends javax.swing.JFrame {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    String jdbcUrl = "jdbc:mysql://localhost:3306/lms";
    String user = "root";
    String dbpassword = "";

    public dashboard() {
        initComponents();
        loadMembersToTable();
        loadBooksToTable();// Load members into the table at startup
    }

    public void loadMembersToTable() {
        String sql = "SELECT ID, name, address, email, phoneNo FROM member";
        try (Connection con = DriverManager.getConnection(jdbcUrl, user, dbpassword); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            // Get table models for all three tables
            DefaultTableModel model1 = (DefaultTableModel) memberTbl.getModel();
            DefaultTableModel model2 = (DefaultTableModel) memberTbl1.getModel();

            // Clear existing rows in all tables
            model1.setRowCount(0);
            model2.setRowCount(0);
            // Initialize member count

            int memberCount = 0;

            // Populate the tables with data
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("email"),
                    rs.getInt("phoneNo")
                };

                // Add the same row to all tables
                model1.addRow(row);
                model2.addRow(row);

                // Increment the member count
                memberCount++;
            }

            // Update the displayTotalMemLbl label with the total number of members
            memberCountLbl.setText(String.valueOf(memberCount));

        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error loading members: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load books into multiple tables
    public void loadBooksToTable() {
        String sql = "SELECT ID, title, author, yearPublished FROM book";
        try (Connection con = DriverManager.getConnection(jdbcUrl, user, dbpassword); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            // Get table models for all book tables
            DefaultTableModel bookModel1 = (DefaultTableModel) bookTbl.getModel();
            DefaultTableModel bookModel2 = (DefaultTableModel) bookTbl1.getModel();

            // Clear existing rows in all tables
            bookModel1.setRowCount(0);
            bookModel2.setRowCount(0);

            // Initialize book count
            int bookCount = 0;

            // Populate the tables with data
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("yearPublished")
                };

                // Add the same row to all tables
                bookModel1.addRow(row);
                bookModel2.addRow(row);

                // Increment the book count
                bookCount++;
            }

            // Update the book count label
            bookCountLbl.setText(String.valueOf(bookCount));

        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error loading books: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ADD, UPDATE, DELETE Members with error handling
    public void manageMembers(String action) {
        try {
            // Retrieve input values
            String memIDStr = memberIDTxt.getText().trim();
            String name = nameTxt.getText().trim();
            String address = addressTxt.getText().trim();
            String email = emailTxt.getText().trim();
            String phoneNoStr = phoneNoTxt.getText().trim();

            if (memIDStr.isEmpty() || name.isEmpty() || address.isEmpty() || email.isEmpty() || phoneNoStr.isEmpty()) {
                throw new Exception("All fields are required!");
            }

            int memID;
            int phoneNo;

            try {
                memID = Integer.parseInt(memIDStr);
                phoneNo = Integer.parseInt(phoneNoStr);
            } catch (NumberFormatException ex) {
                throw new Exception("MemberID and Phone No must be numeric!");
            }

            try (Connection con = DriverManager.getConnection(jdbcUrl, user, dbpassword)) {
                switch (action.toLowerCase()) {
                    case "add":
                        try {
                        // Add Member
                        String addSql = "INSERT INTO member (ID, name, address, email, phoneNo) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement pst = con.prepareStatement(addSql)) {
                            pst.setInt(1, memID);
                            pst.setString(2, name);
                            pst.setString(3, address);
                            pst.setString(4, email);
                            pst.setInt(5, phoneNo);
                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        throw new Exception("Failed to add member. Duplicate ID or database issue!");
                    }
                    break;

                    case "update":
                        try {
                        // Update Member
                        String updateSql = "UPDATE member SET name = ?, address = ?, email = ?, phoneNo = ? WHERE ID = ?";
                        try (PreparedStatement pst = con.prepareStatement(updateSql)) {
                            pst.setString(1, name);
                            pst.setString(2, address);
                            pst.setString(3, email);
                            pst.setInt(4, phoneNo);
                            pst.setInt(5, memID);
                            int rowsUpdated = pst.executeUpdate();
                            if (rowsUpdated > 0) {
                                JOptionPane.showMessageDialog(this, "Member updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                throw new Exception("No member found with the given ID!");
                            }
                        }
                    } catch (SQLException ex) {
                        throw new Exception("Failed to update member. Database issue!");
                    }
                    break;

                    case "delete":
                        try {
                        // Delete Member
                        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this member?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            String deleteSql = "DELETE FROM member WHERE ID = ?";
                            try (PreparedStatement pst = con.prepareStatement(deleteSql)) {
                                pst.setInt(1, memID);
                                int rowsDeleted = pst.executeUpdate();
                                if (rowsDeleted > 0) {
                                    JOptionPane.showMessageDialog(this, "Member deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    throw new Exception("No member found with the given ID!");
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        throw new Exception("Failed to delete member. Database issue!");
                    }
                    break;

                    default:
                        throw new Exception("Invalid action!");
                }

                // Reload table after each operation
                loadMembersToTable();

                // Clear fields
                memberIDTxt.setText("");
                nameTxt.setText("");
                addressTxt.setText("");
                emailTxt.setText("");
                phoneNoTxt.setText("");
            }
        } catch (Exception ex) {
            // Display error message for any issue
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Manage books: Add, Update, Delete
    public void manageBooks(String action) {
        try {
            // Retrieve input values
            String bookIDStr = bookIDTxt.getText().trim();
            String title = titleTxt.getText().trim();
            String author = authorTxt.getText().trim();
            String yearPublishedStr = yearPublishedTxt.getText().trim();

            // Validate input fields
            if (bookIDStr.isEmpty() || title.isEmpty() || author.isEmpty() || yearPublishedStr.isEmpty()) {
                throw new Exception("All fields are required!");
            }

            int bookID, yearPublished;

            // Validate numeric fields
            try {
                bookID = Integer.parseInt(bookIDStr);
                yearPublished = Integer.parseInt(yearPublishedStr);
            } catch (NumberFormatException ex) {
                throw new Exception("BookID and Year Published must be numeric!");
            }

            try (Connection con = DriverManager.getConnection(jdbcUrl, user, dbpassword)) {
                switch (action.toLowerCase()) {
                    case "add":
                        String addSql = "INSERT INTO book (ID, title, author, yearPublished) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement pst = con.prepareStatement(addSql)) {
                            pst.setInt(1, bookID);
                            pst.setString(2, title);
                            pst.setString(3, author);
                            pst.setInt(4, yearPublished);
                            pst.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                        break;

                    case "update":
                        String updateSql = "UPDATE book SET title = ?, author = ?, yearPublished = ? WHERE ID = ?";
                        try (PreparedStatement pst = con.prepareStatement(updateSql)) {
                            pst.setString(1, title);
                            pst.setString(2, author);
                            pst.setInt(3, yearPublished);
                            pst.setInt(4, bookID);
                            int rowsUpdated = pst.executeUpdate();
                            if (rowsUpdated > 0) {
                                JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                throw new Exception("No book found with the given ID!");
                            }
                        }
                        break;

                    case "delete":
                        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            String deleteSql = "DELETE FROM book WHERE ID = ?";
                            try (PreparedStatement pst = con.prepareStatement(deleteSql)) {
                                pst.setInt(1, bookID);
                                int rowsDeleted = pst.executeUpdate();
                                if (rowsDeleted > 0) {
                                    JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    throw new Exception("No book found with the given ID!");
                                }
                            }
                        }
                        break;

                    default:
                        throw new Exception("Invalid action specified!");
                }

                // Reload tables after operation
                loadBooksToTable();

                // Clear fields
                bookIDTxt.setText("");
                titleTxt.setText("");
                authorTxt.setText("");
                yearPublishedTxt.setText("");

            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void searchID() {
        String memberID = memberIDTxt2.getText().trim();
        String bookID = bookIDTxt2.getText().trim();

        if (memberID.isEmpty() || bookID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Member ID and Book ID!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = DriverManager.getConnection(jdbcUrl, user, dbpassword)) {
            // Fetch member details
            String memberSql = "SELECT ID, name, email FROM member WHERE ID = ?";
            try (PreparedStatement memberPst = con.prepareStatement(memberSql)) {
                memberPst.setString(1, memberID);
                try (ResultSet rs = memberPst.executeQuery()) {
                    if (rs.next()) {
                        memberIDLbl2.setText(rs.getString("ID"));
                        nameLbl3.setText(rs.getString("name"));
                        emailLbl2.setText(rs.getString("email"));
                    } else {
                        JOptionPane.showMessageDialog(this, "Member not found!", "Search Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }

            // Fetch book details
            String bookSql = "SELECT ID, title, author FROM book WHERE ID = ?";
            try (PreparedStatement bookPst = con.prepareStatement(bookSql)) {
                bookPst.setString(1, bookID);
                try (ResultSet rs = bookPst.executeQuery()) {
                    if (rs.next()) {
                        bookIDLbl2.setText(rs.getString("ID"));
                        titleLbl3.setText(rs.getString("title"));
                        authorLbl3.setText(rs.getString("author"));
                    } else {
                        JOptionPane.showMessageDialog(this, "Book not found!", "Search Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

 






    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        rSButtonHover1 = new rojerusan.RSButtonHover();
        tabPanel = new javax.swing.JTabbedPane();
        homePanel = new javax.swing.JPanel();
        bookDisPanel = new javax.swing.JPanel();
        bookCountLbl = new javax.swing.JLabel();
        noOfBookLbl = new javax.swing.JLabel();
        memberDetLbl = new javax.swing.JLabel();
        memberDisPanel = new javax.swing.JPanel();
        memberCountLbl = new javax.swing.JLabel();
        issuedBookLbl = new javax.swing.JLabel();
        issueBookPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        memberTbl = new rojeru_san.complementos.RSTableMetro();
        noOfMemberLbl = new javax.swing.JLabel();
        bookDetLbl = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        bookTbl1 = new rojeru_san.complementos.RSTableMetro();
        memberPanel = new javax.swing.JPanel();
        memberIDLbl = new javax.swing.JLabel();
        memberIDTxt = new rojerusan.RSMetroTextPlaceHolder();
        nameTxt = new rojerusan.RSMetroTextPlaceHolder();
        nameLbl = new javax.swing.JLabel();
        addressTxt = new rojerusan.RSMetroTextPlaceHolder();
        addressLbl = new javax.swing.JLabel();
        emailLbl = new javax.swing.JLabel();
        emailTxt = new rojerusan.RSMetroTextPlaceHolder();
        phoneNoTxt = new rojerusan.RSMetroTextPlaceHolder();
        phoneNoLbl = new javax.swing.JLabel();
        addBtn = new rojerusan.RSMaterialButtonRectangle();
        updateBtn = new rojerusan.RSMaterialButtonRectangle();
        resetBtn = new rojerusan.RSMaterialButtonRectangle();
        deleteBtn = new rojerusan.RSMaterialButtonRectangle();
        jScrollPane3 = new javax.swing.JScrollPane();
        memberTbl1 = new rojeru_san.complementos.RSTableMetro();
        bgImage = new rojerusan.RSLabelImage();
        bookPanel = new javax.swing.JPanel();
        bookAddBtn = new rojerusan.RSMaterialButtonRectangle();
        bookUpdateBtn = new rojerusan.RSMaterialButtonRectangle();
        bookDeleteBtn = new rojerusan.RSMaterialButtonRectangle();
        bookResetBtn = new rojerusan.RSMaterialButtonRectangle();
        yearPublishedTxt = new rojerusan.RSMetroTextPlaceHolder();
        yearPublishedLbl = new javax.swing.JLabel();
        authorTxt = new rojerusan.RSMetroTextPlaceHolder();
        authorLbl = new javax.swing.JLabel();
        titleTxt = new rojerusan.RSMetroTextPlaceHolder();
        titleLbl = new javax.swing.JLabel();
        bookIDTxt = new rojerusan.RSMetroTextPlaceHolder();
        bookIDLbl = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        bookTbl = new rojeru_san.complementos.RSTableMetro();
        bgImage1 = new rojerusan.RSLabelImage();
        iBookPanel = new javax.swing.JPanel();
        memberDisPanel1 = new javax.swing.JPanel();
        memberIDLbl1 = new javax.swing.JLabel();
        nameLbl1 = new javax.swing.JLabel();
        memberIDLbl2 = new javax.swing.JLabel();
        emailLbl1 = new javax.swing.JLabel();
        emailLbl2 = new javax.swing.JLabel();
        nameLbl3 = new javax.swing.JLabel();
        bookDisPanel1 = new javax.swing.JPanel();
        bookIDLbl1 = new javax.swing.JLabel();
        bookIDLbl2 = new javax.swing.JLabel();
        titleLbl2 = new javax.swing.JLabel();
        authorLbl2 = new javax.swing.JLabel();
        titleLbl3 = new javax.swing.JLabel();
        authorLbl3 = new javax.swing.JLabel();
        memberIDLbl3 = new javax.swing.JLabel();
        bookIDLbl3 = new javax.swing.JLabel();
        memberIDTxt2 = new rojerusan.RSMetroTextPlaceHolder();
        bookIDTxt2 = new rojerusan.RSMetroTextPlaceHolder();
        issueDateLbl = new javax.swing.JLabel();
        issueDateLbl1 = new javax.swing.JLabel();
        issueBtn = new rojerusan.RSMaterialButtonRectangle();
        searchIDBtn = new rojerusan.RSMaterialButtonRectangle();
        issueDate = new com.toedter.calendar.JDateChooser();
        dueDate = new com.toedter.calendar.JDateChooser();
        rBookPanel = new javax.swing.JPanel();
        memberDisPanel2 = new javax.swing.JPanel();
        issueIdLbl = new javax.swing.JLabel();
        memNameLbl = new javax.swing.JLabel();
        issueIDLbl2 = new javax.swing.JLabel();
        bookTitleLbl = new javax.swing.JLabel();
        bookTitleLbl2 = new javax.swing.JLabel();
        memNameLbl2 = new javax.swing.JLabel();
        issueDateLbl2 = new javax.swing.JLabel();
        issueDateLbl3 = new javax.swing.JLabel();
        dueDateLbl2 = new javax.swing.JLabel();
        dueDateLbl3 = new javax.swing.JLabel();
        bookIDLbl4 = new javax.swing.JLabel();
        bookIDTxt3 = new rojerusan.RSMetroTextPlaceHolder();
        returnBtn = new rojerusan.RSMaterialButtonRectangle();
        studentIdLbl = new javax.swing.JLabel();
        memberIDTxt3 = new rojerusan.RSMetroTextPlaceHolder();
        findBtn = new rojerusan.RSMaterialButtonRectangle();
        recordPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        issueDate2 = new rojeru_san.componentes.RSDateChooser();
        dueDate2 = new rojeru_san.componentes.RSDateChooser();
        jLabel2 = new javax.swing.JLabel();
        searchBtn = new rojerusan.RSMaterialButtonRectangle();
        jScrollPane5 = new javax.swing.JScrollPane();
        memberTbl2 = new rojeru_san.complementos.RSTableMetro();
        menuBar = new javax.swing.JMenuBar();
        homeMenu = new javax.swing.JMenu();
        memberMenu = new javax.swing.JMenu();
        bookMenu = new javax.swing.JMenu();
        iBookMenu = new javax.swing.JMenu();
        rBookMenu = new javax.swing.JMenu();
        recordMenu = new javax.swing.JMenu();
        exitMenu = new javax.swing.JMenu();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setFont(new java.awt.Font("Source Sans 3 ExtraBold", 0, 18)); // NOI18N
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 102, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rSButtonHover1.setBackground(new java.awt.Color(255, 153, 0));
        rSButtonHover1.setText("X");
        rSButtonHover1.setColorHover(new java.awt.Color(0, 0, 0));
        rSButtonHover1.setColorTextHover(new java.awt.Color(255, 153, 0));
        rSButtonHover1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover1ActionPerformed(evt);
            }
        });
        jPanel1.add(rSButtonHover1, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 0, 50, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 0));

        homePanel.setBackground(new java.awt.Color(102, 0, 255));
        homePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bookDisPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        bookDisPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bookCountLbl.setText("13");
        bookDisPanel.add(bookCountLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        homePanel.add(bookDisPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 120, 80));

        noOfBookLbl.setText("No. Of Books");
        homePanel.add(noOfBookLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, -1, -1));

        memberDetLbl.setText("Member Details");
        homePanel.add(memberDetLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, -1, -1));

        memberDisPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        memberDisPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        memberCountLbl.setText("13");
        memberDisPanel.add(memberCountLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        homePanel.add(memberDisPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 120, 80));

        issuedBookLbl.setText("Issued Books");
        homePanel.add(issuedBookLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 40, -1, -1));

        issueBookPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        issueBookPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setText("13");
        issueBookPanel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        homePanel.add(issueBookPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 60, 120, 80));

        memberTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Member ID", "Name", "Address", "Email", "Phone no"
            }
        ));
        memberTbl.setAltoHead(20);
        memberTbl.setColorBackgoundHead(new java.awt.Color(204, 102, 0));
        memberTbl.setColorBordeFilas(new java.awt.Color(51, 51, 51));
        memberTbl.setColorBordeHead(new java.awt.Color(51, 51, 255));
        memberTbl.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        memberTbl.setColorFilasForeground1(new java.awt.Color(255, 102, 0));
        memberTbl.setColorFilasForeground2(new java.awt.Color(102, 102, 102));
        memberTbl.setColorSelBackgound(new java.awt.Color(153, 153, 153));
        memberTbl.setColorSelForeground(new java.awt.Color(0, 0, 0));
        memberTbl.setFuenteFilas(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        memberTbl.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        memberTbl.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        memberTbl.setIntercellSpacing(new java.awt.Dimension(0, 0));
        memberTbl.setRowHeight(20);
        jScrollPane1.setViewportView(memberTbl);

        homePanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 490, 110));

        noOfMemberLbl.setText("No. Of Members");
        homePanel.add(noOfMemberLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, -1, -1));

        bookDetLbl.setText("Book Details");
        homePanel.add(bookDetLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 320, -1, -1));

        bookTbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Book ID", "Title", "Author", "YearPublished"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bookTbl1.setAltoHead(20);
        bookTbl1.setColorBackgoundHead(new java.awt.Color(204, 102, 0));
        bookTbl1.setColorBordeFilas(new java.awt.Color(51, 51, 51));
        bookTbl1.setColorBordeHead(new java.awt.Color(51, 51, 255));
        bookTbl1.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        bookTbl1.setColorFilasForeground1(new java.awt.Color(255, 102, 0));
        bookTbl1.setColorFilasForeground2(new java.awt.Color(102, 102, 102));
        bookTbl1.setColorSelBackgound(new java.awt.Color(153, 153, 153));
        bookTbl1.setFuenteFilas(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        bookTbl1.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        bookTbl1.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        bookTbl1.setIntercellSpacing(new java.awt.Dimension(0, 0));
        bookTbl1.setRowHeight(20);
        jScrollPane2.setViewportView(bookTbl1);

        homePanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, 490, 110));

        tabPanel.addTab("tab1", homePanel);

        memberPanel.setBackground(new java.awt.Color(255, 51, 51));
        memberPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        memberIDLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        memberIDLbl.setForeground(new java.awt.Color(255, 255, 255));
        memberIDLbl.setText("Member ID");
        memberPanel.add(memberIDLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, 20));

        memberIDTxt.setBackground(new java.awt.Color(0, 0, 0));
        memberIDTxt.setForeground(new java.awt.Color(255, 102, 0));
        memberIDTxt.setBorderColor(new java.awt.Color(153, 153, 153));
        memberIDTxt.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        memberIDTxt.setPhColor(new java.awt.Color(153, 153, 153));
        memberIDTxt.setPlaceholder("Enter Name");
        memberPanel.add(memberIDTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 180, 30));

        nameTxt.setBackground(new java.awt.Color(0, 0, 0));
        nameTxt.setForeground(new java.awt.Color(255, 102, 0));
        nameTxt.setBorderColor(new java.awt.Color(153, 153, 153));
        nameTxt.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        nameTxt.setPhColor(new java.awt.Color(153, 153, 153));
        nameTxt.setPlaceholder("Enter Name");
        memberPanel.add(nameTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 180, 30));

        nameLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        nameLbl.setForeground(new java.awt.Color(255, 255, 255));
        nameLbl.setText("Name");
        memberPanel.add(nameLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, 20));

        addressTxt.setBackground(new java.awt.Color(0, 0, 0));
        addressTxt.setForeground(new java.awt.Color(255, 102, 0));
        addressTxt.setBorderColor(new java.awt.Color(153, 153, 153));
        addressTxt.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        addressTxt.setPhColor(new java.awt.Color(153, 153, 153));
        addressTxt.setPlaceholder("Enter Name");
        memberPanel.add(addressTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 180, 30));

        addressLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        addressLbl.setForeground(new java.awt.Color(255, 255, 255));
        addressLbl.setText("Address");
        memberPanel.add(addressLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, 20));

        emailLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        emailLbl.setForeground(new java.awt.Color(255, 255, 255));
        emailLbl.setText("Email");
        memberPanel.add(emailLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, -1, 20));

        emailTxt.setBackground(new java.awt.Color(0, 0, 0));
        emailTxt.setForeground(new java.awt.Color(255, 102, 0));
        emailTxt.setBorderColor(new java.awt.Color(153, 153, 153));
        emailTxt.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        emailTxt.setPhColor(new java.awt.Color(153, 153, 153));
        emailTxt.setPlaceholder("Enter Name");
        memberPanel.add(emailTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 180, 30));

        phoneNoTxt.setBackground(new java.awt.Color(0, 0, 0));
        phoneNoTxt.setForeground(new java.awt.Color(255, 102, 0));
        phoneNoTxt.setBorderColor(new java.awt.Color(153, 153, 153));
        phoneNoTxt.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        phoneNoTxt.setPhColor(new java.awt.Color(153, 153, 153));
        phoneNoTxt.setPlaceholder("Enter Name");
        memberPanel.add(phoneNoTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 180, 30));

        phoneNoLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        phoneNoLbl.setForeground(new java.awt.Color(255, 255, 255));
        phoneNoLbl.setText("PhoneNo");
        memberPanel.add(phoneNoLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, -1, 20));

        addBtn.setBackground(new java.awt.Color(255, 153, 0));
        addBtn.setText("Add");
        addBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        addBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addBtnMouseClicked(evt);
            }
        });
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });
        memberPanel.add(addBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, 112, 38));

        updateBtn.setBackground(new java.awt.Color(255, 153, 0));
        updateBtn.setText("Update");
        updateBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        updateBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateBtnMouseClicked(evt);
            }
        });
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });
        memberPanel.add(updateBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 190, 112, 38));

        resetBtn.setBackground(new java.awt.Color(255, 153, 0));
        resetBtn.setText("reset");
        resetBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        resetBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resetBtnMouseClicked(evt);
            }
        });
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
            }
        });
        memberPanel.add(resetBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 320, 112, 38));

        deleteBtn.setBackground(new java.awt.Color(255, 153, 0));
        deleteBtn.setText("delete");
        deleteBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        deleteBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteBtnMouseClicked(evt);
            }
        });
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });
        memberPanel.add(deleteBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 260, 112, 38));

        memberTbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Member ID", "Name", "Address", "Email", "Phone no"
            }
        ));
        memberTbl1.setAltoHead(20);
        memberTbl1.setColorBackgoundHead(new java.awt.Color(204, 102, 0));
        memberTbl1.setColorBordeFilas(new java.awt.Color(51, 51, 51));
        memberTbl1.setColorBordeHead(new java.awt.Color(51, 51, 255));
        memberTbl1.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        memberTbl1.setColorFilasForeground1(new java.awt.Color(255, 102, 0));
        memberTbl1.setColorFilasForeground2(new java.awt.Color(102, 102, 102));
        memberTbl1.setColorSelBackgound(new java.awt.Color(153, 153, 153));
        memberTbl1.setColorSelForeground(new java.awt.Color(0, 0, 0));
        memberTbl1.setFuenteFilas(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        memberTbl1.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        memberTbl1.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        memberTbl1.setIntercellSpacing(new java.awt.Dimension(0, 0));
        memberTbl1.setRowHeight(20);
        memberTbl1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                memberTbl1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(memberTbl1);

        memberPanel.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 30, 320, 110));

        bgImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/bg/register 3.jpg"))); // NOI18N
        memberPanel.add(bgImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, 480));

        tabPanel.addTab("tab2", memberPanel);

        bookPanel.setBackground(new java.awt.Color(153, 153, 153));
        bookPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bookAddBtn.setBackground(new java.awt.Color(255, 153, 0));
        bookAddBtn.setText("Add");
        bookAddBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        bookAddBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookAddBtnMouseClicked(evt);
            }
        });
        bookPanel.add(bookAddBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, 112, 38));

        bookUpdateBtn.setBackground(new java.awt.Color(255, 153, 0));
        bookUpdateBtn.setText("Update");
        bookUpdateBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        bookUpdateBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookUpdateBtnMouseClicked(evt);
            }
        });
        bookPanel.add(bookUpdateBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 190, 112, 38));

        bookDeleteBtn.setBackground(new java.awt.Color(255, 153, 0));
        bookDeleteBtn.setText("delete");
        bookDeleteBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        bookDeleteBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookDeleteBtnMouseClicked(evt);
            }
        });
        bookPanel.add(bookDeleteBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 260, 112, 38));

        bookResetBtn.setBackground(new java.awt.Color(255, 153, 0));
        bookResetBtn.setText("reset");
        bookResetBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        bookResetBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookResetBtnMouseClicked(evt);
            }
        });
        bookPanel.add(bookResetBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 320, 112, 38));

        yearPublishedTxt.setBackground(new java.awt.Color(0, 0, 0));
        yearPublishedTxt.setForeground(new java.awt.Color(255, 102, 0));
        yearPublishedTxt.setBorderColor(new java.awt.Color(153, 153, 153));
        yearPublishedTxt.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        yearPublishedTxt.setPhColor(new java.awt.Color(153, 153, 153));
        yearPublishedTxt.setPlaceholder("Enter Name");
        bookPanel.add(yearPublishedTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 180, 50));

        yearPublishedLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        yearPublishedLbl.setForeground(new java.awt.Color(255, 255, 255));
        yearPublishedLbl.setText("YearPublished");
        bookPanel.add(yearPublishedLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, -1, 20));

        authorTxt.setBackground(new java.awt.Color(0, 0, 0));
        authorTxt.setForeground(new java.awt.Color(255, 102, 0));
        authorTxt.setBorderColor(new java.awt.Color(153, 153, 153));
        authorTxt.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        authorTxt.setPhColor(new java.awt.Color(153, 153, 153));
        authorTxt.setPlaceholder("Enter Name");
        bookPanel.add(authorTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 180, 50));

        authorLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        authorLbl.setForeground(new java.awt.Color(255, 255, 255));
        authorLbl.setText("Author");
        bookPanel.add(authorLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, 20));

        titleTxt.setBackground(new java.awt.Color(0, 0, 0));
        titleTxt.setForeground(new java.awt.Color(255, 102, 0));
        titleTxt.setBorderColor(new java.awt.Color(153, 153, 153));
        titleTxt.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        titleTxt.setPhColor(new java.awt.Color(153, 153, 153));
        titleTxt.setPlaceholder("Enter Name");
        bookPanel.add(titleTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 180, 50));

        titleLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        titleLbl.setForeground(new java.awt.Color(255, 255, 255));
        titleLbl.setText("Title");
        bookPanel.add(titleLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, 20));

        bookIDTxt.setBackground(new java.awt.Color(0, 0, 0));
        bookIDTxt.setForeground(new java.awt.Color(255, 102, 0));
        bookIDTxt.setBorderColor(new java.awt.Color(153, 153, 153));
        bookIDTxt.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        bookIDTxt.setPhColor(new java.awt.Color(153, 153, 153));
        bookIDTxt.setPlaceholder("Enter Name");
        bookPanel.add(bookIDTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 180, 50));

        bookIDLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        bookIDLbl.setForeground(new java.awt.Color(255, 255, 255));
        bookIDLbl.setText("Book ID");
        bookPanel.add(bookIDLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, 20));

        bookTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Book ID", "Title", "Author", "YearPublished"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        bookTbl.setAltoHead(20);
        bookTbl.setColorBackgoundHead(new java.awt.Color(204, 102, 0));
        bookTbl.setColorBordeFilas(new java.awt.Color(51, 51, 51));
        bookTbl.setColorBordeHead(new java.awt.Color(51, 51, 255));
        bookTbl.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        bookTbl.setColorFilasForeground1(new java.awt.Color(255, 102, 0));
        bookTbl.setColorFilasForeground2(new java.awt.Color(102, 102, 102));
        bookTbl.setColorSelBackgound(new java.awt.Color(153, 153, 153));
        bookTbl.setFuenteFilas(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        bookTbl.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        bookTbl.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        bookTbl.setIntercellSpacing(new java.awt.Dimension(0, 0));
        bookTbl.setRowHeight(20);
        bookTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookTblMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(bookTbl);

        bookPanel.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 180, 350, 120));

        bgImage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/bg/register 3.jpg"))); // NOI18N
        bookPanel.add(bgImage1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 440, 480));

        tabPanel.addTab("tab3", bookPanel);

        iBookPanel.setBackground(new java.awt.Color(204, 204, 255));
        iBookPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        memberDisPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        memberIDLbl1.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        memberIDLbl1.setForeground(new java.awt.Color(255, 255, 255));
        memberIDLbl1.setText("Member ID :");
        memberDisPanel1.add(memberIDLbl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 20));

        nameLbl1.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        nameLbl1.setForeground(new java.awt.Color(255, 255, 255));
        nameLbl1.setText("Name :");
        memberDisPanel1.add(nameLbl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, 20));

        memberIDLbl2.setBackground(new java.awt.Color(255, 255, 255));
        memberIDLbl2.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        memberIDLbl2.setForeground(new java.awt.Color(255, 255, 255));
        memberIDLbl2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(0, 0, 0)));
        memberDisPanel1.add(memberIDLbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 110, 30));

        emailLbl1.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        emailLbl1.setForeground(new java.awt.Color(255, 255, 255));
        emailLbl1.setText("Email :");
        memberDisPanel1.add(emailLbl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, 20));

        emailLbl2.setBackground(new java.awt.Color(255, 255, 255));
        emailLbl2.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        emailLbl2.setForeground(new java.awt.Color(255, 255, 255));
        emailLbl2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(0, 0, 0)));
        memberDisPanel1.add(emailLbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 80, 20));

        nameLbl3.setBackground(new java.awt.Color(255, 255, 255));
        nameLbl3.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        nameLbl3.setForeground(new java.awt.Color(255, 255, 255));
        nameLbl3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(0, 0, 0)));
        memberDisPanel1.add(nameLbl3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 110, 30));

        iBookPanel.add(memberDisPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 240, 150));

        bookDisPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bookIDLbl1.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        bookIDLbl1.setForeground(new java.awt.Color(255, 255, 255));
        bookIDLbl1.setText("Book ID :");
        bookDisPanel1.add(bookIDLbl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 20));

        bookIDLbl2.setBackground(new java.awt.Color(255, 255, 255));
        bookIDLbl2.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        bookIDLbl2.setForeground(new java.awt.Color(255, 255, 255));
        bookIDLbl2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(0, 0, 0)));
        bookDisPanel1.add(bookIDLbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 110, 30));

        titleLbl2.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        titleLbl2.setForeground(new java.awt.Color(255, 255, 255));
        titleLbl2.setText("Title :");
        bookDisPanel1.add(titleLbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, 20));

        authorLbl2.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        authorLbl2.setForeground(new java.awt.Color(255, 255, 255));
        authorLbl2.setText("Author :");
        bookDisPanel1.add(authorLbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, 20));

        titleLbl3.setBackground(new java.awt.Color(255, 255, 255));
        titleLbl3.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        titleLbl3.setForeground(new java.awt.Color(255, 255, 255));
        titleLbl3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(0, 0, 0)));
        bookDisPanel1.add(titleLbl3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 70, 30));

        authorLbl3.setBackground(new java.awt.Color(255, 255, 255));
        authorLbl3.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        authorLbl3.setForeground(new java.awt.Color(255, 255, 255));
        authorLbl3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(0, 0, 0)));
        bookDisPanel1.add(authorLbl3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 90, 20));

        iBookPanel.add(bookDisPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 240, 150));

        memberIDLbl3.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        memberIDLbl3.setForeground(new java.awt.Color(255, 255, 255));
        memberIDLbl3.setText("Member ID :");
        iBookPanel.add(memberIDLbl3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 160, -1, 20));

        bookIDLbl3.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        bookIDLbl3.setForeground(new java.awt.Color(255, 255, 255));
        bookIDLbl3.setText("Book ID :");
        iBookPanel.add(bookIDLbl3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, -1, 20));

        memberIDTxt2.setBackground(new java.awt.Color(0, 0, 0));
        memberIDTxt2.setForeground(new java.awt.Color(255, 102, 0));
        memberIDTxt2.setBorderColor(new java.awt.Color(153, 153, 153));
        memberIDTxt2.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        memberIDTxt2.setPhColor(new java.awt.Color(153, 153, 153));
        memberIDTxt2.setPlaceholder("Enter Name");
        iBookPanel.add(memberIDTxt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 150, 180, 30));

        bookIDTxt2.setBackground(new java.awt.Color(0, 0, 0));
        bookIDTxt2.setForeground(new java.awt.Color(255, 102, 0));
        bookIDTxt2.setBorderColor(new java.awt.Color(153, 153, 153));
        bookIDTxt2.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        bookIDTxt2.setPhColor(new java.awt.Color(153, 153, 153));
        bookIDTxt2.setPlaceholder("Enter Name");
        iBookPanel.add(bookIDTxt2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 70, 180, 30));

        issueDateLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        issueDateLbl.setForeground(new java.awt.Color(255, 255, 255));
        issueDateLbl.setText("Due Date");
        iBookPanel.add(issueDateLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 290, -1, 20));

        issueDateLbl1.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        issueDateLbl1.setForeground(new java.awt.Color(255, 255, 255));
        issueDateLbl1.setText("Issue Date");
        iBookPanel.add(issueDateLbl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 230, -1, 20));

        issueBtn.setBackground(new java.awt.Color(255, 153, 0));
        issueBtn.setText("issue");
        issueBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        issueBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                issueBtnMouseClicked(evt);
            }
        });
        iBookPanel.add(issueBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 360, 112, 38));

        searchIDBtn.setBackground(new java.awt.Color(255, 153, 0));
        searchIDBtn.setText("Search");
        searchIDBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        searchIDBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchIDBtnMouseClicked(evt);
            }
        });
        iBookPanel.add(searchIDBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 90, 112, 38));
        iBookPanel.add(issueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 220, 180, 40));
        iBookPanel.add(dueDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 280, 180, 40));

        tabPanel.addTab("tab4", iBookPanel);

        rBookPanel.setBackground(new java.awt.Color(102, 102, 255));
        rBookPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        memberDisPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        issueIdLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        issueIdLbl.setForeground(new java.awt.Color(255, 255, 255));
        issueIdLbl.setText("Issue ID:");
        memberDisPanel2.add(issueIdLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 20));

        memNameLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        memNameLbl.setForeground(new java.awt.Color(255, 255, 255));
        memNameLbl.setText("Member Name :");
        memberDisPanel2.add(memNameLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, 20));

        issueIDLbl2.setBackground(new java.awt.Color(255, 255, 255));
        issueIDLbl2.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        issueIDLbl2.setForeground(new java.awt.Color(255, 255, 255));
        issueIDLbl2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(0, 0, 0)));
        memberDisPanel2.add(issueIDLbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 110, 30));

        bookTitleLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        bookTitleLbl.setForeground(new java.awt.Color(255, 255, 255));
        bookTitleLbl.setText("Book Title :");
        memberDisPanel2.add(bookTitleLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, 20));

        bookTitleLbl2.setBackground(new java.awt.Color(255, 255, 255));
        bookTitleLbl2.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        bookTitleLbl2.setForeground(new java.awt.Color(255, 255, 255));
        bookTitleLbl2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(0, 0, 0)));
        memberDisPanel2.add(bookTitleLbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 80, 20));

        memNameLbl2.setBackground(new java.awt.Color(255, 255, 255));
        memNameLbl2.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        memNameLbl2.setForeground(new java.awt.Color(255, 255, 255));
        memNameLbl2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(0, 0, 0)));
        memberDisPanel2.add(memNameLbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 110, 30));

        issueDateLbl2.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        issueDateLbl2.setForeground(new java.awt.Color(255, 255, 255));
        issueDateLbl2.setText("Issue Date :");
        memberDisPanel2.add(issueDateLbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, 20));

        issueDateLbl3.setBackground(new java.awt.Color(255, 255, 255));
        issueDateLbl3.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        issueDateLbl3.setForeground(new java.awt.Color(255, 255, 255));
        issueDateLbl3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(0, 0, 0)));
        memberDisPanel2.add(issueDateLbl3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 80, 20));

        dueDateLbl2.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        dueDateLbl2.setForeground(new java.awt.Color(255, 255, 255));
        dueDateLbl2.setText("Due Date :");
        memberDisPanel2.add(dueDateLbl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, 20));

        dueDateLbl3.setBackground(new java.awt.Color(255, 255, 255));
        dueDateLbl3.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        dueDateLbl3.setForeground(new java.awt.Color(255, 255, 255));
        dueDateLbl3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 4, 0, new java.awt.Color(0, 0, 0)));
        memberDisPanel2.add(dueDateLbl3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 80, 20));

        rBookPanel.add(memberDisPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 240, 320));

        bookIDLbl4.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        bookIDLbl4.setForeground(new java.awt.Color(255, 255, 255));
        bookIDLbl4.setText("Book ID :");
        rBookPanel.add(bookIDLbl4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, -1, -1));

        bookIDTxt3.setBackground(new java.awt.Color(0, 0, 0));
        bookIDTxt3.setForeground(new java.awt.Color(255, 102, 0));
        bookIDTxt3.setBorderColor(new java.awt.Color(153, 153, 153));
        bookIDTxt3.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        bookIDTxt3.setPhColor(new java.awt.Color(153, 153, 153));
        bookIDTxt3.setPlaceholder("Enter Name");
        rBookPanel.add(bookIDTxt3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 70, 180, 30));

        returnBtn.setBackground(new java.awt.Color(255, 153, 0));
        returnBtn.setText("return");
        returnBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        rBookPanel.add(returnBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 190, 112, 40));

        studentIdLbl.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 14)); // NOI18N
        studentIdLbl.setForeground(new java.awt.Color(255, 255, 255));
        studentIdLbl.setText("Student ID:");
        rBookPanel.add(studentIdLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 130, -1, -1));

        memberIDTxt3.setBackground(new java.awt.Color(0, 0, 0));
        memberIDTxt3.setForeground(new java.awt.Color(255, 102, 0));
        memberIDTxt3.setBorderColor(new java.awt.Color(153, 153, 153));
        memberIDTxt3.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        memberIDTxt3.setPhColor(new java.awt.Color(153, 153, 153));
        memberIDTxt3.setPlaceholder("Enter Name");
        rBookPanel.add(memberIDTxt3, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 120, 180, 30));

        findBtn.setBackground(new java.awt.Color(255, 153, 0));
        findBtn.setText("FInd");
        findBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        findBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findBtnActionPerformed(evt);
            }
        });
        rBookPanel.add(findBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 190, 112, 40));

        tabPanel.addTab("tab5", rBookPanel);

        recordPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Issue Date: ");
        recordPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, -1, -1));
        recordPanel.add(issueDate2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, 30));
        recordPanel.add(dueDate2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, -1, 30));

        jLabel2.setText("Due Date:");
        recordPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, -1, -1));

        searchBtn.setBackground(new java.awt.Color(255, 153, 0));
        searchBtn.setText("Search");
        searchBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });
        recordPanel.add(searchBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 60, 112, 40));

        memberTbl2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Issue ID", "Member ID", "Book ID", "Book Name", "Member Name", "Issue Date", "Due Date", "Status"
            }
        ));
        memberTbl2.setAltoHead(20);
        memberTbl2.setColorBackgoundHead(new java.awt.Color(204, 102, 0));
        memberTbl2.setColorBordeFilas(new java.awt.Color(51, 51, 51));
        memberTbl2.setColorBordeHead(new java.awt.Color(51, 51, 255));
        memberTbl2.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        memberTbl2.setColorFilasForeground1(new java.awt.Color(255, 102, 0));
        memberTbl2.setColorFilasForeground2(new java.awt.Color(102, 102, 102));
        memberTbl2.setColorSelBackgound(new java.awt.Color(153, 153, 153));
        memberTbl2.setColorSelForeground(new java.awt.Color(0, 0, 0));
        memberTbl2.setFuenteFilas(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        memberTbl2.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        memberTbl2.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        memberTbl2.setIntercellSpacing(new java.awt.Dimension(0, 0));
        memberTbl2.setRowHeight(20);
        jScrollPane5.setViewportView(memberTbl2);

        recordPanel.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 590, 110));

        tabPanel.addTab("tab6", recordPanel);

        getContentPane().add(tabPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, 770, 500));

        homeMenu.setText("Home");
        homeMenu.setFont(new java.awt.Font("Source Sans 3 ExtraBold", 0, 16)); // NOI18N
        homeMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeMenuMouseClicked(evt);
            }
        });
        menuBar.add(homeMenu);

        memberMenu.setText("Manage Member");
        memberMenu.setFont(new java.awt.Font("Source Sans 3 ExtraBold", 0, 16)); // NOI18N
        memberMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                memberMenuMouseClicked(evt);
            }
        });
        menuBar.add(memberMenu);

        bookMenu.setText("Manage Book");
        bookMenu.setFont(new java.awt.Font("Source Sans 3 ExtraBold", 0, 16)); // NOI18N
        bookMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookMenuMouseClicked(evt);
            }
        });

        iBookMenu.setText("Issue Book");
        iBookMenu.setFont(new java.awt.Font("Source Sans 3 ExtraBold", 0, 16)); // NOI18N
        iBookMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                iBookMenuMouseClicked(evt);
            }
        });
        bookMenu.add(iBookMenu);

        rBookMenu.setText("Return Book");
        rBookMenu.setFont(new java.awt.Font("Source Sans 3 ExtraBold", 0, 16)); // NOI18N
        rBookMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rBookMenuMouseClicked(evt);
            }
        });
        bookMenu.add(rBookMenu);

        menuBar.add(bookMenu);

        recordMenu.setText("View Record");
        recordMenu.setFont(new java.awt.Font("Source Sans 3 ExtraBold", 0, 16)); // NOI18N
        recordMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                recordMenuMouseClicked(evt);
            }
        });
        menuBar.add(recordMenu);

        exitMenu.setText("Exit");
        exitMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitMenuMouseClicked(evt);
            }
        });
        menuBar.add(exitMenu);

        setJMenuBar(menuBar);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void rSButtonHover1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_rSButtonHover1ActionPerformed

    private void homeMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMenuMouseClicked
        // TODO add your handling code here:
        tabPanel.setSelectedIndex(0);

    }//GEN-LAST:event_homeMenuMouseClicked

    private void memberMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_memberMenuMouseClicked
        // TODO add your handling code here:
        tabPanel.setSelectedIndex(1);
    }//GEN-LAST:event_memberMenuMouseClicked

    private void bookMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookMenuMouseClicked
        // TODO add your handling code here:
        tabPanel.setSelectedIndex(2);
    }//GEN-LAST:event_bookMenuMouseClicked

    private void iBookMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_iBookMenuMouseClicked
        // TODO add your handling code here:
        tabPanel.setSelectedIndex(3);
    }//GEN-LAST:event_iBookMenuMouseClicked

    private void rBookMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rBookMenuMouseClicked
        // TODO add your handling code here:
        tabPanel.setSelectedIndex(4);
    }//GEN-LAST:event_rBookMenuMouseClicked

    private void recordMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recordMenuMouseClicked
        // TODO add your handling code here:
        tabPanel.setSelectedIndex(5);
    }//GEN-LAST:event_recordMenuMouseClicked

    private void findBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_findBtnActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchBtnActionPerformed

    private void exitMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitMenuMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_exitMenuMouseClicked

    private void addBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addBtnMouseClicked
        // TODO add your handling code here:
        manageMembers("add");
    }//GEN-LAST:event_addBtnMouseClicked

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resetBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_addBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updateBtnActionPerformed

    private void resetBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetBtnMouseClicked
        // TODO add your handling code here:
        memberIDTxt.setText("");
        nameTxt.setText("");
        addressTxt.setText("");
        emailTxt.setText("");
        phoneNoTxt.setText("");
    }//GEN-LAST:event_resetBtnMouseClicked

    private void bookAddBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookAddBtnMouseClicked
        // TODO add your handling code here:
        manageBooks("add");
    }//GEN-LAST:event_bookAddBtnMouseClicked

    private void bookUpdateBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookUpdateBtnMouseClicked
        // TODO add your handling code here:
        manageBooks("update");
    }//GEN-LAST:event_bookUpdateBtnMouseClicked

    private void bookDeleteBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookDeleteBtnMouseClicked
        // TODO add your handling code here:
        manageBooks("delete");
    }//GEN-LAST:event_bookDeleteBtnMouseClicked

    private void bookResetBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookResetBtnMouseClicked
        // TODO add your handling code here:
        bookIDTxt.setText("");
        titleTxt.setText("");
        authorTxt.setText("");
        yearPublishedTxt.setText("");
    }//GEN-LAST:event_bookResetBtnMouseClicked

    private void bookTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookTblMouseClicked
        // TODO add your handling code here:
        try {
            int selectedRow = bookTbl.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) bookTbl.getModel();

            // Display selected row data in text fields
            bookIDTxt.setText(model.getValueAt(selectedRow, 0).toString());
            titleTxt.setText(model.getValueAt(selectedRow, 1).toString());
            authorTxt.setText(model.getValueAt(selectedRow, 2).toString());
            yearPublishedTxt.setText(model.getValueAt(selectedRow, 3).toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error selecting row: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_bookTblMouseClicked

    private void memberTbl1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_memberTbl1MouseClicked
        // TODO add your handling code here:
        try {
            int selectedRow = memberTbl1.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) memberTbl1.getModel();

            // Display selected row data in text fields
            memberIDTxt.setText(model.getValueAt(selectedRow, 0).toString());
            nameTxt.setText(model.getValueAt(selectedRow, 1).toString());
            addressTxt.setText(model.getValueAt(selectedRow, 2).toString());
            emailTxt.setText(model.getValueAt(selectedRow, 3).toString());
            phoneNoTxt.setText(model.getValueAt(selectedRow, 4).toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error selecting row: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_memberTbl1MouseClicked

    private void updateBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateBtnMouseClicked
        // TODO add your handling code here:
        manageMembers("update");
    }//GEN-LAST:event_updateBtnMouseClicked

    private void deleteBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteBtnMouseClicked
        // TODO add your handling code here:
        manageMembers("delete");
    }//GEN-LAST:event_deleteBtnMouseClicked

    private void searchIDBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchIDBtnMouseClicked
        // TODO add your handling code here:
        searchID();
    }//GEN-LAST:event_searchIDBtnMouseClicked

    private void issueBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_issueBtnMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_issueBtnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(dashboard.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dashboard.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dashboard.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dashboard.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSMaterialButtonRectangle addBtn;
    private javax.swing.JLabel addressLbl;
    private rojerusan.RSMetroTextPlaceHolder addressTxt;
    private javax.swing.JLabel authorLbl;
    private javax.swing.JLabel authorLbl2;
    private javax.swing.JLabel authorLbl3;
    private rojerusan.RSMetroTextPlaceHolder authorTxt;
    private rojerusan.RSLabelImage bgImage;
    private rojerusan.RSLabelImage bgImage1;
    private rojerusan.RSMaterialButtonRectangle bookAddBtn;
    private javax.swing.JLabel bookCountLbl;
    private rojerusan.RSMaterialButtonRectangle bookDeleteBtn;
    private javax.swing.JLabel bookDetLbl;
    private javax.swing.JPanel bookDisPanel;
    private javax.swing.JPanel bookDisPanel1;
    private javax.swing.JLabel bookIDLbl;
    private javax.swing.JLabel bookIDLbl1;
    private javax.swing.JLabel bookIDLbl2;
    private javax.swing.JLabel bookIDLbl3;
    private javax.swing.JLabel bookIDLbl4;
    private rojerusan.RSMetroTextPlaceHolder bookIDTxt;
    private rojerusan.RSMetroTextPlaceHolder bookIDTxt2;
    private rojerusan.RSMetroTextPlaceHolder bookIDTxt3;
    private javax.swing.JMenu bookMenu;
    private javax.swing.JPanel bookPanel;
    private rojerusan.RSMaterialButtonRectangle bookResetBtn;
    private rojeru_san.complementos.RSTableMetro bookTbl;
    private rojeru_san.complementos.RSTableMetro bookTbl1;
    private javax.swing.JLabel bookTitleLbl;
    private javax.swing.JLabel bookTitleLbl2;
    private rojerusan.RSMaterialButtonRectangle bookUpdateBtn;
    private rojerusan.RSMaterialButtonRectangle deleteBtn;
    private com.toedter.calendar.JDateChooser dueDate;
    private rojeru_san.componentes.RSDateChooser dueDate2;
    private javax.swing.JLabel dueDateLbl2;
    private javax.swing.JLabel dueDateLbl3;
    private javax.swing.JLabel emailLbl;
    private javax.swing.JLabel emailLbl1;
    private javax.swing.JLabel emailLbl2;
    private rojerusan.RSMetroTextPlaceHolder emailTxt;
    private javax.swing.JMenu exitMenu;
    private rojerusan.RSMaterialButtonRectangle findBtn;
    private javax.swing.JMenu homeMenu;
    private javax.swing.JPanel homePanel;
    private javax.swing.JMenu iBookMenu;
    private javax.swing.JPanel iBookPanel;
    private javax.swing.JPanel issueBookPanel;
    private rojerusan.RSMaterialButtonRectangle issueBtn;
    private com.toedter.calendar.JDateChooser issueDate;
    private rojeru_san.componentes.RSDateChooser issueDate2;
    private javax.swing.JLabel issueDateLbl;
    private javax.swing.JLabel issueDateLbl1;
    private javax.swing.JLabel issueDateLbl2;
    private javax.swing.JLabel issueDateLbl3;
    private javax.swing.JLabel issueIDLbl2;
    private javax.swing.JLabel issueIdLbl;
    private javax.swing.JLabel issuedBookLbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel memNameLbl;
    private javax.swing.JLabel memNameLbl2;
    private javax.swing.JLabel memberCountLbl;
    private javax.swing.JLabel memberDetLbl;
    private javax.swing.JPanel memberDisPanel;
    private javax.swing.JPanel memberDisPanel1;
    private javax.swing.JPanel memberDisPanel2;
    private javax.swing.JLabel memberIDLbl;
    private javax.swing.JLabel memberIDLbl1;
    private javax.swing.JLabel memberIDLbl2;
    private javax.swing.JLabel memberIDLbl3;
    private rojerusan.RSMetroTextPlaceHolder memberIDTxt;
    private rojerusan.RSMetroTextPlaceHolder memberIDTxt2;
    private rojerusan.RSMetroTextPlaceHolder memberIDTxt3;
    private javax.swing.JMenu memberMenu;
    private javax.swing.JPanel memberPanel;
    private rojeru_san.complementos.RSTableMetro memberTbl;
    private rojeru_san.complementos.RSTableMetro memberTbl1;
    private rojeru_san.complementos.RSTableMetro memberTbl2;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel nameLbl;
    private javax.swing.JLabel nameLbl1;
    private javax.swing.JLabel nameLbl3;
    private rojerusan.RSMetroTextPlaceHolder nameTxt;
    private javax.swing.JLabel noOfBookLbl;
    private javax.swing.JLabel noOfMemberLbl;
    private javax.swing.JLabel phoneNoLbl;
    private rojerusan.RSMetroTextPlaceHolder phoneNoTxt;
    private javax.swing.JMenu rBookMenu;
    private javax.swing.JPanel rBookPanel;
    private rojerusan.RSButtonHover rSButtonHover1;
    private javax.swing.JMenu recordMenu;
    private javax.swing.JPanel recordPanel;
    private rojerusan.RSMaterialButtonRectangle resetBtn;
    private rojerusan.RSMaterialButtonRectangle returnBtn;
    private rojerusan.RSMaterialButtonRectangle searchBtn;
    private rojerusan.RSMaterialButtonRectangle searchIDBtn;
    private javax.swing.JLabel studentIdLbl;
    private javax.swing.JTabbedPane tabPanel;
    private javax.swing.JLabel titleLbl;
    private javax.swing.JLabel titleLbl2;
    private javax.swing.JLabel titleLbl3;
    private rojerusan.RSMetroTextPlaceHolder titleTxt;
    private rojerusan.RSMaterialButtonRectangle updateBtn;
    private javax.swing.JLabel yearPublishedLbl;
    private rojerusan.RSMetroTextPlaceHolder yearPublishedTxt;
    // End of variables declaration//GEN-END:variables
}
