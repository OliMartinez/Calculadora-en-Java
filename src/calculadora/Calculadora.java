/*Creada por Oliver de Jesús Martínez Pérez
León Guanajuato México
2022
 */
package calculadora;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import static java.awt.Font.ITALIC;
import java.io.*;
import java.util.Calendar;
import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class

public class Calculadora extends JFrame implements ActionListener {

    private final JTextField display, display1;

    Font fuente = new Font("MS-Console", Font.PLAIN, 40),
            fuente1 = new Font("DS-Digital", Font.BOLD | ITALIC, 50),
            fuente2 = new Font("Calibri", Font.PLAIN, 18);

    private final JButton hist;

    private JButton porc, cos, sen, tan, fact, log, ln, euler, raiz, x2, mod, CE, C,
            borrar, div, por, menos, mas, igual, uno, dos, tres, cuatro, cinco,
            seis, siete, ocho, nueve, cero, dot, exp, par1, par2, pot, pi;

    private final JButton[] keys = {CE, C, borrar, porc, raiz, x2, pot, mod, cos,
        sen, tan, log, ln, fact, div, par1, siete, ocho, nueve, por, par2, cuatro,
        cinco, seis, mas, euler, uno, dos, tres, menos, pi, dot, cero, exp, igual};

    String[] keys_names = {"Ans", "C", "", "%", "√", "x²", "x^y", "mod", "cos",
        "sin", "tan", "Log", "ln", "!", "/", "(", "7", "8", "9", "*", ")", "4",
        "5", "6", "+", "e", "1", "2", "3", "-", "π", ".", "0", "E", "="};

    char[] operandos = {'c', 's', 't', 't', 'L', 'l', 'E', 'E', '√', '√', 'm',
        'm', '^', '^', 'x', '*', '/', '÷', '+', '-'};

    int c, cont, bajar, largo, altura, pos;

    StringBuilder operation = new StringBuilder(), res = new StringBuilder();

    char simbolo, simbolo0, simbolo1;

    String completo, number1 = "", number2 = "", number3 = "", number4 = "",
            res_ant = "", initial_oper = "", mouse, simb;

    boolean selected;

    History hi;

    KeyStroke teclah;

    JPanel content;

    File file = null;
    FileWriter fichero = null;
    PrintWriter pw = null;

    public Calculadora() {
        file = new File("src/hist/Historial");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        //Detectar si se cambia el tamaño del JFrame
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                Resize_result();
            }
        });

        /*addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                hi.Sustituir(hi.texto);
            }
        });*/

        hist = new JButton("Historial");
        hist.setFont(fuente2);
        hist.setBackground(Color.DARK_GRAY);
        hist.setForeground(Color.WHITE);
        hist.addActionListener(this);
        //Acomodar el componente en el layout
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.insets = new Insets(5, 5, 0, 0);
        add(hist, gbc);

        display = new JTextField();
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setForeground(Color.white);
        display.setFont(fuente);
        display.setBorder(BorderFactory.createEmptyBorder());
        display.setBackground(new Color(30, 30, 30));
        display.setCaretColor(Color.white);
        display.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mouse = "si";
                res.delete(0, res.length());
            }
        });
        display.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    resultado();
                    mouse = "no";
                    if (hi != null) {
                        hi.Print_hist();
                    }
                }
            }
        });
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.insets = new Insets(5, 5, 0, 5);
        add(display, gbc);

        display1 = new JTextField();
        display1.setHorizontalAlignment(JTextField.RIGHT);
        display1.setForeground(Color.white);
        display1.setFont(fuente1);
        display1.setBorder(BorderFactory.createEmptyBorder());
        display1.setBackground(new Color(30, 30, 30));
        display1.setEditable(false);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 5, 5, 5);
        add(display1, gbc);

        //Acomodar las teclas en el layout con sus respectivas propiedades
        for (int i = 0; i < keys.length; i++) {
            cont++;
            if (cont == 1 || (cont - 1) % 5 == 0) {
                c = 0;
            } else {
                c = c + 1;
            }

            if (cont < 5) {
                bajar = 3;
            } else if ((cont - 1) % 5 == 0) {
                bajar = bajar + 1;
            }

            if (i == 2) {
                Image img = new ImageIcon("src/imgs/borrar.png").getImage();
                ImageIcon ico_borrar = new ImageIcon(img.getScaledInstance(
                        21, 20, Image.SCALE_SMOOTH));
                keys[i] = new JButton(ico_borrar);
            } else {
                keys[i] = new JButton(keys_names[i]);
            }

            gbc.gridx = c;
            gbc.gridy = bajar;
            gbc.gridwidth = 1;
            gbc.insets = new Insets(0, 0, 0, 0);
            keys[i].setFont(fuente2);
            keys[i].setForeground(Color.WHITE);
            add(keys[i], gbc);
            keys[i].addActionListener(this);

            if (/*isNum(keys_names[i])*/bajar > 5 && (c != 0 && c != 4)) {
                keys[i].setBackground(new Color(7, 7, 7));
            } else {
                keys[i].setBackground(new Color(20, 20, 20));
            }
            keys[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        resultado();
                        mouse = "no";
                        if (hi != null) {
                            hi.Print_hist();
                        }
                    }
                }
            });
        }
        Action teclaHListener = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (hi == null) {
                    hi = new History();
                } else {
                    if (!hi.isVisible()) {
                        hi.setVisible(true);
                    }
                    hi.toFront();
                }
            }
        };
        teclah = KeyStroke.getKeyStroke("H");
        content = (JPanel) getContentPane();
        InputMap inputMap = content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(teclah, "OPEN");
        content.getActionMap().put("OPEN", teclaHListener);
    }

