import { exec } from "child_process";
import * as vscode from "vscode";

interface IJavaCommand {
    readonly command: string;
    readonly description: string;
}

const IJAVA_COMMANDS: readonly IJavaCommand[] = [
    { command: "init", description: "Initialise un projet ijava dans le dossier courant." },
    { command: "compile", description: "Compile les sources ijava (optionnellement un fichier)." },
    { command: "test", description: "Lance les tests ijava." },
    { command: "execute", description: "Exécute le programme ijava principal." },
    { command: "status", description: "Affiche l'état du projet ijava." },
    { command: "start", description: "Démarre un service ou un serveur géré par ijava." },
    { command: "stop", description: "Arrête le service ou serveur ijava." },
    { command: "help", description: "Affiche l'aide ijava." },
    { command: "--info", description: "Montre les informations de version." },
    { command: "self-update", description: "Met à jour l'outil ijava." }
];

const SHELL_LANGUAGES = ["shellscript", "powershell", "bat"] as const;

let sharedTerminal: vscode.Terminal | undefined;
let statusBarItem: vscode.StatusBarItem | undefined;

class IJavaCompletionProvider implements vscode.CompletionItemProvider {
    // Propose les sous-commandes et suggère les fichiers .java ciblés.
    async provideCompletionItems(
        document: vscode.TextDocument,
        position: vscode.Position
    ): Promise<vscode.CompletionItem[]> {
        const linePrefix = document.lineAt(position).text.substring(0, position.character);
        const completions: vscode.CompletionItem[] = [];

        const commandMatch = linePrefix.match(/\bijava\s+([\w-]*)$/);
        if (commandMatch || /\bijava\s+$/.test(linePrefix)) {
            const typed = commandMatch ? commandMatch[1] : "";
            for (const entry of IJAVA_COMMANDS) {
                if (!typed || entry.command.startsWith(typed)) {
                    const item = new vscode.CompletionItem(entry.command, vscode.CompletionItemKind.Function);
                    item.detail = `ijava ${entry.command}`;
                    item.documentation = new vscode.MarkdownString(entry.description);
                    completions.push(item);
                }
            }
        }

        const fileMatch = linePrefix.match(/\bijava\s+(compile|test)\s+([^\s]*)$/);
        if (fileMatch || /\bijava\s+(compile|test)\s+$/.test(linePrefix)) {
            const typedFile = fileMatch ? fileMatch[2] : "";
            const files = await vscode.workspace.findFiles("**/*.java", "**/node_modules/**", 50);
            for (const uri of files) {
                const workspaceFolder = vscode.workspace.getWorkspaceFolder(uri);
                const relativePath = workspaceFolder
                    ? vscode.workspace.asRelativePath(uri)
                    : uri.fsPath;
                if (!typedFile || relativePath.startsWith(typedFile)) {
                    const item = new vscode.CompletionItem(relativePath, vscode.CompletionItemKind.File);
                    item.detail = `Fichier Java pour ijava ${fileMatch ? fileMatch[1] : ""}`.trim();
                    item.insertText = relativePath;
                    completions.push(item);
                }
            }
        }

        return completions;
    }
}

