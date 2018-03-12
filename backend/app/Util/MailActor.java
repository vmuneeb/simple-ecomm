package Util;


import akka.actor.Props;
import akka.actor.UntypedActor;
import controllers.api.OrderController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

/**
 * Created by muneeb on 06/04/17.
 */
public class MailActor extends UntypedActor{

    private static final Logger LOG = LoggerFactory.getLogger(MailActor.class);

    public static Props props(MailerClient mailerClient) {
        return Props.create(MailActor.class, mailerClient);
    }

    private final MailerClient mailerClient;

    public MailActor(MailerClient mailerClient) {
        this.mailerClient = mailerClient;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Email) {
            LOG.info("Sending mail");
            mailerClient.send((Email)message);
        }
    }
}
