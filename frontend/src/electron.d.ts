export interface IElectronAPI{
openFile: () => Promise<string|null>,
saveFeeback:(content:string) =>Promise<boolean>,
saveAudio:(audioFile:string) => Promise<boolean>,
}

declare global{
    interface Window{
        electronAPI: IElectronAPI;
    }
}
