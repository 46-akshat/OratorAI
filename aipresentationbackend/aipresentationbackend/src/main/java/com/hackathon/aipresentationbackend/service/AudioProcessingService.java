package com.hackathon.aipresentationbackend.service;

import com.hackathon.aipresentationbackend.exception.AudioProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Service for processing audio files including validation, conversion, and metadata extraction
 */
@Service
public class AudioProcessingService {
    private static final Logger logger = LoggerFactory.getLogger(AudioProcessingService.class);
    
    private static final Set<String> SUPPORTED_AUDIO_FORMATS = new HashSet<>(
            Arrays.asList("audio/mpeg", "audio/mp3", "audio/wav", "audio/x-wav", "audio/m4a", "audio/x-m4a")
    );
    
    private static final long MAX_FILE_SIZE_BYTES = 100 * 1024 * 1024; // 100MB
    
    @Value("${audio.temp.directory:${java.io.tmpdir}/presentation-coach}")
    private String tempDirectory;
    
    /**
     * Validates an audio file for format and size constraints
     *
     * @param audioFile The audio file to validate
     * @throws AudioProcessingException If the file is invalid
     */
    public void validateAudioFile(MultipartFile audioFile) {
        if (audioFile == null || audioFile.isEmpty()) {
            throw new AudioProcessingException("Audio file is empty or missing", HttpStatus.BAD_REQUEST);
        }
        
        // Check file size
        if (audioFile.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new AudioProcessingException(
                    String.format("Audio file exceeds maximum size of %d MB", MAX_FILE_SIZE_BYTES / (1024 * 1024)),
                    HttpStatus.PAYLOAD_TOO_LARGE
            );
        }
        
        // Check file format
        String contentType = audioFile.getContentType();
        if (contentType == null || !SUPPORTED_AUDIO_FORMATS.contains(contentType.toLowerCase())) {
            throw new AudioProcessingException(
                    "Unsupported audio format. Supported formats: MP3, WAV, M4A",
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE
            );
        }
        
        logger.info("Audio file validated successfully: {}, size: {} bytes", 
                audioFile.getOriginalFilename(), audioFile.getSize());
    }
    
