package controllers;

import org.apache.http.HttpStatus;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;


/**
 * Created by muneeb on 07/01/17.
 */
public class OrderController extends Controller {

    public Result createOrder() {

        return Results.status(HttpStatus.SC_NOT_IMPLEMENTED);
    }

    public Result getOrders() {

        return Results.status(HttpStatus.SC_NOT_IMPLEMENTED);
    }
}

