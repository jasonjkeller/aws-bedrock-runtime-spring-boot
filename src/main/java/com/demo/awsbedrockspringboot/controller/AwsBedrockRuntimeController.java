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
import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.JURASSIC2;
import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.LLAMA2;
import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.STABLE_DIFFUSION;
import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.TITAN_IMAGE;

/**
 * Controller to interact with AWS Bedrock Runtime.
 */
@Controller
public class AwsBedrockRuntimeController {

    private static final String IMG_INVISIBLE = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7";

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
        List<String> modelNameList = Arrays.asList(CLAUDE, JURASSIC2, LLAMA2, STABLE_DIFFUSION, TITAN_IMAGE);
        model.addAttribute("modelNameList", modelNameList);

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
            case INVOKE_MODEL -> AwsBedrockRuntimeHelper.invoke(prompt.getModelName(), prompt.getPrompt());
            case INVOKE_MODEL_ASYNC -> AwsBedrockRuntimeHelper.invokeAsync(prompt.getModelName(), prompt.getPrompt());
            case INVOKE_MODEL_WITH_RESPONSE_STREAM -> AwsBedrockRuntimeHelper.invokeWithResponseStream(prompt.getModelName(), prompt.getPrompt());
            default -> "";
        };

        System.out.print("Generated response:");
        System.out.println(response);

        String imageResponse = IMG_INVISIBLE;
        String htmlResponse = "";

        if (STABLE_DIFFUSION.equals(prompt.getModelName()) || TITAN_IMAGE.equals(prompt.getModelName())) {
            imageResponse = "data:image/png;base64, "+response;
        } else {
            htmlResponse = response
                    .replace("\r", "<br>")
                    .replace("\n", "<br>");
        }
        model.addAttribute("imageBytes", imageResponse);
        model.addAttribute("response", htmlResponse);

        return "prompt-response";
    }

}
