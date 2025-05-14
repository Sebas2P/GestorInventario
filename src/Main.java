import Models.Inventario;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bienvenido al sistema de inventario");
        System.out.println("Fecha: " + java.time.LocalDate.now());

        Inventario inventario = new Inventario();
        inventario.menu();
    }
}