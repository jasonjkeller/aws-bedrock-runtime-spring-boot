package com.demo.awsbedrockspringboot.llm.models;

import com.demo.awsbedrockspringboot.awsbedrockruntime.InvokeModel;
import org.json.JSONObject;

import java.util.List;

import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.MAX_TOKENS;

public class Cohere {
    //  * Cohere
    //    * Command (cohere.command-text-v14)
    //    * Command Light (cohere.command-light-text-v14)
    //    * Embed English (cohere.embed-english-v3)
    //    * Embed Multilingual (cohere.embed-multilingual-v3)
    public static final String COMMAND = "cohere.command-text-v14";
    public static final String COMMAND_LIGHT = "cohere.command-light-text-v14";
    public static final String EMBED_ENGLISH = "cohere.embed-english-v3";
    public static final String EMBED_MULTLINGUAL = "cohere.embed-multilingual-v3";

    public static boolean isCohereModel(String modelId) {
        return COMMAND.equals(modelId) || COMMAND_LIGHT.equals(modelId) || EMBED_ENGLISH.equals(modelId) || EMBED_MULTLINGUAL.equals(modelId);
    }

    public static String invokeCohereModel(String modelId, String prompt, Boolean async) {
        if (COMMAND.equals(modelId)) {
            return invokeCommand(modelId, prompt, async);
        } else if (COMMAND_LIGHT.equals(modelId)) {
            return invokeCommandLight(modelId, prompt, async);
        } else if (EMBED_ENGLISH.equals(modelId)) {
            return invokeEmbedEnglish(modelId, prompt, async);
        } else if (EMBED_MULTLINGUAL.equals(modelId)) {
            return invokeEmbedMultilingual(modelId, prompt, async);
        }
        return "Unsupported Cohere Model: " + modelId;
    }

    public static String invokeCommand(String modelId, String prompt, Boolean async) {
        return invokeCommandModels(modelId, prompt, async);
    }

    public static String invokeCommandLight(String modelId, String prompt, Boolean async) {
        return invokeCommandModels(modelId, prompt, async);
    }

    public static String invokeEmbedEnglish(String modelId, String prompt, Boolean async) {
        return invokeEmbedModels(modelId, prompt, async);
    }

    public static String invokeEmbedMultilingual(String modelId, String prompt, Boolean async) {
        return invokeEmbedModels(modelId, prompt, async);
    }

    public static String invokeCommandModels(String modelId, String prompt, Boolean async) {
        /*
          The different model providers have individual request and response formats.
          For the format, ranges, and default values for Cohere Command, refer to:
          https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-meta.html
          https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-cohere-command.html
         */

        String payload = new JSONObject()
                .put("prompt", prompt)
                .put("max_tokens", MAX_TOKENS)
                .put("temperature", 0.5)
                .put("p", 0.9)
                .put("k", 0)
                .put("stop_sequences", List.of("User:"))
                .put("return_likelihoods", "NONE") // GENERATION|ALL|NONE
                .put("stream", false)
                .put("truncate", "END") // NONE|START|END
//                .put("logit_bias", "{token_id: bias}")
                .toString();

        return InvokeModel.invokeAndGetJSONArray(modelId, payload, "generations", async).getJSONObject(0).getString("text");
    }

    public static String invokeEmbedModels(String modelId, String prompt, Boolean async) {
        /*
          The different model providers have individual request and response formats.
          For the format, ranges, and default values for Cohere Embed, refer to:
          https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-meta.html
          https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-embed.html
         */

        String payload = new JSONObject()
                .put("texts", List.of(prompt))
                .put("input_type", "search_document") // search_document|search_query|classification|clustering
                .put("truncate", "NONE") // NONE|LEFT|RIGHT
                .toString();

        return InvokeModel.invokeAndGetJSONArray(modelId, payload, "embeddings", async).getJSONArray(0).toList().toString();
    }
}
