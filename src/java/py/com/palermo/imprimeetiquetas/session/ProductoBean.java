/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.palermo.imprimeetiquetas.session;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author root
 */
@Stateless
public class ProductoBean {

    @PersistenceContext(unitName = "ImprimeEtiquetasPU")
    private EntityManager em;

    public List<ProductoCantidad> getProductos() {
        List<Object[]> lista = em.createNativeQuery(getConsulta()).getResultList();
        List<ProductoCantidad> R = new ArrayList<>();
        for (Object[] obj : lista) {
            R.add(new ProductoCantidad(obj));
        }
        return R;
    }

    private String getConsulta() {
        return "select pt.name, p.ean13, pt.list_price from product_product p join product_template pt on p.id = pt.id where p.ean13 is not null order by pt.name";
    }
}
