package com.demo.awsbedrockspringboot.llm.models;

import com.demo.awsbedrockspringboot.awsbedrockruntime.InvokeModel;
import org.json.JSONObject;

import java.util.List;

import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.MAX_TOKENS;

public class Anthropic {
    //  * Anthropic
    //    * Claude (anthropic.claude-v2, anthropic.claude-v2:1)
    //    * Claude Instant (anthropic.claude-instant-v1)
    public static final String CLAUDE_V_2 = "anthropic.claude-v2";
    public static final String CLAUDE_V_2_1 = "anthropic.claude-v2:1";
    public static final String CLAUDE_INSTANT_V_1 = "anthropic.claude-instant-v1";

    public static boolean isAnthropicModel(String modelId) {
        return CLAUDE_V_2.equals(modelId) || CLAUDE_V_2_1.equals(modelId) || CLAUDE_INSTANT_V_1.equals(modelId);
    }

    /**
     * Invokes an Anthropic model to run an inference based on the provided input.
     *
     * @param prompt The prompt for the model to complete.
     * @return The generated response.
     */
    public static String invokeAnthropicModel(String modelId, String prompt, Boolean async) {
        if (CLAUDE_INSTANT_V_1.equals(modelId)) {
            return invokeClaudeInstantV1(modelId, prompt, async);
        } else if (CLAUDE_V_2.equals(modelId)) {
            return invokeClaudeV2(modelId, prompt, async);
        } else if (CLAUDE_V_2_1.equals(modelId)) {
            return invokeClaudeV21(modelId, prompt, async);
        }
        return "Unsupported Anthropic Model: " + modelId;
    }

    private static String invokeClaudeInstantV1(String modelId, String prompt, Boolean async) {
        return invokeClaudeTextModels(modelId, prompt, async);
    }

    private static String invokeClaudeV2(String modelId, String prompt, Boolean async) {
        return invokeClaudeTextModels(modelId, prompt, async);
    }

    private static String invokeClaudeV21(String modelId, String prompt, Boolean async) {
        return invokeClaudeTextModels(modelId, prompt, async);
    }

    private static String invokeClaudeTextModels(String modelId, String prompt, Boolean async) {
         /*
          The different model providers have individual request and response formats.
          For the format, ranges, and default values for Anthropic Claude, refer to:
          https://docs.anthropic.com/claude/reference/complete_post
          https://docs.aws.amazon.com/bedrock/latest/userguide/model-parameters-claude.html
         */

        // Claude requires you to enclose the prompt as follows:
        String enclosedPrompt = "Human: " + prompt + "\n\nAssistant:";

        // see https://docs.anthropic.com/claude/reference/migrating-from-text-completions-to-messages
//        String enclosedPrompt = "Human: Hello there\n\nAssistant: Hi, I'm Claude. How can I help?\n\nHuman: " + prompt + "\n\nAssistant:";

        String payload = new JSONObject()
                .put("prompt", enclosedPrompt)
                .put("max_tokens_to_sample", MAX_TOKENS)
                .put("temperature", 0.5)
                .put("stop_sequences", List.of("\n\nHuman:"))
                .toString();

        return InvokeModel.invokeAndGetString(modelId, payload, "completion", async);
    }
}
