package Practica1.pdf;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;  
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class pdf_crear {
    private String folio;
    private String nombreInfante;
    private int grado;
    private String nombreTutor;
    private String fechaRegistro;
	private Document documento;
	private FileOutputStream archivo;
	private Paragraph titulo;
	
	public pdf_crear(String folio,String nombreInfante, int grado, String nombreTutor, String fechaRegistro) {
		this.folio = folio;
		this.nombreInfante = nombreInfante;
		this.grado = grado;
		this.nombreTutor = nombreTutor;
		this.fechaRegistro = fechaRegistro;
		documento = new Document(); 
	}
        
	public void crearPlantilla() {
		try {
			archivo = new FileOutputStream("C:/Users/psp_g/Desktop/archivosEscuela/" + folio + ".pdf");
			PdfWriter.getInstance(documento, archivo);
			documento.open(); /*------Inciio de documento------*/
                        
                        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
                        Paragraph titulo = new Paragraph("REGISTRO EXITOSO", boldFont);
                        titulo.setAlignment(1);
                        documento.add(titulo);
                        documento.add(Chunk.NEWLINE);
                        
			Paragraph texto = new Paragraph("El aspirante con nombre " + nombreInfante + " con numero de folio " + folio 
                                + " ha sido registrado para el grado " + String.valueOf(grado) + " en la fecha " + fechaRegistro + ".");
			texto.setAlignment(Element.ALIGN_JUSTIFIED);
			documento.add(texto);
                        
                        documento.add(new Paragraph("Nombre del tutor del aspirante registrado: " + nombreTutor));
			documento.add(Chunk.NEWLINE);
                        
                        Paragraph encab = new Paragraph("Atentamente\nM.C. Hilda Mejia Matias\nDirectora del Instituto Abraham Lincoln");
			encab.setAlignment(1);
                        documento.add(encab);
                        documento.add(Chunk.NEWLINE);
			
			documento.close();
			System.out.println("ARCHIVO CREADO");
		}catch(FileNotFoundException e) {
			System.err.println(e.getMessage());
		}catch(DocumentException e) {
			System.err.println(e.getMessage());
		}
	}
    
}
