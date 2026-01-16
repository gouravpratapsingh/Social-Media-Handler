async function loadSidebar() {
  const container = document.getElementById("sidebar-container");
  if (!container) return;

  const res = await fetch("component/sidebar.html");
  container.innerHTML = await res.text();

  initSidebarLogic();
}

function initSidebarLogic() {
  const items = document.querySelectorAll(".channel-item");
  const showMoreBtn = document.getElementById("showMoreBtn");
  let showMore = false;

  // Channel click handler
  items.forEach(item => {
    item.addEventListener("click", () => {
      items.forEach(i => i.classList.remove("bg-blue-50", "text-blue-600"));
      item.classList.add("bg-blue-50", "text-blue-600");

      const channel = item.dataset.channel;
      
      // Only connect to channel if it's not "all"
      if (channel !== "all") {
        // Mark that we're redirecting - prevent auto-click on page reload
        sessionStorage.setItem("isRedirecting", "true");
        sessionStorage.setItem("redirectingChannel", channel);
        connectChannel(channel);
      } else {
        // Safe to save "all" as active channel
        localStorage.setItem("activeChannel", channel);
      }
    });
  });

  // Show more button handler
  if (showMoreBtn) {
    showMoreBtn.addEventListener("click", () => {
      showMore = !showMore;
      const hiddenItems = document.querySelectorAll(".channel-item[data-channel='facebook'], .channel-item[data-channel='youtube'], .channel-item[data-channel='tiktok'], .channel-item[data-channel='twitter'], .channel-item[data-channel='snapchat'], .channel-item[data-channel='pinterest'], .channel-item[data-channel='whatsapp']");
      
      hiddenItems.forEach(item => {
        if (showMore) {
          item.classList.remove("hidden");
        } else {
          item.classList.add("hidden");
        }
      });

      // Update button text and rotation
      if (showMore) {
        showMoreBtn.innerHTML = '⌃ <span>Show less channels</span>';
      } else {
        showMoreBtn.innerHTML = '⌄ <span>Show more channels</span>';
      }
    });
  }

  // Restore active channel ONLY if we're not redirecting
  const isRedirecting = sessionStorage.getItem("isRedirecting");
  const saved = localStorage.getItem("activeChannel");
  
  if (saved && !isRedirecting) {
    // Just highlight the channel, don't click it (don't trigger connection)
    const savedItem = document.querySelector(`[data-channel="${saved}"]`);
    if (savedItem) {
      savedItem.classList.add("bg-blue-50", "text-blue-600");
    }
  } else if (saved && isRedirecting) {
    // Clear the redirect flag after a short delay
    setTimeout(() => {
      sessionStorage.removeItem("isRedirecting");
      sessionStorage.removeItem("redirectingChannel");
    }, 1000);
  }
}

// API Base URL
const API_BASE_URL = "http://localhost:8082";

// Track ongoing connection attempts to prevent duplicate redirects
const ONGOING_CONNECTIONS = new Set();

// Timeout for redirect (ms) - prevents hanging if OAuth flow is cancelled
const REDIRECT_TIMEOUT = 5000;

// Channel OAuth endpoints mapping
const OAUTH_ENDPOINTS = {
  youtube: "/oauth/youtube/connect",
  linkedin: "/oauth/linkedin/connect",
  instagram: "/oauth/instagram/connect",
  facebook: "/oauth/facebook/connect",
  twitter: "/oauth/twitter/connect",
  pinterest: "/oauth/pinterest/connect",
  tiktok: "/oauth/tiktok/connect",
  whatsapp: "/oauth/whatsapp/connect"
};



