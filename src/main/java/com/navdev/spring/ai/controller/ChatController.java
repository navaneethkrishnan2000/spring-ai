package com.navdev.spring.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    // For Text only Prompts
    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        return chatClient.prompt() // Called the prompt
                .user(message) //Passed the data
                .call()
                .content(); // Get the output as String
    }

    // For Multimodality
    @PostMapping("/chat-with-image")
    public String chatWithImg(
            @RequestPart("message") String message, // @RequestPart annotation is essential for handling specific parts of a multipart request, especially for file uploads combined with other form data.It offers flexibility by allowing you to handle files and other data independently, making it suitable for building robust upload APIs.
            @RequestPart("image") MultipartFile file) // @RequestPart accepts images as parameters
    {
        return chatClient.prompt()
                .user(promptUserSpec -> {
                    promptUserSpec.text(message)
                            .media(MimeTypeUtils.IMAGE_PNG, new InputStreamResource(file));
                })
                .call()
                .content();
    }
    /*
        In the user section we have to use promptUserSpec, inside the prompt we have to specify the media and the text.
        In the text option we have to pass the file and in the media options we have to pass the MultipartFile received through this request,
           - In the media section I need to provide the kind of image that I am going to give, and then a resource value.
           - If we directly give the file it shows error, so we have to convert this multipart file into a resource using InputStreamResource
    */
}
