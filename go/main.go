package main

import (
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"time"
)

// GitHubUser represents a GitHub user
type GitHubUser struct {
	Login         string `json:"login"`
	ID            int    `json:"id"`
	AvatarURL     string `json:"avatar_url"`
	HTMLURL       string `json:"html_url"`
	Contributions int    `json:"contributions"`
}

// PullRequest represents a GitHub pull request
type PullRequest struct {
	Number    int    `json:"number"`
	Title     string `json:"title"`
	State     string `json:"state"`
	CreatedAt string `json:"created_at"`
	MergedAt  string `json:"merged_at"`
	User      struct {
		Login     string `json:"login"`
		AvatarURL string `json:"avatar_url"`
	} `json:"user"`
}

// HacktoberfestStats contains statistics for Hacktoberfest
type HacktoberfestStats struct {
	Repository      string        `json:"repository"`
	TotalPRs        int           `json:"total_prs"`
	MergedPRs       int           `json:"merged_prs"`
	OpenPRs         int           `json:"open_prs"`
	Contributors    []GitHubUser  `json:"contributors"`
	RecentPRs       []PullRequest `json:"recent_prs"`
	GeneratedAt     time.Time     `json:"generated_at"`
}

// GitHubClient handles GitHub API requests
type GitHubClient struct {
	BaseURL string
	Token   string
}

// NewGitHubClient creates a new GitHub client
func NewGitHubClient(token string) *GitHubClient {
	return &GitHubClient{
		BaseURL: "https://api.github.com",
		Token:   token,
	}
}

// FetchContributors fetches contributors from GitHub API
func (client *GitHubClient) FetchContributors(owner, repo string) ([]GitHubUser, error) {
	url := fmt.Sprintf("%s/repos/%s/%s/contributors", client.BaseURL, owner, repo)
	
	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return nil, fmt.Errorf("creating request: %w", err)
	}
	
	if client.Token != "" {
		req.Header.Set("Authorization", "token "+client.Token)
	}
	
	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return nil, fmt.Errorf("making request: %w", err)
	}
	defer resp.Body.Close()
	
	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("API request failed with status: %d", resp.StatusCode)
	}
	
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, fmt.Errorf("reading response: %w", err)
	}
	
	var contributors []GitHubUser
	if err := json.Unmarshal(body, &contributors); err != nil {
		return nil, fmt.Errorf("unmarshaling response: %w", err)
	}
	
	return contributors, nil
}

// FetchPullRequests fetches pull requests from GitHub API
func (client *GitHubClient) FetchPullRequests(owner, repo string) ([]PullRequest, error) {
	url := fmt.Sprintf("%s/repos/%s/%s/pulls?state=all", client.BaseURL, owner, repo)
	
	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return nil, fmt.Errorf("creating request: %w", err)
	}
	
	if client.Token != "" {
		req.Header.Set("Authorization", "token "+client.Token)
	}
	
	resp, err := http.DefaultClient.Do(req)
	if err != nil {
		return nil, fmt.Errorf("making request: %w", err)
	}
	defer resp.Body.Close()
	
	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("API request failed with status: %d", resp.StatusCode)
	}
	
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, fmt.Errorf("reading response: %w", err)
	}
	
	var prs []PullRequest
	if err := json.Unmarshal(body, &prs); err != nil {
		return nil, fmt.Errorf("unmarshaling response: %w", err)
	}
	
	return prs, nil
}

