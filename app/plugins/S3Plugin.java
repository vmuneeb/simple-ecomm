package plugins;

/**
 * Created by muneeb on 27/03/17.
 */
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.LoggerFactory;
import play.Logger;



@Singleton
public class S3Plugin {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(S3Plugin.class);

    public static AmazonS3 amazonS3;


    @Inject
    public  S3Plugin() {
        LOG.info("Initialising S3Plugin");
        amazonS3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .build();
        if(amazonS3 == null )
            Logger.info("Failed.. amazonS3 is null");
        else
            Logger.info("S3 initialised successfully");
    }

}