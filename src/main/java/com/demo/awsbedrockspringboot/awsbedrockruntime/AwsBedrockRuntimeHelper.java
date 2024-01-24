package com.demo.awsbedrockspringboot.awsbedrockruntime;

import software.amazon.awssdk.services.bedrockruntime.model.BedrockRuntimeException;


/**
 * Demonstrates the invocation of the following models:
 * Anthropic Claude 2
 * <p>
 * Could also be extended to use the following models (see InvokeModel for implementations):
 * AI21 Labs Jurassic-2, Meta Llama 2 Chat, and Stability.ai Stable Diffusion XL.
 */
public class AwsBedrockRuntimeHelper {
    public static final String DEFAULT_PROMPT = "What is a large-language model?";

    public static final String INVOKE_MODEL = "InvokeModel";
    public static final String INVOKE_MODEL_ASYNC = "InvokeModel (async)";
    public static final String INVOKE_MODEL_WITH_RESPONSE_STREAM = "InvokeModelWithResponseStream";
    public static final String DEFAULT_INVOKE_TYPE = INVOKE_MODEL;
    public static final String CLAUDE = "anthropic.claude-v2";
    public static final String JURASSIC2 = "ai21.j2-mid-v1";
    public static final String LLAMA2 = "meta.llama2-13b-chat-v1";
    public static final String STABLE_DIFFUSION = "stability.stable-diffusion-xl";
    public static final String TITAN_IMAGE = "amazon.titan-image-generator-v1";
    public static final String DEFAULT_MODEL_NAME = CLAUDE;

    /**
     * InvokeModel using synchronous BedrockRuntimeClient.
     *
     * @param modelId The model that is being prompted
     * @param prompt The prompt to be made
     * @return response from prompt
     */
    public static String invoke(String modelId, String prompt) {
        System.out.println("\n" + new String(new char[88]).replace("\0", "-"));
        System.out.println("Invoking: " + modelId);
        System.out.println("Prompt: " + prompt);

        try {
            if (modelId.equals(CLAUDE)) {
                return InvokeModel.invokeClaude(prompt);
            } else if (modelId.equals(JURASSIC2)) {
                return InvokeModel.invokeJurassic2(prompt);
            } else if (modelId.equals(LLAMA2)) {
                return InvokeModel.invokeLlama2(prompt);
            } else if (modelId.equals(STABLE_DIFFUSION)) {
                return InvokeModel.invokeStableDiffusion(prompt, 0, null);
            } else if (modelId.equals(TITAN_IMAGE)) {
                return InvokeModel.invokeTitanImage(prompt, 0);
            }
            throw new IllegalStateException("Unexpected value: " + modelId);
        } catch (BedrockRuntimeException e) {
            System.out.println("Couldn't invoke model " + modelId + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * InvokeModel using asynchronous BedrockRuntimeAsyncClient.
     *
     * @param modelId The model that is being prompted
     * @param prompt The prompt to be made
     * @return response from prompt
     */
    public static String invokeAsync(String modelId, String prompt) {
        System.out.println("\n" + new String(new char[88]).replace("\0", "-"));
        System.out.println("Invoking: " + modelId);
        System.out.println("Prompt: " + prompt);

        try {
            if (modelId.equals(CLAUDE)) {
                return InvokeModelAsync.invokeClaude(prompt);
            } else if (modelId.equals(JURASSIC2)) {
                return InvokeModelAsync.invokeJurassic2(prompt);
            } else if (modelId.equals(LLAMA2)) {
                return InvokeModelAsync.invokeLlama2(prompt);
            } else if (modelId.equals(STABLE_DIFFUSION)) {
                return InvokeModelAsync.invokeStableDiffusion(prompt, 0, null);
            } else if (modelId.equals(TITAN_IMAGE)) {
                return InvokeModelAsync.invokeTitanImage(prompt, 0);
            }
            throw new IllegalStateException("Unexpected value: " + modelId);
        } catch (BedrockRuntimeException e) {
            System.out.println("Couldn't invoke model " + modelId + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * InvokeModelWithResponseStream using asynchronous BedrockRuntimeAsyncClient.
     *
     * @param modelId The model that is being prompted
     * @param prompt The prompt to be made
     * @return response from prompt
     */
    public static String invokeWithResponseStream(String modelId, String prompt) {
        System.out.println(new String(new char[88]).replace("\0", "-"));
        modelId = CLAUDE; // Only supporting this for now
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

}
