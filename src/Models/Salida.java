package Models;

public class Salida {

    private String fecha_entrega;
    private double cantidad;
    private double precio;

    public Salida(String fecha_entrega, double cantidad, double precio) {
        this.fecha_entrega = fecha_entrega;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public String getFecha() {
        return fecha_entrega;
    }

    public void setFecha(String fecha) {
        this.fecha_entrega = fecha;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
