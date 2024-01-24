package com.demo.awsbedrockspringboot.model;

import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.DEFAULT_INVOKE_TYPE;
import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.DEFAULT_MODEL_NAME;
import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.DEFAULT_PROMPT;

/**
 * Represents a prompt to be made via the AWS Bedrock Runtime.
 */
public class Prompt {
    private String prompt = DEFAULT_PROMPT;
    private String invokeType = DEFAULT_INVOKE_TYPE;
    private String modelName = DEFAULT_MODEL_NAME;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getInvokeType() {
        return invokeType;
    }

    public void setInvokeType(String invokeType) {
        this.invokeType = invokeType;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return "Prompt: " + prompt;
    }
}
