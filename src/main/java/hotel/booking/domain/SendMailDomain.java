
package hotel.booking.domain;

import java.util.List;
import java.util.Map;


public class SendMailDomain{


	private List<String> toEmail;
	private String subject;
	private String text;
	private Map<String, Object> params;
	private String temp;

	public List<String> getToEmail() {
		return toEmail;
	}

	public void setToEmail(List<String> toEmail) {
		this.toEmail = toEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	@Override
	public String toString() {
		return "SendMailDomain{" +
				"toEmail=" + toEmail +
				", subject='" + subject + '\'' +
				", text='" + text + '\'' +
				", params=" + params +
				", temp='" + temp + '\'' +
				'}';
	}
}
