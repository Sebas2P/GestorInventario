package Models;

import Utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.time.LocalDate;

public class Inventario {
    private double Stock;

    public Inventario() {
    }

    public Inventario(double stock) {
        Stock = stock;
    }

    public double getStock() {
        return Stock;
    }

    public void setStock(double stock) {
        Stock = stock;
    }

    public HashMap<String, Producto> dicProductos = new HashMap<>();
    public HashMap<String, Ingreso> dicIngreso = new HashMap<>();
    public HashMap<String, Salida> dicSalida = new HashMap<>();
    public HashMap<String, Inventario> dicStock = new HashMap<>();

    public void crearProducto() {
        /*
        ArrayList<Producto> productos = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el ID del producto:");
        String id = scanner.nextLine();
        System.out.println("Ingrese el nombre del producto:");
        String nombre = scanner.nextLine();
        System.out.println("Ingrese el stock mínimo del producto:");
        double stock_min = scanner.nextDouble();
        for (Producto producto : productos) {
            if (producto.getId().equals(id)) {
                System.out.println("El ID ya existe. Intente nuevamente.");
                return;
            }else {
                Producto nuevoProducto = new Producto(id, nombre, stock_min);
                productos.add(nuevoProducto);
                System.out.println("Producto creado exitosamente.");
                return;
            }

        }

         */
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el ID del producto:");
        String id = scanner.nextLine();
        System.out.println("Ingrese el nombre del producto:");
        String nombre = scanner.nextLine();
        System.out.println("Ingrese la fecha de reabastecimiento (formato: año-mes-día):");
        String fecha = scanner.nextLine();

        if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("Formato de fecha inválido. Intente nuevamente.");
            return;
        }


