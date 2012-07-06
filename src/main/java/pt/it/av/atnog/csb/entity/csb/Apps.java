package pt.it.av.atnog.csb.entity.csb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "apps"
})
@XmlRootElement(name = "Apps")
public class Apps {

    @XmlElement(name = "App", required = true)
    private List<App> apps;

    /**
     * Gets the value of the apps property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the apps property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApps().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link App }
     * 
     * 
     */
    public List<App> getApps() {
        if (apps == null) {
            apps = new ArrayList<App>();
        }
        return this.apps;
    }

	/**
     * @param apps the apps to set
     */
    public final void setApps(List<App> apps) {
    	this.apps = apps;
    }

}
