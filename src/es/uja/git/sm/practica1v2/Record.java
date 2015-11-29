package es.uja.git.sm.practica1v2;

import java.io.Serializable;

/**
 * Clase de ejemplo para almacenar un usuario, una operacion, un valor, resultado y fecha
 * 
 */
public class Record implements Serializable{
	/**
	 * 
	 */
	transient private static final long serialVersionUID = -319981228737406507L;
	transient private long id;
	private String usuario;
	private String operacion;
	private double valor;
	private double resultado;
	private String fecha;

	public Record() {
		this.usuario = "";
		this.operacion = "";
		this.valor = 0;
		this.resultado = 0;
		this.fecha = "";

	}

	public Record(String user, String op,double value, double res, String fec) {
		this.usuario = user;
		this.operacion = op;
		this.valor =value ;
		this.resultado = res;
		this.fecha = fec;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	@Override
	public String toString() {
		return usuario+"-"+"Operacion: "+operacion+" Valor: "+valor+"="+resultado+" Fecha: "+fecha;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public double getResultado() {
		return resultado;
	}

	public void setResultado(double resultado) {
		this.resultado = resultado;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	
	
}