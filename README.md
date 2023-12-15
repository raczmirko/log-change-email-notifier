# What is this LogChangeEmailNotifier?
This is a wireframe that can be further modified for any specific usage. What this application does is it runs and checks if a specific text file has new lines appended to the end, and if it has then it sends an email notification with the new lines as the email's body. It is important that this application is meant to work with log files, meaning that new lines are linearly appended to the file's end, and nothing is ever modified.
# What technology does the application use?
- Java as language
- Spring as the framework
- Maven as package manager
# Configuration
Below you can see how the application can be configured, but since this is not a finished application there might be some modifications needed if for example one wants to change the resources folder.
## Running the application
Below is a step by step guide to running the application.
### First: Sender email settings configuration
Set the `spring.mail.username` and `spring.mail.password` values in the `src/java/resources/application.properties` file. The username is the email you want to send the notifications FROM, and the password has to be an application password generated for this specific use-case. I was personally using GMAIL with a generated application password. You can check out the official Google Account Help guide on app passwords here: [Sign in with app passwords](https://support.google.com/accounts/answer/185833). If you wish to use another email provider's services, then the SMPT settings should be changed too accordingly.
### Second: Create JAR file
First the app has to be packaged into a JAR file, which can be done with the `mvn clean package` Maven command. The command must be exacuted iin the Maven project's library. To navigate to the library you can use the `cd` command (change directory) on all operating systems (MacOS, Linux and Windows).
### Thrid: Create *resources* folder
If you wish to use the app as is, a resources folder must be created in the same directory the JAR file is located at. This can be changed and is located in the *RESOURCE_DIRECTORY* variable of the *TextFileChangeEmailSender* class. However, you must be cautious when using relative paths because they can easily break when generating a JAR from the java classes.
### Fourth: Run the JAR file
To run the JAR file you must follow the following syntax:
```java -jar <jarName> <toEmail> <examinedFilePath>```
The items in *<brackets>* are the following:
- *jarName* is the name of the generated JAR file. This can be changed in the *pom.xml* file, and is **TextFileChangeEmailSender-0.0.1-SNAPSHOT.jar** by default.
- *toEmail* is the email address you wish to receive the notifications at.
- *examinedFilePath* is the file path to the log file that is checked when the app is ran.
Example for running the app:
```java -jar TextFileChangeEmailSender-0.0.1-SNAPSHOT.jar email@email.com F:\Coding\targetFile.txt```
## Further config
This application makes the most sense if it is periodically run automatically, which can be done by using the `crontab -e` command on Linux for example.
