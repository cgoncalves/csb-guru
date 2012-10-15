package pt.it.av.atnog.csb.entity.csb;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */

@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "", propOrder = { "rules", "provider" })
@XmlRootElement(name = "manifest")
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class Manifest {

	@XmlElementWrapper(name = "rules")
	@XmlElement(name = "rule")
	@JsonProperty("rules")
	private List<Rule> rules;

	@XmlElement(name = "provider")
	@JsonProperty("provider")
	private String provider;

	/**
	 * @return the rules
	 */
	public List<Rule> getRules() {
		return rules;
	}

	/**
	 * @param rules
	 *            the rules to set
	 */
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @param provider
	 *            the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getFramework() {
		for (Rule r : rules)
			if (r.getName().equals("framework"))
				return r.getParams().get(0);
		return null;
	}

}
