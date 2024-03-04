package com.demo.awsbedrockspringboot.awsbedrockruntime;

import com.demo.awsbedrockspringboot.llm.models.AI21Labs;
import com.demo.awsbedrockspringboot.llm.models.Amazon;
import com.demo.awsbedrockspringboot.llm.models.Anthropic;
import com.demo.awsbedrockspringboot.llm.models.Cohere;
import com.demo.awsbedrockspringboot.llm.models.Meta;
import com.demo.awsbedrockspringboot.llm.models.StabilityAI;
import software.amazon.awssdk.services.bedrockruntime.model.BedrockRuntimeException;

/**
 * Demonstrates the invocation of the following models:
 * Anthropic Claude 2
 * <p>
 * Could also be extended to use the following models (see InvokeModel for implementations):
 * AI21 Labs Jurassic-2, Meta Llama 2 Chat, and Stability.ai Stable Diffusion XL.
 */
public class AwsBedrockRuntimeHelper {
    public static final int MAX_TOKENS = 1000;
    public static final String DEFAULT_PROMPT = "What number is after 17?";
    public static final String INVOKE_MODEL = "InvokeModel";
    public static final String INVOKE_MODEL_ASYNC = "InvokeModel (async)";
    public static final String INVOKE_MODEL_WITH_RESPONSE_STREAM = "InvokeModelWithResponseStream";
    public static final String DEFAULT_INVOKE_TYPE = INVOKE_MODEL;
    public static final String DEFAULT_MODEL_NAME = Anthropic.CLAUDE_V_2;

    /**
     * InvokeModel using synchronous BedrockRuntimeClient.
     *
     * @param modelId The model that is being prompted
     * @param prompt  The prompt to be made
     * @return response from prompt
     */
    public static String invoke(String modelId, String prompt) {
        System.out.println("\n" + new String(new char[88]).replace("\0", "-"));
        System.out.println("Invoking: " + modelId);
        System.out.println("Prompt: " + prompt);

        return doInvoke(modelId, prompt, false);
    }

    /**
     * InvokeModel using asynchronous BedrockRuntimeAsyncClient.
     *
     * @param modelId The model that is being prompted
     * @param prompt  The prompt to be made
     * @return response from prompt
     */
    public static String invokeAsync(String modelId, String prompt) {
        System.out.println("\n" + new String(new char[88]).replace("\0", "-"));
        System.out.println("Invoking: " + modelId);
        System.out.println("Prompt: " + prompt);

        return doInvoke(modelId, prompt, true);
    }

    /**
     * InvokeModelWithResponseStream using asynchronous BedrockRuntimeAsyncClient.
     *
     * @param modelId The model that is being prompted
     * @param prompt  The prompt to be made
     * @return response from prompt
     */
    public static String invokeWithResponseStream(String modelId, String prompt) {
        System.out.println(new String(new char[88]).replace("\0", "-"));
        modelId = Anthropic.CLAUDE_V_2; // Only supporting this for now
        System.out.printf("Invoking %s with response stream%n", modelId);
        System.out.println("Prompt: " + prompt);

        try {
            var silent = false;
            return InvokeModelWithResponseStream.invokeClaude(prompt, silent);
        } catch (BedrockRuntimeException e) {
            System.out.println("Couldn't invoke model " + modelId + ": " + e.getMessage());
            throw e;
        }
    }

    private static String doInvoke(String modelId, String prompt, Boolean async) {
        try {
            if (Anthropic.isAnthropicModel(modelId)) {
                return Anthropic.invokeAnthropicModel(modelId, prompt, async);
            } else if (AI21Labs.isAI21LabsModel(modelId)) {
                return AI21Labs.invokeAI21LabsModel(modelId, prompt, async);
            } else if (Meta.isMetaModel(modelId)) {
                return Meta.invokeMetaModel(modelId, prompt, async);
            } else if (StabilityAI.isStabilityAiModel(modelId)) {
                return StabilityAI.invokeStabilityAiModel(modelId, prompt, 0, null, async);
            } else if (Amazon.isAmazonModel(modelId)) {
                return Amazon.invokeAmazonModel(modelId, prompt, 0, async);
            } else if (Cohere.isCohereModel(modelId)) {
                return Cohere.invokeCohereModel(modelId, prompt, async);
            }
            throw new IllegalStateException("Unexpected value: " + modelId);
        } catch (BedrockRuntimeException e) {
            System.out.println("Couldn't invoke model " + modelId + ": " + e.getMessage());
            throw e;
        }
    }
}
