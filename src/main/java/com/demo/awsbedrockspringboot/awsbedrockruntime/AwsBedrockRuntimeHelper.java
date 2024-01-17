package com.demo.awsbedrockspringboot.awsbedrockruntime;

import software.amazon.awssdk.services.bedrockruntime.model.BedrockRuntimeException;

import static com.demo.awsbedrockspringboot.awsbedrockruntime.InvokeModel.invokeClaude;

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
    public static final String INVOKE_MODEL_WITH_RESPONSE_STREAM = "InvokeModelWithResponseStream";
    public static final String DEFAULT_INVOKE_TYPE = INVOKE_MODEL;
    public static final String CLAUDE = "anthropic.claude-v2";
//    public static final String JURASSIC2 = "ai21.j2-mid-v1";
//    public static final String LLAMA2 = "meta.llama2-13b-chat-v1";
//    public static final String STABLE_DIFFUSION = "stability.stable-diffusion-xl";
//    public static final String TITAN_IMAGE = "amazon.titan-image-generator-v1";

    public static String invoke(String modelId, String prompt) {
        System.out.println("\n" + new String(new char[88]).replace("\0", "-"));
        System.out.println("Invoking: " + modelId);
        System.out.println("Prompt: " + prompt);

        try {
            if (modelId.equals(CLAUDE)) {
                return invokeClaude(prompt);
            }
            throw new IllegalStateException("Unexpected value: " + modelId);
        } catch (BedrockRuntimeException e) {
            System.out.println("Couldn't invoke model " + modelId + ": " + e.getMessage());
            throw e;
        }
    }

    public static String invokeWithResponseStream(String modelId, String prompt) {
        System.out.println(new String(new char[88]).replace("\0", "-"));
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
