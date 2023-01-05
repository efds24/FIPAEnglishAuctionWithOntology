package ontologia.impl;


import ontologia.*;

/**
* Protege name: AceptarPuja
* @author OntologyBeanGenerator v4.1
* @version 2023/01/5, 14:46:24
*/
public class DefaultAceptarPuja implements AceptarPuja {

  private static final long serialVersionUID = 6002065570892668532L;

  private String _internalInstanceName = null;

  public DefaultAceptarPuja() {
    this._internalInstanceName = "";
  }

  public DefaultAceptarPuja(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: precioActual
   */
   private float precioActual;
   public void setPrecioActual(float value) { 
    this.precioActual=value;
   }
   public float getPrecioActual() {
     return this.precioActual;
   }

   /**
   * Protege name: libro
   */
   private Libro libro;
   public void setLibro(Libro value) { 
    this.libro=value;
   }
   public Libro getLibro() {
     return this.libro;
   }

}
