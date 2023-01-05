package ontologia;



/**
* Protege name: Informar
* @author OntologyBeanGenerator v4.1
* @version 2023/01/5, 14:46:24
*/
public interface Informar extends jade.content.AgentAction {

   /**
   * Protege name: precioActual
   */
   public void setPrecioActual(float value);
   public float getPrecioActual();

   /**
   * Protege name: libro
   */
   public void setLibro(Libro value);
   public Libro getLibro();

}
