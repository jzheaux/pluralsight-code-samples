package io.jzheaux.pluralsight.ai.stt;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptResponseFormat;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TranscribeController {
    private final OpenAiAudioTranscriptionModel transcriptionModel;

    public TranscribeController(OpenAiAudioTranscriptionModel transcriptionModel) {
        this.transcriptionModel = transcriptionModel;
    }

    @PostMapping("/transcribe")
    public String transcribe(@RequestParam("file") MultipartFile file, @RequestParam(name="language", defaultValue="en") String language) {
        Resource audioFile = file.getResource();
        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
            .withLanguage(language)
			.withResponseFormat(TranscriptResponseFormat.VTT)
			//.withTemperature(0f)
			.build();
		AudioTranscriptionPrompt request = new AudioTranscriptionPrompt(audioFile, options);
		AudioTranscriptionResponse response = this.transcriptionModel.call(request);
        return response.getResult().getOutput();
    }
}
