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

  // restore active channel
  const saved = localStorage.getItem("activeChannel");
  if (saved) {
    document.querySelector(`[data-channel="${saved}"]`)?.click();
  }
}

async function connectChannel(channel) {
  console.log("Connecting:", channel);

  await fetch("/api/channels/connect", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ channel })
  });
}

loadSidebar();

