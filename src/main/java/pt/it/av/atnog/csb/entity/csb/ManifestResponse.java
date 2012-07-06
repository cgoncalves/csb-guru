package pt.it.av.atnog.csb.entity.csb;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import pt.it.av.atnog.csb.entity.common.PaasProvider;
import pt.it.av.atnog.csb.entity.common.PaasProviders;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "paasProviders"
})
@XmlRootElement(name = "ManifestResponse")
public class ManifestResponse {

	@XmlElementWrapper(name = "PaasProviders", required = true)
	@XmlElement(name="paasProviders")
    protected List<PaasProvider> paasProviders;

    /**
     * Gets the value of the paasProviders property.
     * 
     * @return
     *     possible object is
     *     {@link PaasProviders }
     *     
     */
    public List<PaasProvider> getPaasProviders() {
        return paasProviders;
    }

    /**
     * Sets the value of the paasProviders property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaasProviders }
     *     
     */
    public void setPaasProviders(List<PaasProvider> value) {
        this.paasProviders = value;
    }

}
