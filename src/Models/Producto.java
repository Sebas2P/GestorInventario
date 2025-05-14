package Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Producto {

    private String id;
    private String nombre;
    private double stock_min;
    private String fecha_reabastecimiento;

    public Producto(String id, String nombre, double stock_min, String fecha_reabastecimiento) {
        this.id = id;
        this.nombre = nombre;
        this.stock_min = stock_min;
        this.fecha_reabastecimiento = fecha_reabastecimiento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getStock_min() {
        return stock_min;
    }

    public void setStock_min(double stock_min) {
        this.stock_min = stock_min;
    }

    public String getFecha_reabastecimiento() {
        return fecha_reabastecimiento;
    }

    public void setFecha_reabastecimiento(String fecha_reabastecimiento) {
        this.fecha_reabastecimiento = fecha_reabastecimiento;
    }
}