async function connectChannel(channel) {
  console.log("Connecting to:", channel);

  if (ONGOING_CONNECTIONS.has(channel)) {
    console.warn(`Connection to ${channel} is already in progress.`);
    return;
  }

  ONGOING_CONNECTIONS.add(channel);

  try {
    if (OAUTH_ENDPOINTS[channel]) {
      
      let endpoint = OAUTH_ENDPOINTS[channel];
      
      // Handle User ID injection for Twitter & LinkedIn
      if (channel === 'linkedin' || channel === 'twitter' || channel === 'whatsapp') {
         const currentUserId = getUserId(); 
         if (!currentUserId) {
             throw new Error("User ID not found. Please log out and log in again.");
         }
         endpoint += `?userId=${currentUserId}`;
      }

      const backendUrl = `${API_BASE_URL}${endpoint}`;
      console.log("Fetching Auth URL from:", backendUrl);
      showNotification(`Redirecting to ${channel} authorization...`, "info");

      // ✅ FIX: ADD HEADERS HERE (This was missing!)
      const response = await fetch(backendUrl, {
          method: "GET",
          headers: { 
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("authToken")}` 
          }
      });
      
      if (!response.ok) {
          // If 403 happens again, it means the token is invalid or expired
          if (response.status === 403) throw new Error("Permission denied. Try logging out and back in.");
          throw new Error("Failed to get authorization URL from backend");
      }

      const authUrl = await response.text();
      
      // Redirect to Twitter
      window.location.href = authUrl;
      
      // Cleanup
      const timeoutId = setTimeout(() => {
        if (ONGOING_CONNECTIONS.has(channel)) {
          ONGOING_CONNECTIONS.delete(channel);
        }
      }, REDIRECT_TIMEOUT);
      
      sessionStorage.setItem(`${channel}_timeout`, timeoutId);
      return;
    }

    // --- GENERIC FALLBACK (Your existing code) ---
    console.log("Using generic connect API for:", channel);
    showNotification(`Connecting to ${channel}...`, "info");

    const response = await fetch(`${API_BASE_URL}/api/channels/connect`, {
      method: "POST",
      headers: { 
        "Content-Type": "application/json",
        "Authorization": `Bearer ${localStorage.getItem("authToken") || ""}`
      },
      body: JSON.stringify({ channel })
    });

    if (!response.ok) {
      throw new Error(`Failed to connect ${channel}: ${response.statusText}`);
    }

    const data = await response.json();
    console.log("Connection successful:", data);
    showNotification(`Successfully connected to ${channel}!`, "success");
    ONGOING_CONNECTIONS.delete(channel);
    
  } catch (error) {
    console.error("Connection error:", error);
    showNotification(`${error.message}`, "error");
    ONGOING_CONNECTIONS.delete(channel);
  }
}

// --- NEW HELPER FUNCTION ---
// Paste this at the bottom of your file or outside other functions
function getUserId() {
    // 1. First, check if "userId" exists directly in storage
    let userId = localStorage.getItem("userId");
    if (userId) return userId;

    // 2. If not, try to extract it from the "authToken" (JWT)
    const token = localStorage.getItem("authToken");
    if (!token) return null;

    try {
        // Decode the JWT Payload (Standard Method)
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        const payload = JSON.parse(jsonPayload);
        
        // 3. Look for common ID field names in the token
        // 'sub' is standard, but sometimes it's 'id' or 'userId'
        return payload.userId || payload.id || payload.sub; 
    } catch (e) {
        console.error("Error decoding token:", e);
        return null;
    }
}

// Clean up on page visibility change (e.g., when returning from OAuth)
document.addEventListener("visibilitychange", () => {
  if (document.visibilityState === "visible") {
    console.log("Page became visible - clearing stale connection attempts");
    // Clear any stale connection attempts when returning to page
    setTimeout(() => {
      ONGOING_CONNECTIONS.forEach(channel => {
        // Only clear if it's been longer than expected redirect time
        const timeout = sessionStorage.getItem(`${channel}_timeout`);
        if (timeout) {
          clearTimeout(parseInt(timeout));
          sessionStorage.removeItem(`${channel}_timeout`);
        }
        ONGOING_CONNECTIONS.delete(channel);
      });
    }, 1000);
  }
});

// Notification helper (optional - customize based on your UI)
function showNotification(message, type = "info") {
  const bgColor = type === "success" ? "bg-green-500" : type === "error" ? "bg-red-500" : type === "warning" ? "bg-yellow-500" : "bg-blue-500";
  console.log(`[${type.toUpperCase()}] ${message}`);
  
  // Optional: Add visual notification
  // TODO: Implement a toast/alert system here if needed
}
loadSidebar();

