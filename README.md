# AWS Bedrock Runtime With Spring Boot

Simple Spring service with an endpoint for submitting prompts to an LLM via the AWS Bedrock Runtime and AWS SDK for Java 2.x. 

## Requirements

* Java 17
* Create an `~/.aws/credentials` file on your development machine and add a `default` profile:
    ```
    [default]
    aws_access_key_id=...
    aws_secret_access_key=...
    aws_session_token=...
    ```
  You can obtain the required credentials numerous ways but a quick and easy method is to select the "Command line or programmatic access" option when signing in to the `java-agent-dev` account, which will display the credentials.
  ![aws-creds.png](src%2Fmain%2Fresources%2Fstatic%2Faws-creds.png)

## Usage

The Spring service will run on http://localhost:8081/

If you navigate to http://localhost:8081/prompt-form you will be able to select the invocation type and submit a prompt to a model via the AWS Bedrock Runtime. 

![ui.png](src%2Fmain%2Fresources%2Fstatic%2Fui.png)

Currently, this example only uses [Anthropic's Claude](https://aws.amazon.com/bedrock/claude/) model, but it could easily be extended to support other models (see [InvokeModel.java](src%2Fmain%2Fjava%2Fcom%2Fdemo%2Fawsbedrockspringboot%2Fawsbedrockruntime%2FInvokeModel.java) and [InvokeModelAsync.java](src%2Fmain%2Fjava%2Fcom%2Fdemo%2Fawsbedrockspringboot%2Fawsbedrockruntime%2FInvokeModelAsync.java) for other model implementations). This model only supports text input and text output.

The following invocation types are supported by Amazon Bedrock Runtime:
* [InvokeModel](https://docs.aws.amazon.com/bedrock/latest/APIReference/API_runtime_InvokeModel.html)
* [InvokeModelWithResponseStream](https://docs.aws.amazon.com/bedrock/latest/APIReference/API_runtime_InvokeModelWithResponseStream.html)

There is a synchronous [BedrockRuntimeClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/bedrockruntime/BedrockRuntimeClient.html) as well as an asynchronous [BedrockRuntimeAsyncClient](https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/services/bedrockruntime/BedrockRuntimeAsyncClient.html).

## Useful Documentation

* [Set up the Amazon Bedrock API](https://docs.aws.amazon.com/bedrock/latest/userguide/api-setup.html)
* [Set up the AWS SDK for Java 2.x](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/setup.html)
* [AWS SDK for Java Documentation](https://docs.aws.amazon.com/sdk-for-java/)
