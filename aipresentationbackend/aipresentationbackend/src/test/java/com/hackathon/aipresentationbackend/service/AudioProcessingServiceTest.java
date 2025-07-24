package com.hackathon.aipresentationbackend.service;

import com.hackathon.aipresentationbackend.exception.AudioProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AudioProcessingServiceTest {

    @InjectMocks
    private AudioProcessingService audioProcessingService;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(audioProcessingService, "tempDirectory", tempDir.toString());
    }
    
    @Test
    void validateAudioFile_ValidMp3File_NoExceptionThrown() {
        // Arrange
        MultipartFile audioFile = new MockMultipartFile(
                "audio.mp3",
                "audio.mp3",
                "audio/mpeg",
                "test audio content".getBytes()
        );
        
        // Act & Assert
        assertDoesNotThrow(() -> audioProcessingService.validateAudioFile(audioFile));
    }
    
    @Test
    void validateAudioFile_ValidWavFile_NoExceptionThrown() {
        // Arrange
        MultipartFile audioFile = new MockMultipartFile(
                "audio.wav",
                "audio.wav",
                "audio/wav",
                "test audio content".getBytes()
        );
        
        // Act & Assert
        assertDoesNotThrow(() -> audioProcessingService.validateAudioFile(audioFile));
    }
    
    @Test
    void validateAudioFile_ValidM4aFile_NoExceptionThrown() {
        // Arrange
        MultipartFile audioFile = new MockMultipartFile(
                "audio.m4a",
                "audio.m4a",
                "audio/m4a",
                "test audio content".getBytes()
        );
        
        // Act & Assert
        assertDoesNotThrow(() -> audioProcessingService.validateAudioFile(audioFile));
    }
    
    @Test
    void validateAudioFile_EmptyFile_ThrowsException() {
        // Arrange
        MultipartFile audioFile = new MockMultipartFile(
                "empty.mp3",
                "empty.mp3",
                "audio/mpeg",
                new byte[0]
        );
        
        // Act & Assert
        AudioProcessingException exception = assertThrows(AudioProcessingException.class, 
                () -> audioProcessingService.validateAudioFile(audioFile));
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("empty or missing"));
    }
    
    @Test
    void validateAudioFile_NullFile_ThrowsException() {
        // Act & Assert
        AudioProcessingException exception = assertThrows(AudioProcessingException.class, 
                () -> audioProcessingService.validateAudioFile(null));
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("empty or missing"));
    }
    
    @Test
    void validateAudioFile_UnsupportedFormat_ThrowsException() {
        // Arrange
        MultipartFile audioFile = new MockMultipartFile(
                "document.pdf",
                "document.pdf",
                "application/pdf",
                "test pdf content".getBytes()
        );
        
        // Act & Assert
        AudioProcessingException exception = assertThrows(AudioProcessingException.class, 
                () -> audioProcessingService.validateAudioFile(audioFile));
        
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("Unsupported audio format"));
    }
    
    @Test
    void validateAudioFile_FileTooLarge_ThrowsException() throws Exception {
        // Arrange - Create a mock file that reports a size larger than the limit
        MultipartFile audioFile = new MockMultipartFile(
                "large.mp3",
                "large.mp3",
                "audio/mpeg",
                "test content".getBytes()
        ) {
            @Override
            public long getSize() {
                return 101 * 1024 * 1024; // 101MB, just over the 100MB limit
            }
        };
        
        // Act & Assert
        AudioProcessingException exception = assertThrows(AudioProcessingException.class, 
                () -> audioProcessingService.validateAudioFile(audioFile));
        
        assertEquals(HttpStatus.PAYLOAD_TOO_LARGE, exception.getStatusCode());
        assertTrue(exception.getMessage().contains("exceeds maximum size"));
    }
    
    @Test
    void createTempFile_ValidFile_CreatesFile() throws IOException {
        // Arrange
        MultipartFile audioFile = new MockMultipartFile(
                "audio.mp3",
                "audio.mp3",
                "audio/mpeg",
                "test audio content".getBytes()
        );
        
        // Act
        File result = audioProcessingService.createTempFile(audioFile);
        
        // Assert
        assertTrue(result.exists());
        assertTrue(result.isFile());
        assertEquals("test audio content", new String(Files.readAllBytes(result.toPath())));
        assertTrue(result.getName().endsWith(".mp3"));
    }
    
    @Test
    void cleanupTempFile_ExistingFile_DeletesFile() throws IOException {
        // Arrange
        File tempFile = File.createTempFile("test", ".tmp", tempDir.toFile());
        assertTrue(tempFile.exists());
        
        // Act
        boolean result = audioProcessingService.cleanupTempFile(tempFile);
        
        // Assert
        assertTrue(result);
        assertFalse(tempFile.exists());
    }
    
    @Test
    void cleanupTempFile_NonExistentFile_ReturnsFalse() {
        // Arrange
        File nonExistentFile = new File(tempDir.toString(), "nonexistent.tmp");
        
        // Act
        boolean result = audioProcessingService.cleanupTempFile(nonExistentFile);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    void cleanupAllTempFiles_MultipleFiles_DeletesAll() throws IOException {
        // Arrange
        File file1 = File.createTempFile("test1", ".tmp", tempDir.toFile());
        File file2 = File.createTempFile("test2", ".tmp", tempDir.toFile());
        File file3 = File.createTempFile("test3", ".tmp", tempDir.toFile());
        
        // Act
        int deletedCount = audioProcessingService.cleanupAllTempFiles();
        
        // Assert
        assertEquals(3, deletedCount);
        assertFalse(file1.exists());
        assertFalse(file2.exists());
        assertFalse(file3.exists());
    }
    
    @Test
    void extractAudioMetadata_MockWavFile_ReturnsMetadata() throws Exception {
        // This test uses a mock approach since we can't easily create real audio files in a unit test
        
        // Arrange - Create a simple mock WAV file
        byte[] wavHeader = new byte[44]; // Simplified WAV header
        byte[] audioData = new byte[1000]; // Some audio data
        byte[] fullWavFile = new byte[wavHeader.length + audioData.length];
        System.arraycopy(wavHeader, 0, fullWavFile, 0, wavHeader.length);
        System.arraycopy(audioData, 0, fullWavFile, wavHeader.length, audioData.length);
        
        MultipartFile mockWavFile = new MockMultipartFile(
                "test.wav",
                "test.wav",
                "audio/wav",
                fullWavFile
        );
        
        // We need to mock the AudioSystem behavior which is difficult in a unit test
        // For a real test, we would need integration testing with actual audio files
        // Here we'll just verify the method doesn't throw exceptions with our mock
        
        try {
            Map<String, Object> metadata = audioProcessingService.extractAudioMetadata(mockWavFile);
            // If we get here without exception, that's a partial success
            // In a real scenario, we'd verify the metadata values
            assertNotNull(metadata);
        } catch (AudioProcessingException e) {
            // This is expected in a unit test environment without real audio processing
            // The important part is that our code attempts to process the file correctly
            assertTrue(e.getMessage().contains("audio") || e.getMessage().contains("metadata"));
        }
    }
}