//Método para cambiar el tamaño de la letra del resultado si se cambia el tamaño del JFrame
    private void Resize_result() {
        int size = 50;
        fuente1 = new Font("DS-Digital", Font.BOLD | ITALIC, size);
        display1.setFont(fuente1);
        int reswidth = display1.getFontMetrics(fuente1).stringWidth(res.toString());
        while (reswidth > display1.getWidth()) {
            size--;
            fuente1 = new Font("DS-Digital", Font.BOLD | ITALIC, size);
            display1.setFont(fuente1);
            reswidth = display1.getFontMetrics(fuente1).stringWidth(res.toString());
        }
    }

    //Si se da click sobre el display el string operation guarda el valor escrito
    //en pantalla hasta donde se dio click y en number2 desde dónde se dio el click
    //(sólo en la parte de la operación)
    public void Disp_clicked() {
        pos = display.getCaretPosition();
        number2 = operation.substring(pos, operation.length());
        Replace_String(operation, operation.substring(0, pos), "", "");
    }

    //Si se da click sobre algún componente
    @Override
    public void actionPerformed(ActionEvent e) {
        //Si se presiona el botón de Historial, 
        //se muestra la ventana de todas las operaciones realizadas
        selected = false;
        if (e.getSource() == hist) {
            if (hi == null) {
                hi = new History();
            } else {
                if (!hi.isVisible()) {
                    hi.setVisible(true);
                }
                hi.toFront();
            }
        } //Si se presionan los otros botones
        else {
            //En operation se guarda lo que hay en el primer display
            Replace_String(operation, display.getText(), "", "");
            for (int i = 0; i < keys.length; i++) {
                if (e.getSource() == keys[i]) {
                    if (i != keys.length - 1) {
                        //Si se da click en el display y el botón presionado antes no era el de "="
                        if ("si".equals(mouse)) {
                            Disp_clicked();
                        }
                        //Si hay texto seleccionado en el display
                        String select = "";
                        if (display.getSelectedText() != null) {
                            selected = true;
                            String text = display.getText();
                            int t_len = text.length();
                            int p = display.getCaretPosition();
                            select = display.getSelectedText();
                            int sel_len = select.length();
                            //Borramos la parte del texto seleccionado que se encuentra en operation
                            //y number2 tendrá posición inicial dónde termina el texto seleccionado
                            for (int j = 0; j <= t_len; j++) {
                                if (j == p) {
                                    if (p + sel_len <= t_len && text.substring(
                                            p, p + sel_len).equals(select)) {
                                        display.setText(text.substring(0, p)
                                                + text.substring(p + sel_len, t_len));
                                        number2 = text.substring(p + sel_len, t_len);
                                        Replace_String(operation, text.substring(0, p),
                                                "", "");
                                    } else {
                                        display.setText(text.substring(0, p - sel_len)
                                                + text.substring(p, t_len));
                                        number2 = text.substring(p, t_len);
                                        Replace_String(operation, text.substring(
                                                0, p - sel_len), "", "");
                                    }
                                }
                            }
                        }
                        //teclas para poner simbolos en el display (operadores y operandos)
                        if (12 < i && i < keys.length - 1) {
                            simb = keys_names[i];
                            PonerSimb();
                        } else if (i == 0) {
                            if (!"".equals(res_ant) && !"Syntax Error".equals(res_ant)
                                    && !"Math Error".equals(res_ant)) {
                                if ("".equals(operation.toString())) {
                                    operation.append(res_ant);
                                } else if (!"".equals(res.toString())) {
                                    Replace_String(operation, res_ant, "", "");
                                    res.delete(0, res.length());
                                } else {
                                    if (ePiONum(operation.substring(
                                            operation.length() - 1, operation.length()))) {
                                        operation.append("x").append(res_ant);
                                    } else {
                                        operation.append(res_ant);
                                    }
                                    if (!"".equals(number2)
                                            && ePiONum(number2.substring(0, 1))) {
                                        operation.append("x");
                                    }
                                }
                            }
                        } //Si presiona "C" se borra la operación y resultado
                        else if (i == 1) {
                            operation.delete(0, operation.length());
                            res.delete(0, res.length());
                            display1.setText(res.toString());
                            number2 = "";
                        } //Al presionar el botón de retroceder se borra el último simbolo o dónde este el cursor
                        else if (i == 2 && !"".equals(display.getText()) && select.isEmpty()) {
                            int posult = operation.length() - 1;
                            operation.deleteCharAt(posult);
                            if (operation.length() > 0) {
                                boolean borrado = false;
                                for (int j = posult - 1; j >= 0; j--) {
                                    for (int k = 4; k < keys_names.length; k++) {
                                        if ((k == 4 || (6 < k && k < 13)) && operation.substring(j, posult).equals(keys_names[k])) {
                                            operation.delete(j, posult);
                                            borrado = true;
                                            break;
                                        }
                                    }
                                    if (borrado) {
                                        break;
                                    }
                                }
                            }
                        } //Operandos que no son iguales a la muestra en las teclas
                        else if (i == 3) {
                            percent();
                        } else if (i == 4) {
                            simb = "√(";
                            PonerSimb();
                        } else if (i == 5) {
                            simb = "²";
                            PonerSimb();
                        } else if (i == 6) {
                            simb = "^";
                            PonerSimb();
                        } else if (i == 7) {
                            simb = "mod(";
                            PonerSimb();
                        } else if (i == 8) {
                            simb = "cos(";
                            PonerSimb();
                        } else if (i == 9) {
                            simb = "sin(";
                            PonerSimb();
                        } else if (i == 10) {
                            simb = "tan(";
                            PonerSimb();
                        } else if (i == 11) {
                            simb = "Log(";
                            PonerSimb();
                        } else if (i == 12) {
                            simb = "ln(";
                            PonerSimb();
                        }
                        display.setText(operation.toString() + number2);
                        display.setCaretPosition(operation.length());

                        display.requestFocusInWindow();
                    } //De otra forma se muestra el resultado
                    //y cómo el cursor parpadeante se quita, "mouse" valdrá no
                    else {
                        resultado();
                        mouse = "no";
                        if (hi != null) {
                            hi.Print_hist();
                        }
                    }
                }
            }
        }

    }

    //Método para poner el símbolo de la tecla presionada
    public void PonerSimb() {
        if ("".equals(res.toString())) {
            if (!"²".equals(simb) && !"!".equals(simb)) {
                if ("".equals(operation.toString()) && (!nonum(simb)
                        || "√(".equals(simb) || "cos(".equals(simb) || "sin(".equals(simb)
                        || "tan(".equals(simb) || "ln(".equals(simb) || "Log(".equals(simb)
                        || "(".equals(simb) || "-".equals(simb)) || !"".equals(operation.toString())
                        && !nonum_ant(simb, operation.substring(operation.length() - 1))) {
                    operation.append(simb);
                }
            } else if ("²".equals(simb)) {
                sinum(operation, '²');
            } else if ("!".equals(simb)) {
                sinum(operation, '!');
            }
        } else {
            res.delete(0, res.length());
            if (!selected && nonum(simb) || "E".equals(simb)) {
                Replace_String(operation, res_ant, simb, "");
            } else if (!selected) {
                Replace_String(operation, simb, "", "");
            } else {
                operation.append(simb);
            }
        }
    }

    //Método para sacar porcentaje(se saca mientras se escribe por ejemplo si 
    //escribió "5+8" y luego presionaa "%" se sacará el 8% de 5 y en la pantalla
    //de escritura aparecerá "5+(aqui el 8% de 5)"
    public void percent() {
        try {
            initial_oper = operation.toString();
            negativo(operation);
            String numeros1 = "", numeros2 = "";
            int contar = 0, contar1 = 0;
            res.delete(0, res.length());
            for (int j = operation.length() - 1; j > -1; j--) {
                if (operation.charAt(j) == ')'
                        && operation.charAt(operation.length() - 1) == ')') {
                    contar++;
                } else if (operation.charAt(j) == '('
                        && operation.charAt(operation.length() - 1) == ')') {
                    contar1++;
                } else if (operation.charAt(operation.length() - 1) != ')'
                        && nonum(operation.substring(j, j + 1))) {
                    if (!"(".equals(operation.substring(j, j + 1))) {
                        numeros1 = operation.substring(j + 1);
                        numeros2 = operation.substring(0, j);
                    } else {
                        int k;
                        for (k = j - 1; k >= 0; k--) {
                            if (!nonum(operation.substring(k, k + 1))) {
                                break;
                            }
                        }
                        numeros1 = operation.substring(j + 1);
                        numeros2 = operation.substring(0, k + 1);
                    }
                    operation.delete(j + 1, operation.length());
                }
                /*corregir aqui los errores de raiz y mod
                      y otro if para el porcentaje dentro del parentesis*/
                if (contar == contar1 && contar != 0) {
                    numeros1 = operation.substring(j);
                    numeros2 = operation.substring(0, j - 1);
                    operation.delete(j, operation.length());
                    res.append(numeros1.substring(1, numeros1.length() - 1));
                    pi_cu_e_fa();
                    proceso_resp();
                    numeros1 = res.toString();
                }
                if (numeros1.length() != 0 && numeros2.length() != 0) {
                    for (int h = 0; h < numeros2.length(); h++) {
                        ContNoNum(numeros2.substring(h, h + 1), contar);
                    }
                    if (contar == 0) {
                        res.append(numeros1);
                        pi_cu_e_fa();
                        proceso_resp();
                        numeros1 = res.toString();
                        res.delete(0, res.length());
                        res.append(numeros2.replace('n', '-'));
                        parentesis();
                        pi_cu_e_fa();
                        proceso_resp();
                        if (SyntaxYMath(operation.toString())) {
                            Replace_String(operation, numeros2, "", "");
                            break;
                        }
                        numeros2 = res.toString();
                        //Resultado se escribe en la pantalla de escritura(en operation)
                        Double total = Double.parseDouble(numeros2)
                                * (Double.parseDouble(numeros1.replace('n', '-')) / 100);
                        operation.append(Double.toString(total));
                        break;
                    } else {
                        res.delete(0, res.length());
                        if (numeros2.charAt(numeros2.length() - 1) == ')') {
                            numeros2 = numeros2.substring(0, numeros2.length() - 1);
                        }
                        res.append(numeros2);
                        parentesis();
                        pi_cu_e_fa();
                        proceso_resp();
                        if (SyntaxYMath(operation.toString())) {
                            Replace_String(operation, numeros2, "", "");
                            break;
                        }
                        numeros2 = res.toString();
                        //Resultado se escribe en la pantalla de escritura(en operation)
                        Double total = Double.parseDouble(numeros2.replace('n', '-'))
                                * (Double.parseDouble(numeros1.replace('n', '-')) / 100);
                        operation.append(Double.toString(total));
                        break;
                    }
                }
            }
            res.delete(0, res.length());
        } catch (Exception e) {
            operation.replace(0, operation.length(), initial_oper);
            res.replace(0, res.length(), "");
        }
    }

    //Método que analiza la operación para escribir el resultado en la segunda pantalla
    public void resultado() {
        initial_oper = operation.toString();
        Replace_String(res, initial_oper, "", "");
        cont = 0;
        c = 0;
//Se realizan primero las operaciones dentro de los paréntesis, luego Pi y al cuadrado,
//después multiplicaciones y divisiones y al final sumas y restas
        parentesis();
        pi_cu_e_fa();
        proceso_resp();
//Se localiza si hay errores para escirbirlos en la segunda pantalla
        if (!"Math Error".equals(res.toString())) {
            if (!"".equals(operation.toString())) {
                try {
                    Replace_String(res, String.valueOf(Double.parseDouble(res.toString())),
                            "", "");
                    if ("Infinity".equals(res.toString())) {
                        Replace_String(res, "Math Error", "", "");
                    }
                } catch (NumberFormatException e) {
                    Replace_String(res, "Syntax Error", "", "");
                }
            }
        }
//Se escribe la respuesta y en res_ant(ANS) se guarda ésta
        Resize_result();
        display1.setText(res.toString());
        res_ant = res.toString();
        number2 = "";

//Se escribe la operación y día del año detro del fichero "History" para que pueda ser vista
//en el historial
        Calendar cal = Calendar.getInstance();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM  HH:mm");
        String fecha = LocalDateTime.now().format(myFormatObj);
        int yearday = cal.get(Calendar.DAY_OF_YEAR);
        int dia = cal.get(Calendar.DAY_OF_WEEK);
        String[] Dias = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        for (int i = 0; i < Dias.length; i++) {
            if (i + 1 == dia) {
                fecha = Dias[i] + " " + fecha;
                break;
            }
        }

        try {
            fichero = new FileWriter(file, true);
            pw = new PrintWriter(fichero);
            pw.print(display.getText() + "\n" + res + "\n" + fecha + "\n" + yearday + "\n\n");
        } catch (IOException e) {
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (IOException e2) {
            }
        }

    }

