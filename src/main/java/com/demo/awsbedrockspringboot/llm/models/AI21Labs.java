package com.demo.awsbedrockspringboot.llm.models;

import com.demo.awsbedrockspringboot.awsbedrockruntime.InvokeModel;
import org.json.JSONObject;

import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.MAX_TOKENS;

public class AI21Labs {
    //  * AI21 Labs
    //    * Jurassic-2 Ultra (ai21.j2-ultra-v1)
    //    * Jurassic-2 Mid (ai21.j2-mid-v1)
    public static final String JURASSIC_2_ULTRA_V1 = "ai21.j2-ultra-v1";
    public static final String JURASSIC_2_MID_V1 = "ai21.j2-mid-v1";

    public static boolean isAI21LabsModel(String modelId) {
        return JURASSIC_2_ULTRA_V1.equals(modelId) || JURASSIC_2_MID_V1.equals(modelId);
    }

    /**
     * Invokes the AI21 Labs Jurassic-2 model to run an inference based on the provided input.
     *
     * @param prompt The prompt for Jurassic to complete.
     * @return The generated response.
     */
    public static String invokeAI21LabsModel(String modelId, String prompt, Boolean async) {
        if (JURASSIC_2_ULTRA_V1.equals(modelId)) {
            return invokeJurassic2UltraV1(modelId, prompt, async);
        } else if (JURASSIC_2_MID_V1.equals(modelId)) {
            return invokeJurassic2MidV1(modelId, prompt, async);
        }
        return "Unsupported AI21Labs Model: " + modelId;
    }

    private static String invokeJurassic2UltraV1(String modelId, String prompt, Boolean async) {
        return invokeJurassicGeneric(modelId, prompt, async);
    }

    private static String invokeJurassic2MidV1(String modelId, String prompt, Boolean async) {
        return invokeJurassicGeneric(modelId, prompt, async);
    }

    private static String invokeJurassicGeneric(String modelId, String prompt, Boolean async) {
        /*
          The different model providers have individual request and response formats.
          For the format, ranges, and default values for AI21 Labs Jurassic-2, refer to:
          https://docs.ai21.com/reference/j2-complete-ref
         */

        String payload = new JSONObject()
                .put("prompt", prompt)
                .put("temperature", 0.5)
                .put("maxTokens", MAX_TOKENS)
                .toString();

        return InvokeModel.invokeAndGetJSONArray(modelId, payload, "completions", async)
                .getJSONObject(0).getJSONObject("data").getString("text");
    }
}
