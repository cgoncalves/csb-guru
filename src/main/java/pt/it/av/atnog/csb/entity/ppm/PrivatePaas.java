package pt.it.av.atnog.csb.entity.ppm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import pt.it.av.atnog.csb.entity.csb.PaasProvider;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
@Entity
@Table(name = "private_paas")
public class PrivatePaas extends PaasProvider {

	public static enum State {
		NOT_BOOTSTRAPED, BOOTSTRAPPING, BOOTSTRAPPED
	}

	@Column(name = "state", unique = false, nullable = false)
	private State state;
	
	@Column(name = "domain", unique = true, nullable = false)
	private String domain;

	public PrivatePaas() {
	}

	public boolean isBootstrapped() {
		return state == State.BOOTSTRAPPED;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
}
