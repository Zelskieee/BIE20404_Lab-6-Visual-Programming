import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.border.LineBorder;
import javax.swing.GroupLayout.Alignment;

public class PreparedStatementEmployees extends JFrame {

	// Declare variables
	private JTextField txtEmp_No;
	private JTextArea areaResult;
	private Connection conn;
	private PreparedStatement pstmt;

	// Default Constructor for EmployeeDatabase
	public PreparedStatementEmployees() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Zelggx Arif\\Desktop\\VP - Lab & Lecture\\Lab6VP\\Image\\Employees Database.png")); // Custom icon
		setResizable(true);
		setTitle("Zelskieee & CO. Employee Database 2");
		setSize(515, 340);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		// Create GUI components
		JLabel lblEmp_No = new JLabel("Employee No:");
		lblEmp_No.setFont(new Font("Arial Narrow", Font.BOLD, 14));
		txtEmp_No = new JTextField(10);
		txtEmp_No.setFont(new Font("Arial Narrow", Font.PLAIN, 12));
		JButton btnSave = new JButton("Save");
		btnSave.setBackground(new Color(255, 255, 255));
		btnSave.setFont(new Font("Arial Narrow", Font.BOLD, 12));
		JButton btnSearch = new JButton("Search");
		btnSearch.setBackground(new Color(255, 255, 255));
		btnSearch.setFont(new Font("Arial Narrow", Font.BOLD, 12));
		JButton btnEdit = new JButton("Edit");
		btnEdit.setBackground(new Color(255, 255, 255));
		btnEdit.setFont(new Font("Arial Narrow", Font.BOLD, 12));
		JButton btnRemove = new JButton("Remove");
		btnRemove.setBackground(new Color(255, 255, 255));
		btnRemove.setFont(new Font("Arial Narrow", Font.BOLD, 12));
		JPanel pnlResult = new JPanel();
		pnlResult.setBorder(new LineBorder(new Color(169, 169, 169), 1, true));
		pnlResult.setBounds(57, 77, 389, 189);
		areaResult = new JTextArea(7, 25);
		areaResult.setEditable(false);
		areaResult.setFont(new Font("Arial Narrow", Font.PLAIN, 21));
		JLabel lblResult = new JLabel("Result :");
		lblResult.setBounds(57, 51, 104, 22);
		lblResult.setFont(new Font("Arial Narrow", Font.BOLD, 19));
		JPanel pnlInput = new JPanel();
		pnlInput.setBorder(new LineBorder(new Color(192, 192, 192), 1, true));
		pnlInput.setBounds(10, 10, 479, 35);

		// Create panel and add components
		pnlInput.add(lblEmp_No);
		pnlInput.add(txtEmp_No);
		pnlInput.add(btnSave);
		pnlInput.add(btnSearch);
		pnlInput.add(btnEdit);
		pnlInput.add(btnRemove);
		getContentPane().add(lblResult);
		getContentPane().add(pnlInput);
		getContentPane().add(pnlResult);
		GroupLayout gl_pnlResult = new GroupLayout(pnlResult);
		gl_pnlResult.setHorizontalGroup(
				gl_pnlResult.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlResult.createSequentialGroup()
						.addGap(60)
						.addComponent(areaResult, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				);
		gl_pnlResult.setVerticalGroup(
				gl_pnlResult.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlResult.createSequentialGroup()
						.addGap(80)
						.addComponent(areaResult, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				);
		pnlResult.setLayout(gl_pnlResult);

		// Register button listeners
		btnSave.addActionListener(new SaveButtonListener());
		btnSearch.addActionListener(new SearchButtonListener());
		btnEdit.addActionListener(new EditButtonListener());
		btnRemove.addActionListener(new RemoveButtonListener());

		// Open database connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employeesdb", "root", "");
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
			System.exit(0);
		}
	}

	// Save Button listeners
	private class SaveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				// Prompt the user to enter employee details
				String empNo = JOptionPane.showInputDialog(PreparedStatementEmployees.this, "Enter Employee No :");
				String birthDate = JOptionPane.showInputDialog(PreparedStatementEmployees.this, "Enter Birth Date (YYYY-MM-DD) :");
				String firstName = JOptionPane.showInputDialog(PreparedStatementEmployees.this, "Enter First Name :");
				String lastName = JOptionPane.showInputDialog(PreparedStatementEmployees.this, "Enter Last Name :");
				String gender = JOptionPane.showInputDialog(PreparedStatementEmployees.this, "Enter Gender (M/F):");
				String hireDate = JOptionPane.showInputDialog(PreparedStatementEmployees.this, "Enter Hire Date (YYYY-MM-DD):");

				// Create the SQL query with the provided employee details
				String query = "INSERT INTO employees (emp_no, birth_date, first_name, last_name, gender, hire_date) " +
						"VALUES (?, ?, ?, ?, ?, ?)";

				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, empNo);
				pstmt.setString(2, birthDate);
				pstmt.setString(3, firstName);
				pstmt.setString(4, lastName);
				pstmt.setString(5, gender);
				pstmt.setString(6, hireDate);

				// Execute the query
				pstmt.executeUpdate();

				JOptionPane.showMessageDialog(PreparedStatementEmployees.this, "Employee record saved.");
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(PreparedStatementEmployees.this, "Failed to save employee record.");
			}
		}
	}

	// Search Button listeners
	private class SearchButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				String empNo = txtEmp_No.getText();
				String query = "SELECT * FROM employees WHERE emp_no = ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, empNo);
				ResultSet resultSet = pstmt.executeQuery();

				if (resultSet.next()) {
					String result = "Employee Number: " + resultSet.getString("emp_no") + "\n";
					result += "First Name: " + resultSet.getString("first_name")+ "\n";
					result += "Last Name: " + resultSet.getString("last_name") + "\n";
					result += "Birthday: " + resultSet.getString("birth_date") + "\n";
					String gender = resultSet.getString("gender");
					if (gender.equals("M")) {
						gender = "Male";
					} else {
						gender = "Female";
					}
					result += "Gender: " + gender + "\n";
					result += "Hire Date: " + resultSet.getString("hire_date") + "\n";
					areaResult.setText(result);
				} else {
					areaResult.setText("No employee found with the given employee number.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(PreparedStatementEmployees.this, "Failed to search employee record.");
			}
		}
	}

	// Edit Button listeners
	private class EditButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				String empNo = txtEmp_No.getText();
				String newLastName = JOptionPane.showInputDialog("Enter the new last name:");
				String query = "UPDATE employees SET last_name = ? WHERE emp_no = ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, newLastName);
				pstmt.setString(2, empNo);
				int rowsAffected = pstmt.executeUpdate();

				if (rowsAffected > 0) {
					JOptionPane.showMessageDialog(null, "Employee record updated.");
				} else {
					JOptionPane.showMessageDialog(null, "No employee found with the given employee number.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Failed to update employee record.");
			}
		}
	}

	// Remove Button listeners
	private class RemoveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				String empNo = txtEmp_No.getText();
				String query = "DELETE FROM employees WHERE emp_no = ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, empNo);
				int rowsAffected = pstmt.executeUpdate();

				if (rowsAffected > 0) {
					JOptionPane.showMessageDialog(PreparedStatementEmployees.this, "Employee record removed.");
				} else {
					JOptionPane.showMessageDialog(PreparedStatementEmployees.this, "No employee found with the given employee number.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(PreparedStatementEmployees.this, "Failed to remove employee record.");
			}
		}
	}

	// Main method to run PreparedStatementEmployees
	public static void main(String[] args) {
		new PreparedStatementEmployees();
	}
}
