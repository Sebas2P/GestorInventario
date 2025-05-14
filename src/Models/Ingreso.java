package Models;

import java.util.Set;

public class Ingreso {

    private String fecha;
    private double cantidad;
    private double precio;

    public Ingreso(String fecha, double cantidad, double precio) {
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
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
