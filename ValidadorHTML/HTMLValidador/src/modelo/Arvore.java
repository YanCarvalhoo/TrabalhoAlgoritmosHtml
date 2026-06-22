package modelo;

public class Arvore<T>
{
	
	private NoArvore<T> raiz;

    public NoArvore<T> getRaiz()
    {
		return raiz;
	}    

	public void setRaiz(NoArvore<T> raiz) 
	{
		this.raiz = raiz;
	}

	public Arvore() 
    {
        raiz = null;
    }

	public boolean pertence(T info) 
	{
		
		if(raiz == null)
		{
			return false;
		}
		
		return pertence(raiz,info);
	}
	
	public boolean pertence(NoArvore<T> no, T info) 
	{
		if(no.getInfo().equals(info)) 
		{
			return true;
		}
		
		NoArvore<T> p = no.getPrimeiro();
		
		while(p != null) 
		{
			if(pertence(p, info))
			{
				return true;
			}
			
			p = p.getProximo();
		}
		
		return false;
	}
	
	public boolean estaVazia() 
	{
		return raiz == null;
	}
	
	public String toString() 
	{
		if(estaVazia()) 
		{
			return "";
		}
		
		return obterRepresentacaoTextual(raiz);
	}
	
	public String obterRepresentacaoTextual(NoArvore<T> no) 
	{
		String s = "<" + no.getInfo();
		NoArvore<T> p = no.getPrimeiro();
		
		while(p != null)
		{
			s += obterRepresentacaoTextual(p);
			p = p.getProximo();
		}
		
		s += ">";
		return s;
	}
	
	
	public int contarNos() 
	{
		if(estaVazia()) 
		{
			return 0;
		}
		
		return contarNos(raiz);
	}
	
	private int contarNos(NoArvore<T> no) 
	{
		int quantidade = 1;
		
		NoArvore<T> p = no.getPrimeiro();
		while (p != null) 
		{
			quantidade += contarNos(p);
			p = p.getProximo();			
		}
		
		return quantidade;
	}
}
