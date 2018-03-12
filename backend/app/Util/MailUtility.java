package Util;

/**
 * Created by muneeb on 20/03/17.
 */
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import models.order.Order;
import models.user.ResetPassWord;
import models.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.mailer.Attachment;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import javax.inject.Inject;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MailUtility {

    private static final Logger LOG = LoggerFactory.getLogger(MailUtility.class);

    final ActorRef mailActor;


    @Inject
    public MailUtility(ActorSystem system,MailerClient mailerClient) {
        mailActor = system.actorOf(MailActor.props(mailerClient));
    }

    public void sendEmail() {
        Email email = new Email()
                .setSubject("Simple email")
                .setFrom("info@zxer.in")
                .addTo("muneeb.v@ril.com")
                // adds attachment
                //.addAttachment("attachment.pdf", new File("/some/path/attachment.pdf"))
                // adds inline attachment from byte array
                //.addAttachment("data.txt", "data".getBytes(), "text/plain", "Simple data", EmailAttachment.INLINE)
                // adds cid attachment
                //.addAttachment("image.jpg", new File("/some/path/image.jpg"), cid)
                // sends text, HTML or both...
                .setBodyText("This is a sample message")
                .setBodyHtml("<html><body><p>An <b>html</b> message with cid <img src='http://zxer.in/content/images/2016/11/Simple-tcp-actor.png'></p></body></html>");
        mailActor.tell(email,ActorRef.noSender());
    }

    public void sendPassWordResetEmail(ResetPassWord resetPassWord) {
        if(resetPassWord  != null ) {
            Email email = new Email()
                    .setSubject("Reset your password")
                    .setFrom("Fortune Stationery<noreply@fortunestationery.com>")
                    .addTo(resetPassWord.user.email)
                    .setBodyText("This email was sent automatically by fortune stationery in response to your request to reset your password." +
                            "To reset your password and access your account, either click on the button or copy and paste the following link (expires in 24 hours) into the address bar of your browser:\n" +
                            "http://fortunestationery.com/reset?email="+resetPassWord.user.email+"&token="+resetPassWord.token);
            mailActor.tell(email,ActorRef.noSender());
        }
    }

    public void sendWelcomeMail(User user) {
            String body = views.html.admin.email.welcome.render(user).body();
            Email email = new Email()
                    .setSubject("Hi "+user.name+", Welcome to Fortune Stationery!!")
                    .setFrom("Fortune Stationery<noreply@fortunestationery.com>")
                   // .addTo(user.email)
                    .addTo(user.email)
                    .setBodyHtml(body);
        mailActor.tell(email,ActorRef.noSender());
    }

    public void sendInvoice(Order order) {
        List<Attachment> attachmentList = new LinkedList<>();
        String fileName = PdfUtility.getInvoice(order);
        File invoice = new File(fileName);
        Email email = new Email()
                .setSubject("Hi "+order.user.name+", Your order from fortune stationery has been confirmed")
                .setFrom("Fortune Stationery<order-update@fortunestationery.com>")
                .addTo(order.user.email)
                .setBodyHtml("Please find your order invoice attached.")
                .addAttachment("attachment.pdf", invoice);
        mailActor.tell(email,ActorRef.noSender());
    }
}