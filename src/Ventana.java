import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class Ventana extends JDialog {
    PreparedStatement ps;
    private JPanel contentPane;
    private JButton buscarButton;
    private JButton borrarButton;
    private JButton crearButton;
    private JButton actualizarButton;
    private JComboBox generoBOX;
    private JComboBox edadBOX;
    private JTextField nombreTXT;
    private JTextField apellidoTXT;
    private JTextField cedulaTXT;
    private JTextField celularTXT;

    public Ventana() {

        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con;
                try {
                    con = getConection();
                    ps = con.prepareStatement("INSERT INTO datos (nombre, apellido, cedula, genero, celular, edad) VALUES(?,?,?,?,?,?) ");
                    ps.setString(1, nombreTXT.getText());
                    ps.setString(2, apellidoTXT.getText());
                    ps.setString(3, cedulaTXT.getText());
                    ps.setString(4, generoBOX.getSelectedItem().toString());
                    ps.setString(5, celularTXT.getText());
                    ps.setString(6, edadBOX.getSelectedItem().toString());
                    System.out.println(ps);
                    int res = ps.executeUpdate();

                    if (res > 0) {
                        JOptionPane.showMessageDialog(null, "Persona Creada");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al Crear persona");
                    }

                    limpiar();
                    con.close();

                } catch (HeadlessException | SQLException f) {
                    System.err.println(f);
                }
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con;

                try {
                    con = getConection();
                    String query = "select * from datos;";
                    Statement s = con.createStatement();
                    ps = con.prepareStatement("SELECT * FROM datos WHERE cedula = ?");
                    ps.setString(1, cedulaTXT.getText());
                    ResultSet rs = s.executeQuery(query);

                    rs = ps.executeQuery();

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Estudiante Encontrado");
                        cedulaTXT.setText(rs.getString("cedula"));
                        nombreTXT.setText(rs.getString("nombre"));
                        apellidoTXT.setText(rs.getString("apellido"));
                        celularTXT.setText(rs.getString("celular"));
                        edadBOX.setSelectedItem(rs.getString("edad"));
                        generoBOX.setSelectedItem(rs.getString("genero"));
                    } else {
                        JOptionPane.showMessageDialog(null, "No existe una persona con ese número de cédula");
                        limpiar();
                    }

                } catch (HeadlessException | SQLException f) {
                    System.err.println(f);
                }
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con;

                try {
                    con = getConection();
                    ps = con.prepareStatement("UPDATE datos SET nombre=?, apellido=?, genero=?, celular=");

                    int res = ps.executeUpdate();

                    if (res > 0) {
                        JOptionPane.showMessageDialog(null, "Persona Modificada");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al Modificar persona");
                    }

                    limpiar();
                    con.close();

                } catch (HeadlessException | SQLException f) {
                    System.err.println(f);
                }
            }
        });

        borrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con;

                limpiar();
            }
        });
    }

    private void limpiar() {
        nombreTXT.setText(null);
        apellidoTXT.setText(null);
        generoBOX.setSelectedIndex(0);
        cedulaTXT.setText(null);
        celularTXT.setText(null);
        edadBOX.setSelectedIndex(0);
    }

    public static Connection getConection(){
        Connection con = null;
        String url = "jdbc:mysql://localhost/Estudiantes",
                user = "root",
                password = "UGPCUGR2002";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e);
        }
        return con;
    }

    public static void main(String[] args) {
        JFrame frame =new JFrame("Néstor Chumania");

        frame.setContentPane(new Ventana().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,1000);
        frame.pack();
        frame.setVisible(true);
    }
}
