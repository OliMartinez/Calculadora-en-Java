package calculadora;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 *
 * @author olixe
 */
public class History extends JFrame implements ActionListener {

    private final JPanel hist;
    private ArrayList<JPanel> opers;
    private ArrayList<JPanel> opers_parts;
    private ArrayList<JButton> borrar_opers;
    private final JButton borrar_todo;
    private final JScrollPane scroll;
    FontMetrics metrics;
    private final Font button = new Font("", Font.PLAIN, 18), 
    font_hist = new Font("Calibri", Font.PLAIN, 28);
    private File archivo;
    private String linea;
    private FileReader fr;
    private BufferedReader entrada;
    private String file;
    private FileWriter fichero;
    private PrintWriter pw;
    String texto;

    public History() {
        setVisible(true);
        ImageIcon ImageIcon = new ImageIcon("src/imgs/icono.jpg");
        Image image = ImageIcon.getImage();
        setIconImage(image);
        setTitle("Historial");
        setBounds(1175, 300, 423, 672);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        texto = "";
        //items del historial que contienen una operación cada uno
        opers = new ArrayList<>();
        //partes de cada item, sin contar el botón de borrar
        opers_parts = new ArrayList<>();
        //todos los botones de borrar para los items
        borrar_opers = new ArrayList<>();

        borrar_todo = new JButton("Borrar todo");
        borrar_todo.setBounds(140, 0, 120, 30);
        borrar_todo.addActionListener(this);
        borrar_todo.setBackground(Color.DARK_GRAY);
        borrar_todo.setFont(button);
        borrar_todo.setForeground(Color.WHITE);
        //Acomodar el botón borrar en el layout
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.gridx = 2;
        gbc.insets = new Insets(10, 0, 5, 0);
        add(borrar_todo, gbc);
        //Panel dónde se encuentran los items de las operaciones
        hist = new JPanel();
        hist.setLayout(new GridBagLayout());
        hist.setFont(font_hist);
        hist.setBackground(new Color(15, 15, 15));
        hist.setForeground(Color.WHITE);
        scroll = new JScrollPane(hist);
        scroll.setBounds(27, 60, 350, 400);
        scroll.getVerticalScrollBar().setBackground(new Color(30, 30, 30));
        scroll.getHorizontalScrollBar().setBackground(new Color(30, 30, 30));
        //Acomodar el scroll que contiene el panel
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 4;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(scroll, gbc);

        //Método para leer el archivo de "Historial" y crear los items con las operaciones
        Print_hist();
    }