export function activate(context: vscode.ExtensionContext) {
    // Prépare les complétions pour les principaux langages de shell.
    for (const language of SHELL_LANGUAGES) {
        const provider = vscode.languages.registerCompletionItemProvider({ language }, new IJavaCompletionProvider(), "-", ".", ":", " ");
        context.subscriptions.push(provider);
    }

    statusBarItem = vscode.window.createStatusBarItem(vscode.StatusBarAlignment.Left, 100);
    statusBarItem.name = "iJava Tools";
    statusBarItem.command = "ijava.showInfo";
    statusBarItem.text = "iJava: ...";
    statusBarItem.show();
    context.subscriptions.push(statusBarItem);

    context.subscriptions.push(
        vscode.window.onDidCloseTerminal((term: vscode.Terminal) => {
            if (term === sharedTerminal) {
                sharedTerminal = undefined;
            }
        })
    );

    context.subscriptions.push(
        vscode.commands.registerCommand("ijava.runCommand", async () => {
            const pickItems = IJAVA_COMMANDS.map((entry) => ({
                label: entry.command,
                description: entry.description
            }));
            const selection = await vscode.window.showQuickPick(pickItems, {
                placeHolder: "Choisissez une commande ijava",
                matchOnDescription: true
            });
            if (!selection) {
                return;
            }

            let extraArg: string | undefined;
            if (selection.label === "compile" || selection.label === "test") {
                extraArg = await pickJavaFile();
                if (extraArg === undefined) {
                    return;
                }
            }

            const terminal = ensureTerminal();
            terminal.show(true);
            const commandText = extraArg ? `ijava ${selection.label} ${extraArg}` : `ijava ${selection.label}`;
            terminal.sendText(commandText, true);
        })
    );

    context.subscriptions.push(
        vscode.commands.registerCommand("ijava.showInfo", async () => {
            const result = await runIJava(["--info"]);
            if (result === undefined) {
                vscode.window.showErrorMessage("Impossible d'exécuter ijava --info. Vérifiez l'installation.");
                return;
            }
            vscode.window.showInformationMessage(result.stdout || result.stderr || "iJava n'a rien retourné.");
        })
    );

    updateStatusBar();
}

export function deactivate() {
    sharedTerminal?.dispose();
    statusBarItem?.dispose();
}

async function pickJavaFile(): Promise<string | undefined> {
    const files = await vscode.workspace.findFiles("**/*.java", "**/node_modules/**", 200);
    if (!files.length) {
        const create = "Créer un nouveau fichier Java";
        const choice = await vscode.window.showWarningMessage(
            "Aucun fichier .java trouvé.",
            { modal: false },
            create
        );
        if (choice === create) {
            const uri = await vscode.window.showSaveDialog({
                filters: { Java: ["java"] },
                defaultUri: vscode.workspace.workspaceFolders?.[0]?.uri
            });
            if (uri) {
                const wsEdit = new vscode.WorkspaceEdit();
                wsEdit.createFile(uri, { ignoreIfExists: true });
                await vscode.workspace.applyEdit(wsEdit);
                return vscode.workspace.asRelativePath(uri);
            }
        }
        return undefined;
    }

    const items = files.map((uri: vscode.Uri) => ({
        label: vscode.workspace.asRelativePath(uri),
        uri
    }));
    const picked = await vscode.window.showQuickPick(items, {
        placeHolder: "Sélectionnez le fichier Java à utiliser",
        matchOnDescription: false
    });
    return picked?.label;
}

function ensureTerminal(): vscode.Terminal {
    if (sharedTerminal && !sharedTerminal.exitStatus) {
        return sharedTerminal;
    }
    sharedTerminal = vscode.window.createTerminal({ name: "iJava" });
    return sharedTerminal;
}

async function updateStatusBar() {
    if (!statusBarItem) {
        return;
    }
    const info = await runIJava(["--info"]);
    if (!info) {
        statusBarItem.text = "iJava: non disponible";
        statusBarItem.tooltip = "ijava introuvable dans le PATH";
        return;
    }

    const version = extractVersion(info.stdout || info.stderr);
    statusBarItem.text = version ? `iJava ${version}` : "iJava actif";
    statusBarItem.tooltip = (info.stdout || info.stderr || "").trim();
}

function extractVersion(output: string): string | undefined {
    const lines = output.split(/\r?\n/).map((line) => line.trim()).filter(Boolean);
    for (const line of lines) {
        const match = line.match(/version\s*:?[\s]*([\w.\-]+)/i);
        if (match) {
            return match[1];
        }
    }
    return undefined;
}

interface CommandResult {
    stdout: string;
    stderr: string;
}

function runIJava(args: string[]): Promise<CommandResult | undefined> {
    return new Promise((resolve) => {
        const child = exec(`ijava ${args.join(" ")}`, { cwd: vscode.workspace.workspaceFolders?.[0]?.uri.fsPath });
        let stdout = "";
        let stderr = "";
        child.stdout?.on("data", (chunk: string) => {
            stdout += chunk;
        });
        child.stderr?.on("data", (chunk: string) => {
            stderr += chunk;
        });
        child.on("error", () => resolve(undefined));
        child.on("close", () => resolve({ stdout, stderr }));
    });
}
