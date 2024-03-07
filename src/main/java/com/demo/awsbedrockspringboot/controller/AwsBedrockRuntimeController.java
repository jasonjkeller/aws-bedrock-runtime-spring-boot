package com.demo.awsbedrockspringboot.controller;

import com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper;
import com.demo.awsbedrockspringboot.llm.models.AI21Labs;
import com.demo.awsbedrockspringboot.llm.models.Amazon;
import com.demo.awsbedrockspringboot.llm.models.Anthropic;
import com.demo.awsbedrockspringboot.llm.models.Cohere;
import com.demo.awsbedrockspringboot.llm.models.Meta;
import com.demo.awsbedrockspringboot.llm.models.StabilityAI;
import com.demo.awsbedrockspringboot.model.Prompt;
import com.newrelic.api.agent.NewRelic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.INVOKE_MODEL;
import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.INVOKE_MODEL_ASYNC;
import static com.demo.awsbedrockspringboot.awsbedrockruntime.AwsBedrockRuntimeHelper.INVOKE_MODEL_WITH_RESPONSE_STREAM;

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

        List<String> modelNameList = Arrays.asList(
                AI21Labs.JURASSIC_2_ULTRA_V1,
                AI21Labs.JURASSIC_2_MID_V1,
                Amazon.TITAN_IMAGE_GENERATOR_G1,
                Amazon.TITAN_EMBEDDINGS_G1_TEXT,
                Amazon.TITAN_MULTIMODAL_EMBEDDINGS_G1,
                Amazon.TITAN_TEXT_G1_EXPRESS,
                Amazon.TITAN_TEXT_G1_LITE,
                Anthropic.CLAUDE_INSTANT_V_1,
                Anthropic.CLAUDE_V_2,
                Anthropic.CLAUDE_V_2_1,
                Cohere.COMMAND,
                Cohere.COMMAND_LIGHT,
                Cohere.EMBED_ENGLISH,
                Cohere.EMBED_MULTLINGUAL,
                Meta.LLAMA_2_CHAT_13_B,
                Meta.LLAMA_2_CHAT_70_B,
                StabilityAI.SDXL_V_1_0
        );
        model.addAttribute("modelNameList", modelNameList);

        return "prompt-form";
    }

    /**
     * Displays the response from the AWS Bedrock Runtime prompt.
     *
     * @param prompt The prompt to be made
     * @param model  Model for UI templates
     * @return the prompt-response view
     */
    @PostMapping("/submit-prompt-form")
    public String submitForm(@ModelAttribute Prompt prompt, Model model) {
        model.addAttribute("prompt", prompt);

        // Add some attributes to verify that those with the llm. prefix get added to NR LlmEvents
        NewRelic.addCustomParameter("no-llm", true);
        NewRelic.addCustomParameter("llm.test-number", 2);
        NewRelic.addCustomParameter("llm.test-string", "Bye");
        NewRelic.addCustomParameter("llm.test-boolean", false);

        // llm.conversation_id can be used to group conversations in NR APM
        NewRelic.addCustomParameter("llm.conversation_id", "submit-prompt-form:" + UUID.randomUUID());

        String response = invokeModel(prompt);
//        response += "\n\nResponse 2:\n\n" + invokeModel(prompt); // make another request to the LLM model

        System.out.print("Generated response:");
        System.out.println(response);

        String imageResponse = IMG_INVISIBLE;
        String htmlResponse = "";

        if (StabilityAI.SDXL_V_1_0.equals(prompt.getModelName()) || Amazon.TITAN_IMAGE_GENERATOR_G1.equals(prompt.getModelName())) {
            imageResponse = "data:image/png;base64, " + response;
        } else {
            htmlResponse = response
                    .replace("\r", "<br>")
                    .replace("\n", "<br>");
        }
        model.addAttribute("imageBytes", imageResponse);
        model.addAttribute("response", htmlResponse);

        recordLlmFeedback(NewRelic.getAgent().getTraceMetadata().getTraceId());

        return "prompt-response";
    }

    private static void recordLlmFeedback(String traceId) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            System.out.println("\nRecording LLM feedback event for trace: " + traceId);
            // TODO add a call to the feedback API here
        });
    }

    private static String invokeModel(Prompt prompt) {
        return switch (prompt.getInvokeType()) {
            case INVOKE_MODEL -> AwsBedrockRuntimeHelper.invoke(prompt.getModelName(), prompt.getPrompt());
            case INVOKE_MODEL_ASYNC -> AwsBedrockRuntimeHelper.invokeAsync(prompt.getModelName(), prompt.getPrompt());
            case INVOKE_MODEL_WITH_RESPONSE_STREAM -> AwsBedrockRuntimeHelper.invokeWithResponseStream(prompt.getModelName(), prompt.getPrompt());
            default -> "";
        };
    }
}
