package installer

import (
	"crypto/sha256"
	"fmt"
	"io"
	"net/http"
	"os"
	"time"
)

// DownloadProgress représente la progression du téléchargement
type DownloadProgress struct {
	Total       int64
	Downloaded  int64
	Percent     float64
	BytesPerSec float64
}

// ProgressCallback est appelé pendant le téléchargement
type ProgressCallback func(progress DownloadProgress)

// DownloadFile télécharge un fichier depuis une URL avec callback de progression
func DownloadFile(url, dest string, callback ProgressCallback) error {
	// Créer le fichier de destination
	out, err := os.Create(dest)
	if err != nil {
		return fmt.Errorf("impossible de créer le fichier: %w", err)
	}
	defer out.Close()

	// Effectuer la requête HTTP
	client := &http.Client{
		Timeout: 30 * time.Minute,
	}

	resp, err := client.Get(url)
	if err != nil {
		return fmt.Errorf("échec de la requête HTTP: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("code HTTP inattendu: %d", resp.StatusCode)
	}

	totalSize := resp.ContentLength

	// Créer un reader avec progression
	reader := &progressReader{
		reader:   resp.Body,
		total:    totalSize,
		callback: callback,
		lastTime: time.Now(),
	}

	// Copier le contenu
	if _, err := io.Copy(out, reader); err != nil {
		return fmt.Errorf("erreur lors du téléchargement: %w", err)
	}

	return nil
}

// progressReader wraps un io.Reader et appelle un callback avec la progression
type progressReader struct {
	reader     io.Reader
	total      int64
	downloaded int64
	callback   ProgressCallback
	lastTime   time.Time
	lastBytes  int64
}

func (pr *progressReader) Read(p []byte) (int, error) {
	n, err := pr.reader.Read(p)
	pr.downloaded += int64(n)

	// Calculer la progression
	now := time.Now()
	elapsed := now.Sub(pr.lastTime).Seconds()

	if elapsed >= 0.1 { // Mettre à jour toutes les 100ms
		bytesPerSec := float64(pr.downloaded-pr.lastBytes) / elapsed
		percent := float64(pr.downloaded) / float64(pr.total) * 100

		if pr.callback != nil {
			pr.callback(DownloadProgress{
				Total:       pr.total,
				Downloaded:  pr.downloaded,
				Percent:     percent,
				BytesPerSec: bytesPerSec,
			})
		}

		pr.lastTime = now
		pr.lastBytes = pr.downloaded
	}

	return n, err
}

// CalculateSHA256 calcule le hash SHA256 d'un fichier
func CalculateSHA256(filepath string) (string, error) {
	file, err := os.Open(filepath)
	if err != nil {
		return "", fmt.Errorf("impossible d'ouvrir le fichier: %w", err)
	}
	defer file.Close()

	hash := sha256.New()
	if _, err := io.Copy(hash, file); err != nil {
		return "", fmt.Errorf("erreur lors du calcul du hash: %w", err)
	}

	return fmt.Sprintf("%x", hash.Sum(nil)), nil
}

// FormatBytes formate une taille en bytes en format humain
func FormatBytes(bytes int64) string {
	const unit = 1024
	if bytes < unit {
		return fmt.Sprintf("%d B", bytes)
	}
	div, exp := int64(unit), 0
	for n := bytes / unit; n >= unit; n /= unit {
		div *= unit
		exp++
	}
	return fmt.Sprintf("%.1f %cB", float64(bytes)/float64(div), "KMGTPE"[exp])
}

// FormatSpeed formate une vitesse en bytes/sec en format humain
func FormatSpeed(bytesPerSec float64) string {
	return FormatBytes(int64(bytesPerSec)) + "/s"
}
