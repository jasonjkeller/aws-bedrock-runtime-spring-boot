package com.demo.awsbedrockspringboot.llm.models;

import com.demo.awsbedrockspringboot.awsbedrockruntime.InvokeModel;
import org.json.JSONObject;

public class Meta {
    //  * Meta
    //    * Llama 2 Chat 13B (meta.llama2-13b-chat-v1)
    //    * Llama 2 Chat 70B (meta.llama2-70b-chat-v1)
    //    * Llama 2 13B (?)
    //    * Llama 2 70B (?)
    public static final String LLAMA_2_CHAT_13_B = "meta.llama2-13b-chat-v1";
    public static final String LLAMA_2_CHAT_70_B = "meta.llama2-70b-chat-v1";
    public static final String LLAMA_2_13_B = "?"; // FIXME
    public static final String LLAMA_2_70_B = "??"; // FIXME

    public static boolean isMetaModel(String modelId) {
        return LLAMA_2_CHAT_13_B.equals(modelId) || LLAMA_2_CHAT_70_B.equals(modelId) || LLAMA_2_13_B.equals(modelId) || LLAMA_2_70_B.equals(modelId);
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
        } else if (LLAMA_2_13_B.equals(modelId)) {
            return invokeLlama213B(modelId, prompt, async);
        } else if (LLAMA_2_70_B.equals(modelId)) {
            return invokeLlama270B(modelId, prompt, async);
        }
        return "Unsupported Meta Model: " + modelId;
    }

    public static String invokeLlama2Chat13B(String modelId, String prompt, Boolean async) {
        return invokeMetaGeneric(modelId, prompt, async);
    }

    public static String invokeLlama2Chat70B(String modelId, String prompt, Boolean async) {
        return invokeMetaGeneric(modelId, prompt, async);
    }

    public static String invokeLlama213B(String modelId, String prompt, Boolean async) {
        // FIXME implement
        return invokeMetaGeneric(modelId, prompt, async);
    }

    public static String invokeLlama270B(String modelId, String prompt, Boolean async) {
        // FIXME implement
        return invokeMetaGeneric(modelId, prompt, async);
    }

    public static String invokeMetaGeneric(String modelId, String prompt, Boolean async) {
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

        return InvokeModel.invokeAndGetString(modelId, payload, "generation", async);
    }
}
