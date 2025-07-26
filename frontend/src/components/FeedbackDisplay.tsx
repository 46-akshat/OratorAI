import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Award, TrendingUp, Volume2, AlertCircle, Download, Mic, Play, Pause } from "lucide-react";
import { useToast } from "@/hooks/use-toast";
import { useState, useRef } from "react";

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
  spokenTranscript?: string;
  voiceRecommendation?: VoiceRecommendation; // New field
}

interface FeedbackDisplayProps {
  feedbackData: FeedbackData | null;
  error: string | null;
}

const FeedbackDisplay = ({ feedbackData, error }: FeedbackDisplayProps) => {
  const { toast } = useToast();
  const [isPlayingRecommended, setIsPlayingRecommended] = useState(false);
  const recommendedAudioRef = useRef<HTMLAudioElement>(null);

  const cardVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0 },
  };

  const staggerContainer = {
    hidden: {},
    visible: {
      transition: {
        staggerChildren: 0.1,
      },
    },
  };

  const getScoreColor = (score: number) => {
    if (score >= 8) return "text-green-400";
    if (score >= 5) return "text-yellow-400";
    return "text-red-400";
  };

  const getScoreBadgeVariant = (score: number): "default" | "secondary" | "destructive" => {
    if (score >= 8) return "default";
    if (score >= 5) return "secondary";
    return "destructive";
  };

  const handleDownloadAudio = async () => {
    if (!feedbackData || !feedbackData.audioUrl) return;
    if (!window.electronAPI) {
      toast({ title: "Feature not available in web browser.", variant: "destructive" });
      return;
    }
    try {
      const success = await window.electronAPI.saveAudio(feedbackData.audioUrl);
      if (success) {
        toast({ title: "Success", description: "Audio file saved." });
      } else {
        toast({ title: "Info", description: "Save was cancelled." });
      }
    } catch (error) {
      console.error("Failed to save audio:", error);
      toast({ title: "Error", description: "Failed to save the audio file.", variant: "destructive" });
    }
  };

  const handleExportFeedback = async () => {
    if (!feedbackData) return;
    if (!window.electronAPI) {
      toast({ title: "Feature not available in web browser.", variant: "destructive" });
      return;
    }

    const content = `AI Presentation Feedback\n\n---\nScore: ${feedbackData.score}/10\n\nWhat Went Well:\n${feedbackData.positiveFeedback}\n\nAreas for Improvement:\n${feedbackData.improvementPoints}`;

    try {
      const success = await window.electronAPI.saveFeedback(content);
      if (success) {
        toast({ title: "Success", description: "Feedback exported successfully." });
      } else {
        toast({ title: "Save Cancelled", description: "The feedback file was not saved." });
      }
    } catch (error) {
      console.error("Failed to export feedback:", error);
      toast({ title: "Error", description: "An error occurred while exporting feedback.", variant: "destructive" });
    }
  };

  if (error) {
    return (
      <div className="flex flex-col h-full items-center justify-center text-center">
        <AlertCircle className="h-12 w-12 text-destructive mb-4" />
        <h3 className="text-lg font-semibold text-destructive mb-2">Analysis Failed</h3>
        <p className="text-muted-foreground">{error}</p>
      </div>
    );
  }

  if (feedbackData) {
    return (
      <motion.div
        initial="hidden"
        animate="visible"
        variants={staggerContainer}
        className="flex flex-col h-full"
      >
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center gap-3">
            <Award className="h-6 w-6 text-primary" />
            <h2 className="text-xl font-bold text-foreground">AI Feedback</h2>
          </div>
          <Button variant="outline" size="sm" onClick={handleExportFeedback}>
            <Download className="mr-2 h-4 w-4" />
            Export
          </Button>
        </div>

        {/* --- THIS IS THE CORRECTED LINE --- */}
        <div className="flex-1 space-y-4 overflow-y-auto pr-2">
          {/* Score Card */}
          <motion.div variants={cardVariants}>
            <Card className="bg-gradient-surface border-border">
              <CardHeader>
                <CardTitle className="flex items-center justify-between text-lg">
                  Overall Score
                  <Badge variant={getScoreBadgeVariant(feedbackData.score)} className="text-lg px-3 py-1">
                    {feedbackData.score}/10
                  </Badge>
                </CardTitle>
              </CardHeader>
            </Card>
          </motion.div>

          {/* What Went Well Card */}
          <motion.div variants={cardVariants}>
            <Card className="bg-gradient-surface border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2 text-green-400">
                  <Award className="h-5 w-5" /> What Went Well
                </CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-foreground">{feedbackData.positiveFeedback}</p>
              </CardContent>
            </Card>
          </motion.div>

          {/* Areas for Improvement Card */}
          <motion.div variants={cardVariants}>
            <Card className="bg-gradient-surface border-border">
              <CardHeader>
                <CardTitle className="flex items-center gap-2 text-yellow-400">
                  <TrendingUp className="h-5 w-5" /> Areas for Improvement
                </CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-foreground">{feedbackData.improvementPoints}</p>
              </CardContent>
            </Card>
          </motion.div>

          {/* Voice Recommendation Card */}
          {feedbackData.voiceRecommendation && (
            <motion.div variants={cardVariants}>
              <Card className="bg-gradient-surface border-border">
                <CardHeader>
                  <CardTitle className="flex items-center gap-2 text-purple-400">
                    <Mic className="h-5 w-5" /> Voice Recommendation
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  {/* Voice Profile Section */}
                  <div className="mb-4">
                    <div className="flex items-center gap-3 mb-3">
                      <div className="w-10 h-10 bg-gradient-to-r from-purple-500 to-blue-500 rounded-full flex items-center justify-center text-white font-bold">
                        {feedbackData.voiceRecommendation.voiceOption.name.charAt(0)}
                      </div>
                      <div>
                        <h4 className="font-semibold text-foreground text-lg">
                          {feedbackData.voiceRecommendation.voiceOption.name}
                        </h4>
                        <div className="flex gap-2 mt-1">
                          <Badge variant="secondary" className="text-xs">
                            {feedbackData.voiceRecommendation.voiceOption.gender}
                          </Badge>
                          <Badge variant="outline" className="text-xs capitalize">
                            {feedbackData.voiceRecommendation.recommendedTone}
                          </Badge>
                        </div>
                      </div>
                    </div>
                    <p className="text-foreground leading-relaxed">
                      {feedbackData.voiceRecommendation.recommendationReason}
                    </p>
                  </div>
                  
                  {/* Confidence Score */}
                  <div className="space-y-2">
                    <div className="flex items-center justify-between">
                      <span className="text-sm font-medium text-foreground">AI Confidence</span>
                      <span className="text-sm font-bold text-purple-400">
                        {Math.round(feedbackData.voiceRecommendation.confidenceScore * 100)}%
                      </span>
                    </div>
                    <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                      <motion.div
                        className="bg-gradient-to-r from-purple-500 to-blue-500 h-2 rounded-full"
                        initial={{ width: 0 }}
                        animate={{ width: `${feedbackData.voiceRecommendation.confidenceScore * 100}%` }}
                        transition={{ duration: 1, delay: 0.5 }}
                      />
                    </div>
                  </div>
                </CardContent>
              </Card>
            </motion.div>
          )}

          {/* Audio Feedback Card with Download Button */}
          {feedbackData.audioUrl && (
            <motion.div variants={cardVariants}>
              <Card className="bg-gradient-surface border-border">
                <CardHeader>
                  <CardTitle className="flex items-center justify-between text-primary">
                    <div className="flex items-center gap-2">
                      <Volume2 className="h-5 w-5" />
                      {feedbackData.voiceRecommendation ? "Hear Your Speech in Recommended Voice" : "Hear an Ideal Delivery"}
                    </div>
                    <Button variant="ghost" size="icon" onClick={handleDownloadAudio} aria-label="Download audio">
                      <Download className="h-5 w-5" />
                    </Button>
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <audio controls className="w-full h-12" src={feedbackData.audioUrl}>
                    Your browser does not support the audio element.
                  </audio>
                  {feedbackData.voiceRecommendation && (
                    <p className="text-sm text-muted-foreground mt-2">
                      This audio demonstrates how your speech sounds with the recommended {feedbackData.voiceRecommendation.voiceOption.name} voice using a {feedbackData.voiceRecommendation.recommendedTone} tone.
                    </p>
                  )}
                </CardContent>
              </Card>
            </motion.div>
          )}
        </div>
      </motion.div>
    );
  }

  return (
    <div className="flex flex-col h-full items-center justify-center text-center text-muted-foreground">
      <Award className="h-16 w-16 mb-4" />
      <h3 className="text-lg font-semibold">Your feedback will appear here</h3>
      <p>Complete your recording and analysis first.</p>
    </div>
  );
};

export default FeedbackDisplay;
