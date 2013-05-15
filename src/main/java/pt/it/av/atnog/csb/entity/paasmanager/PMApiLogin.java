package pt.it.av.atnog.csb.entity.paasmanager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "apiLogin", propOrder = { "apiKey", "apiSecret" })
@XmlRootElement(name = "apiLogin")
public class PMApiLogin {

	@XmlElement(name = "apiKey", required = false)
	private String apiKey;
	
	@XmlElement(name = "apiSecret", required = false)
	private String apiSecret;
	
	public PMApiLogin() {
	}
	
	public PMApiLogin(String apiKey, String apiSecret) {
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}
}