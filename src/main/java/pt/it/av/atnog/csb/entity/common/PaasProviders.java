package pt.it.av.atnog.csb.entity.common;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Deprecated
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaasProviders", propOrder = {
    "paasProviders"
})
@XmlRootElement(name = "PaasProviders")
public class PaasProviders {

    @XmlElement(name = "paasProvider")
    protected List<PaasProvider> paasProviders;

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
     * {@link PaasProvider }
     * 
     * 
     */
    public List<PaasProvider> getPaasProviders() {
        if (paasProviders == null) {
            paasProviders = new ArrayList<PaasProvider>();
        }
        return this.paasProviders;
    }

}
