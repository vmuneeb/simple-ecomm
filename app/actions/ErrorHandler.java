package actions;

import play.http.HttpErrorHandler;
import play.mvc.*;
import play.mvc.Http.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Singleton;

@Singleton
public class ErrorHandler implements HttpErrorHandler {
    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
        System.out.println(message);
        return CompletableFuture.completedFuture(
                Results.status(statusCode, "Error")
        );
    }

    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        exception.printStackTrace();
        return CompletableFuture.completedFuture(
                Results.internalServerError("Something went wrong")
        );
    }
}
