package controllers.portal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

/**
 * Created by muneeb on 30/01/17.
 */
public class HomeController extends Controller {
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    public Result index() {
        return Results.ok("index");
    }
}

