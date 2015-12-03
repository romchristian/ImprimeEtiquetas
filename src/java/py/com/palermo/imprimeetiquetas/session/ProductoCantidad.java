 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.palermo.imprimeetiquetas.session;

import java.math.BigDecimal;

/**
 *
 * @author root
 */
public class ProductoCantidad {

    private String nombre;
    private String codigo;
    private BigDecimal precio;
    private int cantidad;

    public ProductoCantidad() {
    }

    public ProductoCantidad(Object[] obj) {
        this.nombre = (String) obj[0];
        this.codigo = (String) obj[1];
        this.precio = (BigDecimal) obj[2];
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
