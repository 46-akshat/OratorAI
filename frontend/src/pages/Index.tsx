import { useState } from "react";
import axios from "axios";
import { AnimatePresence, motion } from "framer-motion";
import ScriptInput from "@/components/ScriptInput";
import Recorder from "@/components/Recorder";
import FeedbackDisplay from "@/components/FeedbackDisplay";
import ModalLoader from "@/components/ModalLoader";
import SplashScreen from "@/components/SplashScreen";
import { useToast } from "@/hooks/use-toast";
import { Brain } from "lucide-react";

interface VoiceOption {
  voiceId: string;
  name: string;
  gender: string;
  accent: string;
  description: string;
  supportedTones: string[];
}

interface VoiceRecommendation {
  voiceOption: VoiceOption;
  recommendedTone: string;
  recommendationReason: string;
  confidenceScore: number;
}

interface FeedbackData {
  score: number;
  positiveFeedback: string;
  improvementPoints: string;
  audioUrl: string;
  spokenTranscript?: string; // Added missing field
  voiceRecommendation?: VoiceRecommendation;
}

const Index = () => {
  const [script, setScript] = useState("");
  const [isScriptLocked, setIsScriptLocked] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [feedbackData, setFeedbackData] = useState<FeedbackData | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [transcribedText, setTranscribedText] = useState<string>("");
  const { toast } = useToast();
  const [isAppReady, setIsAppReady] = useState(false);

  const handleLockToggle = () => {
    if (!script.trim() && !isScriptLocked) {
      toast({
        title: "Script is Empty",
        description: "Please enter a script before locking.",
        variant: "destructive",
      });
      return;
    }
    setIsScriptLocked(!isScriptLocked);
    toast({
      title: isScriptLocked ? "Script Unlocked" : "Script Locked",
      description: isScriptLocked ? "You can now edit your script." : "You can now start recording your delivery.",
    });
  };

  const handleRecordingComplete = async (audioBlob: Blob) => {
    if (!script.trim()) {
      toast({
        title: "Script is Empty",
        description: "Please enter your script before analyzing.",
        variant: "destructive",
      });
      return;
    }
    setIsLoading(true);
    setError(null);
    setFeedbackData(null);
    setTranscribedText(""); // Clear previous transcription
    const formData = new FormData();
    formData.append("audioFile", audioBlob, "recording.wav");
    formData.append("originalScript", script);
    try {
      const response = await axios.post(
        "http://localhost:8080/api/v1/analyze",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
      console.log("Data received from backend:", response.data);
      console.log("Spoken transcript:", response.data.spokenTranscript);
      console.log("Voice recommendation:", response.data.voiceRecommendation);
      
      setFeedbackData(response.data);
      // Extract and set the transcribed text
      if (response.data.spokenTranscript) {
        setTranscribedText(response.data.spokenTranscript);
        console.log("Transcribed text set:", response.data.spokenTranscript);
      } else {
        console.log("No spokenTranscript in response");
      }
      toast({
        title: "Analysis Complete",
        description: "Your presentation feedback is ready!",
      });
    } catch (err: any) {
      console.error("Analysis failed:", err);
      const errorMessage = err.response?.data?.message || "An unknown error occurred.";
      setError(errorMessage);
      toast({
        title: "Analysis Failed",
        description: errorMessage,
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    // CORRECTED: 'overflow-hidden' has been removed from this line
    <div className="min-h-screen bg-background relative">
      <AnimatePresence>
        {isLoading && <ModalLoader />}
      </AnimatePresence>

      <AnimatePresence mode="wait">
        {!isAppReady ? (
          <SplashScreen key="splash" onReady={() => setIsAppReady(true)} />
        ) : (
          <motion.div 
            key="main-app" 
            initial={{ opacity: 0 }} 
            animate={{ opacity: 1 }} 
            transition={{ duration: 0.5 }}
          >
            <header className="border-b border-border bg-gradient-surface">
              <div className="container mx-auto px-6 py-4">
                <div className="flex items-center gap-3">
                  <motion.div layoutId="header-logo-icon">
                    <Brain className="h-8 w-8 text-primary" />
                  </motion.div>
                  <motion.h1 layoutId="header-logo-text" className="text-2xl font-bold text-foreground">
                    Orator AI
                  </motion.h1>
                </div>
              </div>
            </header>

            <main className="container mx-auto px-6 py-8">
              <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 h-[calc(100vh-160px)]">
                <div className="bg-gradient-surface rounded-xl p-6 border border-border shadow-card">
                  <ScriptInput
                    script={script}
                    onScriptChange={setScript}
                    isLocked={isScriptLocked}
                    onLockToggle={handleLockToggle}
                  />
                </div>
                <div className="bg-gradient-surface rounded-xl p-6 border border-border shadow-card">
                  <Recorder
                    isScriptLocked={isScriptLocked}
                    onRecordingComplete={handleRecordingComplete}
                    isLoading={isLoading}
                    transcribedText={transcribedText}
                  />
                </div>
                <div className="bg-gradient-surface rounded-xl p-6 border border-border shadow-card">
                  <FeedbackDisplay
                    feedbackData={feedbackData}
                    error={error}
                  />
                </div>
              </div>
            </main>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default Index;
