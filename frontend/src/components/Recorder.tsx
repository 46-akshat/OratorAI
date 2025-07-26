import { useState, useRef } from "react";
import { motion } from "framer-motion";
import { Button } from "@/components/ui/button";
import { Mic, Square, BarChart3, FileText } from "lucide-react";
import { useToast } from "@/hooks/use-toast";

interface RecorderProps {
  isScriptLocked: boolean;
  onRecordingComplete: (audioBlob: Blob | null) => void;
  isLoading: boolean;
  transcribedText?: string; // New prop to receive the final transcript
}

const Recorder = ({
  isScriptLocked,
  onRecordingComplete,
  isLoading,
  transcribedText
}: RecorderProps) => {
  const [isRecording, setIsRecording] = useState(false);
  const [hasRecorded, setHasRecorded] = useState(false);
  const mediaRecorderRef = useRef<MediaRecorder | null>(null);
  const audioChunksRef = useRef<Blob[]>([]);
  const { toast } = useToast();

  const startRecording = async () => {
    // Signal to the parent to clear previous feedback before starting a new recording
    onRecordingComplete(null);

    if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
      try {
        const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
        mediaRecorderRef.current = new MediaRecorder(stream);
        audioChunksRef.current = [];

        mediaRecorderRef.current.ondataavailable = (event) => {
          audioChunksRef.current.push(event.data);
        };

        mediaRecorderRef.current.onstop = () => {
          const audioBlob = new Blob(audioChunksRef.current, { type: 'audio/wav' });
          onRecordingComplete(audioBlob);
          stream.getTracks().forEach(track => track.stop());
        };

        mediaRecorderRef.current.start();
        setIsRecording(true);
        setHasRecorded(true);
        toast({ title: "Recording Started" });

      } catch (err) {
        toast({ title: "Microphone Error", variant: "destructive" });
      }
    }
  };

  const stopRecording = () => {
    if (mediaRecorderRef.current && isRecording) {
      mediaRecorderRef.current.stop();
      setIsRecording(false);
      toast({ title: "Recording Complete", description: "Analysis will begin shortly." });
    }
  };

  // This function determines what to display in the component's body
  const renderContent = () => {
    // State 1: During recording
    if (isRecording) {
      return (
        <div className="flex-1 flex flex-col justify-center items-center text-center space-y-4">
          <motion.div animate={{ scale: [1, 1.1, 1] }} transition={{ duration: 1.5, repeat: Infinity }}>
            <Mic className="h-16 w-16 text-red-500" />
          </motion.div>
          <p className="text-lg text-muted-foreground">Recording in progress...</p>
          <Button onClick={stopRecording} variant="destructive" size="lg" className="w-full">
            <Square className="mr-2 h-4 w-4" /> Stop Recording
          </Button>
        </div>
      );
    }
    
    // State 2: After analysis is complete and we have the final transcript
    if (transcribedText) {
      return (
        <div className="flex-1 flex flex-col gap-4 justify-between">
          <motion.div 
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="bg-white/5 rounded-lg p-4 border border-white/10 h-full overflow-y-auto"
          >
            <h3 className="text-sm font-semibold text-muted-foreground mb-2 flex items-center gap-2">
              <FileText className="h-4 w-4" /> What We Heard:
            </h3>
            <p className="text-sm text-foreground leading-relaxed italic">
              "{transcribedText}"
            </p>
          </motion.div>
          <Button onClick={startRecording} size="lg" className="w-full">
            <Mic className="mr-2 h-4 w-4" /> Record Again
          </Button>
        </div>
      );
    }
    
    // State 3: Default view, ready to record (or loading)
    return (
      <div className="flex-1 flex flex-col justify-center items-center text-center space-y-4">
        <Mic className="h-16 w-16 text-primary/50" />
        <h3 className="text-xl font-semibold text-foreground">
          {hasRecorded ? "Ready for your next take?" : "Ready to Record?"}
        </h3>
        <p className="text-muted-foreground">
          {isScriptLocked ? "Press the button to begin." : "Lock your script to start."}
        </p>
        <Button onClick={startRecording} disabled={!isScriptLocked || isLoading} size="lg" className="w-full">
          {isLoading ? <><BarChart3 className="mr-2 h-4 w-4 animate-spin" /> Analyzing...</> : <><Mic className="mr-2 h-4 w-4" /> {hasRecorded ? "Record Again" : "Start Recording"}</>}
        </Button>
      </div>
    );
  };

  return (
    <motion.div className="flex flex-col h-full">
      <div className="flex items-center gap-3 mb-4">
        <Mic className="h-6 w-6 text-primary" />
        <h2 className="text-xl font-bold text-foreground">Delivery & Recording</h2>
      </div>
      {renderContent()}
    </motion.div>
  );
};

export default Recorder;
