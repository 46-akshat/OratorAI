import { motion } from "framer-motion";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import { Lock, Unlock, FileText, FolderOpen } from "lucide-react"; // Import the Unlock icon

interface ScriptInputProps {
  script: string; 
  onScriptChange: (script: string) => void;
  isLocked: boolean;
  onLockToggle: () => void; // Renamed for clarity
}

const ScriptInput = ({ script, onScriptChange, isLocked, onLockToggle }: ScriptInputProps) => {

  const handleOpenFile = async () => {
    try {
      const fileContent = await window.electronAPI.openFile();
      if (fileContent !== null) {
        onScriptChange(fileContent);
      }
    } catch (error) {
      console.error("Failed to open file:", error);
    }
  };

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
        className="flex items-center justify-between mb-4"
      >
        <div className="flex items-center gap-3">
          <FileText className="h-6 w-6 text-primary" />
          <h2 className="text-xl font-bold text-foreground">Script Input</h2>
        </div>
        
        <Button
          variant="outline"
          size="sm"
          onClick={handleOpenFile}
          disabled={isLocked}
        >
          <FolderOpen className="mr-2 h-4 w-4" />
          Open File
        </Button>
      </motion.div>
      
      <motion.div 
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4, delay: 0.2 }}
        className="flex-1 flex flex-col gap-4"
      >
        <Textarea
          placeholder="Paste your presentation script here, or open a file..."
          value={script}
          onChange={(e) => onScriptChange(e.target.value)}
          className="flex-1 min-h-[300px] resize-none bg-gradient-surface border-border text-foreground placeholder:text-muted-foreground focus:border-primary transition-all"
          disabled={isLocked}
        />
        
        <motion.div
          whileHover={{ scale: 1.02 }}
          whileTap={{ scale: 0.98 }}
        >
          {/* --- THIS BUTTON IS NOW A TOGGLE --- */}
          <Button
            onClick={onLockToggle} // Calls the toggle handler
            disabled={!script.trim() && !isLocked} // Allow unlocking even if script is cleared
            variant={isLocked ? "secondary" : "coach"} // Change style when locked
            size="coach"
            className="w-full"
          >
            {isLocked ? <Unlock className="h-4 w-4 mr-2" /> : <Lock className="h-4 w-4 mr-2" />}
            {isLocked ? "Unlock Script" : "Lock Script"}
          </Button>
        </motion.div>
      </motion.div>
    </motion.div>
  );
};

export default ScriptInput;
