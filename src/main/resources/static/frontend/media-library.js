const mediaGrid = document.getElementById("mediaGrid");
const uploadInput = document.getElementById("mediaUpload");
const previewModal = document.getElementById("previewModal");
const previewContent = document.getElementById("previewContent");

let mediaItems = [];

uploadInput.addEventListener("change", (e) => {
  const files = Array.from(e.target.files);

  files.forEach(file => {
    const url = URL.createObjectURL(file);
    mediaItems.push({
      type: file.type.startsWith("image") ? "image" : "video",
      url
    });
  });

  renderMedia("all");
});

function renderMedia(filter) {
  mediaGrid.innerHTML = "";

  const filteredItems = mediaItems
    .filter(item => filter === "all" || item.type === filter);

  if (filteredItems.length === 0) {
    const emptyState = document.createElement('div');
    emptyState.className = 'text-center py-12 text-gray-400 col-span-full';
    emptyState.innerHTML = '<p>No media uploaded yet. Upload images or videos to get started.</p>';
    mediaGrid.appendChild(emptyState);
    return;
  }

  filteredItems.forEach((item, index) => {
    const div = document.createElement("div");
    div.className = "group relative rounded-xl overflow-hidden shadow hover:shadow-lg cursor-pointer bg-white";

    div.innerHTML = item.type === "image"
      ? `<img src="${item.url}" class="h-32 w-full object-cover group-hover:scale-105 transition" />`
      : `<video src="${item.url}" class="h-32 w-full object-cover" muted></video>`;

    div.onclick = () => openPreview(item);
    
    // Add delete button on hover
    const deleteBtn = document.createElement('button');
    deleteBtn.className = 'absolute top-1 right-1 bg-red-500 text-white p-1 rounded-md opacity-0 group-hover:opacity-100 transition';
    deleteBtn.innerHTML = '✕';
    deleteBtn.onclick = (e) => {
      e.stopPropagation();
      mediaItems.splice(mediaItems.indexOf(item), 1);
      const activeFilter = document.querySelector('.filter-btn.active')?.textContent?.toLowerCase() || 'all';
      renderMedia(activeFilter.includes('image') ? 'image' : activeFilter.includes('video') ? 'video' : 'all');
    };
    div.appendChild(deleteBtn);
    mediaGrid.appendChild(div);
  });
}


function filterMedia(type) {
  document.querySelectorAll(".filter-btn").forEach(btn => btn.classList.remove("active"));
  event.target.classList.add("active");
  renderMedia(type);
}

function openPreview(item) {
  previewModal.classList.remove("hidden");
  previewModal.classList.add("flex");

  previewContent.innerHTML = item.type === "image"
    ? `<img src="${item.url}" class="w-full rounded-lg" />`
    : `<video src="${item.url}" controls class="w-full rounded-lg"></video>`;
}

function closePreview() {
  previewModal.classList.add("hidden");
  previewModal.classList.remove("flex");
}

// Close preview on outside click
previewModal.addEventListener('click', (e) => {
  if (e.target === previewModal) {
    closePreview();
  }
});

// ===== LOGOUT & PROFILE SETUP =====
function logout() {
  if (confirm('Are you sure you want to logout?')) {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userName');
    window.location.href = 'login.html';
  }
}

// Initialize on page load
window.addEventListener("load", () => {
  const token = localStorage.getItem('authToken');
  if (!token) {
    window.location.href = 'login.html';
  }

  const userEmail = localStorage.getItem("userEmail") || "user@example.com";
  const userEmailElement = document.getElementById("userEmail");
  if (userEmailElement) {
    userEmailElement.textContent = userEmail;
  }
});

// ===== PROFILE DROPDOWN =====
function initializeProfileDropdown() {
  const profileBtn = document.getElementById('profileBtn');
  const profileMenu = document.getElementById('profileMenu');

  if (profileBtn && profileMenu) {
    profileBtn.addEventListener('click', (e) => {
      e.stopPropagation();
      profileMenu.classList.toggle('hidden');
    });

    document.addEventListener('click', (e) => {
      if (!profileBtn.contains(e.target) && !profileMenu.contains(e.target)) {
        profileMenu.classList.add('hidden');
      }
    });

    profileMenu.addEventListener('click', (e) => {
      e.stopPropagation();
    });
  }
}

// Initialize profile dropdown when DOM is ready
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', initializeProfileDropdown);
} else {
  // DOM is already loaded
  initializeProfileDropdown();
}
