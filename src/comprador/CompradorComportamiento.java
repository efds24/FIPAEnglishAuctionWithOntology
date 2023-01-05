package comprador;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ontologia.IniciarPuja;

public class CompradorComportamiento extends CyclicBehaviour {

    public CompradorComportamiento(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg == null) {
            block();
        } else {
            switch (msg.getPerformative()) {
                case ACLMessage.CFP: {
                    try {
                        Action a = (Action) myAgent.getContentManager().extractContent(msg);
                        IniciarPuja o = (IniciarPuja) a.getAction();
                    
                        String libro = o.getLibro().getTitulo();
                        double precioActual = Double.parseDouble(o.getPrecioActual().toString());
                    } catch (CodecException | OntologyException e) {
                        e.printStackTrace();
                    }
                    ACLMessage reply = msg.createReply();
                    if (((AgenteComprador) myAgent).getGui().getLibros().containsKey(libro)) {
                        double precioMaximo = ((AgenteComprador) myAgent).getGui().getLibros().get(libro);
                        if (precioMaximo >= precioActual) {
                            reply.setPerformative(ACLMessage.PROPOSE);
                            reply.setContent("interested");
                        } else {
                            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                            reply.setContent("not-interested");
                            java.awt.EventQueue.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    javax.swing.JOptionPane.showMessageDialog(null, "El precio actual de la subasta de " + libro + " supera tu precio máximo\nHas salido de la subasta", myAgent.getLocalName(), javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                }
                            });
                            //Quitar de la tabla de subastas activas
                            ((AgenteComprador) myAgent).getGui().rmFilaSubastasActivas(libro);
                        }
                    } else {
                        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                        reply.setContent("not-interested");
                    }
                    myAgent.send(reply);
                    break;
                }
                case ACLMessage.ACCEPT_PROPOSAL: {
                    String[] aux = msg.getContent().split(" ");
                    String libro = aux[0];
                    String precio = aux[1];
                    ((AgenteComprador) myAgent).getGui().addFilaSubastasActivas(libro, precio, Boolean.TRUE);
                    break;
                }
                case ACLMessage.INFORM: {
                    //Avisar de que se ha terminado la puja
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            javax.swing.JOptionPane.showMessageDialog(null, "No has ganado la subasta de " + msg.getContent(), myAgent.getLocalName(), javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        }
                    });

                    String[] aux = msg.getContent().split(" ");
                    String libro = aux[0];
                    String precio = aux[1];
                    ((AgenteComprador) myAgent).getGui().rmFilaSubastasActivas(libro);
                    break;
                }
                case ACLMessage.REQUEST: {
                    //Avisar de que eres el ganador
                    String[] aux = msg.getContent().split(" ");
                    String libro = aux[0];
                    String precio = aux[1];
                    java.awt.EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            javax.swing.JOptionPane.showMessageDialog(null, "Has ganado la subasta de " + libro, myAgent.getLocalName(), javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                    ((AgenteComprador) myAgent).getGui().rmFilaSubastasActivas(libro);
                    ((AgenteComprador) myAgent).getGui().rmLibro(libro);
                    ((AgenteComprador) myAgent).getGui().addFilaSubastasGanadas(libro, precio);
                    break;
                }
                case ACLMessage.REJECT_PROPOSAL: {
                    String[] aux = msg.getContent().split(" ");
                    String libro = aux[0];
                    String precio = aux[1];
                    ((AgenteComprador) myAgent).getGui().addFilaSubastasActivas(libro, precio, Boolean.FALSE);
                    break;
                }
                default:
                    break;
            }
        }

    }

}
