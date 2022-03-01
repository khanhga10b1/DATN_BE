
package hotel.booking.domain;

import java.util.List;


public class SendMailDomain{


	private List<String> toEmail;
	private String subject;
	private String text;

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
}
