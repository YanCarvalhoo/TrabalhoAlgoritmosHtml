package controle;

import modelo.Fila;
import java.util.List;

public class ResultadoAnalise {

    private boolean bemFormatado;
    private Fila<String> erros;
    private List<TagFrequencia> tabelaFrequencia;
    private String hierarquia;

    public boolean isBemFormatado() { 
    	return bemFormatado; 
    }
    
    public void setBemFormatado(boolean bemFormatado) { 
    	this.bemFormatado = bemFormatado; 
    }

    public Fila<String> getErros() { 
    	return erros; 
    }
    
    public void setErros(Fila<String> erros) { 
    	this.erros = erros; 
    }

    public List<TagFrequencia> getTabelaFrequencia() { 
    	return tabelaFrequencia; 
    }
    
    public void setTabelaFrequencia(List<TagFrequencia> tabelaFrequencia) { 
    	this.tabelaFrequencia = tabelaFrequencia; 
    }

    public String getHierarquia() { 
    	return hierarquia;
    }
    
    public void setHierarquia(String hierarquia) { 
    	this.hierarquia = hierarquia; 
    }
}