//En este método se realizan primero las operaciones dentro de los paréntesis    
    private void parentesis() {
        String numero1, numero2, numero3;
        int numizqpar = 0, numderpar = 0;
        for (int i = 0; i < res.length(); i++) {
            if (res.charAt(i) == '(') {
                numizqpar++;
                if (i != 0 && (ePiONum(res.substring(i - 1, i)))) {
                    Replace_String(res, res.substring(0, i) + "x",
                            res.substring(i), "");
                    i = i + 1;
                }
                for (int j = i + 1; j < res.length(); j++) {
                    if (res.charAt(j) == '(') {
                        numizqpar--;
                        i = j - 1;
                        break;
                    } else if (res.charAt(j) == ')') {
                        numderpar++;
                        if (j != res.length() - 1
                                && (ePiONum(res.substring(j + 1, j + 2)))) {
                            Replace_String(res, res.substring(0, j + 1) + "x",
                                    res.substring(j + 1), "");
                        }
                        numero2 = res.substring(0, i);
                        numero3 = res.substring(j + 1);
                        Replace_String(res, res.substring(i + 1, j), "", "");
                        pi_cu_e_fa();
                        proceso_resp();
                        numero1 = res.toString();
                        if (SyntaxYMath(numero1)) {
                            break;
                        }
                        Replace_String(res, numero2, numero1, numero3);
                        i = CharSig('(', '(', i, -1);
                        break;
                    } else if (j == res.length() - 1 && res.charAt(j) != ')') {
                        numero1 = res.substring(0, i);
                        numero2 = res.substring(i + 1);
                        Replace_String(res, numero2, "", "");
                        pi_cu_e_fa();
                        proceso_resp();
                        if (SyntaxYMath(numero2)) {
                            res.append(numero2);
                            break;
                        }
                        numero2 = res.toString();
                        Replace_String(res, numero1, numero2, "");
                        i = res.length() - 1;
                        i = CharSig('(', '(', i, -1);
                        break;
                    }
                }
            } else if (res.charAt(i) == ')') {
                Replace_String(res, "Syntax Error", "", "");
            }
            if (SyntaxYMath(res.toString())) {
                break;
            }
        }
        int pars = numizqpar - numderpar;
        for (int i = 0; i < pars; i++) {
            operation.append(')');
        }
        display.setText(operation.toString());
    }

