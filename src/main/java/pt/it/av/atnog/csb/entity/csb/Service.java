package pt.it.av.atnog.csb.entity.csb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "service")
public class Service {

	@XmlElement(name = "id")
	private String id;

	@XmlElement(name = "vendor_id")
	private String vendorId;
	
	@XmlElement(name = "url")
	private String url;
	
	@XmlElement(name = "username")
	private String username;
	
	@XmlElement(name = "password")
	private String password;

	/**
     * @return the id
     */
    public final String getId() {
    	return id;
    }

	/**
     * @param id the id to set
     */
    public final void setId(String id) {
    	this.id = id;
    }

	/**
     * @return the vendorId
     */
    public final String getVendorId() {
    	return vendorId;
    }

	/**
     * @param vendorId the vendorId to set
     */
    public final void setVendorId(String vendorId) {
    	this.vendorId = vendorId;
    }

	/**
     * @return the url
     */
    public final String getUrl() {
    	return url;
    }

	/**
     * @param url the url to set
     */
    public final void setUrl(String url) {
    	this.url = url;
    }

	/**
     * @return the username
     */
    public final String getUsername() {
    	return username;
    }

	/**
     * @param username the username to set
     */
    public final void setUsername(String username) {
    	this.username = username;
    }

	/**
     * @return the password
     */
    public final String getPassword() {
    	return password;
    }

	/**
     * @param password the password to set
     */
    public final void setPassword(String password) {
    	this.password = password;
    }
	
}
