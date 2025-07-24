import { useState } from "react";
import axios from "axios";
import { AnimatePresence } from "framer-motion";
import ScriptInput from "@/components/ScriptInput";
import Recorder from "@/components/Recorder";
import FeedbackDisplay from "@/components/FeedbackDisplay";
import ModalLoader from "@/components/ModalLoader";
import { useToast } from "@/hooks/use-toast";
import { Brain } from "lucide-react";

interface FeedbackData {
  score: number;
  positiveFeedback: string;
  improvementPoints: string;
  audioUrl: string;
}

const Index = () => {
  const [script, setScript] = useState("");
  const [isScriptLocked, setIsScriptLocked] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [feedbackData, setFeedbackData] = useState<FeedbackData | null>(null);
  const [error, setError] = useState<string | null>(null);
  const { toast } = useToast();

  const handleLockScript = () => {
    if (script.trim()) {
      setIsScriptLocked(true);
      toast({
        title: "Script Locked",
        description: "You can now start recording your delivery.",
      });
    }
  };

  // This function now receives the audio blob from the Recorder
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

    // Create a FormData object to send the file and script
    const formData = new FormData();
    formData.append("audioFile", audioBlob, "recording.wav");
    formData.append("originalScript", script);

    try {
      // This now calls your backend endpoint that accepts a file
      const response = await axios.post(
        "http://localhost:8080/api/v1/analyze", 
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      console.log("Data received from backend:", response.data); // For debugging
      setFeedbackData(response.data);
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
    <div className="min-h-screen bg-background relative">
      <AnimatePresence>
        {isLoading && <ModalLoader />}
      </AnimatePresence>

      <header className="border-b border-border bg-gradient-surface">
        <div className="container mx-auto px-6 py-4">
          <div className="flex items-center gap-3">
            <Brain className="h-8 w-8 text-primary" />
            <h1 className="text-2xl font-bold text-foreground">AI Presentation Coach</h1>
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
              onLockScript={handleLockScript}
            />
          </div>
          <div className="bg-gradient-surface rounded-xl p-6 border border-border shadow-card">
            {/* The props passed to Recorder are now updated for audio recording */}
            <Recorder
              isScriptLocked={isScriptLocked}
              onRecordingComplete={handleRecordingComplete}
              isLoading={isLoading}
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
    </div>
  );
};

export default Index;