//Se realizan las operciones que tengan el símbolo de Pi o el de la elevación al cuadrado 
    private void pi_cu_e_fa() {
        if (!SyntaxYMath(res.toString())) {
            for (int i = 0; i < res.length(); i++) {
                if (res.charAt(i) == 'π' || res.charAt(i) == 'e') {
                    double constant;
                    if (res.charAt(i) == 'π') {
                        constant = Math.PI;
                    } else {
                        constant = Math.E;
                    }
                    number2 = res.substring(i + 1);
                    if ("Syntax Error".equals(SE_pi_e_cu_fa(number2, res).toString())) {
                        break;
                    }
                    if (i == 0) {
                        Replace_String(res, String.valueOf(constant), number2, "");
                    } else {
                        if (!isNum(res.substring(i - 1, i))) {
                            Replace_String(res, res.substring(0, i),
                                    String.valueOf(constant), number2);
                        } else if (isNum(res.substring(i - 1, i))) {
                            Replace_String(res, res.substring(0, i) + "x",
                                    String.valueOf(constant), number2);
                        }
                    }
                }
            }
            Unary_Oper('²');
            Unary_Oper('!');
        }
    }

//Aquí se realiza el proceso para realizar toda la operación y obtener el resultado   
    private void proceso_resp() {
        if (!SyntaxYMath(res.toString())) {
            for (int i = 0; i < operandos.length; i++) {
                if (i == 0 || i % 2 == 0) {
                    simbolo0 = operandos[i];
                } else {
                    simbolo1 = operandos[i];
                    negativo(res);
                    Oper_2_operandos();
                }

                if (SyntaxYMath(res.toString())) {
                    break;
                }
            }
            if (!SyntaxYMath(res.toString())) {
                for (int i = 0; i < res.length(); i++) {
                    if (res.charAt(i) == 'n') {
                        res = res.replace(i, i + 1, "-");
                    }
                }
            }
        }
    }

