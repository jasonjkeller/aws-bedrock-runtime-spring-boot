package com.demo.awsbedrockspringboot.llm.models;

import com.demo.awsbedrockspringboot.awsbedrockruntime.InvokeModel;
import org.json.JSONObject;

import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.MAX_TOKENS;

public class Meta {
    //  * Meta
    //    * Llama 2 Chat 13B (meta.llama2-13b-chat-v1)
    //    * Llama 2 Chat 70B (meta.llama2-70b-chat-v1)
    public static final String LLAMA_2_CHAT_13_B = "meta.llama2-13b-chat-v1";
    public static final String LLAMA_2_CHAT_70_B = "meta.llama2-70b-chat-v1";

    public static boolean isMetaModel(String modelId) {
        return LLAMA_2_CHAT_13_B.equals(modelId) || LLAMA_2_CHAT_70_B.equals(modelId);
    }

    /**
     * Invokes the Meta Llama 2 Chat model to run an inference based on the provided input.
     *
     * @param prompt The prompt for Llama 2 to complete.
     * @return The generated response.
     */
    public static String invokeMetaModel(String modelId, String prompt, Boolean async) {
        if (LLAMA_2_CHAT_13_B.equals(modelId)) {
            return invokeLlama2Chat13B(modelId, prompt, async);
        } else if (LLAMA_2_CHAT_70_B.equals(modelId)) {
            return invokeLlama2Chat70B(modelId, prompt, async);
        }
        return "Unsupported Meta Model: " + modelId;
    }

    public static String invokeLlama2Chat13B(String modelId, String prompt, Boolean async) {
        return invokeLlama2ChatModels(modelId, prompt, async);
    }

    public static String invokeLlama2Chat70B(String modelId, String prompt, Boolean async) {
        return invokeLlama2ChatModels(modelId, prompt, async);
    }

    public static String invokeLlama2ChatModels(String modelId, String prompt, Boolean async) {
        /*
          The different model providers have individual request and response formats.
          For the format, ranges, and default values for Meta Llama 2 Chat, refer to:
          https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-meta.html
         */

        String payload = new JSONObject()
                .put("prompt", prompt)
                .put("max_gen_len", MAX_TOKENS)
                .put("temperature", 0.5)
                .put("top_p", 0.9)
                .toString();

        return InvokeModel.invokeAndGetString(modelId, payload, "generation", async);
    }
}
