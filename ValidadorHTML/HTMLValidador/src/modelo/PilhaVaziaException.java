package modelo;

public class PilhaVaziaException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PilhaVaziaException() {
        super("A pilha esta vazia.");
    }
}