//  Se realiza la operación de los dos operandos que rodean al operador correspondiente 
    private void Oper_2_operandos() {
        for (int k = 0; k < res.length(); k++) {
            if (res.charAt(k) == simbolo0 || res.charAt(k) == simbolo1) {
                simbolo = res.charAt(k);
                // Si está estos primeros simbolos significa que el operador
                // abarca 4 simbolos, por ejemplo, 'c' es de 'cos('
                if (simbolo == 'm' || simbolo == 'c' || simbolo == 's'
                        || simbolo == 't' || simbolo == 'L') {
                    number1 = res.substring(0, k);
                    number2 = res.substring(k + 3);
                } else if (simbolo == 'l') {
                    number1 = res.substring(0, k);
                    number2 = res.substring(k + 2);
                } else {
                    number1 = res.substring(0, k);
                    number2 = res.substring(k + 1);
                }
                for (int j = number1.length() - 1; j > -1; j--) {
                    cont = ContNoNum(number1.substring(j, j + 1), cont);
                }
                if (cont != 0) {
                    for (int j = number1.length() - 1; j > -1; j--) {
                        if (nonum(number1.substring(j, j + 1))) {
                            number3 = number1.substring(j + 1);
                            number1 = number1.substring(0, j + 1);
                            break;
                        }
                    }
                } else {
                    number3 = number1;
                    number1 = "";
                }
                for (int j = 0; j < number2.length(); j++) {
                    c = ContNoNum(number2.substring(j, j + 1), c);
                }
                if (c != 0) {
                    for (int j = 0; j < number2.length(); j++) {
                        if (nonum(number2.substring(j, j + 1))) {
                            number4 = number2.substring(0, j);
                            number2 = number2.substring(j);
                            break;
                        }
                    }
                } else {
                    number4 = number2;
                    number2 = "";
                }
                res.delete(0, res.length());
                number3 = E(number3.replace('n', '-'));
                number4 = E(number4.replace('n', '-'));

                if (SyntaxError(number3, number4)) {
                    res.append("Syntax Error");
                    break;
                }

                res.append(number1).append(elegir_oper(number3, number4)).append(number2);
                if ("Math Error".equals(SyntaxOMath("m", res).toString())) {
                    break;
                }
                k = CharSig(simbolo0, simbolo1, k, 0);
                if (simbolo == 'E') {
                    break;
                }
            }
            cont = 0;
            c = 0;
        }
    }

