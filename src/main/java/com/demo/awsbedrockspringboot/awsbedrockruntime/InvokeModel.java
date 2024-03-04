package com.demo.awsbedrockspringboot.awsbedrockruntime;

import org.json.JSONArray;
import org.json.JSONObject;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest;
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class InvokeModel {
    public static JSONArray invokeAndGetJSONArray(String modelId, String payload, String fieldName, Boolean async) {
        InvokeModelResponse response = async ?
                invokeAsyncAndGetResponse(modelId, payload) : invokeSyncAndGetResponse(modelId, payload);
        JSONObject responseBody = new JSONObject(response.body().asUtf8String());
        return responseBody.getJSONArray(fieldName);
    }

    public static String invokeAndGetString(String modelId, String payload, String fieldName, Boolean async) {
        InvokeModelResponse response = async ?
                invokeAsyncAndGetResponse(modelId, payload) : invokeSyncAndGetResponse(modelId, payload);
        JSONObject responseBody = new JSONObject(response.body().asUtf8String());
        return responseBody.getString(fieldName);
    }

    private static InvokeModelResponse invokeSyncAndGetResponse(String modelId, String payload) {
        System.out.println("Invoking synchronously");
        InvokeModelRequest request = buildInvokeModelRequest(modelId, payload);

        return getClientSync().invokeModel(request);
    }

    private static InvokeModelResponse invokeAsyncAndGetResponse(String modelId, String payload) {
        System.out.println("Invoking asynchronously");
        InvokeModelRequest request = buildInvokeModelRequest(modelId, payload);

        CompletableFuture<InvokeModelResponse> completableFuture = getClientAsync().invokeModel(request)
                .whenComplete((response, exception) -> {
                    if (exception != null) {
                        System.out.println("Model invocation failed: " + exception);
                    }
                });

        InvokeModelResponse response = null;
        try {
            response = completableFuture.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(e.getMessage());
        } catch (ExecutionException e) {
            System.err.println(e.getMessage());
        }
        return response;
    }

    private static InvokeModelRequest buildInvokeModelRequest(String modelId, String payload) {
        InvokeModelRequest request = InvokeModelRequest.builder()
                .body(SdkBytes.fromUtf8String(payload))
                .modelId(modelId)
                .contentType("application/json")
                .accept("application/json")
                .build();
        return request;
    }

    private static BedrockRuntimeClient getClientSync() {
        return BedrockRuntimeClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

    private static BedrockRuntimeAsyncClient getClientAsync() {
        return BedrockRuntimeAsyncClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

}