// GenerateStats generates Hacktoberfest statistics
func GenerateStats(owner, repo, token string) (*HacktoberfestStats, error) {
	fmt.Println("🎃 Generating Hacktoberfest 2025 Stats with Go...")
	
	client := NewGitHubClient(token)
	
	// Fetch contributors
	contributors, err := client.FetchContributors(owner, repo)
	if err != nil {
		return nil, fmt.Errorf("fetching contributors: %w", err)
	}
	
	// Fetch pull requests
	prs, err := client.FetchPullRequests(owner, repo)
	if err != nil {
		return nil, fmt.Errorf("fetching pull requests: %w", err)
	}
	
	// Analyze pull requests
	var mergedPRs, openPRs int
	for _, pr := range prs {
		if pr.State == "open" {
			openPRs++
		} else if pr.MergedAt != "" {
			mergedPRs++
		}
	}
	
	// Get recent PRs (first 10)
	recentPRs := prs
	if len(recentPRs) > 10 {
		recentPRs = recentPRs[:10]
	}
	
	stats := &HacktoberfestStats{
		Repository:   fmt.Sprintf("%s/%s", owner, repo),
		TotalPRs:     len(prs),
		MergedPRs:    mergedPRs,
		OpenPRs:      openPRs,
		Contributors: contributors,
		RecentPRs:    recentPRs,
		GeneratedAt:  time.Now(),
	}
	
	return stats, nil
}

// SaveStatsToFile saves statistics to a JSON file
func SaveStatsToFile(stats *HacktoberfestStats, filename string) error {
	data, err := json.MarshalIndent(stats, "", "  ")
	if err != nil {
		return fmt.Errorf("marshaling stats: %w", err)
	}
	
	if err := os.WriteFile(filename, data, 0644); err != nil {
		return fmt.Errorf("writing file: %w", err)
	}
	
	fmt.Printf("📊 Stats saved to: %s\n", filename)
	return nil
}

// PrintStats prints statistics to console
func PrintStats(stats *HacktoberfestStats) {
	fmt.Println("\n" + "="*60)
	fmt.Printf("🎃 Hacktoberfest 2025 Statistics\n")
	fmt.Printf("Repository: %s\n", stats.Repository)
	fmt.Printf("Generated: %s\n", stats.GeneratedAt.Format("2006-01-02 15:04:05"))
	fmt.Println("="*60)
	fmt.Printf("📊 Total Pull Requests: %d\n", stats.TotalPRs)
	fmt.Printf("✅ Merged Pull Requests: %d\n", stats.MergedPRs)
	fmt.Printf("🔄 Open Pull Requests: %d\n", stats.OpenPRs)
	fmt.Printf("👥 Total Contributors: %d\n", len(stats.Contributors))
	fmt.Println("="*60)
	
	if len(stats.Contributors) > 0 {
		fmt.Println("🏆 Top Contributors:")
		for i, contributor := range stats.Contributors {
			if i >= 5 { // Show top 5
				break
			}
			fmt.Printf("  %d. %s (%d contributions)\n", i+1, contributor.Login, contributor.Contributions)
		}
	}
	
	if len(stats.RecentPRs) > 0 {
		fmt.Println("\n🔄 Recent Pull Requests:")
		for i, pr := range stats.RecentPRs {
			if i >= 5 { // Show 5 recent PRs
				break
			}
			fmt.Printf("  #%d: %s (%s)\n", pr.Number, pr.Title, pr.State)
		}
	}
	
	fmt.Println("\n🎉 Happy Hacktoberfest 2025! 🚀")
}

func main() {
	fmt.Println("🎃 Hacktoberfest 2025 Go Statistics Generator")
	fmt.Println("=" * 50)
	
	// Configuration
	owner := "hari7261"
	repo := "Hacktoberfest-2025"
	token := os.Getenv("GITHUB_TOKEN") // Optional: set GITHUB_TOKEN environment variable
	
	if token == "" {
		fmt.Println("💡 Tip: Set GITHUB_TOKEN environment variable for higher API rate limits")
	}
	
	// Generate statistics
	stats, err := GenerateStats(owner, repo, token)
	if err != nil {
		log.Fatalf("Error generating stats: %v", err)
	}
	
	// Print statistics
	PrintStats(stats)
	
	// Save to file
	filename := fmt.Sprintf("hacktoberfest_stats_%s.json", time.Now().Format("20060102_150405"))
	if err := SaveStatsToFile(stats, filename); err != nil {
		log.Printf("Error saving stats: %v", err)
	}
}