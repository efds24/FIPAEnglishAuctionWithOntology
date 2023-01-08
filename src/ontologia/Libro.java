package ontologia;

/**
* Protege name: Libro
* @author OntologyBeanGenerator v4.1
* @version 2023/01/6, 09:41:36
*/
public class Libro implements jade.content.Concept {

  private static final long serialVersionUID = -9132062179456803291L;

  private String _internalInstanceName = null;

  public Libro() {
    this._internalInstanceName = "";
  }

  public Libro(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: titulo
   */
   private String titulo;
   public void setTitulo(String value) { 
    this.titulo=value;
   }
   public String getTitulo() {
     return this.titulo;
   }

}
