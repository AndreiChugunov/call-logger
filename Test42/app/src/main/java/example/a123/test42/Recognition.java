package example.a123.test42;

// Imports the Google Cloud client library
import android.util.Log;

import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Recognition {
    /**
     * Demonstrates using the Speech API to transcribe an audio file.
     */
    public static void main(String... pathToSoundFile) throws Exception {
        List<String> resultingList = new ArrayList<String>();
        // Instantiates a client
        try (SpeechClient speechClient = SpeechClient.create()) {
            // The path to the audio file to transcribe
            //String fileName = pathToSoundFile;
            String fileName = "./app/src/main/res/raw/sample.wav";
            // Reads the audio file into memory
            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);

            ByteString audioBytes = ByteString.copyFrom(data);

            // Builds the sync recognize request
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(AudioEncoding.LINEAR16)
                    .setSampleRateHertz(11025)
                    .setLanguageCode("ru-RU")
                    .setEnableSpeakerDiarization(true)
                    .setDiarizationSpeakerCount(2)
                    .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            // Performs speech recognition on the audio file
            RecognizeResponse response = speechClient.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternatives(0);
                String resultRec = alternative.getTranscript();
                int resultSpeaker = alternative.getWords((alternative.getWordsCount() - 1)).getSpeakerTag();
                String resultingString = "Person" + String.valueOf(resultSpeaker)+":"+resultRec;
                System.out.format(
                        "Speaker Tag %s: %s\n",
                        alternative.getWords((alternative.getWordsCount() - 1)).getSpeakerTag(),
                        alternative.getTranscript());
                resultingList.add(resultingString);
                //return resultingString;

                //Log.i("Transcription:",resultRec);
                //System.out.println("Transcription:" + resultRec);
            }
        }
        //return resultingList;
    }
}
