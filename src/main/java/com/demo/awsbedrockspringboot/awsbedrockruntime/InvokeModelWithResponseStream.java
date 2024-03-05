package com.demo.awsbedrockspringboot.awsbedrockruntime;

import org.json.JSONObject;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelWithResponseStreamResponseHandler;

import java.util.concurrent.atomic.AtomicReference;

public class InvokeModelWithResponseStream {

    /**
     * Invokes the Anthropic Claude 2 model and processes the response stream.
     *
     * @param prompt The prompt for Claude to complete.
     * @param silent Suppress console output of the individual response stream chunks.
     * @return The generated response.
     */
    public static String invokeClaude(String prompt, boolean silent) {

        BedrockRuntimeAsyncClient client = BedrockRuntimeAsyncClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        var finalCompletion = new AtomicReference<>("");

        var payload = new JSONObject()
                .put("prompt", "Human: " + prompt + " Assistant:")
                .put("temperature", 0.5)
                .put("max_tokens_to_sample", 300)
                .toString();

        var request = InvokeModelWithResponseStreamRequest.builder()
                .body(SdkBytes.fromUtf8String(payload))
                .modelId("anthropic.claude-v2")
                .contentType("application/json")
                .accept("application/json")
                .build();

        System.out.print("Generated text:");

        var visitor = InvokeModelWithResponseStreamResponseHandler.Visitor.builder()
                .onChunk(chunk -> {
                    var json = new JSONObject(chunk.bytes().asUtf8String());
                    var completion = json.getString("completion");
                    finalCompletion.set(finalCompletion.get() + completion);
                    if (!silent) {
                        System.out.print(completion);
                    }
                })
                .build();

        var handler = InvokeModelWithResponseStreamResponseHandler.builder()
                .onEventStream(stream -> stream.subscribe(event -> event.accept(visitor)))
                .onComplete(() -> {
                })
                .onError(e -> System.out.println("\n\nError: " + e.getMessage()))
                .build();

        client.invokeModelWithResponseStream(request, handler).join();

        return finalCompletion.get();
    }

}
