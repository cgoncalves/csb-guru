package pt.it.av.atnog.csb.entity.paasmanager;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PMPaasProviders", propOrder = {
    "paasProviders"
})
@XmlRootElement(name = "PaasProviders")
public class PMPaasProviders {

    @XmlElement(name = "paasProvider")
    protected List<PMPaasProvider> paasProviders;

    /**
     * Gets the value of the paasProviders property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the paasProviders property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPaasProviders().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PMPaasProvider }
     * 
     * 
     */
    public List<PMPaasProvider> getPaasProviders() {
        if (paasProviders == null) {
            paasProviders = new ArrayList<PMPaasProvider>();
        }
        return this.paasProviders;
    }

	public void setPaasProviders(List<PMPaasProvider> paasProviders) {
		this.paasProviders = paasProviders;
	}

}
