[Setup]
AppName=ENavbat Thermal Printer Service
AppVersion=1.0
; 64-bit papkaga o‘rnatsin:
DefaultDirName={pf64}\GBWay\E-Navbat Printer
DefaultGroupName=ENavbat Printer
OutputBaseFilename=ENavbat-Printer-Service-Setup
Compression=lzma
SolidCompression=yes
OutputDir=output
ArchitecturesInstallIn64BitMode=x64


[Files]
Source: "run.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "ENavbat-Printer-Service.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "jre\*"; DestDir: "{app}\jre"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\ENavbat Printer"; Filename: "{app}\run.exe"
Name: "{userdesktop}\ENavbat Printer"; Filename: "{app}\run.exe"; Tasks: desktopicon

[Run]
Filename: "{app}\run.exe"; Description: "Dastur ishga tushirish"; Flags: nowait postinstall runascurrentuser skipifsilent


[Registry]
Root: HKCU; Subkey: "Software\Microsoft\Windows\CurrentVersion\Run"; \
    ValueType: string; ValueName: "ENavbatPrinter"; \
    ValueData: """{app}\run.exe"""; Flags: uninsdeletevalue

[Tasks]
Name: "desktopicon"; Description: "Yarliq qo'shish"; GroupDescription: "Qo‘shimcha"