    /**
     * Converts an audio file to a standardized format (WAV)
     *
     * @param audioFile The audio file to convert
     * @return The converted file
     * @throws AudioProcessingException If conversion fails
     */
    public File convertAudioFormat(MultipartFile audioFile) {
        validateAudioFile(audioFile);
        
        try {
            // Create temp directory if it doesn't exist
            File tempDir = new File(tempDirectory);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            
            // Create a temporary file for the original upload
            String originalFilename = audioFile.getOriginalFilename();
            String fileExtension = originalFilename != null ? 
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".tmp";
            
            File originalFile = File.createTempFile("original_", fileExtension, tempDir);
            audioFile.transferTo(originalFile);
            
            // If already WAV format, just return the file
            if (audioFile.getContentType() != null && 
                    (audioFile.getContentType().contains("wav") || audioFile.getContentType().contains("x-wav"))) {
                logger.info("Audio file already in WAV format, no conversion needed");
                return originalFile;
            }
            
            // Create output WAV file
            File convertedFile = File.createTempFile("converted_", ".wav", tempDir);
            
            // Use AudioSystem for conversion
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(originalFile);
            AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
            AudioInputStream convertedStream = AudioSystem.getAudioInputStream(format, audioInputStream);
            
            AudioSystem.write(convertedStream, AudioFileFormat.Type.WAVE, convertedFile);
            
            // Close streams
            audioInputStream.close();
            convertedStream.close();
            
            // Delete original temp file
            originalFile.delete();
            
            logger.info("Audio file converted successfully to WAV format: {}", convertedFile.getName());
            return convertedFile;
            
        } catch (UnsupportedAudioFileException e) {
            throw new AudioProcessingException("Unsupported audio format for conversion", 
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, e);
        } catch (IOException e) {
            throw new AudioProcessingException("Failed to process audio file", 
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
    
    /**
     * Extracts metadata from an audio file
     *
     * @param audioFile The audio file
     * @return Map containing metadata (duration, format, channels, etc.)
     * @throws AudioProcessingException If metadata extraction fails
     */
    public Map<String, Object> extractAudioMetadata(MultipartFile audioFile) {
        validateAudioFile(audioFile);
        
        Map<String, Object> metadata = new HashMap<>();
        File tempFile = null;
        
        try {
            // Create temp file
            File tempDir = new File(tempDirectory);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            
            tempFile = File.createTempFile("metadata_", ".tmp", tempDir);
            audioFile.transferTo(tempFile);
            
            // Extract audio format information
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(tempFile);
            AudioFormat format = audioInputStream.getFormat();
            
            // Calculate duration
            long frames = audioInputStream.getFrameLength();
            double durationInSeconds = (frames == -1) ? -1 : frames / format.getFrameRate();
            
            // Populate metadata
            metadata.put("filename", audioFile.getOriginalFilename());
            metadata.put("contentType", audioFile.getContentType());
            metadata.put("sizeBytes", audioFile.getSize());
            metadata.put("durationSeconds", durationInSeconds);
            metadata.put("sampleRate", format.getSampleRate());
            metadata.put("channels", format.getChannels());
            metadata.put("encoding", format.getEncoding().toString());
            metadata.put("frameRate", format.getFrameRate());
            metadata.put("frameSize", format.getFrameSize());
            metadata.put("bigEndian", format.isBigEndian());
            
            audioInputStream.close();
            
            logger.info("Audio metadata extracted successfully for: {}", audioFile.getOriginalFilename());
            return metadata;
            
        } catch (UnsupportedAudioFileException e) {
            throw new AudioProcessingException("Unsupported audio format for metadata extraction", 
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE, e);
        } catch (IOException e) {
            throw new AudioProcessingException("Failed to extract audio metadata", 
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        } finally {
            // Clean up temp file
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
    
    /**
     * Cleans up temporary audio files
     *
     * @param file The file to delete
     * @return true if cleanup was successful, false otherwise
     */
    public boolean cleanupTempFile(File file) {
        if (file != null && file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                logger.info("Temporary file deleted successfully: {}", file.getName());
            } else {
                logger.warn("Failed to delete temporary file: {}", file.getName());
            }
            return deleted;
        }
        return false;
    }
    
    /**
     * Cleans up all temporary files in the temp directory
     *
     * @return The number of files deleted
     */
    public int cleanupAllTempFiles() {
        File tempDir = new File(tempDirectory);
        if (!tempDir.exists() || !tempDir.isDirectory()) {
            return 0;
        }
        
        File[] tempFiles = tempDir.listFiles();
        if (tempFiles == null) {
            return 0;
        }
        
        int deletedCount = 0;
        for (File file : tempFiles) {
            if (file.isFile() && file.delete()) {
                deletedCount++;
            }
        }
        
        logger.info("Cleaned up {} temporary audio files", deletedCount);
        return deletedCount;
    }
    
    /**
     * Creates a temporary file from a MultipartFile
     *
     * @param multipartFile The multipart file
     * @return The temporary file
     * @throws AudioProcessingException If file creation fails
     */
    public File createTempFile(MultipartFile multipartFile) {
        try {
            // Create temp directory if it doesn't exist
            File tempDir = new File(tempDirectory);
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            
            String originalFilename = multipartFile.getOriginalFilename();
            String fileExtension = originalFilename != null ? 
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".tmp";
            
            File tempFile = File.createTempFile("upload_", fileExtension, tempDir);
            multipartFile.transferTo(tempFile);
            
            logger.info("Temporary file created: {}", tempFile.getName());
            return tempFile;
            
        } catch (IOException e) {
            throw new AudioProcessingException("Failed to create temporary file", 
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}