package models.banner;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import controllers.api.OrderController;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.format.Formats;
import plugins.S3Plugin;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by muneeb on 21/01/17.
 */

@Entity
public class Banner extends Model {

    @Transient
    private static final Logger LOG = LoggerFactory.getLogger(Banner.class);


    @Id
    public long id;

    @Transient
    String BUCKET = "aws.s3.bucket";

    private String getThumbNailName() {
        return "banner" + "/thumbnail/" + fileName;
    }

    public String getThumpNailImage() {
        return config.getString("aws.url") + config.getString(BUCKET)+"/"+getThumbNailName();
    }

    public String name;

    public String fileName;

    @CreatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date createdTime = new Date();


    @UpdatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date updatedTime = new Date();


    @Transient
    Config config = ConfigFactory.load();

    @Inject
    @Transient
    S3Plugin s3Plugin;


    private String getActualFileName() {
        return "banners" + "/" + fileName;
    }

    public String getImageUrl() {
        return config.getString("aws.url") + config.getString("aws.s3.bucket")+"/"+getActualFileName();
    }




    public void setImage(File file) {
        if (s3Plugin.amazonS3 == null) {
            LOG.error("Could not save because amazonS3 was null");
            throw new RuntimeException("Could not save");
        }
        else {
            this.fileName = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), "jpg");
            PutObjectRequest putObjectRequest = new PutObjectRequest(config.getString("aws.s3.bucket"), getActualFileName(), file);
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all
            s3Plugin.amazonS3.putObject(putObjectRequest); // upload file
        }

        //Upload thumb nail

        try {
            String tempFileName = "/tmp/"+fileName+"-thumbnail.jpg";
            Thumbnails.of(file)
                    .size(960, 480)
                    .toFile(new File(tempFileName));
            File thumbFile=new File(tempFileName);
            PutObjectRequest putThumbnailRequest = new PutObjectRequest(config.getString(BUCKET), getThumbNailName(), thumbFile);
            putThumbnailRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all
            s3Plugin.amazonS3.putObject(putThumbnailRequest); // upload file
        }catch (IOException ex) {
            LOG.error("Error {}",ex);
            throw new RuntimeException("Could not create thumbnail");
        }
        file.delete();
    }



    public static Find<Long, Banner> find = new Find<Long, Banner>() {
    };
}