//Realizar la operación con "simbolo" entre los dos números "número3" y "numero4"   
    private String elegir_oper(String numero3, String numero4) {
        String total = "";

        if (simbolo == '√') {
            if ("".equals(numero3) || "-".equals(numero3)) {
                total = numero3 + String.valueOf(Math.sqrt(Double.parseDouble(numero4)));
            } else {
                total = String.valueOf(Double.parseDouble(numero3) * Math.sqrt(
                        Double.parseDouble(numero4)));
            }
        } else if (simbolo == 'E') {
            if ("".equals(numero3) || "-".equals(numero3)) {
                total = numero3 + String.valueOf(Math.pow(10, Double.parseDouble(numero4)));
            } else {
                total = String.valueOf(Double.parseDouble(numero3)
                        * Math.pow(10, Double.parseDouble(numero4)));
            }
        } else if (simbolo == 'c') {
            if ("".equals(numero3) || "-".equals(numero3)) {
                total = numero3 + String.valueOf(Math.cos(Math.toRadians(Double.parseDouble(numero4))));
            } else {
                total = String.valueOf(Double.parseDouble(numero3) * Math.cos(Math.toRadians(
                        (Double.parseDouble(numero4)))));
            }
        } else if (simbolo == 's') {
            if ("".equals(numero3) || "-".equals(numero3)) {
                total = numero3 + String.valueOf(Math.sin(Math.toRadians(Double.parseDouble(numero4))));
            } else {
                total = String.valueOf(Double.parseDouble(numero3) * Math.sin(Math.toRadians(
                        Double.parseDouble(numero4))));
            }
        } else if (simbolo == 't') {
            if ("".equals(numero3) || "-".equals(numero3)) {
                total = numero3 + String.valueOf(Math.tan(Math.toRadians(Double.parseDouble(numero4))));
            } else {
                total = String.valueOf(Double.parseDouble(numero3) * Math.tan(Math.toRadians(
                        Double.parseDouble(numero4))));
            }
        } else if (simbolo == 'L') {
            if ("".equals(numero3) || "-".equals(numero3)) {
                total = numero3 + String.valueOf(Math.log10(Double.parseDouble(numero4)));
            } else {
                total = String.valueOf(Double.parseDouble(numero3) * Math.log(
                        Double.parseDouble(numero4)));
            }
        } else if (simbolo == 'l') {
            if ("".equals(numero3) || "-".equals(numero3)) {
                total = numero3 + String.valueOf(Math.log(Double.parseDouble(numero4)));
            } else {
                total = String.valueOf(Double.parseDouble(numero3) * Math.log(
                        Double.parseDouble(numero4)));
            }
        } else if (simbolo == 'm') {
            total = String.valueOf(Double.parseDouble(numero3) % Double.parseDouble(numero4));
        } else if (simbolo == '^') {
            total = String.valueOf(Math.pow(Double.parseDouble(numero3),
                    Double.parseDouble(numero4)));
        } else if (simbolo == 'x' || simbolo == '*') {
            total = String.valueOf(Double.parseDouble(numero3) * Double.parseDouble(numero4));
        } else if (simbolo == '/' || simbolo == '÷') {
            total = String.valueOf(Double.parseDouble(numero3) / Double.parseDouble(numero4));
        } else if (simbolo == '+') {
            total = String.valueOf(Double.parseDouble(numero3) + Double.parseDouble(numero4));
        } else if (simbolo == '-') {
            total = String.valueOf(Double.parseDouble(numero3) - Double.parseDouble(numero4));
        }
        return total;
    }

//Comprobar si el string es número    
    public static boolean isNum(String cad) {
        boolean res;

        try {
            Double.parseDouble(cad);
            res = true;
        } catch (NumberFormatException exp) {
            res = false;
        }

        return res;
    }

//Método para calcular los factoriales de un número, por ejemplo, de 5!! 
//se calcularán dos factoriales 
    private static double factorial(double n, int facts) {
        double fact_res = 0;
        for (int i = 0; i < facts; i++) {
            fact_res = fact(n);
        }
        return fact_res;
    }

//Método para calcular el factorial de un número 
    private static double fact(double n) {
        if (n == 0) {
            return 1;
        } else {
            return (n * fact(n - 1));
        }
    }

