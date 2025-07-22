import { motion } from "framer-motion";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import { Lock, FileText } from "lucide-react";

interface ScriptInputProps {
  script: string;
  onScriptChange: (script: string) => void;
  isLocked: boolean;
  onLockScript: () => void;
}

const ScriptInput = ({ script, onScriptChange, isLocked, onLockScript }: ScriptInputProps) => {
  return (
    <motion.div 
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.5, ease: "easeOut" }}
      className="flex flex-col h-full"
    >
      <motion.div 
        initial={{ opacity: 0, y: -10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4, delay: 0.1 }}
        className="flex items-center gap-3 mb-4"
      >
        <FileText className="h-6 w-6 text-primary" />
        <h2 className="text-xl font-bold text-foreground">Script Input</h2>
      </motion.div>
      
      <motion.div 
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4, delay: 0.2 }}
        className="flex-1 flex flex-col gap-4"
      >
        <Textarea
          placeholder="Paste your presentation script here..."
          value={script}
          onChange={(e) => onScriptChange(e.target.value)}
          className="flex-1 min-h-[300px] resize-none bg-gradient-surface border-border text-foreground placeholder:text-muted-foreground focus:border-primary transition-all"
          disabled={isLocked}
        />
        
        <motion.div
          whileHover={{ scale: 1.02 }}
          whileTap={{ scale: 0.98 }}
        >
          <Button
            onClick={onLockScript}
            disabled={!script.trim() || isLocked}
            variant="coach"
            size="coach"
            className="w-full"
          >
            <Lock className="h-4 w-4" />
            {isLocked ? "Script Locked" : "Lock Script"}
          </Button>
        </motion.div>
      </motion.div>
    </motion.div>
  );
};

export default ScriptInput;