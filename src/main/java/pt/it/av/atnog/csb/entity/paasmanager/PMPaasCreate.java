package pt.it.av.atnog.csb.entity.paasmanager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "PaasCreate", propOrder = { "name", "type", "apiEndpoint", "apiLogin" })
@XmlRootElement(name = "PaasCreate")
public class PMPaasCreate {

	@XmlElement(name = "paasProvider", required = true)
	private String name;

	@XmlElement(name = "paasType", required = false)
	private String type;
	
	@XmlElement(name = "apiEndpoint", required = false)
	private String apiEndpoint;
	
	@XmlElement(name = "apiLogin", required = false)
	private PMApiLogin apiLogin;
	
	public PMPaasCreate() {
	}
	
	public PMPaasCreate(String name, String type, String apiEndpoint, PMApiLogin apiLogin) {
		super();
		this.name = name;
		this.type = type;
		this.apiEndpoint = apiEndpoint;
		this.apiLogin = apiLogin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getApiEndpoint() {
		return apiEndpoint;
	}

	public void setApiEndpoint(String apiEndpoint) {
		this.apiEndpoint = apiEndpoint;
	}

	public PMApiLogin getApiLogin() {
		return apiLogin;
	}

	public void setApiLogin(PMApiLogin apiLogin) {
		this.apiLogin = apiLogin;
	}
}