//Función  
    private static StringBuilder sinum(StringBuilder caden, char simbol) {
        StringBuilder numeros1 = new StringBuilder();
        StringBuilder numeros2 = new StringBuilder();

        if (caden.length() != 0) {
            if (ePiONum(caden.substring(caden.length() - 1, caden.length()))
                    || ")".equals(caden.substring(caden.length() - 1, caden.length()))
                    || simbol == caden.charAt(caden.length() - 1)) {
                if (caden.charAt(caden.length() - 1) == simbol) {
                    caden.delete(caden.length() - 1, caden.length());
                    for (int j = caden.length() - 1; j > -1; j--) {
                        if (!nonum(caden.substring(j, j + 1))) {
                            numeros1.append(caden.charAt(j));
                            caden.delete(j, j + 1);
                        } else if (caden.charAt(j) == ')' && j == caden.length() - 1) {
                            for (int k = caden.length() - 1; k > -1; k--) {
                                if (caden.charAt(k) == '(') {
                                    numeros1.append(caden.substring(k));
                                    caden.delete(k, caden.length());
                                    break;
                                }
                            }
                            break;
                        } else {
                            break;
                        }
                    }
                    if (numeros1.charAt(numeros1.length() - 1) != ')') {
                        for (int k = numeros1.length() - 1; k > -1; k--) {
                            numeros2.append(numeros1.charAt(k));
                        }
                    } else {
                        numeros2.append(numeros1);
                    }
                    caden.append("(").append(numeros2).append(simbol).append(")").
                            append(simbol);
                } else {
                    caden.append(simbol);
                }
            }
        }
        return caden;
    }

//Método para calcular operaciones con operadores unarios (! y ²)
    private void Unary_Oper(char oper) {
        int times = 0;
        String exps = "";
        int g = 0, h;
        for (int i = 0; i < res.length(); i++) {
            negativo(res);
            if (res.charAt(i) == oper) {
                if (res.charAt(i - 1) != oper) {
                    g = i;
                }
                if (oper == '!') {
                    times++;
                } else {
                    exps += "2";
                }
            }
            if (g != 0 && ((res.charAt(i) == oper && i == res.length() - 1)
                    || (res.charAt(i) == oper && res.charAt(i + 1) != oper))) {
                h = i + 1;
                number1 = res.substring(0, g);
                number2 = res.substring(h).replace('n', '-');

                if (!"Syntax Error".equals(SE_pi_e_cu_fa(number2, res).toString())) {
                    cont = 0;
                    for (int j = number1.length() - 1; j > -1; j--) {
                        cont = ContNoNum(number1.substring(j, j + 1), cont);
                    }
                    if (cont != 0) {
                        for (int j = number1.length() - 1; j > -1; j--) {
                            if (nonum(number1.substring(j, j + 1))) {
                                number3 = number1.substring(j + 1).replace('n', '-');
                                number1 = number1.substring(0, j + 1);

                                if ("Syntax Error".equals(SyntaxOMath(number3,
                                        res).toString())) {
                                    break;
                                }
                                String resp = "";
                                if (oper == '!') {
                                    try {
                                        resp = String.valueOf(factorial(
                                                Double.parseDouble(number3), times));
                                    } catch (StackOverflowError er) {
                                        resp = "Infinity";
                                    }
                                } else {
                                    resp = String.valueOf(
                                            Math.pow(Double.parseDouble(number3),
                                                    Integer.parseInt(exps)));
                                }
                                Replace_String(res, number1, resp,
                                        number2);
                                break;
                            }
                        }
                    } else {
                        number1 = number1.replace('n', '-');
                        if (!"Syntax Error".equals(SyntaxOMath(number1, res).
                                toString())) {
                            String resp = "";
                            if (oper == '!') {
                                try {
                                    resp = String.valueOf(factorial(
                                            Double.parseDouble(number1), times));
                                } catch (StackOverflowError er) {
                                    resp = "Infinity";
                                }
                            } else {
                                resp = String.valueOf(Math.pow(
                                        Double.parseDouble(number1), Integer.parseInt(exps)));
                            }
                            Replace_String(res, String.valueOf(resp),
                                    number2, "");
                        }
                    }
                }
                res = SyntaxOMath("m", res);
                break;
            }
        }
    }

//Método para eliminar negativos si hay "n-" o agregar "n" si hay "-" en screen  
    private static StringBuilder negativo(StringBuilder screen) {
        for (int k = 0; k < screen.length(); k++) {
            if (screen.charAt(k) == '-') {
                if (k != 0 && "n".equals(screen.substring(k - 1, k))) {
                    screen.delete(k - 1, k + 1);
                    k = k - 2;
                } else if (k == 0 || k != 0 && !isNum(screen.substring(k - 1, k))) {
                    screen.replace(k, k + 1, "n");
                }
            }
        }
        return screen;
    }

//Si es pi, número punto devuelve true
    private static boolean ePiONum(String cadena) {
        boolean valor = false;
        if (isNum(cadena) || ".".equals(cadena) || "π".equals(cadena)
                || "e".equals(cadena)) {
            valor = true;
        }
        return valor;

    }

