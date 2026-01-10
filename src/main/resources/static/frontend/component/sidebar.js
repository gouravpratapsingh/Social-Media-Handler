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
      localStorage.setItem("activeChannel", channel);

      if (channel !== "all") connectChannel(channel);
    });
  });

  // Show more button handler
  if (showMoreBtn) {
    showMoreBtn.addEventListener("click", () => {
      showMore = !showMore;
      const hiddenItems = document.querySelectorAll(".channel-item[data-channel='facebook'], .channel-item[data-channel='youtube'], .channel-item[data-channel='twitter'], .channel-item[data-channel='instagram'], .channel-item[data-channel='snapchat'], .channel-item[data-channel='pinterest'], .channel-item[data-channel='whatsapp']");
      
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

  // restore active channel
  const saved = localStorage.getItem("activeChannel");
  if (saved) {
    document.querySelector(`[data-channel="${saved}"]`)?.click();
  }
}

function connectChannel(channel) {
  if (channel === "youtube") {
    const popup = window.open(
      "http://localhost:8081/oauth/youtube/connect",
      "youtube-oauth",
      "width=600,height=700"
    );

    // Listen for messages from the popup
    window.addEventListener('message', (event) => {
      // It's a good practice to check the origin of the message for security
      if (event.origin !== "http://localhost:8081") {
        return;
      }

      if (event.data === 'youtube-auth-success') {
        // The popup has sent a success message.
        console.log('YouTube authentication successful!');
        if (popup) {
          popup.close();
        }
        // Reload the page to reflect the new state.
        window.location.reload();
      }
    }, { once: true }); // Use { once: true } to automatically remove the listener after it's called
  }
  if (channel === "linkedin") {
    const jwtToken = localStorage.getItem("authToken"); // Get the JWT token from localStorage

    if (!jwtToken) {
      console.error("JWT token not found. User must be logged in.");
      alert("Please log in to connect your LinkedIn account.");
      return;
    }

    fetch("http://localhost:8081/oauth/linkedin/connect", {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${jwtToken}`
      }
    })
    .then(response => {
      if (!response.ok) {
        if (response.status === 401) {
          alert("Unauthorized: Please log in again.");
          localStorage.removeItem('authToken');
          window.location.href = 'login.html';
        }
        throw new Error("Failed to get LinkedIn authorization URL, status: " + response.status);
      }
      return response.text();
    })
    .then(authUrl => {
      const popup = window.open(
        authUrl,
        "linkedin-oauth",
        "width=600,height=700"
      );

      // Listen for messages from the popup
      window.addEventListener('message', (event) => {
        // It's a good practice to check the origin of the message for security
        // The callback from LinkedIn will redirect to our /oauth/linkedin/callback endpoint,
        // which then returns the HTML with the postMessage. So the origin will be our server.
        if (event.origin !== "http://localhost:8081") {
          return;
        }

        if (event.data === 'linkedin-auth-success') {
          console.log('LinkedIn authentication successful!');
          if (popup) {
            popup.close();
          }
          // Reload the page to reflect the new state, e.g., show LinkedIn as connected
          window.location.reload();
        }
      }, { once: true }); // Use { once: true } to automatically remove the listener after it's called
    })
    .catch(error => {
      console.error("Error connecting to LinkedIn:", error);
      alert("Failed to connect to LinkedIn. Please try again.");
    });
  }
  // You can add other channels (facebook, twitter, etc.) here later
}
loadSidebar();

