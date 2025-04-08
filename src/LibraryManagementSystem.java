import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class LibraryManagementSystem extends JFrame {
    // Swing Components
    private JTable tableBooks;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtTitle, txtAuthor, txtPublisher, txtYear, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnSearch, btnRefresh;

    public LibraryManagementSystem() {
        setTitle("Library Management System");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
        loadBooks();
    }

    private void initComponents() {
        // Form Panel for book details
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Book Details"));

        panelForm.add(new JLabel("ID (auto):"));
        txtId = new JTextField();
        txtId.setEditable(false);
        panelForm.add(txtId);

        panelForm.add(new JLabel("Title:"));
        txtTitle = new JTextField();
        panelForm.add(txtTitle);

        panelForm.add(new JLabel("Author:"));
        txtAuthor = new JTextField();
        panelForm.add(txtAuthor);

        panelForm.add(new JLabel("Publisher:"));
        txtPublisher = new JTextField();
        panelForm.add(txtPublisher);

        panelForm.add(new JLabel("Year:"));
        txtYear = new JTextField();
        panelForm.add(txtYear);

        // Buttons panel
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnAdd = new JButton("Add Book");
        btnUpdate = new JButton("Update Book");
        btnDelete = new JButton("Delete Book");
        panelButtons.add(btnAdd);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnDelete);

        // Search Panel
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        txtSearch = new JTextField(20);
        btnSearch = new JButton("Search");
        btnRefresh = new JButton("Refresh");
        panelSearch.add(new JLabel("Search by Title:"));
        panelSearch.add(txtSearch);
        panelSearch.add(btnSearch);
        panelSearch.add(btnRefresh);

        // Table for displaying books
        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Publisher", "Year", "Available"}, 0);
        tableBooks = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableBooks);

        // Add listeners to table row selection
        tableBooks.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableBooks.getSelectedRow();
                if (selectedRow != -1) {
                    txtId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtTitle.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtAuthor.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtPublisher.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    txtYear.setText(tableModel.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        // Button action listeners
        btnAdd.addActionListener(e -> addBook());
        btnUpdate.addActionListener(e -> updateBook());
        btnDelete.addActionListener(e -> deleteBook());
        btnSearch.addActionListener(e -> searchBooks());
        btnRefresh.addActionListener(e -> loadBooks());

        // Layout the components in the frame
        setLayout(new BorderLayout(10, 10));
        add(panelForm, BorderLayout.NORTH);
        add(panelButtons, BorderLayout.CENTER);
        add(panelSearch, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.EAST);

        // Adjusting layout: using a main panel to organize form and table side by side
        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.add(panelForm, BorderLayout.NORTH);
        mainPanel.add(panelButtons, BorderLayout.CENTER);
        mainPanel.add(panelSearch, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(mainPanel, BorderLayout.WEST);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    // Load books from the database into the table
    private void loadBooks() {
        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            tableModel.setRowCount(0); // Clear table

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("title"));
                row.add(rs.getString("author"));
                row.add(rs.getString("publisher"));
                row.add(rs.getInt("year"));
                row.add(rs.getBoolean("available") ? "Yes" : "No");
                tableModel.addRow(row);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + ex.getMessage());
        }
    }

    // Add a new book to the database
    private void addBook() {
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        String publisher = txtPublisher.getText();
        int year = 0;
        try {
            year = Integer.parseInt(txtYear.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year must be a number.");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO books (title, author, publisher, year) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, publisher);
            ps.setInt(4, year);
            ps.executeUpdate();
            ps.close();
            con.close();
            loadBooks();
            JOptionPane.showMessageDialog(this, "Book added successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding book: " + ex.getMessage());
        }
    }

    // Update selected book record
    private void updateBook() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a book to update.");
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        String publisher = txtPublisher.getText();
        int year = 0;
        try {
            year = Integer.parseInt(txtYear.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Year must be a number.");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();
            String sql = "UPDATE books SET title=?, author=?, publisher=?, year=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, publisher);
            ps.setInt(4, year);
            ps.setInt(5, id);
            ps.executeUpdate();
            ps.close();
            con.close();
            loadBooks();
            JOptionPane.showMessageDialog(this, "Book updated successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating book: " + ex.getMessage());
        }
    }

    // Delete selected book record
    private void deleteBook() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            return;
        }
        int id = Integer.parseInt(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Connection con = DBConnection.getConnection();
            String sql = "DELETE FROM books WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            con.close();
            loadBooks();
            JOptionPane.showMessageDialog(this, "Book deleted successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting book: " + ex.getMessage());
        }
    }

    // Search books by title
    private void searchBooks() {
        String searchTerm = txtSearch.getText();
        if (searchTerm.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search term.");
            return;
        }
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT * FROM books WHERE title LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + searchTerm + "%");
            ResultSet rs = ps.executeQuery();
            tableModel.setRowCount(0); // Clear table
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("title"));
                row.add(rs.getString("author"));
                row.add(rs.getString("publisher"));
                row.add(rs.getInt("year"));
                row.add(rs.getBoolean("available") ? "Yes" : "No");
                tableModel.addRow(row);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching books: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        // Ensure the look and feel is set to the system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> {
            LibraryManagementSystem lms = new LibraryManagementSystem();
            lms.setVisible(true);
        });
    }
}