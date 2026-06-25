package modelo;

public class PilhaVaziaException extends RuntimeException {
 
	private static final long serialVersionUID = 1L;
	public PilhaVaziaException() {
        super("Pilha vazia.");
    }
    public PilhaVaziaException(String msg) {
        super(msg);
    }
}
