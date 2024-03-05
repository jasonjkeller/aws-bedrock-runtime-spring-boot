package com.demo.awsbedrockspringboot.llm.models;

import com.demo.awsbedrockspringboot.awsbedrockruntime.InvokeModel;
import org.json.JSONObject;

import java.util.List;

import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.MAX_TOKENS;

public class Amazon {
    //  * Amazon
    //    * Titan Embeddings G1 - Text (amazon.titan-embed-text-v1)
    //    * Titan Text G1 - Lite (amazon.titan-text-lite-v1)
    //    * Titan Text G1 - Express (amazon.titan-text-express-v1)
    //    * Titan Multimodal Embeddings G1 (amazon.titan-embed-image-v1)
    public static final String TITAN_EMBEDDINGS_G1_TEXT = "amazon.titan-embed-text-v1";
    public static final String TITAN_TEXT_G1_LITE = "amazon.titan-text-lite-v1";
    public static final String TITAN_TEXT_G1_EXPRESS = "amazon.titan-text-express-v1";
    public static final String TITAN_MULTIMODAL_EMBEDDINGS_G1 = "amazon.titan-embed-image-v1";
    public static final String TITAN_IMAGE_GENERATOR_G1 = "amazon.titan-image-generator-v1";

    public static boolean isAmazonModel(String modelId) {
        return TITAN_EMBEDDINGS_G1_TEXT.equals(modelId) || TITAN_TEXT_G1_LITE.equals(modelId) || TITAN_TEXT_G1_EXPRESS.equals(modelId) ||
                TITAN_MULTIMODAL_EMBEDDINGS_G1.equals(modelId) || TITAN_IMAGE_GENERATOR_G1.equals(modelId);
    }

    public static String invokeAmazonModel(String modelId, String prompt, long seed, Boolean async) {
        if (TITAN_IMAGE_GENERATOR_G1.equals(modelId)) {
            return invokeTitanImageGeneratorG1(prompt, seed, async);
        } else if (TITAN_EMBEDDINGS_G1_TEXT.equals(modelId)) {
            return invokeTitanEmbeddingsG1Text(prompt, async);
        } else if (TITAN_MULTIMODAL_EMBEDDINGS_G1.equals(modelId)) {
            return invokeTitanMultimodalEmbeddingsG1(prompt, async);
        } else if (TITAN_TEXT_G1_LITE.equals(modelId)) {
            return invokeTitanTextG1Lite(modelId, prompt, async);
        } else if (TITAN_TEXT_G1_EXPRESS.equals(modelId)) {
            return invokeTitanTextG1Express(modelId, prompt, async);
        }
        return "Unsupported Amazon Model: " + modelId;
    }

    /**
     * Invokes the Amazon Titan image generation model to create an image using the input
     * provided in the request body.
     *
     * @param prompt The prompt that you want Amazon Titan to use for image generation.
     * @param seed   The random noise seed for image generation (Range: 0 to 2147483647).
     * @return A Base64-encoded string representing the generated image.
     */
    public static String invokeTitanImageGeneratorG1(String prompt, long seed, Boolean async) {
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

        return InvokeModel.invokeAndGetJSONArray(Amazon.TITAN_IMAGE_GENERATOR_G1, payload, "images", async)
                .getString(0);
    }

    /**
     * Invokes the Amazon Titan embed text generation model to run an inference based on the provided input.
     *
     * @param prompt The prompt that you want Amazon Titan to use for embed generation.
     * @return The generated response.
     */
    public static String invokeTitanEmbeddingsG1Text(String prompt, Boolean async) {
        /*
         The different model providers have individual request and response formats.
         For the format, ranges, and default values for Titan Embed Text models refer to:
         https://docs.aws.amazon.com/bedrock/latest/userguide/titan-embedding-models.html
         https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-titan-embed-text.html
        */

        String payload = new JSONObject()
                .put("inputText", prompt)
                .toString();

        return InvokeModel.invokeAndGetJSONArray(Amazon.TITAN_EMBEDDINGS_G1_TEXT, payload, "embedding", async)
                .toList().toString();
    }

    /**
     * Invokes the Amazon Titan embed image generation model to create an image using the input
     * provided in the request body.
     *
     * @param prompt The prompt that you want Amazon Titan to use for image generation.
     * @return A Base64-encoded string representing the generated image.
     */
    public static String invokeTitanMultimodalEmbeddingsG1(String prompt, Boolean async) {
        /*
         The different model providers have individual request and response formats.
         For the format, ranges, and default values for Titan Embed Image models refer to:
         https://docs.aws.amazon.com/bedrock/latest/userguide/titan-multiemb-models.html
         https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-titan-embed-mm.html
        */

        JSONObject embeddingConfig = new JSONObject()
                .put("outputEmbeddingLength", 256);  // 256, 384 or 1024

        String payload = new JSONObject()
                .put("inputText", prompt)
                //.put("inputImage", null)
                .put("embeddingConfig", embeddingConfig)
                .toString();

        return InvokeModel.invokeAndGetJSONArray(Amazon.TITAN_MULTIMODAL_EMBEDDINGS_G1, payload, "embedding", async)
                .toList().toString();
    }

    public static String invokeTitanTextG1Lite(String modelId, String prompt, Boolean async) {
        return invokeTitanTextModels(modelId, prompt, async);
    }

    public static String invokeTitanTextG1Express(String modelId, String prompt, Boolean async) {
        return invokeTitanTextModels(modelId, prompt, async);
    }

    public static String invokeTitanTextModels(String modelId, String prompt, Boolean async) {
        /*
         The different model providers have individual request and response formats.
         For the format, ranges, and default values for Titan Embed Image models refer to:
         https://docs.aws.amazon.com/bedrock/latest/userguide/titan-text-models.html
         https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-titan-text.html
        */

        JSONObject textGenerationConfig = new JSONObject()
                .put("maxTokenCount", MAX_TOKENS)
                .put("stopSequences", List.of("User:"))
                .put("temperature", 0.5)
                .put("topP", 0.9);

        String payload = new JSONObject()
                .put("inputText", prompt)
                .put("textGenerationConfig", textGenerationConfig)
                .toString();

        return InvokeModel.invokeAndGetJSONArray(modelId, payload, "results", async).getJSONObject(0).getString("outputText");
    }
}
