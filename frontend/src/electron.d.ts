export interface IElectronAPI {
  openFile: () => Promise<string | null>;
  // CORRECTED: The typo "saveFeeback" has been fixed to "saveFeedback"
  saveFeedback: (content: string) => Promise<boolean>;
  saveAudio: (url: string) => Promise<boolean>;
}

declare global {
  interface Window {
    electronAPI: IElectronAPI;
  }
}
