/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.palermo.imprimeetiquetas.web;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import py.com.palermo.imprimeetiquetas.session.ProductoBean;
import py.com.palermo.imprimeetiquetas.session.ProductoCantidad;

/**
 *
 * @author root
 */
@ViewScoped
@Named
public class ProductoController implements Serializable {

    @EJB
    private ProductoBean productoBean;

    private List<ProductoCantidad> productos;

    public List<ProductoCantidad> getProductos() {
        if (productos == null) {
            cargaProductos();
        }
        return productos;
    }

    public void setProductos(List<ProductoCantidad> productos) {
        this.productos = productos;
    }

    public void cargaProductos() {
        productos = productoBean.getProductos();
    }

    public String createPdf() throws IOException, DocumentException {

        HttpServletResponse response
                = (HttpServletResponse) FacesContext.getCurrentInstance()
                .getExternalContext().getResponse();
        response.setContentType("application/x-pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"etiquetas.pdf\"");

        // step 1
        Document document = new Document(new Rectangle(86, 35));
        // step 2

        document.setMargins(0f, 0f, 0f, 0f);

        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        // step 3
        document.open();

        // step 4
        PdfContentByte cb = writer.getDirectContent();

        for (ProductoCantidad p : productos) {
            if (p.getCantidad() > 0) {

                BarcodeEAN codeEAN = new BarcodeEAN();
                codeEAN.setCode(p.getCodigo());
                codeEAN.setCodeType(Barcode.EAN13);
                codeEAN.setBarHeight(10f);
                codeEAN.setX(0.7f);
                codeEAN.setSize(4f);

                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "py"));
                Font fontbold = FontFactory.getFont("Times-Roman", 5, Font.NORMAL);
                Chunk productTitle = new Chunk(p.getNombre() + "," + " " + nf.format(p.getPrecio()), fontbold);

                // EAN 13
                Paragraph pTitile = new Paragraph(productTitle);
                pTitile.setAlignment(Element.ALIGN_CENTER);
                pTitile.setLeading(0, 1);

                PdfPTable table = new PdfPTable(1);
                table.setPaddingTop(0f);

                table.setWidthPercentage(96);
                PdfPCell cell = new PdfPCell();
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(codeEAN.createImageWithBarcode(cb, null, BaseColor.BLACK));

                table.addCell(cell);

                PdfPCell cell2 = new PdfPCell();
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setBorder(Rectangle.NO_BORDER);
                cell2.addElement(pTitile);

                table.addCell(cell2);

                for (int i = 0; i < p.getCantidad(); i++) {
                    document.add(table);
                    document.newPage();
                }
            }
        }

        // step 5
        document.close();

        response.getOutputStream().flush();
        response.getOutputStream().close();
        FacesContext.getCurrentInstance().responseComplete();
        return null;
    }
}
