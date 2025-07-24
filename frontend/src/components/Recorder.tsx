import { useState, useRef } from "react";
import { motion } from "framer-motion";
import { Button } from "@/components/ui/button";
import { Mic, Square, BarChart3 } from "lucide-react";
import { useToast } from "@/hooks/use-toast";

// The props have been simplified for the new functionality
interface RecorderProps {
  isScriptLocked: boolean;
  onRecordingComplete: (audioBlob: Blob) => void;
  isLoading: boolean;
}

const Recorder = ({
  isScriptLocked,
  onRecordingComplete,
  isLoading
}: RecorderProps) => {
  const [isRecording, setIsRecording] = useState(false);
  const [hasRecorded, setHasRecorded] = useState(false);
  const mediaRecorderRef = useRef<MediaRecorder | null>(null);
  const audioChunksRef = useRef<Blob[]>([]);
  const { toast } = useToast();

  const startRecording = async () => {
    if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
      try {
        const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
        mediaRecorderRef.current = new MediaRecorder(stream);
        audioChunksRef.current = []; // Clear previous recording

        // This event handler collects the audio data
        mediaRecorderRef.current.ondataavailable = (event) => {
          audioChunksRef.current.push(event.data);
        };

        // This event handler is called when recording stops
        mediaRecorderRef.current.onstop = () => {
          // Combine all chunks into a single audio file (Blob)
          const audioBlob = new Blob(audioChunksRef.current, { type: 'audio/wav' });
          // Send the complete audio file to the parent component to trigger analysis
          onRecordingComplete(audioBlob);
          // Stop the microphone track to turn off the browser's recording indicator
          stream.getTracks().forEach(track => track.stop());
        };

        mediaRecorderRef.current.start();
        setIsRecording(true);
        setHasRecorded(true);
        toast({ title: "Recording Started", description: "Start speaking your presentation now." });

      } catch (err) {
        console.error("Microphone access error:", err);
        toast({
          title: "Microphone Error",
          description: "Could not access the microphone. Please check your browser permissions.",
          variant: "destructive",
        });
      }
    } else {
      toast({
        title: "Browser Not Supported",
        description: "Your browser does not support audio recording.",
        variant: "destructive",
      });
    }
  };

  const stopRecording = () => {
    if (mediaRecorderRef.current && isRecording) {
      mediaRecorderRef.current.stop(); // This will trigger the 'onstop' event handler
      setIsRecording(false);
      toast({ title: "Recording Complete", description: "Analysis will begin shortly." });
    }
  };

  // The UI is now much simpler
  const renderButton = () => {
    if (!isScriptLocked) {
      return <Button disabled variant="secondary" className="w-full text-lg py-6"><Mic className="mr-2 h-5 w-5" />Lock Script to Record</Button>;
    }
    if (isRecording) {
      return <Button onClick={stopRecording} variant="destructive" className="w-full text-lg py-6 animate-pulse"><Square className="mr-2 h-5 w-5" />Stop Recording</Button>;
    }
    // After recording, the button will show the loading state from the parent
    if (isLoading) {
        return <Button disabled className="w-full text-lg py-6"><BarChart3 className="mr-2 h-5 w-5 animate-spin" />Analyzing...</Button>;
    }
    // Default state and after analysis is complete
    return <Button onClick={startRecording} className="w-full text-lg py-6"><Mic className="mr-2 h-5 w-5" />{hasRecorded ? "Record Again" : "Start Recording"}</Button>;
  };

  return (
    <motion.div 
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.5, ease: "easeOut", delay: 0.1 }}
      className="flex flex-col h-full"
    >
      <div className="flex items-center gap-3 mb-4">
        <Mic className="h-6 w-6 text-primary" />
        <h2 className="text-xl font-bold text-foreground">Delivery & Recording</h2>
      </div>
      
      <div className="flex-1 flex flex-col justify-center items-center gap-6">
        <div className="text-center text-muted-foreground text-lg">
            {isRecording && "Recording in progress..."}
            {isLoading && "Please wait while we analyze your speech."}
            {!isRecording && !isLoading && (hasRecorded ? "Recording complete. You can record again or view your feedback." : "Press 'Start Recording' to begin.")}
        </div>
        <motion.div
          whileHover={{ scale: 1.02 }}
          whileTap={{ scale: 0.98 }}
          className="w-full"
        >
          {renderButton()}
        </motion.div>
      </div>
    </motion.div>
  );
};

export default Recorder;
