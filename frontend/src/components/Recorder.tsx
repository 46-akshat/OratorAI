import { useState, useRef, useEffect } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import { Mic, Square, Play, BarChart3 } from "lucide-react";
import { useToast } from "@/hooks/use-toast";

interface RecorderProps {
  isScriptLocked: boolean;
  transcript: string;
  onTranscriptChange: (transcript: string) => void;
  isRecording: boolean;
  onStartRecording: () => void;
  onStopRecording: () => void;
  onAnalyze: () => void;
  isLoading: boolean;
}

const Recorder = ({
  isScriptLocked,
  transcript,
  onTranscriptChange,
  isRecording,
  onStartRecording,
  onStopRecording,
  onAnalyze,
  isLoading
}: RecorderProps) => {
  const [hasRecorded, setHasRecorded] = useState(false);
  const recognitionRef = useRef<SpeechRecognition | null>(null);
  const { toast } = useToast();

  useEffect(() => {
    // Initialize speech recognition
    if ('SpeechRecognition' in window || 'webkitSpeechRecognition' in window) {
      const SpeechRecognition = (window as any).SpeechRecognition || (window as any).webkitSpeechRecognition;
      recognitionRef.current = new SpeechRecognition();
      
      if (recognitionRef.current) {
        recognitionRef.current.continuous = true;
        recognitionRef.current.interimResults = true;
        recognitionRef.current.lang = 'en-US';

        recognitionRef.current.onresult = (event: SpeechRecognitionEvent) => {
          let finalTranscript = '';
          for (let i = event.resultIndex; i < event.results.length; i++) {
            if (event.results[i].isFinal) {
              finalTranscript += event.results[i][0].transcript + ' ';
            }
          }
          if (finalTranscript) {
            onTranscriptChange(transcript + finalTranscript);
          }
        };

        recognitionRef.current.onerror = (event) => {
          console.error('Speech recognition error:', event.error);
          toast({
            title: "Speech Recognition Error",
            description: "There was an issue with speech recognition. Please try again.",
            variant: "destructive",
          });
          onStopRecording();
        };
      }
    } else {
      toast({
        title: "Speech Recognition Not Supported",
        description: "Your browser doesn't support speech recognition. Please use Chrome or Edge.",
        variant: "destructive",
      });
    }

    return () => {
      if (recognitionRef.current) {
        recognitionRef.current.stop();
      }
    };
  }, [onTranscriptChange, onStopRecording, toast]);

  const startRecording = () => {
    if (recognitionRef.current) {
      onTranscriptChange(''); // Clear previous transcript
      recognitionRef.current.start();
      onStartRecording();
      toast({
        title: "Recording Started",
        description: "Start speaking your presentation now.",
      });
    }
  };

  const stopRecording = () => {
    if (recognitionRef.current) {
      recognitionRef.current.stop();
      onStopRecording();
      setHasRecorded(true);
      toast({
        title: "Recording Stopped",
        description: "Your delivery has been captured successfully.",
      });
    }
  };

  const getButtonProps = () => {
    if (!isScriptLocked) {
      return {
        disabled: true,
        children: "Lock Script First",
        onClick: () => {},
        variant: "secondary" as const,
        icon: <Mic className="h-5 w-5" />
      };
    }

    if (isRecording) {
      return {
        disabled: false,
        children: "Stop Recording",
        onClick: stopRecording,
        variant: "recording" as const,
        icon: <Square className="h-5 w-5" />
      };
    }

    if (hasRecorded && transcript.trim()) {
      return {
        disabled: isLoading,
        children: isLoading ? "Analyzing..." : "Analyze Delivery",
        onClick: onAnalyze,
        variant: "analyze" as const,
        icon: <BarChart3 className="h-5 w-5" />
      };
    }

    return {
      disabled: false,
      children: "Start Recording",
      onClick: startRecording,
      variant: "coach" as const,
      icon: <Play className="h-5 w-5" />
    };
  };

  const buttonProps = getButtonProps();

  return (
    <motion.div 
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.5, ease: "easeOut", delay: 0.1 }}
      className="flex flex-col h-full"
    >
      <motion.div 
        initial={{ opacity: 0, y: -10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4, delay: 0.2 }}
        className="flex items-center gap-3 mb-4"
      >
        <Mic className="h-6 w-6 text-primary" />
        <h2 className="text-xl font-bold text-foreground">Delivery & Recording</h2>
      </motion.div>
      
      <motion.div 
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4, delay: 0.3 }}
        className="flex-1 flex flex-col gap-4"
      >
        <div className="relative">
          <Textarea
            placeholder={isRecording ? "Speak now... your words will appear here" : "Your spoken transcript will appear here..."}
            value={transcript}
            readOnly={isRecording}
            onChange={e =>!isRecording && onTranscriptChange(e.target.value)}
            className="flex-1 min-h-[300px] resize-none bg-gradient-surface border-border text-foreground placeholder:text-muted-foreground"
          />
          
          <AnimatePresence>
            {isRecording && (
              <motion.div
                initial={{ opacity: 0, scale: 0.8 }}
                animate={{ opacity: 1, scale: 1 }}
                exit={{ opacity: 0, scale: 0.8 }}
                transition={{ duration: 0.3 }}
                className="absolute top-3 right-3 w-3 h-3 bg-recording rounded-full animate-pulse"
              />
            )}
          </AnimatePresence>
        </div>
        
        <motion.div
          whileHover={{ scale: 1.02 }}
          whileTap={{ scale: 0.98 }}
        >
          <Button
            {...buttonProps}
            size="coach"
            className="w-full"
          >
            {buttonProps.icon}
            {buttonProps.children}
          </Button>
        </motion.div>
      </motion.div>
    </motion.div>
  );
};

export default Recorder;