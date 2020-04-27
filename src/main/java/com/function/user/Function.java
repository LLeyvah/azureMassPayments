package com.function.user;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.google.gson.Gson;
import com.microsoft.azure.functions.*;
import com.microsoft.azure.servicebus.TopicClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import java.util.concurrent.CompletableFuture;
import com.microsoft.azure.servicebus.Message;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it
     * using "curl" command in bash: 1. curl -d "HTTP Body" {your
     * host}/api/HttpExample 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */ //  UseDevelopmentStorage=true
    //Escribiendo un mensaje en cola
    //Se aprovicionan un ambiente de function en mi laptop
    //Manda un mensaje al bus de servicio ya se proceso en cola
    private String connectionString = "Endpoint=sb://masspayments.servicebus.windows.net/;SharedAccessKeyName=send;SharedAccessKey=o3cXU+g5JGFOvooa/wJzp7SfywIp5wr4kzWZG0ok/bs=;EntityPath=topic-masspayments";
    static final Gson GSON = new Gson();

    @FunctionName("HttpTriggerJava")
    public HttpResponseMessage HttpTriggerJava(@HttpTrigger(name = "req", methods = {
        HttpMethod.POST }, authLevel = AuthorizationLevel.FUNCTION, route = "{contractId}/user") HttpRequestMessage<Optional<String>> request,
        final ExecutionContext context) throws InterruptedException, ServiceBusException {
        context.getLogger().info("Java HTTP trigger processed a request.");

        String appcode = request.getHeaders().get("app-code");
        String query = request.getQueryParameters().get("name");
        String name = request.getBody().orElse(query);
        context.getLogger().info("appcode, " + appcode);
        context.getLogger().info("Query, " + query);
        context.getLogger().info("Name, " + name);
        
        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Please pass a name on the query string or in the request body").build();
        } else {
            context.getLogger().info("connectionString, " + connectionString);
            TopicClient sendClient = new TopicClient(new ConnectionStringBuilder(connectionString, "topic-masspayments"));
            sendMessagesAsync(sendClient, name).thenRunAsync(() -> sendClient.closeAsync());
            sendClient.close();
            return request.createResponseBuilder(HttpStatus.OK).body(name).build();
        }

    }

    CompletableFuture<Void> sendMessagesAsync(TopicClient sendClient2, String name) {
        List<CompletableFuture> tasks = new ArrayList<>();
        Message message = new Message(GSON.toJson(name).getBytes());
        tasks.add(sendClient2.sendAsync(message).thenRunAsync(() -> {
            System.out.printf("\n\tMessage acknowledged: Id = %s", message.getMessageId());
        }));

        return CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[tasks.size()]));
    }
}
