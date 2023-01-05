package ontologia.impl;


import ontologia.*;

/**
* Protege name: Libro
* @author OntologyBeanGenerator v4.1
* @version 2023/01/5, 14:46:24
*/
public class DefaultLibro implements Libro {

  private static final long serialVersionUID = 6002065570892668532L;

  private String _internalInstanceName = null;

  public DefaultLibro() {
    this._internalInstanceName = "";
  }

  public DefaultLibro(String instance_name) {
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
