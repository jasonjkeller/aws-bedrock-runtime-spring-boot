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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class InvokeModel {

    /**
     * Invokes the Anthropic Claude 2 model to run an inference based on the provided input.
     *
     * @param prompt The prompt for Claude to complete.
     * @return The generated response.
     */
    public static String invokeClaude(String prompt, Boolean async) {
        /*
          The different model providers have individual request and response formats.
          For the format, ranges, and default values for Anthropic Claude, refer to:
          https://docs.anthropic.com/claude/reference/complete_post
         */

        // Claude requires you to enclose the prompt as follows:
        String enclosedPrompt = "Human: " + prompt + "\n\nAssistant:";

        String payload = new JSONObject()
                .put("prompt", enclosedPrompt)
                .put("max_tokens_to_sample", 200)
                .put("temperature", 0.5)
                .put("stop_sequences", List.of("\n\nHuman:"))
                .toString();

        return invokeAndGetString(AwsBedrockRuntimeHelper.CLAUDE, payload, "completion", async);
    }

    /**
     * Invokes the AI21 Labs Jurassic-2 model to run an inference based on the provided input.
     *
     * @param prompt The prompt for Jurassic to complete.
     * @return The generated response.
     */
    public static String invokeJurassic2(String prompt, Boolean async) {
        /*
          The different model providers have individual request and response formats.
          For the format, ranges, and default values for AI21 Labs Jurassic-2, refer to:
          https://docs.ai21.com/reference/j2-complete-ref
         */

        String payload = new JSONObject()
                .put("prompt", prompt)
                .put("temperature", 0.5)
                .put("maxTokens", 200)
                .toString();

        return invokeAndGetJSONArray(AwsBedrockRuntimeHelper.JURASSIC2, payload, "completions", async)
                .getJSONObject(0).getJSONObject("data").getString("text");
    }

    /**
     * Invokes the Meta Llama 2 Chat model to run an inference based on the provided input.
     *
     * @param prompt The prompt for Llama 2 to complete.
     * @return The generated response.
     */
    public static String invokeLlama2(String prompt, Boolean async) {
        /*
          The different model providers have individual request and response formats.
          For the format, ranges, and default values for Meta Llama 2 Chat, refer to:
          https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-meta.html
         */

        String payload = new JSONObject()
                .put("prompt", prompt)
                .put("max_gen_len", 512)
                .put("temperature", 0.5)
                .put("top_p", 0.9)
                .toString();

        return invokeAndGetString(AwsBedrockRuntimeHelper.LLAMA2, payload, "generation", async);
    }

    /**
     * Invokes the Stability.ai Stable Diffusion XL model to create an image based on the provided input.
     *
     * @param prompt      The prompt that guides the Stable Diffusion model.
     * @param seed        The random noise seed for image generation (use 0 or omit for a random seed).
     * @param stylePreset The style preset to guide the image model towards a specific style.
     * @return A Base64-encoded string representing the generated image.
     */
    public static String invokeStableDiffusion(String prompt, long seed, String stylePreset, Boolean async) {
        /*
          The different model providers have individual request and response formats.
          For the format, ranges, and available style_presets of Stable Diffusion models refer to:
          https://platform.stability.ai/docs/api-reference#tag/v1generation
         */

        JSONArray wrappedPrompt = new JSONArray().put(new JSONObject().put("text", prompt));

        JSONObject payload = new JSONObject()
                .put("text_prompts", wrappedPrompt)
                .put("seed", seed);

        if (!(stylePreset == null || stylePreset.isEmpty())) {
            payload.put("style_preset", stylePreset);
        }

        return invokeAndGetJSONArray(AwsBedrockRuntimeHelper.STABLE_DIFFUSION, payload.toString(), "artifacts", async)
                .getJSONObject(0).getString("base64");
    }

    /**
     * Invokes the Amazon Titan image generation model to create an image using the input
     * provided in the request body.
     *
     * @param prompt The prompt that you want Amazon Titan to use for image generation.
     * @param seed   The random noise seed for image generation (Range: 0 to 2147483647).
     * @return A Base64-encoded string representing the generated image.
     */
    public static String invokeTitanImage(String prompt, long seed, Boolean async) {
        /*
         The different model providers have individual request and response formats.
         For the format, ranges, and default values for Titan Image models refer to:
         https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-titan-image.html
        */

        var textToImageParams = new JSONObject().put("text", prompt);

        var imageGenerationConfig = new JSONObject()
                .put("numberOfImages", 1)
                .put("quality", "standard")
                .put("cfgScale", 8.0)
                .put("height", 512)
                .put("width", 512)
                .put("seed", seed);

        String payload = new JSONObject()
                .put("taskType", "TEXT_IMAGE")
                .put("textToImageParams", textToImageParams)
                .put("imageGenerationConfig", imageGenerationConfig)
                .toString();

        return invokeAndGetJSONArray(AwsBedrockRuntimeHelper.TITAN_IMAGE, payload, "images", async)
                .getString(0);
    }

    /**
     * Invokes the Amazon Titan embed text generation model to run an inference based on the provided input.
     *
     * @param prompt The prompt that you want Amazon Titan to use for embed generation.
     * @return The generated response.
     */
    public static String invokeTitanEmbedText(String prompt, Boolean async) {
        /*
         The different model providers have individual request and response formats.
         For the format, ranges, and default values for Titan Embed Text models refer to:
         https://docs.aws.amazon.com/bedrock/latest/userguide/titan-embedding-models.html
        */

        String payload = new JSONObject()
                .put("inputText", prompt)
                .toString();

        return invokeAndGetJSONArray(AwsBedrockRuntimeHelper.TITAN_EMBED_TEXT, payload, "embedding", async)
                .toList().toString();
    }

    /**
     * Invokes the Amazon Titan embed image generation model to create an image using the input
     * provided in the request body.
     *
     * @param prompt The prompt that you want Amazon Titan to use for image generation.
     * @return A Base64-encoded string representing the generated image.
     */
    public static String invokeTitanEmbedImage(String prompt, Boolean async) {
        /*
         The different model providers have individual request and response formats.
         For the format, ranges, and default values for Titan Embed Image models refer to:
         https://docs.aws.amazon.com/bedrock/latest/userguide/titan-multiemb-models.html
        */

        JSONObject embeddingConfig = new JSONObject()
                .put("outputEmbeddingLength", 256);  // 256, 384 or 1024

        String payload = new JSONObject()
                .put("inputText", prompt)
                //.put("inputImage", null)
                .put("embeddingConfig", embeddingConfig)
                .toString();

        return invokeAndGetJSONArray(AwsBedrockRuntimeHelper.TITAN_EMBED_IMAGE, payload, "embedding", async)
                .toList().toString();
    }

    private static JSONArray invokeAndGetJSONArray(String modelId, String payload, String fieldName, Boolean async) {
        InvokeModelResponse response = async ?
                invokeAsyncAndGetResponse(modelId, payload) : invokeSyncAndGetResponse(modelId, payload);
        JSONObject responseBody = new JSONObject(response.body().asUtf8String());
        return responseBody.getJSONArray(fieldName);
    }

    private static String invokeAndGetString(String modelId, String payload, String fieldName, Boolean async) {
        InvokeModelResponse response = async ?
                invokeAsyncAndGetResponse(modelId, payload) : invokeSyncAndGetResponse(modelId, payload);
        JSONObject responseBody = new JSONObject(response.body().asUtf8String());
        return responseBody.getString(fieldName);
    }

    private static InvokeModelResponse invokeSyncAndGetResponse(String modelId, String payload) {
        System.out.println("Invoking synchronously");
        InvokeModelRequest request = InvokeModelRequest.builder()
                .body(SdkBytes.fromUtf8String(payload))
                .modelId(modelId)
                .contentType("application/json")
                .accept("application/json")
                .build();

        return getClientSync().invokeModel(request);
    }

    private static BedrockRuntimeClient getClientSync() {
        return BedrockRuntimeClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

    private static InvokeModelResponse invokeAsyncAndGetResponse(String modelId, String payload) {
        System.out.println("Invoking asynchronously");
        InvokeModelRequest request = InvokeModelRequest.builder()
                .body(SdkBytes.fromUtf8String(payload))
                .modelId(modelId)
                .contentType("application/json")
                .accept("application/json")
                .build();

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

    private static BedrockRuntimeAsyncClient getClientAsync() {
        return BedrockRuntimeAsyncClient.builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

}
