package com.demo.awsbedrockspringboot.llm.models;

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
        // FIXME implement
        return "Cohere model invoke is not yet supported: " + modelId;
    }

    public static String invokeCommandLight(String modelId, String prompt, Boolean async) {
        // FIXME implement
        return "Cohere model invoke is not yet supported: " + modelId;
    }

    public static String invokeEmbedEnglish(String modelId, String prompt, Boolean async) {
        // FIXME implement
        return "Cohere model invoke is not yet supported: " + modelId;
    }

    public static String invokeEmbedMultilingual(String modelId, String prompt, Boolean async) {
        // FIXME implement
        return "Cohere model invoke is not yet supported: " + modelId;
    }
}
