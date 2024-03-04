package com.demo.awsbedrockspringboot.llm.models;

import com.demo.awsbedrockspringboot.awsbedrockruntime.InvokeModel;
import org.json.JSONArray;
import org.json.JSONObject;

public class StabilityAI {
    //  * Stability AI
    //    * SDXL 1.0 (stability.stable-diffusion-xl-v1)
    public static final String SDXL_V_1_0 = "stability.stable-diffusion-xl-v1";

    public static boolean isStabilityAiModel(String modelId) {
        return SDXL_V_1_0.equals(modelId);
    }

    /**
     * Invokes the Stability.ai Stable Diffusion XL model to create an image based on the provided input.
     *
     * @param prompt      The prompt that guides the Stable Diffusion model.
     * @param seed        The random noise seed for image generation (use 0 or omit for a random seed).
     * @param stylePreset The style preset to guide the image model towards a specific style.
     * @return A Base64-encoded string representing the generated image.
     */
    public static String invokeStabilityAiModel(String modelId, String prompt, long seed, String stylePreset, Boolean async) {
        if (SDXL_V_1_0.equals(modelId)) {
            return invokeSDXL1(modelId, prompt, seed, stylePreset, async);
        }
        return "Unsupported StabilityAI Model: " + modelId;
    }

    public static String invokeSDXL1(String modelId, String prompt, long seed, String stylePreset, Boolean async) {
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

        return InvokeModel.invokeAndGetJSONArray(modelId, payload.toString(), "artifacts", async)
                .getJSONObject(0).getString("base64");
    }
}
