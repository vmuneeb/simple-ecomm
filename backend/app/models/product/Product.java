package models.product;




import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.format.Formats;
import plugins.S3Plugin;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Entity
public class Product extends Model{

    @Transient
    private static final Logger LOG = LoggerFactory.getLogger(Product.class);


    public String getImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    @Id
    public long id;
    public String name;
    public double price;

    @JsonProperty("discount_price")
    public double discountPrice;

    @JsonProperty("price_formatted")
    public String formattedPrice;

    public String getPrice() {
        return String.format("%.2f", price) ;
    }

    public String getDiscountPrice() {
        return String.format("%.2f", discountPrice) ;
    }

    @JsonProperty("discount_price_formatted")
    public String discountPriceFormatted;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonRawValue(true)
    public Category category;


    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    public SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    public Brand brand;

    public String description;

    @JsonProperty("main_image")
    private String mainImage;

    public boolean featured = false;

    public int quantity;


    @CreatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date createdTime = new Date();

    @UpdatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date updatedTime = new Date();

    @Transient
    Config config = ConfigFactory.load();

    @Transient
    String BUCKET = "aws.s3.bucket";

    @Transient
    private  String ThumpNailImage;

    @Inject
    @Transient
    S3Plugin s3Plugin;


    private String getActualFileName() {
        return "products" + "/" + mainImage;
    }

    private String getThumbNailName() {
        return "products" + "/thumbnail/" + mainImage;
    }

    public String getThumpNailImage() {
        return config.getString("aws.url") + config.getString(BUCKET)+"/"+getThumbNailName();
    }

    public String getMainImage() {
        return config.getString("aws.url") + config.getString(BUCKET)+"/"+getActualFileName();
    }




    public void setImage(File file) {
        String fileName = null;
        if (s3Plugin.amazonS3 == null) {
            LOG.error("Could not save because amazonS3 was null");
            throw new RuntimeException("Could not save");
        }
        else {
            fileName = RandomStringUtils.randomAlphanumeric(8);
            this.mainImage = String.format("%s.%s", fileName, "jpg");
            PutObjectRequest putObjectRequest = new PutObjectRequest(config.getString(BUCKET), getActualFileName(), file);
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all
            s3Plugin.amazonS3.putObject(putObjectRequest); // upload file

            //Upload thumb nail

            try {
                String tempFileName = "/tmp/"+fileName+"-thumbnail.jpg";
                Thumbnails.of(file)
                        .size(500, 500)
                        .toFile(new File(tempFileName));
                File thumbFile=new File(tempFileName);
                PutObjectRequest putThumbnailRequest = new PutObjectRequest(config.getString(BUCKET), getThumbNailName(), thumbFile);
                putThumbnailRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all
                s3Plugin.amazonS3.putObject(putThumbnailRequest); // upload file
            }catch (IOException ex) {
                LOG.error("Error {}",ex);
                throw new RuntimeException("Could not create thumbnail");
            }
        }
        file.delete();
    }


    public static Find<Long, Product> find = new Find<Long, Product>() {
    };
}

