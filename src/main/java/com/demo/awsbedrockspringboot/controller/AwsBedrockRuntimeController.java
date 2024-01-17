package com.demo.awsbedrockspringboot.controller;

import com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper;
import com.demo.awsbedrockspringboot.model.Prompt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;

import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.CLAUDE;
import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.INVOKE_MODEL;
import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.INVOKE_MODEL_ASYNC;
import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.INVOKE_MODEL_WITH_RESPONSE_STREAM;

/**
 * Controller to interact with AWS Bedrock Runtime.
 */
@Controller
public class AwsBedrockRuntimeController {

    /**
     * Displays a form for submitting a prompt via the AWS Bedrock Runtime.
     *
     * @param model Model for UI templates
     * @return the prompt-form view
     */
    @GetMapping("/prompt-form")
    public String promptForm(Model model) {
        model.addAttribute("prompt", new Prompt());

        List<String> invokeTypeList = Arrays.asList(INVOKE_MODEL, INVOKE_MODEL_ASYNC, INVOKE_MODEL_WITH_RESPONSE_STREAM);
        model.addAttribute("invokeTypeList", invokeTypeList);

        return "prompt-form";
    }

    /**
     * Displays the response from the AWS Bedrock Runtime prompt.
     *
     * @param prompt The prompt to be made
     * @param model Model for UI templates
     * @return the prompt-response view
     */
    @PostMapping("/submit-prompt-form")
    public String submitForm(@ModelAttribute Prompt prompt, Model model) {
        model.addAttribute("prompt", prompt);

        String response = switch (prompt.getInvokeType()) {
            case INVOKE_MODEL -> AwsBedrockRuntimeHelper.invoke(CLAUDE, prompt.getPrompt());
            case INVOKE_MODEL_ASYNC -> AwsBedrockRuntimeHelper.invokeAsync(CLAUDE, prompt.getPrompt());
            case INVOKE_MODEL_WITH_RESPONSE_STREAM -> AwsBedrockRuntimeHelper.invokeWithResponseStream(CLAUDE, prompt.getPrompt());
            default -> "";
        };

        model.addAttribute("response", response);

        return "prompt-response";
    }

}
