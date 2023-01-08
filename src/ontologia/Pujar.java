package ontologia;

/**
* Protege name: Pujar
* @author OntologyBeanGenerator v4.1
* @version 2023/01/6, 12:06:57
*/
public class Pujar implements jade.content.AgentAction {

  private static final long serialVersionUID = 7150169933411678710L;

  private String _internalInstanceName = null;

  public Pujar() {
    this._internalInstanceName = "";
  }

  public Pujar(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
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

   /**
   * Protege name: pujar
   */
   private boolean pujar;
   public void setPujar(boolean value) { 
    this.pujar=value;
   }
   public boolean getPujar() {
     return this.pujar;
   }

}
