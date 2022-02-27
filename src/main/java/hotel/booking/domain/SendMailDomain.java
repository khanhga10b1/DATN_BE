
package hotel.booking.domain;

import java.util.List;


public class SendMailDomain{


	private List<String> toEmail;
	private String subject;

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


}