//  Si la cadena no es algo que forme parte de un número devuelve true 
    private static boolean nonum(String num) {
        boolean signos = false;
        if (!isNum(num) && !".".equals(num) && !"E".equals(num)
                && !"n".equals(num) && !"π".equals(num) && !"e".equals(num)) {
            signos = true;
        }
        return signos;
    }

//Método para comprobar si el simbolo anterior no es número, cierre de parentésis o ²
//y si el simbolo actual es un operador, entonces devolverá true
    private boolean nonum_ant(String simb, String simb_ant) {
        boolean error = false;
        if (nonum(simb_ant) && !")".equals(simb_ant) && !"²".equals(simb_ant)
                && !"!".equals(simb_ant)) {
            for (int i = 10; i < operandos.length - 1; i++) {
                if (simb.charAt(0) == operandos[i] || simb.charAt(0) == '*') {
                    error = true;
                    break;
                }
            }
        }
        return error;
    }

//Remplazamos en el SB "actual" su contenido actual por el contenido de "cambio","cambio1","cambio2"
    private static StringBuilder Replace_String(StringBuilder actual, String cambio,
            String cambio1, String cambio2) {
        actual.delete(0, actual.length());
        actual.append(cambio).append(cambio1).append(cambio2);
        return actual;
    }

//Escribir "Syntax Error" si no hay nada antes del operador o hay un "-" o "Math Error"
//si el resultado es "Infinity"
    private static StringBuilder SyntaxOMath(String num, StringBuilder num1) {
        if ("".equals(num) || "-".equals(num)) {
            Replace_String(num1, "Syntax Error", "", "");
        } else if ("Infinity".equals(num1.toString())) {
            Replace_String(num1, "Math Error", "", "");
        }
        return num1;
    }

//Si después de ² o π hay "." hay un número será un Syntax Error
    private static StringBuilder SE_pi_e_cu_fa(String num, StringBuilder num1) {
        if (!"".equals(num) && (isNum(num.substring(0, 1)) || "."
                .equals(num.substring(0, 1)))) {
            Replace_String(num1, "Syntax Error", "", "");
        }
        return num1;
    }

//Contar todo aquello que no sea ni forme parte de un número   
    private static int ContNoNum(String str, int count) {
        if (nonum(str)) {
            count++;
        }
        return count;
    }

//Si hay un error devolver true    
    private static boolean SyntaxYMath(String num) {
        boolean r = false;
        if ("Syntax Error".equals(num) || "Math Error".equals(num)) {
            r = true;
        }
        return r;
    }

//Si un número tiene "E" entonces la parte antes de E se multiplicará por 10 
//a la potencia escrita después de "E"   
    private static String E(String num) {
        String E = "";
        Double n = 0.0;
        if (!"".equals(num)) {
            if (num.charAt(num.length() - 1) != 'E') {
                for (int i = num.length() - 1; i >= 0; i--) {
                    if (num.charAt(i) == 'E') {
                        E = "E";
                        if (n == 0.0) {
                            n = Math.pow(10, Double.parseDouble(num.substring(i + 1)));
                        } else {
                            n = Math.pow(10, n);
                        }
                    } else if ("E".equals(E)) {
                        for (int j = num.substring(0, i + 1).length() - 1; j >= 0; j--) {
                            if (j == 0 || (!ePiONum(num.substring(j, j + 1))
                                    && !"-".equals(num.substring(j, j + 1)))) {
                                if (j != 0) {
                                    n = Double.parseDouble(
                                            num.substring(j + 1, i + 1)) * n;
                                } else {
                                    n = Double.parseDouble(
                                            num.substring(j, i + 1)) * n;
                                    E = "";
                                }
                                i = j + 1;
                                break;
                            }
                        }
                    }
                }
            }
            if ("E".equals(E)) {
                num = String.valueOf(n);
            }
        }
        return num;
    }

//Si no se puede realizar la operacion entre los dos números
//(por que uno o ambos tienen simbolos que no son parte de un número)   
//devuelve true
    private boolean SyntaxError(String num, String num1) {
        boolean res;
        try {
            elegir_oper(num, num1);
            res = false;
        } catch (NumberFormatException exp) {
            res = true;
        }
        return res;
    }

// Función    
    private int CharSig(char sign, char sign1, int pos_char, int no) {
        for (int reco = 0; reco < res.length(); reco++) {
            if (res.charAt(reco) == sign
                    || res.charAt(reco) == sign1 && reco != no) {
                pos_char = reco - 1;
                break;
            }
        }
        return pos_char;
    }

    public static void main(String[] ar) {
        Calculadora calc = new Calculadora();
        ImageIcon ImageIcon = new ImageIcon("src/imgs/icono.jpg");
        Image image = ImageIcon.getImage();
        calc.setIconImage(image);
        calc.setTitle("Calculadora");
        calc.setBounds(750, 300, 423, 672);
        calc.getContentPane().setBackground(new Color(30, 30, 30));
        calc.setVisible(true);
        calc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