    public void Print_hist() {
        texto = "";
        hist.removeAll();
        opers = new ArrayList<>();
        opers_parts = new ArrayList<>();
        borrar_opers = new ArrayList<>();
        archivo = new File("src/hist/Historial");
        //Se usa el calendario para poner la fecha de la operación en el item
        Calendar cal = Calendar.getInstance();
        //pos = posición de la linea de la información para cada item
        int pos = 0;
        //contador para cada item
        int itemcount = 0;
        //validador de si se borro el item anterior
        boolean borrarant = false;
        //leer el archivo de "Historial"
        try {
            fr = new FileReader(archivo);
            entrada = new BufferedReader(fr);
            linea = entrada.readLine();
        } catch (IOException ex) {
            Logger.getLogger(History.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (linea != null) {
            //En texto1 se guardan todas las líneas
            texto += linea + "\n";
            //Si estamos en la linea del día de la operación, la comparamos con el día actual
            if (!"".equals(linea) && pos == 4) {
                int rest = cal.get(Calendar.DAY_OF_YEAR) - Integer.parseInt(linea);
                //Si la diferencia es de 14 días, borramos esa operación
                if (rest > 14) {
                    borrarant = true;
                    texto = "";
                }
                pos = 0;
            } else {
                //Si la linea no está vacía y no se encuentra una fecha (pos!=4)
                if (!"".equals(linea)) {
                    pos++;
                    //Si se lee la primera línea no vacía se crea el item para la lista (el panel)
                    if (pos == 1) {
                        JPanel oper = new JPanel();
                        oper.setLayout(new BoxLayout(oper, X_AXIS));
                        oper.setBackground(new Color(30, 30, 30));

                        Border border = new LineBorder(Color.BLACK, 4, true);
                        oper.setBorder(border);

                        JPanel texts = new JPanel();
                        texts.setLayout(new BoxLayout(texts, Y_AXIS));
                        texts.setBackground(new Color(30, 30, 30));
                        opers_parts.add(texts);

                        JTextField op = new JTextField(linea);
                        op.setBackground(new Color(30, 30, 30));
                        op.setForeground(Color.white);
                        op.setEditable(false);

                        opers_parts.get(opers_parts.size() - 1).add(op);

                        oper.add(opers_parts.get(opers_parts.size() - 1));

                        opers.add(oper);
                    } 
                    //Si se leen las otras líneas también se crean y se ponen 
                    //los otros items en el panel
                    else if (pos != 4) {
                        JTextField op = new JTextField(linea);
                        op.setEditable(false);
                        op.setBackground(new Color(30, 30, 30));
                        op.setForeground(Color.white);
                        opers_parts.get(opers_parts.size() - 1).add(op);
                        //Si se lee la penúltima línea, también se agrega el botón de quitar en el item
                        if (pos == 3) {
                            JButton quitar = new JButton("quitar");
                            quitar.setBackground(Color.DARK_GRAY);
                            quitar.setForeground(Color.white);
                            borrar_opers.add(quitar);
                            borrar_opers.get(borrar_opers.size() - 1).addActionListener(this);
                            opers.get(opers.size() - 1).add(borrar_opers.get(borrar_opers.size() - 1));
                        }
                        //el scroll avanzará la altura de las partes de cada item
                        metrics = op.getFontMetrics(op.getFont());
                        int lineHeight = metrics.getHeight();
                        scroll.getVerticalScrollBar().setUnitIncrement(lineHeight);
                    }
                    //Acomodar el item en el panel hist
                    JPanel item = opers.get(opers.size() - 1);
                    GridBagConstraints gcs = new GridBagConstraints();
                    gcs.fill = GridBagConstraints.HORIZONTAL;
                    gcs.gridx = 0;
                    gcs.gridy = itemcount;
                    gcs.weightx = 1;
                    hist.add(item, gcs);
                    itemcount++;
                } else {
                    pos = 0;
                }
            }
            //Leer la siguiente línea
            try {
                linea = entrada.readLine();
            } catch (IOException ex) {
                Logger.getLogger(History.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Poner un panel vacío después de los items para que aparezcan acomodados de arriba a abajo
        GridBagConstraints gcs = new GridBagConstraints();
        JPanel nothing = new JPanel();
        nothing.setBackground(new Color(15, 15, 15));
        gcs.gridy = itemcount;
        gcs.weighty = 1;
        hist.add(nothing, gcs);
        //Si se borraron operaciones porque son de hace más de 14 días, quedará un salto de 
        //línea al principio del texto con fecha(texto1) y del otro(texto), por lo que lo descartamos
        if (borrarant == true) {
            texto = texto.substring(1);
            Sustituir(texto);
        }
        //Actualizar el panel
        hist.revalidate();
        hist.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Al presionar "borrar" se borra el contenido del fichero y lo que se muestra en el textarea
        if (e.getSource() == borrar_todo) {
            Sustituir("");
            hist.removeAll();        
        } 
        //Si se presionan los botones de los items
        else {
            boolean yasequito = false;
            //buscamos el botón de borrar presionado
            for (int i = 0; i < borrar_opers.size(); i++) {
                //una vez que lo encontremos
                if (e.getSource() == borrar_opers.get(i)) {
                    int c = 0;
                    //recorremos el contenido de "Historial" que está en texto1
                    for (int j = 0; j < texto.length(); j++) {
                        //si encontramos dos saltos de línea o apenas comienza el recorrido
                        if (j == 0 || (j + 2 <= texto.length() && "\n\n".equals(texto.substring(j, j + 2)))) {
                            //si se encuentra en la posición correcta del item determinado
                            if (c == i) {
                                //después de los dos saltos, hacemos otro recorrido
                                for (int k = j + 2; k < texto.length(); k++) {
                                    //quitamos la parte del texto que corresponde al item borrado
                                    if (k + 2 <= texto.length() && ("\n\n".equals(texto.substring(k, k + 2))||(k==texto.length()-1))) {
                                        if (j == 0 && k!=texto.length()-1) {
                                            texto = texto.substring(k+2);
                                        } 
                                        else if(k==texto.length()-1){
                                            texto = texto.substring(0, j);
                                        }
                                        else {
                                            texto = texto.substring(0, j) + texto.substring(k);
                                        }
                                        //Se borra el item y sus partes 
                                        Sustituir(texto);
                                        hist.remove(opers.get(i));
                                        opers.get(i).removeAll();
                                        opers.remove(i);
                                        opers_parts.remove(i);
                                        borrar_opers.remove(i);
                                        //Se confirma que ya se quitó el item
                                        yasequito = true;
                                        break;
                                    }
                                }
                            }
                            //Se rompe el ciclo porque ya se encontró el item a borrar
                            if (yasequito) {
                                break;
                            }
                            //se incrementa el contador para la posición de los items en el texto
                            c++;
                        }
                    }
                    if (yasequito) {
                        break;
                    }
                }
            }
        }
        hist.revalidate();
        hist.repaint();
    }

/*        void EliminarItems(){
            for(int i = 0; i < opers_parts.size(); i++){
                JTextField jtf;
                String oper_info = "";
                for(int j = 0; j < 3; j++){
                    jtf = (JTextField)opers_parts.get(i).getComponent(j);
                    oper_info += jtf.getText() +"\n";
                }

                oper_info +="\n\n";
            }
        }*/

//Método para sustituir lo que hay en el fichero
    void Sustituir(String txt) {
        try {
            file = "src/hist/Historial";
            fichero = new FileWriter(file);
            pw = new PrintWriter(fichero);
            pw.print(txt);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }
}
