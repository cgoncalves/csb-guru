package pt.it.av.atnog.csb.entity.paasmanager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "paasProvider",
    "paasStatus",
    "paasEndpoint"
})
@XmlRootElement(name = "ProviderCreateResponse")
public class PMProviderCreateResponse {

	@XmlElement(required = true)
    protected String paasProvider;
    
    @XmlElement(required = true)
    protected String paasStatus;
    
    @XmlElement(required = true)
    protected String paasEndpoint;
    
    public PMProviderCreateResponse() {
    }

	public PMProviderCreateResponse(String paasProvider, String paasStatus,
			String paasEndpoint) {
		super();
		this.paasProvider = paasProvider;
		this.paasStatus = paasStatus;
		this.paasEndpoint = paasEndpoint;
	}

	public String getPaasProvider() {
		return paasProvider;
	}

	public void setPaasProvider(String paasProvider) {
		this.paasProvider = paasProvider;
	}

	public String getPaasStatus() {
		return paasStatus;
	}

	public void setPaasStatus(String paasStatus) {
		this.paasStatus = paasStatus;
	}

	public String getPaasEndpoint() {
		return paasEndpoint;
	}

	public void setPaasEndpoint(String paasEndpoint) {
		this.paasEndpoint = paasEndpoint;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((paasEndpoint == null) ? 0 : paasEndpoint.hashCode());
		result = prime * result
				+ ((paasProvider == null) ? 0 : paasProvider.hashCode());
		result = prime * result
				+ ((paasStatus == null) ? 0 : paasStatus.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PMProviderCreateResponse other = (PMProviderCreateResponse) obj;
		if (paasEndpoint == null) {
			if (other.paasEndpoint != null)
				return false;
		} else if (!paasEndpoint.equals(other.paasEndpoint))
			return false;
		if (paasProvider == null) {
			if (other.paasProvider != null)
				return false;
		} else if (!paasProvider.equals(other.paasProvider))
			return false;
		if (paasStatus == null) {
			if (other.paasStatus != null)
				return false;
		} else if (!paasStatus.equals(other.paasStatus))
			return false;
		return true;
	}
}
