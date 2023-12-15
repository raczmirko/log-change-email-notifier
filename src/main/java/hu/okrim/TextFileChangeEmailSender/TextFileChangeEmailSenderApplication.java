package hu.okrim.TextFileChangeEmailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TextFileChangeEmailSenderApplication implements CommandLineRunner {
	@Autowired
	private EmailSenderService senderService;

	public static void main(String[] args) {
		SpringApplication.run(TextFileChangeEmailSenderApplication.class, args);
	}
@Override
public void run(String... args) {
	if (args.length == 0) {
		System.out.println("Usage: java -jar your-jar-file.jar <toEmail>");
	} else {
		String toEmail = args[0];
		senderService.sendMail(toEmail, "Test", "This is a test mail.");
	}
}
}
