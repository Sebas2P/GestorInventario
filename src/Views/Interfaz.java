package Views;

import Models.Ingreso;
import Models.Inventario;
import Models.Producto;
import Models.Salida;
import Utils.Util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;

public class Interfaz extends JFrame {
    private Inventario inventario;
    HashMap<String, Producto> dicProductos = new HashMap<>();
    HashMap<String, Ingreso> dicIngreso = new HashMap<>();
    HashMap<String, Salida> dicSalida = new HashMap<>();
    private DefaultListModel<String> productosListModel; // Modelo para la lista de productos
    private JList<String> productosList; // Lista para mostrar productos
    private JTable productosTable; // Tabla para mostrar productos
    private DefaultTableModel productosTableModel; // Modelo para la tabla
    private JPanel calendarioReabastecimientoPanel; // Declarar la variable

    public Interfaz() {
        // Configuración básica de la ventana
        setTitle("Gestor de Inventario");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inventario = new Inventario(); // Inicializar el inventario

        // Crear el JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Inicializar el modelo de la tabla con encabezados
        productosTableModel = new DefaultTableModel(new String[]{"ID", "Nombre", "Stock Mínimo", "Fecha Reabastecimiento"}, 0);
        productosTable = new JTable(productosTableModel);

        // Renombrar la tabla de productos
        productosTable.setName("producto");

        // Pestaña 1: Crear Producto
        JPanel crearProductoPanel = new JPanel(new BorderLayout());

        // Panel superior para mostrar la fecha actual
        JPanel fechaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel fechaLabel = new JLabel("Fecha actual: " + Util.FECHA_ACTUAL.toString());
        fechaPanel.add(fechaLabel);
        crearProductoPanel.add(fechaPanel, BorderLayout.NORTH);

        crearProductoPanel.add(new JLabel("Crear producto"), BorderLayout.CENTER);

        // Panel izquierdo para mostrar productos en una tabla
        JScrollPane scrollPane = new JScrollPane(productosTable);
        scrollPane.setPreferredSize(new Dimension(500, 100));
        crearProductoPanel.add(scrollPane, BorderLayout.WEST);

        // Panel derecho para ingresar datos del producto
        JPanel datosProductoPanel = new JPanel(new GridLayout(5, 2));
        JTextField idField = new JTextField("Ingrese el ID del producto", 20);
        JTextField nombreField = new JTextField("Ingrese el nombre del producto", 20);
        JTextField stockMinField = new JTextField("Ingrese el stock mínimo", 20);
        JTextField fechaReabastecimientoField = new JTextField("Ingrese la fecha (YYYY-MM-DD)", 20);
        JButton agregarProductoButton = new JButton("Agregar Producto");

        // Configurar placeholders (limpiar texto al enfocar)
        idField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (idField.getText().equals("Ingrese el ID del producto")) {
                    idField.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (idField.getText().isEmpty()) {
                    idField.setText("Ingrese el ID del producto");
                }
            }
        });

        nombreField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (nombreField.getText().equals("Ingrese el nombre del producto")) {
                    nombreField.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (nombreField.getText().isEmpty()) {
                    nombreField.setText("Ingrese el nombre del producto");
                }
            }
        });

        stockMinField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (stockMinField.getText().equals("Ingrese el stock mínimo")) {
                    stockMinField.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (stockMinField.getText().isEmpty()) {
                    stockMinField.setText("Ingrese el stock mínimo");
                }
            }
        });

        fechaReabastecimientoField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (fechaReabastecimientoField.getText().equals("Ingrese la fecha (YYYY-MM-DD)")) {
                    fechaReabastecimientoField.setText("");
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (fechaReabastecimientoField.getText().isEmpty()) {
                    fechaReabastecimientoField.setText("Ingrese la fecha (YYYY-MM-DD)");
                } else {
                    // Actualizar el calendario de reabastecimiento
                    String fechaTexto = fechaReabastecimientoField.getText();
                    if (fechaTexto.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        LocalDate fechaReabastecimientoDate = LocalDate.parse(fechaTexto);
                        calendarioReabastecimientoPanel.removeAll();
                        calendarioReabastecimientoPanel.add(crearPanelCalendario(fechaReabastecimientoDate, "Fecha de Reabastecimiento"));
                        calendarioReabastecimientoPanel.revalidate();
                        calendarioReabastecimientoPanel.repaint();
                    }
                }
            }
        });

        datosProductoPanel.add(new JLabel(""));
        datosProductoPanel.add(idField);
        datosProductoPanel.add(new JLabel(""));
        datosProductoPanel.add(nombreField);
        datosProductoPanel.add(new JLabel(""));
        datosProductoPanel.add(stockMinField);
        datosProductoPanel.add(new JLabel(""));
        datosProductoPanel.add(fechaReabastecimientoField);
        datosProductoPanel.add(new JLabel()); // Espacio vacío
        datosProductoPanel.add(agregarProductoButton);

        crearProductoPanel.add(datosProductoPanel, BorderLayout.EAST);

        // Inicializar el panel de calendario de reabastecimiento
        calendarioReabastecimientoPanel = crearPanelCalendario(LocalDate.now(), "");

        // Crear y agregar el calendario en la parte inferior
        JPanel calendarioPanel = new JPanel(new GridLayout(2, 1));
        JPanel calendarioPrincipalPanel = crearPanelCalendario();
        // Reemplazar la inicialización anterior con la variable ya declarada
        calendarioPanel.add(calendarioPrincipalPanel);
        calendarioPanel.add(calendarioReabastecimientoPanel);
        crearProductoPanel.add(calendarioPanel, BorderLayout.SOUTH);

        // Acción del botón para agregar producto
        agregarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String nombre = nombreField.getText();
                String stockMinStr = stockMinField.getText();
                String fechaReabastecimiento = fechaReabastecimientoField.getText();

                try {
                    double stockMin = Double.parseDouble(stockMinStr);

                    if (!fechaReabastecimiento.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (inventario.dicProductos.containsKey(id)) {
                        JOptionPane.showMessageDialog(null, "El ID del producto ya existe. Intente con otro ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Producto nuevoProducto = new Producto(id, nombre, stockMin, fechaReabastecimiento);
                    inventario.dicProductos.put(id, nuevoProducto); // Guardar en el inventario

                    // Actualizar la tabla de productos
                    productosTableModel.addRow(new Object[]{id, nombre, stockMin, fechaReabastecimiento});

                    // Actualizar el calendario de reabastecimiento
                    LocalDate fechaReabastecimientoDate = LocalDate.parse(fechaReabastecimiento);
                    calendarioReabastecimientoPanel.removeAll();
                    calendarioReabastecimientoPanel.add(crearPanelCalendario(fechaReabastecimientoDate, "Fecha de Reabastecimiento"));
                    calendarioReabastecimientoPanel.revalidate();
                    calendarioReabastecimientoPanel.repaint();

                    JOptionPane.showMessageDialog(null, "Producto creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Limpiar campos
                    idField.setText("Ingrese el ID del producto");
                    nombreField.setText("Ingrese el nombre del producto");
                    stockMinField.setText("Ingrese el stock mínimo");
                    fechaReabastecimientoField.setText("Ingrese la fecha (YYYY-MM-DD)");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "El stock mínimo debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        tabbedPane.addTab("Crear Producto", crearProductoPanel);

        // Pestaña 2: Ingresos y Salidas
        JPanel ingresosSalidasPanel = new JPanel(new BorderLayout());

        // Panel para mostrar el presupuesto en la parte superior izquierda
        JPanel presupuestoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        presupuestoPanel.setBorder(BorderFactory.createTitledBorder("Presupuesto Disponible"));
        JLabel presupuestoLabel = new JLabel("$" + Util.presupuesto);
        presupuestoLabel.setFont(new Font("Arial", Font.BOLD, 50));
        presupuestoLabel.setForeground(new Color(0, 100, 0)); // Color verde oscuro
        presupuestoPanel.add(presupuestoLabel);

        // Panel para mostrar la capacidad de bodega en la parte superior derecha
        JPanel bodegaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bodegaPanel.setBorder(BorderFactory.createTitledBorder("Capacidad de Bodega"));
        JLabel bodegaLabel = new JLabel(Util.cantidad_bodega + " unidades");
        bodegaLabel.setFont(new Font("Arial", Font.BOLD, 50));
        bodegaLabel.setForeground(new Color(11, 96, 207)); // Color azul oscuro
        bodegaPanel.add(bodegaLabel);

        // Panel superior que incluirá el presupuesto y la capacidad de bodega
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.add(presupuestoPanel);
        topPanel.add(bodegaPanel);
        ingresosSalidasPanel.add(topPanel, BorderLayout.NORTH);

        // Panel central para Ingresos y Salidas
        JPanel ingresoSalidaFormPanel = new JPanel(new GridLayout(2, 1));

        // Panel para Ingresos
        JPanel ingresoPanel = new JPanel(new GridLayout(5, 2));
        ingresoPanel.setBorder(BorderFactory.createTitledBorder("Registrar Compra"));

        JTextField idIngresoField = new JTextField();
        JTextField fechaIngresoField = new JTextField();
        JTextField cantidadIngresoField = new JTextField();
        JTextField precioIngresoField = new JTextField();
        JButton registrarIngresoButton = new JButton("Registrar Compra");

        // Crear calendario para la fecha de ingreso
        JPanel calendarioIngresoPanel = crearPanelCalendario(LocalDate.now(), "Fecha de Ingreso");

        ingresoPanel.add(new JLabel("ID del Producto:"));
        ingresoPanel.add(idIngresoField);
        ingresoPanel.add(new JLabel("Fecha de Ingreso:"));
        ingresoPanel.add(fechaIngresoField);
        ingresoPanel.add(new JLabel("Cantidad:"));
        ingresoPanel.add(cantidadIngresoField);
        ingresoPanel.add(new JLabel("Costo:"));
        ingresoPanel.add(precioIngresoField);
        ingresoPanel.add(new JLabel());
        ingresoPanel.add(registrarIngresoButton);

        // Acción del botón para registrar ingreso
        registrarIngresoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idIngresoField.getText();
                String fecha = fechaIngresoField.getText();
                String cantidadStr = cantidadIngresoField.getText();
                String precioStr = precioIngresoField.getText();

                try {
                    double cantidad = Double.parseDouble(cantidadStr);
                    double precio = Double.parseDouble(precioStr);

                    // Validar si el total supera el presupuesto
                    double totalIngreso = cantidad * precio;
                    if (totalIngreso > Util.presupuesto) {
                        JOptionPane.showMessageDialog(null, "El total de compra supera el presupuesto disponible.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Validar si el producto existe
                    if (!inventario.dicProductos.containsKey(id)) {
                        JOptionPane.showMessageDialog(null, "El producto con el ID especificado no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Producto producto = inventario.dicProductos.get(id);

                    // Validar si el stock actual supera la capacidad de la bodega
                    double stockActual = inventario.dicStock.containsKey(id) ? inventario.dicStock.get(id).getStock() : 0;
                    if (stockActual + cantidad > Util.cantidad_bodega) {
                        JOptionPane.showMessageDialog(null,
                            "No se puede registrar el ingreso. El stock total superará la capacidad de la bodega.\n" +
                            "Capacidad de la bodega: " + Util.cantidad_bodega + "\n" +
                            "Stock actual: " + stockActual + "\n" +
                            "Cantidad a ingresar: " + cantidad,
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Actualizar el calendario de ingreso
                    LocalDate fechaIngreso = LocalDate.parse(fecha);
                    calendarioIngresoPanel.removeAll();
                    calendarioIngresoPanel.add(crearPanelCalendario(fechaIngreso, "Fecha de Ingreso"));
                    calendarioIngresoPanel.revalidate();
                    calendarioIngresoPanel.repaint();

                    if (cantidad < producto.getStock_min()) {
                        JOptionPane.showMessageDialog(null, "La cantidad no puede ser menor al stock mínimo.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Crear y guardar el ingreso en el inventario
                    Ingreso nuevoIngreso = new Ingreso(fecha, cantidad, precio);
                    dicIngreso.put(id, nuevoIngreso);
                    inventario.dicIngreso.put(id, nuevoIngreso);

                    JOptionPane.showMessageDialog(null, "Compra registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Limpiar campos
                    idIngresoField.setText("");
                    fechaIngresoField.setText("");
                    cantidadIngresoField.setText("");
                    precioIngresoField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Cantidad y costo deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Panel para Salidas
        JPanel salidaPanel = new JPanel(new GridLayout(5, 2));
        salidaPanel.setBorder(BorderFactory.createTitledBorder("Registrar Venta"));

        JTextField idSalidaField = new JTextField();
        JTextField fechaSalidaField = new JTextField();
        JTextField cantidadSalidaField = new JTextField();
        JTextField precioSalidaField = new JTextField();
        JButton registrarSalidaButton = new JButton("Registrar Venta");

        // Crear calendario para la fecha de entrega
        JPanel calendarioEntregaPanel = crearPanelCalendario(LocalDate.now(), "Fecha de Entrega");

        salidaPanel.add(new JLabel("ID del Producto:"));
        salidaPanel.add(idSalidaField);
        salidaPanel.add(new JLabel("Fecha de Entrega:"));
        salidaPanel.add(fechaSalidaField);
        salidaPanel.add(new JLabel("Cantidad:"));
        salidaPanel.add(cantidadSalidaField);
        salidaPanel.add(new JLabel("Precio:"));
        salidaPanel.add(precioSalidaField);
        salidaPanel.add(new JLabel());
        salidaPanel.add(registrarSalidaButton);

        // Acción del botón para registrar salida
        registrarSalidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idSalidaField.getText();
                String fecha = fechaSalidaField.getText();
                String cantidadStr = cantidadSalidaField.getText();
                String precioStr = precioSalidaField.getText();

                try {
                    double cantidad = Double.parseDouble(cantidadStr);
                    double precio = Double.parseDouble(precioStr);

                    if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    LocalDate fechaEntrega = LocalDate.parse(fecha);
                    if (fechaEntrega.isBefore(Util.FECHA_ACTUAL)) {
                        JOptionPane.showMessageDialog(null, "La fecha de entrega no puede ser menor a la fecha actual.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Actualizar el calendario de entrega
                    calendarioEntregaPanel.removeAll();
                    calendarioEntregaPanel.add(crearPanelCalendario(fechaEntrega, "Fecha de Entrega"));
                    calendarioEntregaPanel.revalidate();
                    calendarioEntregaPanel.repaint();

                    if (!inventario.dicIngreso.containsKey(id)) {
                        JOptionPane.showMessageDialog(null, "No hay ingresos registrados para el producto con el ID especificado.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Ingreso ingreso = inventario.dicIngreso.get(id);
                    if (cantidad > ingreso.getCantidad()) {
                        JOptionPane.showMessageDialog(null, "La cantidad de venta no puede ser mayor a la cantidad de ingreso.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Salida nuevaSalida = new Salida(fecha, cantidad, precio);
                    dicSalida.put(id, nuevaSalida);
                    inventario.dicSalida.put(id, nuevaSalida);

                    inventario.calcularStock(id); // Calcular el stock después del ingreso

                    // Validar si el stock supera la capacidad de la bodega
                    double stockActual = inventario.dicStock.get(id).getStock();
                    if (stockActual > Util.cantidad_bodega) {
                        JOptionPane.showMessageDialog(null,
                                "La capacidad de la bodega se está acabando.\n" +
                                        "Capacidad de la bodega: " + Util.cantidad_bodega + "\n" +
                                        "Stock actual: " + stockActual,
                                "Advertencia",
                                JOptionPane.WARNING_MESSAGE);
                    }

                    Producto producto = inventario.dicProductos.get(id);

                    if (stockActual < producto.getStock_min()) {
                        JOptionPane.showMessageDialog(null,
                            "Advertencia: El stock está por debajo del mínimo.\nStock total actual: " + stockActual,
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                    }

                    JOptionPane.showMessageDialog(null, "Venta registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Limpiar campos
                    idSalidaField.setText("");
                    fechaSalidaField.setText("");
                    cantidadSalidaField.setText("");
                    precioSalidaField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Cantidad y precio deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Agregar paneles de ingreso y salida al panel central
        ingresoSalidaFormPanel.add(ingresoPanel);
        ingresoSalidaFormPanel.add(salidaPanel);

        // Panel derecho para los calendarios
        calendarioPanel = new JPanel(new GridLayout(3, 1));
        calendarioPanel.setBorder(BorderFactory.createTitledBorder("Calendarios"));

        // Calendario 1: Fecha actual
        JPanel calendarioActualPanel = crearPanelCalendario(Util.FECHA_ACTUAL, "Fecha Actual");
        calendarioPanel.add(calendarioActualPanel);

        // Agregar los calendarios de ingreso y entrega
        calendarioPanel.add(calendarioIngresoPanel);
        calendarioPanel.add(calendarioEntregaPanel);

        // Agregar paneles al panel principal
        ingresosSalidasPanel.add(ingresoSalidaFormPanel, BorderLayout.CENTER);
        ingresosSalidasPanel.add(calendarioPanel, BorderLayout.EAST);

        tabbedPane.addTab("Compra y Venta", ingresosSalidasPanel);

        // Pestaña 3: Mostrar Productos
        JPanel mostrarProductosPanel = new JPanel(new BorderLayout());

        // Tabla para mostrar productos con encabezado agrupado
        DefaultTableModel mostrarProductosTableModel = new DefaultTableModel(new String[]{"Código", "Nombre", "Fecha Reabastecimiento", "Detalles"}, 0);
        JTable mostrarProductosTable = new JTable(mostrarProductosTableModel);
        mostrarProductosTable.setName("Productos"); // Renombrar la tabla a "Productos"

        // Tabla para mostrar datos de ingreso
        DefaultTableModel mostrarIngresosTableModel = new DefaultTableModel(new String[]{"ID Producto", "Fecha Ingreso", "Cantidad", "Costo"}, 0);
        JTable mostrarIngresosTable = new JTable(mostrarIngresosTableModel);
        mostrarIngresosTable.setName("Compra"); // Renombrar la tabla a "Ingresos"

        // Tabla para mostrar datos de salida
        DefaultTableModel mostrarSalidasTableModel = new DefaultTableModel(new String[]{"ID Producto", "Fecha Entrega", "Cantidad", "Precio", "Entrega", "Detalle"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) { // Columna "Entrega" como Boolean para que muestre checkbox
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer que la columna "Entrega" sea editable
                return column == 4;
            }
        };

        JTable mostrarSalidasTable = new JTable(mostrarSalidasTableModel);
        mostrarSalidasTable.setName("Venta"); // Renombrar la tabla a "Salidas"

        // Tabla para mostrar datos de stock
        DefaultTableModel mostrarStockTableModel = new DefaultTableModel(new String[]{"ID Producto", "Stock Actual"}, 0);
        JTable mostrarStockTable = new JTable(mostrarStockTableModel);
        mostrarStockTable.setName("stock del producto"); // Renombrar la tabla a "Stock"

        JTableHeader headerStock = mostrarStockTable.getTableHeader();
        headerStock.setReorderingAllowed(false);

        JScrollPane mostrarStockScrollPane = new JScrollPane(mostrarStockTable);

        // Crear encabezado simple para las tablas
        JTableHeader headerProductos = mostrarProductosTable.getTableHeader();
        headerProductos.setReorderingAllowed(false);

        JTableHeader headerIngresos = mostrarIngresosTable.getTableHeader();
        headerIngresos.setReorderingAllowed(false);

        JTableHeader headerSalidas = mostrarSalidasTable.getTableHeader();
        headerSalidas.setReorderingAllowed(false);

        JScrollPane mostrarProductosScrollPane = new JScrollPane(mostrarProductosTable);
        JScrollPane mostrarIngresosScrollPane = new JScrollPane(mostrarIngresosTable);
        JScrollPane mostrarSalidasScrollPane = new JScrollPane(mostrarSalidasTable);

        // Ajustar el tamaño de las tablas en la pestaña "Mostrar Productos"
        mostrarProductosTable.setRowHeight(25); // Aumentar altura de las filas
        mostrarProductosTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Aumentar tamaño de fuente
        mostrarProductosTable.getTableHeader().setPreferredSize(new Dimension(0, 20)); // Reducir altura del encabezado

        mostrarIngresosTable.setRowHeight(25); // Aumentar altura de las filas
        mostrarIngresosTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Aumentar tamaño de fuente
        mostrarIngresosTable.getTableHeader().setPreferredSize(new Dimension(0, 20)); // Reducir altura del encabezado

        mostrarSalidasTable.setRowHeight(25); // Aumentar altura de las filas
        mostrarSalidasTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Aumentar tamaño de fuente
        mostrarSalidasTable.getTableHeader().setPreferredSize(new Dimension(0, 20)); // Reducir altura del encabezado

        mostrarStockTable.setRowHeight(25); // Aumentar altura de las filas
        mostrarStockTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Aumentar tamaño de fuente
        mostrarStockTable.getTableHeader().setPreferredSize(new Dimension(0, 20)); // Reducir altura del encabezado

        // Botón para cargar productos, ingresos, salidas y stock
        JButton cargarProductosButton = new JButton("Cargar Productos, Compras, Ventas y Stock");
        cargarProductosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Limpiar las tablas antes de cargar nuevos datos
                mostrarProductosTableModel.setRowCount(0);
                mostrarIngresosTableModel.setRowCount(0);
                mostrarSalidasTableModel.setRowCount(0);
                mostrarStockTableModel.setRowCount(0);

                // Cargar datos de los productos
                for (String key : inventario.dicProductos.keySet()) {
                    Producto producto = inventario.dicProductos.get(key);
                    String detalles = ""; // Inicializar detalles

                    // Verificar si se requiere reabastecimiento
                    LocalDate fechaReabastecimiento = LocalDate.parse(producto.getFecha_reabastecimiento());
                    if (!fechaReabastecimiento.isAfter(Util.FECHA_ACTUAL)) {
                        detalles = "se requiere reabastecimiento";
                    }

                    mostrarProductosTableModel.addRow(new Object[]{
                        producto.getId(),
                        producto.getNombre(),
                        producto.getFecha_reabastecimiento(),
                        detalles // Agregar el mensaje a la columna "Detalles"
                    });
                }

                // Cargar datos de los ingresos
                for (String key : inventario.dicIngreso.keySet()) {
                    Ingreso ingreso = inventario.dicIngreso.get(key);
                    mostrarIngresosTableModel.addRow(new Object[]{
                        key,
                        ingreso.getFecha(),
                        ingreso.getCantidad(),
                        ingreso.getPrecio()
                    });
                }

                // Cargar datos de las salidas
                for (String key : inventario.dicSalida.keySet()) {
                    Salida salida = inventario.dicSalida.get(key);
                    String detalle = ""; // Inicializar detalle
                    Boolean estadoEntrega = false; // Por defecto, no entregado

                    // Verificar si la cantidad de salida supera un umbral (ejemplo: 50)
                    if (salida.getCantidad() > 50) {
                        detalle = "Cantidad alta";
                    }

                    // Verificar si la fecha de entrega está próxima (menos de 2 días)
                    LocalDate fechaEntrega = LocalDate.parse(salida.getFecha());
                    if (!fechaEntrega.isBefore(Util.FECHA_ACTUAL) &&
                        fechaEntrega.minusDays(2).isBefore(Util.FECHA_ACTUAL)) {
                        detalle = "Fecha de Entrega próxima";
                    }

                    // Determinar el estado de entrega - ahora como Boolean
                    if (fechaEntrega.isBefore(Util.FECHA_ACTUAL)) {
                        estadoEntrega = true; // Ya entregado
                    }

                    mostrarSalidasTableModel.addRow(new Object[]{
                        key,
                        salida.getFecha(),
                        salida.getCantidad(),
                        salida.getPrecio(),
                        estadoEntrega, // Ahora es un Boolean para el checkbox
                        detalle
                    });
                }

                // Cargar datos de stock
                for (String key : inventario.dicStock.keySet()) {
                    mostrarStockTableModel.addRow(new Object[]{
                        key,
                        inventario.dicStock.get(key).getStock()
                    });
                }
            }
        });

        // Añadir escucha de eventos para la columna de checkbox
        mostrarSalidasTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = mostrarSalidasTable.rowAtPoint(evt.getPoint());
                int col = mostrarSalidasTable.columnAtPoint(evt.getPoint());

                if (col == 4 && row >= 0) { // Si hacen clic en la columna "Entrega"
                    boolean checked = (boolean) mostrarSalidasTable.getValueAt(row, col);
                    mostrarSalidasTable.setValueAt(!checked, row, col); // Cambiar el valor

                    // Obtener el ID del producto de esta fila
                    String idProducto = (String) mostrarSalidasTable.getValueAt(row, 0);

                    // Opcionalmente puedes actualizar el diccionario de salidas con este nuevo estado
                    // Por ejemplo: inventario.dicSalida.get(idProducto).setEntregado(!checked);
                    // (requeriría añadir un atributo "entregado" a la clase Salida)
                }
            }
        });

        // Panel para mostrar el calendario de reabastecimiento de un producto específico
        JPanel calendarioProductoPanel = new JPanel(new BorderLayout());
        calendarioProductoPanel.setBorder(BorderFactory.createTitledBorder("Calendarios de Productos"));

        JTextField codigoProductoField = new JTextField("Código producto");
        JButton mostrarCalendarioButton = new JButton("Fecha Reabastecimiento");
        JButton mostrarCalendarioIngresoButton = new JButton("Fecha de Ingreso");
        JButton mostrarCalendarioEntregaButton = new JButton("Fecha de Entrega");

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(codigoProductoField);
        inputPanel.add(mostrarCalendarioButton);
        inputPanel.add(mostrarCalendarioIngresoButton);
        inputPanel.add(mostrarCalendarioEntregaButton);

        JPanel calendarioReabastecimientoProductoPanel = new JPanel(new BorderLayout());
        calendarioProductoPanel.add(inputPanel, BorderLayout.NORTH);
        calendarioProductoPanel.add(calendarioReabastecimientoProductoPanel, BorderLayout.CENTER);

        mostrarCalendarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigoProducto = codigoProductoField.getText();
                if (inventario.dicProductos.containsKey(codigoProducto)) {
                    Producto producto = inventario.dicProductos.get(codigoProducto);
                    LocalDate fechaReabastecimiento = LocalDate.parse(producto.getFecha_reabastecimiento());

                    // Crear un panel con el calendario
                    JPanel calendarioDialogPanel = crearPanelCalendario(fechaReabastecimiento, "Calendario de Reabastecimiento");

                    // Mostrar el calendario en un cuadro de diálogo
                    JOptionPane.showMessageDialog(
                        null,
                        calendarioDialogPanel,
                        "Calendario de Reabastecimiento",
                        JOptionPane.PLAIN_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(null, "El producto con el código especificado no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mostrarCalendarioIngresoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigoProducto = codigoProductoField.getText();
                if (inventario.dicIngreso.containsKey(codigoProducto)) {
                    Ingreso ingreso = inventario.dicIngreso.get(codigoProducto);
                    LocalDate fechaIngreso = LocalDate.parse(ingreso.getFecha());

                    // Crear un panel con el calendario
                    JPanel calendarioDialogPanel = crearPanelCalendario(fechaIngreso, "Calendario de Fecha de Ingreso");

                    // Mostrar el calendario en un cuadro de diálogo
                    JOptionPane.showMessageDialog(
                        null,
                        calendarioDialogPanel,
                        "Calendario de Fecha de Ingreso",
                        JOptionPane.PLAIN_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(null, "No hay ingresos registrados para el producto con el código especificado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        mostrarCalendarioEntregaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigoProducto = codigoProductoField.getText();
                if (inventario.dicSalida.containsKey(codigoProducto)) {
                    Salida salida = inventario.dicSalida.get(codigoProducto);
                    LocalDate fechaEntrega = LocalDate.parse(salida.getFecha());

                    // Crear un panel con el calendario
                    JPanel calendarioDialogPanel = crearPanelCalendario(fechaEntrega, "Calendario de Fecha de Entrega");

                    // Mostrar el calendario en un cuadro de diálogo
                    JOptionPane.showMessageDialog(
                        null,
                        calendarioDialogPanel,
                        "Calendario de Fecha de Entrega",
                        JOptionPane.PLAIN_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(null, "No hay salidas registradas para el producto con el código especificado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Agregar el panel del calendario de reabastecimiento al panel principal
        mostrarProductosPanel.add(calendarioProductoPanel, BorderLayout.NORTH);

        // Panel para organizar las tablas
        JPanel tablasPanel = new JPanel(new GridLayout(4, 20)); // Cambiar a 8 filas para incluir títulos

        // Agregar título y tabla de productos
        JLabel tituloProductos = new JLabel("Producto", SwingConstants.CENTER);
        tituloProductos.setFont(new Font("Arial", Font.PLAIN, 12)); // Reducir tamaño de fuente
        tituloProductos.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Contorno azul
        tablasPanel.add(tituloProductos);
        tablasPanel.add(mostrarProductosScrollPane);

        // Agregar título y tabla de ingresos
        JLabel tituloIngresos = new JLabel("Compra", SwingConstants.CENTER);
        tituloIngresos.setFont(new Font("Arial", Font.PLAIN, 12)); // Reducir tamaño de fuente
        tituloIngresos.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Contorno azul
        tablasPanel.add(tituloIngresos);
        tablasPanel.add(mostrarIngresosScrollPane);

        // Agregar título y tabla de salidas
        JLabel tituloSalidas = new JLabel("Venta", SwingConstants.CENTER);
        tituloSalidas.setFont(new Font("Arial", Font.PLAIN, 12)); // Reducir tamaño de fuente
        tituloSalidas.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Contorno azul
        tablasPanel.add(tituloSalidas);
        tablasPanel.add(mostrarSalidasScrollPane);

        // Agregar título y tabla de stock
        JLabel tituloStock = new JLabel("Stock", SwingConstants.CENTER);
        tituloStock.setFont(new Font("Arial", Font.PLAIN, 12)); // Reducir tamaño de fuente
        tituloStock.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Contorno azul
        tablasPanel.add(tituloStock);
        tablasPanel.add(mostrarStockScrollPane);

        // Agregar componentes al panel
        mostrarProductosPanel.add(tablasPanel, BorderLayout.CENTER);
        mostrarProductosPanel.add(cargarProductosButton, BorderLayout.SOUTH);

        tabbedPane.addTab("Registro productos", mostrarProductosPanel);

        // Agregar el JTabbedPane al JFrame
        add(tabbedPane);

        // Hacer visible la ventana
        setVisible(true);
    }

    /**
     * Crea un panel con un calendario que muestra la fecha actual
     * @return JPanel con el calendario
     */
    private JPanel crearPanelCalendario() {
        JPanel panelCalendario = new JPanel();
        panelCalendario.setBorder(BorderFactory.createTitledBorder("Calendario"));
        panelCalendario.setLayout(new BorderLayout());

        // Obtener la fecha actual de Util
        LocalDate fechaActual = Util.FECHA_ACTUAL;
        YearMonth yearMonth = YearMonth.from(fechaActual);

        // Panel superior con mes y año
        JPanel panelMesAnio = new JPanel();
        JLabel labelMesAnio = new JLabel(fechaActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) +
                                         " " + fechaActual.getYear());
        labelMesAnio.setFont(new Font("Arial", Font.BOLD, 16));
        panelMesAnio.add(labelMesAnio);

        // Panel para los días de la semana
        JPanel panelDiasSemana = new JPanel(new GridLayout(1, 7));
        for (DayOfWeek day : DayOfWeek.values()) {
            String nombreDia = day.getDisplayName(TextStyle.SHORT, new Locale("es", "ES")).toUpperCase();
            JLabel label = new JLabel(nombreDia, SwingConstants.CENTER);
            panelDiasSemana.add(label);
        }

        // Panel para los días del mes
        JPanel panelDias = new JPanel(new GridLayout(6, 7));

        // Obtener el primer día del mes y el total de días
        LocalDate primerDiaMes = yearMonth.atDay(1);
        int diasEnMes = yearMonth.lengthOfMonth();

        // Determinar en qué día de la semana comienza el mes (0 = lunes, 6 = domingo)
        int diaSemanaInicial = primerDiaMes.getDayOfWeek().getValue() - 1;
        if (diaSemanaInicial == -1) diaSemanaInicial = 6; // Ajuste para domingo

        // Agregar espacios en blanco para los días anteriores al inicio del mes
        for (int i = 0; i < diaSemanaInicial; i++) {
            panelDias.add(new JLabel(""));
        }

        // Agregar los días del mes
        for (int dia = 1; dia <= diasEnMes; dia++) {
            JLabel labelDia = new JLabel(String.valueOf(dia), SwingConstants.CENTER);
            // Resaltar la fecha actual
            if (dia == fechaActual.getDayOfMonth()) {
                labelDia.setOpaque(true);
                labelDia.setBackground(new Color(173, 66, 244));
                labelDia.setForeground(Color.WHITE);
                labelDia.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
            panelDias.add(labelDia);
        }

        // Completar los espacios restantes de la última semana
        int espaciosRestantes = 42 - (diaSemanaInicial + diasEnMes); // 42 = 6 semanas * 7 días
        for (int i = 0; i < espaciosRestantes; i++) {
            panelDias.add(new JLabel(""));
        }

        // Agregar todos los paneles al panel principal
        panelCalendario.add(panelMesAnio, BorderLayout.NORTH);
        panelCalendario.add(panelDiasSemana, BorderLayout.CENTER);
        panelCalendario.add(panelDias, BorderLayout.SOUTH);

        return panelCalendario;
    }

    /**
     * Crea un panel con un calendario que muestra una fecha específica
     * @param fecha Fecha a mostrar en el calendario
     * @param titulo Título del calendario
     * @return JPanel con el calendario
     */
    private JPanel crearPanelCalendario(LocalDate fecha, String titulo) {
        JPanel panelCalendario = new JPanel();
        panelCalendario.setBorder(BorderFactory.createTitledBorder(titulo));
        panelCalendario.setLayout(new BorderLayout());

        YearMonth yearMonth = YearMonth.from(fecha);

        // Panel superior con mes y año
        JPanel panelMesAnio = new JPanel();
        JLabel labelMesAnio = new JLabel(fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")) +
                                         " " + fecha.getYear());
        labelMesAnio.setFont(new Font("Arial", Font.BOLD, 16));
        panelMesAnio.add(labelMesAnio);

        // Panel para los días de la semana
        JPanel panelDiasSemana = new JPanel(new GridLayout(1, 7));
        for (DayOfWeek day : DayOfWeek.values()) {
            String nombreDia = day.getDisplayName(TextStyle.SHORT, new Locale("es", "ES")).toUpperCase();
            JLabel label = new JLabel(nombreDia, SwingConstants.CENTER);
            panelDiasSemana.add(label);
        }

        // Panel para los días del mes
        JPanel panelDias = new JPanel(new GridLayout(6, 7));

        // Obtener el primer día del mes y el total de días
        LocalDate primerDiaMes = yearMonth.atDay(1);
        int diasEnMes = yearMonth.lengthOfMonth();

        // Determinar en qué día de la semana comienza el mes (0 = lunes, 6 = domingo)
        int diaSemanaInicial = primerDiaMes.getDayOfWeek().getValue() - 1;
        if (diaSemanaInicial == -1) diaSemanaInicial = 6; // Ajuste para domingo

        // Agregar espacios en blanco para los días anteriores al inicio del mes
        for (int i = 0; i < diaSemanaInicial; i++) {
            panelDias.add(new JLabel(""));
        }

        // Agregar los días del mes
        for (int dia = 1; dia <= diasEnMes; dia++) {
            JLabel labelDia = new JLabel(String.valueOf(dia), SwingConstants.CENTER);
            // Resaltar la fecha proporcionada
            if (dia == fecha.getDayOfMonth()) {
                labelDia.setOpaque(true);
                labelDia.setBackground(new Color(89, 202, 237));
                labelDia.setForeground(Color.WHITE);
                labelDia.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            }
            panelDias.add(labelDia);
        }

        // Completar los espacios restantes de la última semana
        int espaciosRestantes = 42 - (diaSemanaInicial + diasEnMes); // 42 = 6 semanas * 7 días
        for (int i = 0; i < espaciosRestantes; i++) {
            panelDias.add(new JLabel(""));
        }

        // Agregar todos los paneles al panel principal
        panelCalendario.add(panelMesAnio, BorderLayout.NORTH);
        panelCalendario.add(panelDiasSemana, BorderLayout.CENTER);
        panelCalendario.add(panelDias, BorderLayout.SOUTH);

        return panelCalendario;
    }

    public static void main(String[] args) {
        new Interfaz();
    }
}

