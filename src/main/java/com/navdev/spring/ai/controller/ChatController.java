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
            @RequestPart("message") String message,
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
}
