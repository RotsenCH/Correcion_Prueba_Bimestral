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
    private JComboBox carreraBOX;
    private JTextField nombreTXT;
    private JTextField apellidoTXT;
    private JTextField cedulaTXT;
    private JTextField celularTXT;

    public Ventana() {
        llenarComboBox_Género();
        llenarComboBox_Carrera();
        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connection con;
                try {
                    con = getConection();
                    ps = con.prepareStatement("INSERT INTO datos (nombre, apellido, cedula, genero, celular, carrera) VALUES(?,?,?,?,?,?) ");
                    ps.setString(1, nombreTXT.getText());
                    ps.setString(2, apellidoTXT.getText());
                    ps.setString(3, cedulaTXT.getText());
                    ps.setString(4, generoBOX.getSelectedItem().toString());
                    ps.setString(5, celularTXT.getText());
                    ps.setString(6, carreraBOX.getSelectedItem().toString());
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
                        carreraBOX.setSelectedItem(rs.getString("carrera"));
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
                buscar_Estudiante();
                Connection con;

                try {
                    con = getConection();
                    ps = con.prepareStatement("UPDATE datos SET nombre=?, apellido=?, genero=?, celular=?, carrera=? Where cedula = ?");

                    ps.setString(1, nombreTXT.getText());
                    ps.setString(2, apellidoTXT.getText());
                    ps.setString(6, cedulaTXT.getText());
                    ps.setString(3, generoBOX.getSelectedItem().toString());
                    ps.setString(4, celularTXT.getText());
                    ps.setString(5, carreraBOX.getSelectedItem().toString());
                    System.out.println(ps);

                    int res = ps.executeUpdate();

                    if (res > 0) {
                        JOptionPane.showMessageDialog(null, "Estudiante Modificado");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al Modificar Estudiante, ingrese un estudiante válido");
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
                buscar_Estudiante();
                Connection con;

                try {
                    con = getConection();
                    ps = con.prepareStatement("DELETE FROM datos  Where cedula = ?");
                    ps.setString(1, cedulaTXT.getText());

                    System.out.println(ps);

                    int res = ps.executeUpdate();

                    if (res > 0) {
                        JOptionPane.showMessageDialog(null, "Estudiante Eliminado Satisfactoriamente");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al Eliminar Estudiante, ingrese un estudiante válido");
                    }

                    limpiar();
                    con.close();

                } catch (HeadlessException | SQLException f) {
                    System.err.println(f);
                }
            }
        });
    }

    private void buscar_Estudiante(){
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
            } else {
                JOptionPane.showMessageDialog(null, "No existe una persona con ese número de cédula");
                limpiar();
            }

        } catch (HeadlessException | SQLException f) {
            System.err.println(f);
        }
    }

    private void limpiar() {
        nombreTXT.setText(null);
        apellidoTXT.setText(null);
        generoBOX.setSelectedIndex(0);
        cedulaTXT.setText(null);
        celularTXT.setText(null);
        carreraBOX.setSelectedIndex(0);
    }

    private void llenarComboBox_Género(){
        Connection con;
        try {
            con = getConection();
            String query = "SELECT * FROM Generos_ComboBox";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()){
                generoBOX.addItem(rs.getString(1));
            }
            con.close();

        } catch (HeadlessException | SQLException f) {
            System.err.println(f);
        }
    }

    private void llenarComboBox_Carrera(){
        Connection con;
        try {
            con = getConection();
            String query = "SELECT * FROM Carrera_ComboBox";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()){
                carreraBOX.addItem(rs.getString(1));
            }
            con.close();

        } catch (HeadlessException | SQLException f) {
            System.err.println(f);
        }
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
