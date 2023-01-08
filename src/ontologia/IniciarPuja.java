package ontologia;

/**
* Protege name: IniciarPuja
* @author OntologyBeanGenerator v4.1
* @version 2023/01/6, 09:41:36
*/
public class IniciarPuja implements jade.content.AgentAction {

  private static final long serialVersionUID = -9132062179456803291L;

  private String _internalInstanceName = null;

  public IniciarPuja() {
    this._internalInstanceName = "";
  }

  public IniciarPuja(String instance_name) {
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
