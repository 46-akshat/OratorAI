import { motion } from "framer-motion";
import { Mic, Volume2, Play, Pause } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
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

interface VoiceRecommendationProps {
  voiceRecommendation: VoiceRecommendation | null;
  audioUrl?: string;
}

const VoiceRecommendation = ({ voiceRecommendation, audioUrl }: VoiceRecommendationProps) => {
  const [isPlaying, setIsPlaying] = useState(false);
  const audioRef = useRef<HTMLAudioElement>(null);

  const handlePlayPause = () => {
    if (!audioRef.current || !audioUrl) return;

    if (isPlaying) {
      audioRef.current.pause();
      setIsPlaying(false);
    } else {
      audioRef.current.play();
      setIsPlaying(true);
    }
  };

  const handleAudioEnded = () => {
    setIsPlaying(false);
  };

  if (!voiceRecommendation) {
    return (
      <Card className="h-full">
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Mic className="h-5 w-5" />
            Voice Recommendation
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="text-center text-muted-foreground py-8">
            Complete your speech analysis to get personalized voice recommendations
          </div>
        </CardContent>
      </Card>
    );
  }

  const { voiceOption, recommendedTone, recommendationReason, confidenceScore } = voiceRecommendation;

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
    >
      <Card className="h-full">
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Mic className="h-5 w-5" />
            Voice Recommendation
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {/* Voice Profile */}
          <div className="bg-gradient-to-r from-blue-50 to-purple-50 dark:from-blue-950/20 dark:to-purple-950/20 rounded-lg p-4">
            <div className="flex items-center gap-3 mb-2">
              <div className="w-10 h-10 bg-gradient-to-r from-blue-500 to-purple-500 rounded-full flex items-center justify-center text-white font-bold">
                {voiceOption.name.charAt(0)}
              </div>
              <div>
                <h3 className="font-semibold text-lg">{voiceOption.name}</h3>
                <div className="flex gap-2">
                  <Badge variant="secondary">{voiceOption.gender}</Badge>
                  <Badge variant="outline">{voiceOption.accent}</Badge>
                </div>
              </div>
            </div>
            <p className="text-sm text-muted-foreground">{voiceOption.description}</p>
          </div>

          {/* Recommended Tone */}
          <div className="space-y-2">
            <h4 className="font-medium">Recommended Tone</h4>
            <Badge className="capitalize" variant="default">
              {recommendedTone}
            </Badge>
          </div>

          {/* Confidence Score */}
          <div className="space-y-2">
            <h4 className="font-medium">Confidence Score</h4>
            <div className="flex items-center gap-2">
              <div className="flex-1 bg-gray-200 dark:bg-gray-700 rounded-full h-2">
                <motion.div
                  className="bg-gradient-to-r from-green-500 to-blue-500 h-2 rounded-full"
                  initial={{ width: 0 }}
                  animate={{ width: `${confidenceScore * 100}%` }}
                  transition={{ duration: 1, delay: 0.5 }}
                />
              </div>
              <span className="text-sm font-medium">
                {Math.round(confidenceScore * 100)}%
              </span>
            </div>
          </div>

          {/* Recommendation Reason */}
          <div className="space-y-2">
            <h4 className="font-medium">Why This Voice?</h4>
            <p className="text-sm text-muted-foreground leading-relaxed">
              {recommendationReason}
            </p>
          </div>

          {/* Audio Player */}
          {audioUrl && (
            <div className="space-y-2">
              <h4 className="font-medium">Listen to Recommended Delivery</h4>
              <div className="bg-gray-50 dark:bg-gray-800 rounded-lg p-4">
                <div className="flex items-center gap-3">
                  <Button
                    onClick={handlePlayPause}
                    size="sm"
                    className="flex items-center gap-2"
                  >
                    {isPlaying ? (
                      <>
                        <Pause className="h-4 w-4" />
                        Pause
                      </>
                    ) : (
                      <>
                        <Play className="h-4 w-4" />
                        Play
                      </>
                    )}
                  </Button>
                  <Volume2 className="h-4 w-4 text-muted-foreground" />
                  <span className="text-sm text-muted-foreground">
                    Hear your speech in the recommended voice
                  </span>
                </div>
                <audio
                  ref={audioRef}
                  src={audioUrl}
                  onEnded={handleAudioEnded}
                  className="hidden"
                />
              </div>
            </div>
          )}

          {/* Supported Tones */}
          <div className="space-y-2">
            <h4 className="font-medium">Supported Tones</h4>
            <div className="flex flex-wrap gap-1">
              {voiceOption.supportedTones.map((tone) => (
                <Badge
                  key={tone}
                  variant={tone === recommendedTone ? "default" : "outline"}
                  className="text-xs capitalize"
                >
                  {tone}
                </Badge>
              ))}
            </div>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
};

export default VoiceRecommendation;