/**
 * Progress Banner - Shows session progress on exercise pages
 * 
 * This script fetches progress data from /api/status and displays it
 * as an interactive banner at the top of the page.
 */

(function() {
  'use strict';
  
  // Get session name from URL (format: /tpXX/ExerciseName)
  const sessionName = extractSessionFromURL();
  
  if (!sessionName) {
    console.warn('Could not determine session name from URL');
    return;
  }
  
  // Fetch all sessions overview first, then current session details
  fetchAllSessions(sessionName);
  
  // Auto-refresh every 30 seconds
  setInterval(function() {
    refreshProgressData(sessionName);
  }, 30000);
  
  /**
   * Extract session name from current URL path
   */
  function extractSessionFromURL() {
    const path = window.location.pathname;
    const match = path.match(/\/(tp\d+)\//);
    return match ? match[1] : null;
  }
  
  /**
   * Refresh progress data (for auto-update)
   */
  function refreshProgressData(sessionName) {
    // Remove existing banner
    const existingBanner = document.getElementById('progress-banner');
    if (existingBanner) {
      existingBanner.remove();
    }
    // Fetch fresh data
    fetchAllSessions(sessionName);
  }
  
  /**
   * Fetch all sessions overview
   */
  function fetchAllSessions(currentSessionName) {
    fetch('/api/sessions')
      .then(response => response.json())
      .then(sessionsData => {
        if (sessionsData.error) {
          console.error('Failed to load sessions:', sessionsData.error);
          // Continue anyway with just current session
          fetchProgress(currentSessionName, null);
          return;
        }
        // Now fetch current session details
        fetchProgress(currentSessionName, sessionsData.sessions);
      })
      .catch(error => {
        console.error('Error fetching sessions:', error);
        // Continue anyway with just current session
        fetchProgress(currentSessionName, null);
      });
  }
  
  /**
   * Fetch progress data from API
   */
  function fetchProgress(sessionName, allSessions) {
    fetch('/api/status?session=' + sessionName)
      .then(response => response.json())
      .then(data => {
        if (data.error) {
          console.error('Failed to load progress:', data.error);
          return;
        }
        createProgressBanner(data, allSessions);
      })
      .catch(error => {
        console.error('Error fetching progress:', error);
      });
  }
  
  /**
   * Get status class for an exercise
   */
  function getStatusClass(exercise) {
    if (exercise.type === 'qcm') {
      return exercise.score > 0 ? 'completed' : 'not-started';
    }
    return exercise.status === 'completed' ? 'completed' : 
           exercise.status === 'in_progress' ? 'in-progress' : 'not-started';
  }
  
  /**
   * Get status icon for display
   */
  function getStatusIcon(statusClass) {
    if (statusClass === 'completed') return '✓';
    if (statusClass === 'in-progress') return '◐';
    return '○';
  }
  
  /**
   * Truncate exercise name for display
   */
  function truncateName(name, maxLength) {
    if (name.length <= maxLength) return name;
    return name.substring(0, maxLength - 1) + '…';
  }
  
  /**
   * Parse time string (e.g., "9:47", "multi-day") to seconds
   */
  function parseTimeToSeconds(timeStr) {
    if (!timeStr || timeStr === '' || timeStr === 'multi-day') return 60; // Default 1 min
    const parts = timeStr.split(':');
    if (parts.length === 2) {
      return parseInt(parts[0]) * 60 + parseInt(parts[1]);
    }
    return 60; // Default 1 min
  }
  
  /**
   * Get time intensity class based on seconds
   * Light blue (0-5 min) -> Blue (5-10) -> Orange (10-20) -> Red (20-30) -> Dark red (30+)
   */
  function getTimeIntensityClass(seconds) {
    if (seconds < 300) return 'time-minimal';     // 0-5 min: light blue
    if (seconds < 600) return 'time-short';       // 5-10 min: blue
    if (seconds < 1200) return 'time-medium';     // 10-20 min: orange
    if (seconds < 1800) return 'time-long';       // 20-30 min: red
    return 'time-extreme';                        // 30+ min: dark red
  }
  
  /**
   * Get background color based on completion percentage using 4-color scheme
   * Returns background color (text color controlled by CSS)
   */
  function getProgressColor(percentage) {
    // 4-color scheme:
    // Red (<25%), Orange (<50%), Light Green (<75%), Green (>=75%)
    if (percentage < 25) {
      return '#dc2626'; // red-600
    } else if (percentage < 50) {
      return '#f97316'; // orange-500
    } else if (percentage < 75) {
      return '#84cc16'; // lime-500
    } else {
      return '#22c55e'; // green-500
    }
  }
  
  /**
   * Create the progress banner element
   */
  function createProgressBanner(data, allSessions) {
    // Calculate time-based widths for segments
    const times = data.exercises.map(ex => parseTimeToSeconds(ex.time));
    const totalTime = times.reduce((sum, t) => sum + t, 0);
    const widths = times.map(t => totalTime > 0 ? (t / totalTime * 100) : (100 / data.exercises.length));
    
    // Generate segmented progress bar with two-tone segments
    const segments = data.exercises.map((ex, index) => {
      const statusClass = getStatusClass(ex);
      const timeIntensityClass = getTimeIntensityClass(times[index]);
      const shortName = truncateName(ex.name, 15);
      const timeDisplay = ex.time || '';
      const width = widths[index];
      return '<div class="progress-segment ' + statusClass + ' ' + timeIntensityClass + '" ' +
               'style="flex: ' + width + ' 1 0%; min-width: 60px;" ' +
               'title="' + escapeHtml(ex.name) + '">' +
               '<div class="status-half">' +
                 '<div class="exercise-name-short">' + escapeHtml(shortName) + '</div>' +
                 '<div class="exercise-score">' + ex.score + '%</div>' +
               '</div>' +
               '<div class="time-half">' +
                 '<div class="exercise-time">' + escapeHtml(timeDisplay) + '</div>' +
               '</div>' +
             '</div>';
    }).join('');
    
    // Create sessions overview HTML (if available)
    let sessionsOverview = '';
    if (allSessions && allSessions.length > 0) {
      const currentSessionName = data.sessionName.toLowerCase();
      const sessionBadges = allSessions.map(session => {
        const percentage = Math.round(session.completionPercentage);
        const bgColor = getProgressColor(percentage);
        // Extract just the number from session name (e.g., 'tp12' -> '12')
        const sessionNum = session.name.replace(/^tp/, '');
        // Mark current session
        const isCurrent = session.name === currentSessionName;
        const currentClass = isCurrent ? ' current' : '';
        return '<div class="session-badge' + currentClass + '" style="background: ' + bgColor + ';">' +
                 '<span class="session-num">' + escapeHtml(sessionNum) + '</span>' +
                 '<span class="session-percent">' + percentage + '%</span>' +
               '</div>';
      }).join('');
      
      sessionsOverview = '<div class="sessions-overview">' + sessionBadges + '</div>';
    }
    
    // Create banner element with sessions overview and progress bar
    const banner = document.createElement('div');
    banner.id = 'progress-banner';
    banner.innerHTML = 
      '<div class="banner-compact">' +
        sessionsOverview +
        '<div class="progress-info">' +
          '<div class="segmented-progress">' + segments + '</div>' +
        '</div>' +
      '</div>';
    
    // Insert at top of body
    document.body.insertBefore(banner, document.body.firstChild);
  }
  
  /**
   * Simple HTML escaping for security
   */
  function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
  }
  
})();