        System.out.println("Ingrese el stock mínimo del producto:");
        double stock_min = scanner.nextDouble();
        if (dicProductos.containsKey(id)) {
            System.out.println("El ID ya existe. Intente nuevamente.");
        } else {
            Producto nuevoProducto = new Producto(id, nombre, stock_min, fecha);
            dicProductos.put(id, nuevoProducto);
            System.out.println("Producto creado exitosamente.");
        }
    }


    public void notaIngreso(){
        // Verificar si el total de ingresos supera el presupuesto
        if (dicProductos.isEmpty()){
            System.out.println("No hay productos registrados");
            return;
        }

        double totalIngreso = 0;

        // Si no supera el presupuesto, continuar con la creación de la nota de ingreso
        System.out.println("Ingrese el ID del producto:");
        Scanner scanner = new Scanner(System.in);
        String id = scanner.nextLine();

        Set<String> keys = dicProductos.keySet();
        for (String key : keys){
            Producto producto = dicProductos.get(key);
            if(producto.getId().equals(key)){
                System.out.println("Nombre del producto: "+ producto.getNombre());
                System.out.println("Ingrese la fecha en la que se resivio el pedido(formato: año-mes-día):");
                String fecha = scanner.nextLine();

                if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    System.out.println("Formato de fecha inválido. Intente nuevamente.");
                    return;
                }

                System.out.println("Ingresa la cantidad de ingreso:");
                double cantidad = scanner.nextDouble();
                if (cantidad < dicProductos.get(key).getStock_min()) {
                    System.out.println("La cantidad de ingreso no puede ser menor al stock mínimo.");
                    return;
                }
                System.out.println("Ingrese el precio de ingreso:");
                double precio = scanner.nextDouble();
                totalIngreso += cantidad * precio;

                if (totalIngreso >= Util.presupuesto) {
                    System.out.println("El total de ingresos supera el presupuesto.");
                    return;
                }

                Ingreso nuevoIngreso = new Ingreso(fecha, cantidad, precio);
                dicIngreso.put(key, nuevoIngreso);
            }
        }
    }

    public void notaSalida(){

        if (dicIngreso.isEmpty()) {
            System.out.println("No hay ingresos registrados para el producto");
            return;
        }
        if (dicProductos.isEmpty()){
            System.out.println("No hay productos registrados");
            return;
        }

        System.out.println("Ingrese el ID del producto:");
        Scanner scanner = new Scanner(System.in);
        String id = scanner.nextLine();
        Set<String> keys = dicProductos.keySet();
        for (String key : keys){
            Producto producto = dicProductos.get(key);
            if(producto.getId().equals(key)){
                System.out.println("Nombre del producto: "+ producto.getNombre());
                System.out.println("Ingrese la fecha de entrega (formato: año-mes-día):");
                String fecha = scanner.nextLine();

                if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    System.out.println("Formato de fecha inválido. Intente nuevamente.");
                    return;
                }

                if (LocalDate.parse(fecha).isBefore(LocalDate.now())) {
                    System.out.println("La fecha de entrega no puede ser anterior a la fecha actual.");
                    return;
                }

                System.out.println("Ingrese la cantidad de salida:");
                double cantidad = scanner.nextDouble();
                if (cantidad >= dicIngreso.get(key).getCantidad()) {
                    System.out.println("La cantidad de salida no puede ser mayor a la cantidad de ingreso.");
                    return;
                }
                System.out.println("Ingrese el precio de salida:");
                double precio = scanner.nextDouble();
                Salida nuevaSalida = new Salida(fecha, cantidad, precio);
                dicSalida.put(key, nuevaSalida);
                calcularStock(key);
            }
        }
        // Verificar si la cantidad de salida es mayor a la cantidad de ingreso
    }

    public double calcularStock(String key) {
        Producto producto = dicProductos.get(key);
        Ingreso ingreso = dicIngreso.get(key);
        Salida salida = dicSalida.get(key);

        double cantidad = ingreso.getCantidad() - salida.getCantidad();

        Inventario nuevoStock = new Inventario(cantidad);
        dicStock.put(key, nuevoStock);

        return cantidad; // Devolver el stock actual
    }

    public void mostrarProductos(){
        Set<String> keys = dicProductos.keySet();
        if (keys.isEmpty()){
            System.out.println("No hay productos registrados");
            return;
        }

        for (String key : keys){
            Producto producto = dicProductos.get(key);
            Ingreso ingreso = dicIngreso.get(key);
            Salida salida = dicSalida.get(key);
            if (ingreso == null || salida == null) {
                System.out.println("No hay ingresos o salidas registrados para el producto con ID: " + key);
                return;
            }

            System.out.println("Nombre del producto: "+ producto.getNombre());
            System.out.println("ID del producto: "+ producto.getId());
            System.out.println("Stock mínimo del producto: "+ producto.getStock_min());
            System.out.println("Fecha de reabastecimiento: "+ producto.getFecha_reabastecimiento());

            if (LocalDate.parse(producto.getFecha_reabastecimiento()).isEqual(Util.FECHA_ACTUAL)) {
                System.out.println("Se necesita realizar ya el reabastecimiento de los productos.");
            } else if (LocalDate.parse(producto.getFecha_reabastecimiento()).isAfter(Util.FECHA_ACTUAL)) {
                System.out.println("Se acerca la fecha de reabastecimiento, se recomienda ya realizarlo.");
            }

            System.out.println("--------------------------------------------------");
            System.out.println("Ingreso del producto");
            System.out.println("Fecha de ingreso: "+ ingreso.getFecha());
            System.out.println("Cantidad de ingreso: "+ ingreso.getCantidad());
            System.out.println("Precio de ingreso: "+ ingreso.getPrecio());
            System.out.println("--------------------------------------------------");
            System.out.println("Salida del producto");
            System.out.println("Fecha de salida: "+ salida.getFecha());
            System.out.println("Cantidad de salida: "+ salida.getCantidad());
            System.out.println("Precio de salida: "+ salida.getPrecio());
            System.out.println("--------------------------------------------------");

            System.out.println("Stock total: "+ dicStock.get(key).getStock());

        }
    }

    public void menu(){
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;
        do {
            System.out.println("1. Crear producto");
            System.out.println("2. Crear nota de ingreso");
            System.out.println("3. Crear nota de salida");
            System.out.println("4. Calcular stock");
            System.out.println("5. Mostrar productos");
            System.out.println("6. Salir");
            System.out.print("Ingrese una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    crearProducto();
                    break;
                case 2:
                    notaIngreso();
                    break;
                case 3:
                    notaSalida();
                    break;
                case 4:
                    break;
                case 5:
                    mostrarProductos();
                    break;
                case 6:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 6);
    }




}
