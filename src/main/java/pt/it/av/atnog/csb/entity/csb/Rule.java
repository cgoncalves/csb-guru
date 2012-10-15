package pt.it.av.atnog.csb.entity.csb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "name", "params" })
@XmlRootElement(name = "rule")
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class Rule {

	@XmlElement(name = "name")
	@JsonProperty("name")
	private String name;

	@XmlTransient
	private List<String> providersIn;

	@XmlTransient
	private List<String> providersOut;

	@XmlElementWrapper(name = "params")
	@XmlElement(name = "param")
	@JsonProperty("params")
	private List<String> params;

	public Rule() {
	}

	/**
	 * @param name
	 */
	public Rule(String name) {
		this.name = name;
		this.providersIn = new ArrayList<String>();
		this.providersOut = new ArrayList<String>();
		this.params = new ArrayList<String>();
	}

	/**
	 * @param name
	 * @param providersIn
	 */
	public Rule(String name, List<String> providersIn) {
		this(name);
		this.providersIn = providersIn;
	}

	/**
	 * @param name
	 * @param params
	 */
	public Rule(String name, String... params) {
		this(name);
		this.params = Arrays.asList(params);
	}

	/**
	 * @param name
	 * @param providersIn
	 * @param params
	 */
	public Rule(String name, List<String> providersIn, String... params) {
		this(name, providersIn);
		this.params = Arrays.asList(params);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the providersIn
	 */
	public List<String> getProvidersIn() {
		return providersIn;
	}

	/**
	 * @param providersIn
	 *            the providersIn to set
	 */
	public void setProvidersIn(List<String> providersIn) {
		this.providersIn = providersIn;
	}

	/**
	 * @return the providersOut
	 */
	public List<String> getProvidersOut() {
		return providersOut;
	}

	/**
	 * @return the params
	 */
	public List<String> getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(List<String> params) {
		this.params = params;
	}
}
