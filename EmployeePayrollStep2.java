import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Employee {
    private int id;
    private String name;
    private String designation;
    private double basicSalary;
    private double allowances;
    private double deductions;

    public Employee(int id, String name, String designation, double basicSalary, double allowances, double deductions) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.basicSalary = basicSalary;
        this.allowances = allowances;
        this.deductions = deductions;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDesignation() { return designation; }
    public double getBasicSalary() { return basicSalary; }
    public double getAllowances() { return allowances; }
    public double getDeductions() { return deductions; }
    public double getNetSalary() {
        return basicSalary + allowances - deductions;
    }
}

public class EmployeePayrollStep2 extends JFrame {
    private ArrayList<Employee> employees = new ArrayList<>();

    // Components for Add Employee tab
    private JTextField idField, nameField, designationField, basicSalaryField, allowancesField, deductionsField;
    private JButton addButton;
    private JLabel statusLabel;

    // Components for View Employees tab
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public EmployeePayrollStep2() {
        setTitle("Employee Payroll System - Step 2");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add Employee Panel
        JPanel addPanel = createAddEmployeePanel();
        tabbedPane.addTab("Add Employee", addPanel);

        // View Employees Panel
        JPanel viewPanel = createViewEmployeesPanel();
        tabbedPane.addTab("View Employees", viewPanel);

        add(tabbedPane);
    }

    private JPanel createAddEmployeePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        JLabel idLabel = new JLabel("Employee ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel designationLabel = new JLabel("Designation:");
        JLabel basicSalaryLabel = new JLabel("Basic Salary:");
        JLabel allowancesLabel = new JLabel("Allowances:");
        JLabel deductionsLabel = new JLabel("Deductions:");

        idField = new JTextField(15);
        nameField = new JTextField(15);
        designationField = new JTextField(15);
        basicSalaryField = new JTextField(15);
        allowancesField = new JTextField(15);
        deductionsField = new JTextField(15);

        addButton = new JButton("Add Employee");
        statusLabel = new JLabel(" ");

        // Position components
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST; panel.add(idLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; panel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; panel.add(designationLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; panel.add(designationField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; panel.add(basicSalaryLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; panel.add(basicSalaryField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST; panel.add(allowancesLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; panel.add(allowancesField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.EAST; panel.add(deductionsLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; panel.add(deductionsField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(addButton, gbc);

        gbc.gridy = 7;
        panel.add(statusLabel, gbc);

        addButton.addActionListener(e -> addEmployee());

        return panel;
    }

    private JPanel createViewEmployeesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"ID", "Name", "Designation", "Basic Salary", "Allowances", "Deductions", "Net Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        employeeTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addEmployee() {
        // Check if any field is empty
        if (idField.getText().trim().isEmpty() ||
            nameField.getText().trim().isEmpty() ||
            designationField.getText().trim().isEmpty() ||
            basicSalaryField.getText().trim().isEmpty() ||
            allowancesField.getText().trim().isEmpty() ||
            deductionsField.getText().trim().isEmpty()) {

            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String designation = designationField.getText().trim();
            double basicSalary = Double.parseDouble(basicSalaryField.getText().trim());
            double allowances = Double.parseDouble(allowancesField.getText().trim());
            double deductions = Double.parseDouble(deductionsField.getText().trim());

            // Check for duplicate ID
            for (Employee emp : employees) {
                if (emp.getId() == id) {
                    statusLabel.setForeground(Color.RED);
                    statusLabel.setText("Employee ID already exists!");
                    return;
                }
            }

            Employee emp = new Employee(id, name, designation, basicSalary, allowances, deductions);
            employees.add(emp);
            statusLabel.setForeground(Color.GREEN);
            statusLabel.setText("Employee added successfully!");

            clearFields();
            refreshEmployeeTable();

        } catch (NumberFormatException ex) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Please enter valid numeric values for ID, Salary, Allowances, and Deductions.");
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        designationField.setText("");
        basicSalaryField.setText("");
        allowancesField.setText("");
        deductionsField.setText("");
    }

    private void refreshEmployeeTable() {
        // Clear existing rows
        tableModel.setRowCount(0);

        for(Employee emp : employees) {
            Object[] row = {
                emp.getId(),
                emp.getName(),
                emp.getDesignation(),
                String.format("%.2f", emp.getBasicSalary()),
                String.format("%.2f", emp.getAllowances()),
                String.format("%.2f", emp.getDeductions()),
                String.format("%.2f", emp.getNetSalary())
            };
            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmployeePayrollStep2 frame = new EmployeePayrollStep2();
            frame.setVisible(true);
        });
    }
}
